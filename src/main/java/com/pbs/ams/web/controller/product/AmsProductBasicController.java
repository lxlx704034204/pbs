package com.pbs.ams.web.controller.product;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pbs.ams.common.annotation.Log;
import com.pbs.ams.common.constant.ResultSet;
import com.pbs.ams.common.constant.StatusCode;
import com.pbs.ams.common.util.DateUtil;
import com.pbs.ams.common.util.ExcelUtil;
import com.pbs.ams.common.util.IdGeneratorUtil;
import com.pbs.ams.common.validator.LengthValidator;
import com.pbs.ams.web.controller.BaseController;
import com.pbs.ams.web.model.*;
import com.pbs.ams.web.service.*;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * Created by TiAmo on 17/6/23.
 */
@Controller
@Api(value = "产品基本信息", description = "产品基本信息")
@RequestMapping(value = "/product")
public class AmsProductBasicController extends BaseController {

    @Autowired
    private AmsProductService amsProductService;

    @Autowired
    private UpmsCompanyService upmsCompanyService;

    @Autowired
    private AmsTradeAccountService amsTradeAccountService;

    @Autowired
    private UpmsCompanyUserService upmsCompanyUserService;

    @Autowired
    private UpmsUserService upmsUserService;

    @Autowired
    private AmsProductUserService amsProductUserService;

    @Autowired
    private AmsTradeAccountExtService amsTradeAccountExtService;

    @Autowired
    private AmsStockHoldingService amsStockHoldingService;

    @Autowired
    private AmsBrokerService amsBrokerService;
    @Log(value = "产品管理首页")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/product/index.jsp";
    }

    @Log(value = "产品列表")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search) {

        UpmsUser user = getCurrentUser();
        Map<String,Object> map = Maps.newHashMap();
        map.put("offset",offset);
        map.put("limit",limit);
        if (!StringUtils.isBlank(search)) {
            map.put("search",search);
        }
        if (user != null) {
            if (!user.isSuperUser()) {//如果是超级管理员的话查询全部，否则带上公司进行查询
                map.put("companyId", user.getCompanyId());
            }
        }
        List<Map> rows = amsProductService.selectProduct(map);
        long total = amsProductService.selectProductCount(map);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }


    @Log(value = "新增产品页")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/createProduct", method = RequestMethod.GET )
    public String createProduct(HttpServletRequest request) {
        UpmsUser user = getCurrentUser();
        Long userId = user.getUserId();
        List<UpmsCompany> upmsCompanies =upmsCompanyService.selectCompanyByUserId(userId);//获取当前的所属公司
        List<Map> listMap = Lists.newArrayList();
        List<Long> companyIds = new ArrayList<Long>();
        for(UpmsCompany upmsCompany : upmsCompanies){
            listMap.add(objectToMap(upmsCompany));
            companyIds.add(upmsCompany.getCompanyId());//将id存放
        }
        List<UpmsCompanyUser> upmsCompanyUsers = upmsCompanyUserService.getUsersByCompanyId(companyIds);//获取公司下的全部用户
        List<Long> userIds = new ArrayList<Long>();
        for (UpmsCompanyUser uc : upmsCompanyUsers) {
            userIds.add(uc.getUserId());
        }
        List<UpmsUser> users = upmsUserService.selectUsersById(userIds);
        request.setAttribute("upmsCompanies",listMap);
        request.setAttribute("users",users);
        return "/product/create/create_product.jsp";
    }


    @Log(value = "新增产品")
    @RequiresPermissions("ams:product:read")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST )
    public Object create(AmsProduct amsProduct, Long userId, Long companyId,
                         String beginDate,String finishDate) {
        //获取session,取当前用户
        Session session = SecurityUtils.getSubject().getSession();
        UpmsUser upmsUser = (UpmsUser) session.getAttribute("user");
        if (upmsUser != null) {
            amsProduct.setOperatorId(upmsUser.getUserId());
            amsProduct.setO32Id(upmsUser.getUserId());
        }
        ComplexResult result = FluentValidator.checkAll()
                .on(amsProduct.getProductName(), new LengthValidator(1, 20, "产品名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess() || userId == null) {
            return new ResultSet(StatusCode.ERROR_NONE, result.getErrors());
        }
        long id = IdGeneratorUtil.getKey("ams_product", 1000);
        amsProduct.setProductId(id);
        //向产品用户关系表中存入关系
        AmsProductUser amsProductUser = new AmsProductUser();
        amsProductUser.setProductUserId(IdGeneratorUtil.getKey("ams_product_user"));
        amsProductUser.setUserId(userId);
        amsProductUser.setProductId(id);
        amsProduct.setStartDate(DateUtil.removeDateSymbol(beginDate));
        amsProduct.setEndDate(DateUtil.removeDateSymbol(finishDate));
        int count = amsProductService.insertProductAndUserRelation(amsProduct, amsProductUser);
        if (count > 0) {
            return new ResultSet(StatusCode.ERROR_NONE);
        }
        return new ResultSet(StatusCode.SQL_ERROR);
    }

    @Log(value = "产品详情")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public String details(@PathVariable("id") long id, HttpServletRequest request) {
        AmsProduct amsProduct = amsProductService.selectByPrimaryKey(id);
        request.setAttribute("amsProduct", amsProduct);
        return "/product/query/query_product_tabs.jsp";
    }

    @Log(value = "详情tab页")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/details/tab/{iframeName}/{id}", method = RequestMethod.GET)
    public String details(HttpServletRequest request, @PathVariable("iframeName") String iframeName, @PathVariable("id") Long id) {
        if (null != iframeName) {
            AmsProduct amsProduct = amsProductService.selectByPrimaryKey(id);
            request.setAttribute("amsProduct", amsProduct);
            return "/product/query/" + iframeName + ".jsp";
        }
        return null;
    }

    @Log(value = "总览列表")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    @ResponseBody
    public Object  positionList(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", offset);
        params.put("limit", limit);
        List<Map> rows = amsTradeAccountExtService.selectAmsTradeAccountExtWithDetail(params);
        long total = amsTradeAccountExtService.selectAmsTradeAccountExtWithDetailCount(params);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

    @Log(value = "持仓列表")
    @RequiresPermissions("upms:account:read")
    @RequestMapping(value = "/stockHolding", method = RequestMethod.GET)
    @ResponseBody
    public Object  positionList(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "positionDate") String position_date) {
        Map<String, Object> params = Maps.newHashMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(position_date)) {
            Long date = Long.parseLong(position_date.replaceAll("-",""));
            params.put("positionDate",date);
        }
        params.put("offset", offset);
        params.put("limit", limit);
        List<Map> rows = amsStockHoldingService.selectStockHoldingWithDetail(params);
        long total = amsStockHoldingService.selectStockHoldingWithDetailCount(params);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }

//    @Log(value = "指令列表")
//    @RequiresPermissions("ams:product:read")
//    @RequestMapping(value = "/stockHolding", method = RequestMethod.GET)
//    @ResponseBody
//    public Object  positionList(
//            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
//            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit) {
//        Map<String, Object> params = Maps.newHashMap();
//        params.put("offset", offset);
//        params.put("limit", limit);
//        List<Map> rows = amsTradeAccountService.selectTradeAccoutWithDetail(params);
//        long total = amsTradeAccountService.selectTradeAccoutWithDetailCount(params);
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("total", total);
//        result.put("rows", rows);
//        return result;
//    }


    @Log(value = "编辑tab页")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/update/tab/{iframeName}/{id}", method = RequestMethod.GET)
    public String updateTab(ModelMap modelMap, @PathVariable("iframeName") String iframeName, @PathVariable("id") Long id) {
        if (null != iframeName) {
            AmsProduct amsProduct = amsProductService.selectByPrimaryKey(id);
            modelMap.put("amsProduct", amsProduct);
            //从中间表查询关联的用户
            AmsProductUser amsProductUser = new AmsProductUser();
            amsProductUser.setProductId(id);
            List<AmsProductUser> amsProductUsers = amsProductUserService.select(amsProductUser);
            if (amsProductUsers != null && amsProductUsers.size() > 0) {
                modelMap.put("amsProductUsers", amsProductUsers.get(0)); //暂时一对一
            }
            List<UpmsCompany> upmsCompanies =upmsCompanyService.selectCompanyByUserId(getCurrentUser().getUserId());//获取当前的所属公司
            List<Long> companyIds = new ArrayList<Long>();
            for(UpmsCompany upmsCompany : upmsCompanies){
                companyIds.add(upmsCompany.getCompanyId());//将id存放
            }
            List<UpmsCompanyUser> upmsCompanyUsers = upmsCompanyUserService.getUsersByCompanyId(companyIds);//获取公司下的全部用户
            List<Long> userIds = new ArrayList<Long>();
            for (UpmsCompanyUser uc : upmsCompanyUsers) {
                userIds.add(uc.getUserId());
            }
            List<UpmsUser> users = upmsUserService.selectUsersById(userIds);
            modelMap.put("endDate", DateUtil.divideDate(amsProduct.getEndDate()));
            modelMap.put("startDate", DateUtil.divideDate(amsProduct.getStartDate()));
            modelMap.put("users",users);
            modelMap.put("upmsCompanies",upmsCompanies);
            return "/product/edit/" + iframeName + ".jsp";
        }
        return null;
    }
    @Log(value = "编辑产品")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") long id,ModelMap modelMap) throws ParseException {
        AmsProduct amsProduct = amsProductService.selectByPrimaryKey(id);
        /*SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd");
        Date date = sdf.parse(DateUtil.divideDate(amsProduct.getEndDate()));
        modelMap.put("date",date);*/
        modelMap.put("date", DateUtil.divideDate(amsProduct.getEndDate()));
        modelMap.put("amsProduct", amsProduct);
        return  "/product/edit/edit_product_tabs.jsp";
    }

    @Log(value = "修改产品")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") long id, AmsProduct amsProduct, Long userId,
                         String beginDate,String finishDate) {

        amsProduct.setStartDate(DateUtil.removeDateSymbol(beginDate));
        amsProduct.setEndDate(DateUtil.removeDateSymbol(finishDate));
        ComplexResult result = FluentValidator.checkAll()
                .on(amsProduct.getProductName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess() || userId == null) {
            return new ResultSet(StatusCode.EMPTY_USERNAME);
        }
        AmsProductUser productUser = new AmsProductUser();
        productUser.setProductId(id);
        List<AmsProductUser> amsProductUsers = amsProductUserService.select(productUser);
        productUser.setProductUserId(amsProductUsers.get(0).getProductUserId());
        productUser.setUserId(userId);//修改后的user
        int count = amsProductService.updateProductAndUserRelation(amsProduct, productUser);
        if (count > 0) {
            return new ResultSet(StatusCode.ERROR_NONE);
        }
        return new ResultSet(StatusCode.SQL_ERROR);
    }

    @Log(value = "删除产品")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            int count = 0;
            String[] productIds = ids.split(",");
            List<Long> idList = new ArrayList<Long>();
            Map<String, Long> params = new HashMap<String, Long>();
            for (String id : productIds) {
//                params.put("productId",Long.parseLong(id));
//                if (CheckIsDeleteUtil.isDelete(params)) {//可以删除
                    idList.add(Long.parseLong(id));
//                } else {
//                    return new ResultSet(StatusCode.INVALID_DELETE, "存在关联关系，不能删除！");
//                }
            }
            count = amsProductService.deleteByPrimaryKeys(idList);
            return new ResultSet(StatusCode.ERROR_NONE,count);
        }
        return 0;
    }

    @Log(value = "导出数据")
    @RequiresPermissions("ams:product:read")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public ResponseEntity<byte[]> exportExcel(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> params = Maps.newHashMap();
        List<Map> rows = amsProductService.selectProductWithDetail(params);
        if (rows != null) {//当查询出结果的时候
            int rowSize = rows.size();//行数
            HSSFWorkbook workbook = new HSSFWorkbook();//创建excel文件
            HSSFSheet sheet = ExcelUtil.createSheet(workbook, "证券帐号");
            Map<String, Object> account = rows.get(0);//取出第一行  方便长度的计算
            final int cellSize = account.size();//单元格数   字段
            Object[][] value = new Object[rowSize][cellSize];//初始化一个二维数组
            for (int i = 0; i < rowSize; i ++) {
                Map<String, Object> ac = rows.get(i);
                for (int j = 0; j < cellSize; j ++) {
                    for (Iterator<Map.Entry<String, Object>> it = ac.entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Object> entry = it.next();
                        value[i][j] = entry.getValue();
                        it.remove();
                        break;
                    }
                }
            }
            HSSFRow row0 =sheet.createRow(0);
            String[] title = {"No","产品名称","产品类型","产品代码","产品经理","产品状态","产品净值","单位净值","产品份额","证券总资产",
                    "证券总市值","股票总市值","空单总市值","创建人","创建时间"};
            ExcelUtil.writeArrayToExcel(title,workbook,sheet, rowSize, cellSize, value);
            ExcelUtil.writeWorkbook(workbook, "D://1.xls");
        }
        return null;
    }
}

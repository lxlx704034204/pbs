﻿﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
	<form id="updateForm" method="post">
		<div class="control-group">
			<label for="companyId" class="control-label control-label2">所属公司：</label>
			<div class="controls">
				<select name="companyId" id="companyId">
					<option value="0">---请选择---</option>
					<c:forEach var="company" items="${upmsCompanies}">
						<option value="${company.companyId}" <c:if test='${company.companyId == user.companyId}'> selected='selected' </c:if>>${company.companyName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label for="companyId" class="control-label control-label2">关联产品：</label>
			<div class="controls">
				<select name="productId" id="productId">
					<option value="0">---请选择---</option>
					<c:forEach var="product" items="${products}">
						<option value="${product.product_id}" <c:if test='${product.product_id == amsProductUsers.productId}'> selected='selected' </c:if>>${product.product_name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label for="username">帐号</label>
			<input id="username" type="text" class="form-control" name="username" maxlength="20" value="${user.username}">
		</div> 
		<div class="form-group">
			<label for="realname">姓名</label>
			<input id="realname" type="text" class="form-control" name="realname" maxlength="20" value="${user.realname}">
		</div>
		<%--<div class="form-group">--%>
			<%--<label for="avatar">头像</label>--%>
			<%--<input id="avatar" type="text" class="form-control" name="avatar" maxlength="50" value="${user.avatar}">--%>
		<%--</div>--%>
		<div class="form-group">
			<label for="phone">电话</label>
			<input id="phone" type="text" class="form-control" name="phone" maxlength="20" value="${user.phone}">
		</div>
		<div class="form-group">
			<label for="email">邮箱</label>
			<input id="email" type="text" class="form-control" name="email" maxlength="50" value="${user.email}">
		</div>
		<div class="radio">
			<div class="radio radio-inline radio-info">
				<input id="sex_1" type="radio" name="sex" value="1" <c:if test="${user.sex==1}">checked</c:if>>
				<label for="sex_1">男 </label>
			</div>
			<div class="radio radio-inline radio-danger">
				<input id="sex_0" type="radio" name="sex" value="0" <c:if test="${user.sex==0}">checked</c:if>>
				<label for="sex_0">女 </label>
			</div>
			<div class="radio radio-inline radio-success">
				<input id="locked_0" type="radio" name="locked" value="0" <c:if test="${user.locked==0}">checked</c:if>>
				<label for="locked_0">正常 </label>
			</div>
			<div class="radio radio-inline">
				<input id="locked_1" type="radio" name="locked" value="1" <c:if test="${user.locked==1}">checked</c:if>>
				<label for="locked_1">锁定 </label>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/user/update/${user.userId}',
        data: $('#updateForm').serialize(),
        beforeSend: function() {
            if ($('#username').val() == '') {
                $('#username').focus();
                return false;
            }
        },
        success: function(result) {
			if (result.code != 1) {
				if (result.data instanceof Array) {
					$.each(result.data, function(index, value) {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: value.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
					});
				} else {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: result.data.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
				}
			} else {
				updateDialog.close();
				$table.bootstrapTable('refresh');
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
			$.confirm({
				theme: 'dark',
				animation: 'rotateX',
				closeAnimation: 'rotateX',
				title: false,
				content: textStatus,
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'waves-effect waves-button waves-light'
					}
				}
			});
        }
    });
}
</script>
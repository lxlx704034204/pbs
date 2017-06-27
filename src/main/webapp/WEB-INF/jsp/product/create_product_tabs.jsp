<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="../../../../resources/pbs-admin/plugins/fullPage/jquery.fullPage.css" rel="stylesheet"/>
    <link href="../../../../resources/pbs-admin/plugins/bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="../../../../resources/pbs-admin/plugins/material-design-iconic-font-2.2.0/css/material-design-iconic-font.min.css" rel="stylesheet"/>
    <link href="../../../../resources/pbs-admin/plugins/waves-0.7.5/waves.min.css" rel="stylesheet"/>
    <link href="../../../../resources/pbs-admin/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.min.css" rel="stylesheet"/>
    <link href="../../../../resources/pbs-admin/css/admin.css" rel="stylesheet"/>
    <style>
        .content_tab>ul>li,
        .content_tab>ul>li>a{
            height: 38px;
            line-height: 38px;
            font-size: 12px;
        }
        .tab_iframe {height:340px;}
    </style>
</head>
<body>
<section id="content" style="padding-left: 0;">
    <div class="content_tab" style="background-color: #333;">
        <div class="tab_left">
            <a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-left"></i></a>
        </div>
        <div class="tab_right">
            <a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-right"></i></a>
        </div>
        <ul id="tabs" class="tabs">
            <!-- li的id 和 data-index 和它对应的iframe 的值后半段一定要一样-->
            <li id="tab__manage_new_product" data-index="_manage_new_product" class="cur">
                <a class="waves-effect waves-light">新建产品</a>
            </li>
            <li id="tab__manage_setAccount_index" data-index="_manage_setAccount_index" class="">
                <a class="waves-effect waves-light">账号设置</a>
            </li>
            <li id="tab__manage_other_interests" data-index="_manage_other_interests" class="">
                <a class="waves-effect waves-light">其他权益设置</a>
            </li>
            <li id="tab__manage_setTransaction" data-index="_manage_setTransaction" class="">
                <a class="waves-effect waves-light">交易设置</a>
            </li>
        </ul>
    </div>
    <div class="content_main">
        <div id="iframe__manage_new_product" class="iframe cur">
            <iframe class="tab_iframe" src="create_product.html" width="100%" frameborder="0" scrolling="auto" onload="changeFrameHeight(this)"></iframe>
        </div>
        <div id="iframe__manage_setAccount_index" class="iframe">
            <iframe class="tab_iframe" src="product_account_settings.html" width="100%" frameborder="0" scrolling="auto" onload="changeFrameHeight(this)"></iframe>
        </div>
        <div id="iframe__manage_other_interests" class="iframe">
            <iframe class="tab_iframe" src="product_other_settings.html" width="100%" frameborder="0" scrolling="auto" onload="changeFrameHeight(this)"></iframe>
        </div>
        <div id="iframe__manage_setTransaction" class="iframe">
            <iframe class="tab_iframe" src="product_trade_settings.html" width="100%" frameborder="0" scrolling="auto" onload="changeFrameHeight(this)"></iframe>
        </div>
    </div>
</section>
<script src="../../../../resources/pbs-admin/plugins/jquery.1.12.4.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/waves-0.7.5/waves.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/device.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/fullPage/jquery.fullPage.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/fullPage/jquery.jdirk.min.js"></script>
<script src="../../../../resources/pbs-admin/plugins/jquery.cookie.js"></script>

<script src="../../../../resources/pbs-admin/js/admin.js"></script>
</body>
</html>
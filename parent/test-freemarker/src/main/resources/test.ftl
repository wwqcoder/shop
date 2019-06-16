<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>
<#include "head.ftl">
<#--我只是一个注释，我不会有任何输出  -->
${name},你好。${message}
<hr>
<br>
<#assign linkname="王先生">
${linkname}
<hr>
<fieldset>
    <#assign info={"name":"创业时代","address":"北京市昌平区"}>
    电视剧名：${info.name} <br>
    地址：${info.address}
</fieldset>

<fieldset>
    <#if success=true>
        你已通过实名认证
    <#else>
        你未通过实名认证
    </#if>

</fieldset>

<fieldset>
    <#list goodsList as good>
        ${good_index+1} 水果名称：${good.name} 水果价格：${good.price} <br>

    </#list>

    <hr>
    共 ${goodsList?size} 条记录

</fieldset>

<fieldset>
    <#assign test="{'name':'王玮琦','age':'23'}">
    <#assign data=test?eval>

    名字：${data.name}  年龄：${data.age}
</fieldset>
<fieldset>
    日期：${date?date}
    时间：${date?time}
    日期时间：${date?datetime}
    时间格式化：${date?string("yyyy年MM月dd日")}

</fieldset>

<fieldset>
<#--    ${point?c}-->
    <#if point??>
        ${point?c}

        <#else>
          ${point!"哈哈哈"}
    </#if>
</fieldset>





</body>
</html>
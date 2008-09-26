<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:m="http://mayaa.seasar.org" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="./../../../css/global.css"/>
</head>
<body>

<div m:id="errors"></div>

<form m:id="form">
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = true || isVersionColumn(mapping) = true>
        <input m:id="${mapping.javaFieldName}" type="hidden" />
    </#if>
</#list>
<table class="tablebg">
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
		    <input m:id="${mapping.javaFieldName}" type="text" />
		</td>	
	</tr>
	</#if>
</#list>

</table>

<input type="submit" name="update" value="UPDATE" />
</form>
<br/><br/>

<a m:id="listLink" href="list.html">list page</a>

<body>
</html>
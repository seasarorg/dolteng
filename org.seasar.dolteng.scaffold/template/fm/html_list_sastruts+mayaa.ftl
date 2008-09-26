<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:m="http://mayaa.seasar.org" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="./../../../css/global.css"/>
</head>
<body>

<div m:id="errors"></div>

<table border="1">
<tr style="background-color:pink">

<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<th>${mapping.javaFieldName}</th>
	</#if>
</#list>
<th></th><th></th><th></th>
</tr>

	<tr m:id="items">
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
		<td>
			<span m:id="${mapping.javaFieldName}">${mapping.javaFieldName}</span>
		</td>
	</#if>
</#list>
		<td><a m:id="showLink" href="show.html"> show </a></td>
		<td><a m:id="editLink" href="edit.html"> edit </a></td>
		<td><a m:id="deleteLink" onclick="return confirm('delete OK?');" href="list.html">delete</a></td>
	</tr>

</table>
<br/>
<a m:id="createLink" href="create.html"> create new Object </a>


<body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:m="http://mayaa.seasar.org" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="./../../../css/global.css"/>
</head>
<body>

<div m:id="errors"></div>
<#if isSelectedExisted() = true>	
<form m:id="form">
<input m:id="offset" type="hidden" />
<input m:id="count" type="hidden" />
<table border="1">
<#list selectedColumnsMappings as condition>
	<tr>
		<th>${condition.javaFieldName}</th><td><input m:id="${condition.javaFieldName}" /></td>
	</tr>
</#list>
	<tr>
		<th></th><td><input type="submit" name="retrieve" value="retrieve" /></td>
	</tr>
</table>
<br/>
</#if>

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
			<span m:id="l_${mapping.javaFieldName}">${mapping.javaFieldName}</span>
		</td>
	</#if>
</#list>
		<td><a m:id="showLink" href="show.html"> show </a></td>
		<td><a m:id="editLink" href="edit.html"> edit </a></td>
		<td><a m:id="deleteLink" onclick="return confirm('delete OK?');" href="list.html">delete</a></td>
	</tr>

</table>

<#if isSelectedExisted() = true>	
<table>
	<tr>
		<td>
			<span m:id="totalNumber">totalNumber</span>Items
		</td>
	</tr>
	<tr>
		<td>
			<span m:id="currentPageIndex">currentPageIndex</span>/<span m:id="totalPageIndex">totalPageIndex</span>
		</td>
	</tr>
</table>
<table>
	<tr>
	    <#noparse>
		<td><input type="submit" name="firsPage" value="firsPage" ${isPrevPage == "true" ? '' : 'disabled'} /></td>
		<td><input type="submit" name="prevPage" value="prevPage" ${isPrevPage == "true" ? '' : 'disabled'} /></td>
		<td><input type="submit" name="nextPage" value="nextPage" ${isNextPage == "true" ? '' : 'disabled'} /></td>
		<td><input type="submit" name="lastPage" value="lastPage" ${isNextPage == "true" ? '' : 'disabled'} /></td>
		</#noparse>
	</tr>
</table>
</form>
</#if>

<a m:id="createLink" href="create.html"> create new Object </a>


<body>
</html>
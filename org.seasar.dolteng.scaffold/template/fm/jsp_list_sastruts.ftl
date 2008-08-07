<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<#noparse>
<link rel="stylesheet" type="text/css" href="${f:url('/css/global.css')}"/>
</#noparse>
</head>
<body>

<html:errors/>

<form action="show" >

<table border="1">
<tr style="background-color:pink">

<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<th>${mapping.javaFieldName}</th>
	</#if>
</#list>
<th></th><th></th><th></th>
</tr>

<#noparse><c:forEach var="e" varStatus="s" items="${</#noparse>${configs.table}Items<#noparse>}"></#noparse>
<#noparse>
	<tr style="background-color:${s.index %2 == 0 ? 'white' : 'aqua'}">
</#noparse>
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
		<td>
			<#noparse>${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse>
		</td>
	</#if>
</#list>
		<td><s:link href="show<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse></#if></#list>"> show </s:link></td>
		<td><s:link href="edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse></#if></#list>"> edit </s:link></td>
		<td><s:link onclick="return confirm('delete OK?');" href="delete<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse></#if><#if isVersionColumn(mapping) = true><#noparse>/${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse></#if></#list>">delete</s:link></td>
	</tr>
</c:forEach>

</table>
<br/>
<a href="create"> create new Object </a>


<body>
</html>
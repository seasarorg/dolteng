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
	<th>${mapping.javaFieldName}</th>
</#list>
<th></th><th></th><th></th>
</tr>
<#noparse>
<c:forEach var="e" varStatus="s" items="${recordList}">
	<tr style="background-color:${s.index %2 == 0 ? 'white' : 'aqua'}">
</#noparse>
<#list mappings as mapping>
		<td>
			<#noparse><c:out value="${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}" /></#noparse>
		</td>
</#list>
		<td><a href="show?<#list mappings as mapping><#if mapping.isPrimaryKey() = true>${mapping.javaFieldName}=<#noparse>${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}&</#noparse></#if></#list>"> show </a></td>
		<td><a href="edit?<#list mappings as mapping><#if mapping.isPrimaryKey() = true>${mapping.javaFieldName}=<#noparse>${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}&</#noparse></#if></#list>"> edit </a></td>
		<td><a onclick="return confirm('delete OK?');" href="delete?<#list mappings as mapping><#if mapping.isPrimaryKey() = true>${mapping.javaFieldName}=<#noparse>${f:h(e.</#noparse>${mapping.javaFieldName}<#noparse>)}&</#noparse></#if></#list>">delete</a></td>
	</tr>
</c:forEach>

</table>
<br/>
<a href="create"> create new Object </a>


<body>
</html>
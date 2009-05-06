<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<#noparse>
<link rel="stylesheet" type="text/css" href="${f:url('/css/global.css')}"/>
</#noparse>
</head>
<body>

<html:errors/>

<s:form>

<table class="tablebg">
<#list mappings as mapping>
	<#if mapping.isPrimaryKey() = true>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
			<html:text property="${mapping.javaFieldName}" />
		</td>	
	</tr>
    <#elseif mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
			<html:text property="${mapping.javaFieldName}" />
		</td>	
	</tr>
	</#if>
</#list>

</table>









<table border="1">
<tr>
<#list meisaiColumnsMappings as mapping>
<#if mapping.isPrimaryKey() = false>
  <td>${mapping.javaFieldName}</td>
</#if>
</#list>
</tr>
<c:forEach var="${configs.meisaitable}Items" items="<#noparse>${</#noparse>${configs.meisaitable}Items}">
<tr>
<#list meisaiColumnsMappings as mapping>
<#if mapping.isPrimaryKey() = false>
  <td><html:text name="${configs.meisaitable}Items" property="${mapping.javaFieldName}" indexed="true"/></td>
</#if>
</#list>
</tr>
</c:forEach>
</table>















<input type="submit" name="insert" value="CREATE" />
</s:form>
<br/><br/>

<s:link href="/${configs.table}/">list page</s:link>

<body>
</html>

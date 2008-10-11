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
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
			<html:text property="${mapping.javaFieldName}" />
		</td>	
	</tr>
	</#if>
</#list>

</table>

<input type="submit" name="insert" value="CREATE" />
</s:form>
<br/><br/>

<s:link href="/${configs.table}/">list page</s:link>

<body>
</html>
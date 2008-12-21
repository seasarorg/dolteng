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

<table class="tablebg">
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<tr>
		<td> ${mapping.javaFieldName} </td>
		<td>
			<#noparse>${f:h(</#noparse>${mapping.javaFieldName}<#noparse>)}</#noparse>
		</td>	
	</tr>
	</#if>
</#list>

</table>

<s:link href="edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>"> edit </s:link>


<br/><br/>
<s:link href="/${configs.table}/">list page</s:link>
<body>
</html>
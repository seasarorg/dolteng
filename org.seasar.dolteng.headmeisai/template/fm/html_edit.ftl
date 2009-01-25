<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../../css/global.css"/>
</head>
<body>
<form id="${configs.table_capitalize}EditForm"><input type="hidden" id="crudType" />
<div>
<span id="messages"></span>
</div>
<table class="tablebg"><#assign pkcount=0>
<#list mappings as mapping>
<#if mapping.isPrimaryKey() = true || isVersionColumn(mapping) = true>
<tr>
	<td><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></td>
	<td><input type="text" id="${mapping.javaFieldName}" /></td>
</tr>
<#elseif mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
<tr>
	<td><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></td>
	<td><#if mapping.isPrimaryKey() = true><div id="isCreate<#if 0 &lt; pkcount>-${pkcount-1}</#if>">
			<input type="text" id="${mapping.javaFieldName}"<#if mapping.isDate() = true> class="T_date"</#if>/>
		</div>
		<div id="isNotCreate<#if 0 &lt; pkcount>-${pkcount-1}</#if>" style="display: none;">
			<span id="${mapping.javaFieldName}-out">${mapping.javaFieldName}</span><input type="hidden" id="${mapping.javaFieldName}-hidden" />
		</div><#assign pkcount=pkcount + 1><#else><input type="text" id="${mapping.javaFieldName}"<#if mapping.isDate() = true> class="T_date"</#if>/></#if></td>
	<td><span id="${mapping.javaFieldName}Message"></span></td>
</tr>
</#if>
</#list>
</table>




<input type="hidden" id="${configs.meisaitable?uncap_first}ItemsSave"/>

<table id="meisai" class="tablebg">
	<thead>
		<tr>
<#list meisaiColumnsMappings as mapping>
	    <#if mapping.isPrimaryKey() = true || isVersionColumn(mapping) = true>
	    <#elseif isHeadPkey(mapping)>
	    <#else>
	    <th colspan="2"><span id="meisai${mapping.javaFieldName?cap_first}Label">${mapping.javaFieldName?uncap_first}</span></th>
	    </#if>
</#list>
		</tr>
	</thead>
	<tbody id="${configs.meisaitable?uncap_first}Items">
		<tr>
<#list meisaiColumnsMappings as mapping>
	    <#if mapping.isPrimaryKey() = true || isVersionColumn(mapping) = true>
	    <input type="hidden" id="meisai${mapping.javaFieldName?cap_first}" />
	    <#elseif isHeadPkey(mapping)>
	    <#else>
	    <td><input type="text" id="meisai${mapping.javaFieldName?cap_first}" /></td>
	    <td><span id="meisai${mapping.javaFieldName?cap_first}Message"></span></td>
	    </#if>
</#list>
		</tr>
	</tbody>
</table>









<input type="button" id="jump${configs.table_capitalize}List" value="Previous"
	onclick="location.href='${configs.table}List.html'"/>
<div id="isNotRead">
<input type="button" id="go${configs.table_capitalize}Confirm" value="Confirm"
	onclick="location.href='${configs.table}Confirm.html'"/></div>
</form>
</body></html>
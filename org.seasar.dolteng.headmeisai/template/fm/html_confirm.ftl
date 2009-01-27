<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:te="http://www.seasar.org/teeda/extension" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../../css/global.css"/>
</head>
<body>
<form id="${configs.table_capitalize}ConfirmForm"><input type="hidden" id="crudType" />
<div>
<span id="messages"></span>
</div>
<table class="tablebg">
<#list mappings as mapping>
<#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
<tr>
	<th><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></th>
	<td><span id="${mapping.javaFieldName}">${mapping.javaFieldName}</span><input type="hidden" id="${mapping.javaFieldName}-hidden" /></td>
	<td><span id="${mapping.javaFieldName}Message"></span></td>
</tr>
<#elseif mapping.isPrimaryKey() = true>
<tr>
	<th><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></th>
	<td><span id="${mapping.javaFieldName}">${mapping.javaFieldName}</span><input type="hidden" id="${mapping.javaFieldName}-hidden"/></td>
	<td><span id="${mapping.javaFieldName}Message"></span></td>
</tr>
<#elseif isVersionColumn(mapping) = true>
<tr>
	<th><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></th>
	<td><span id="${mapping.javaFieldName}">${mapping.javaFieldName}</span><input type="hidden" id="${mapping.javaFieldName}-hidden"/></td>
	<td><span id="${mapping.javaFieldName}Message"></span></td>
</tr>
</#if>
</#list>
</table>
<br />








<table class="tablebg">
	<thead>
		<tr>
<#list meisaiColumnsMappings as mapping>
	    <#if mapping.isPrimaryKey() = true || isVersionColumn(mapping) = true>
	    <#elseif isHeadPkey(mapping)>
	    <#else>
	    <th><span id="meisai${mapping.javaFieldName?cap_first}Label">${mapping.javaFieldName?uncap_first}</span></th>
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
	    <td><span id="meisai${mapping.javaFieldName?cap_first}">meisai${mapping.javaFieldName?cap_first}</span></td>
	    </#if>
</#list>
		</tr>
	</tbody>
</table>











<div id="isComeFromList">
	<input type="button" id="jump${configs.table_capitalize}List" value="Previous" 
		onclick="location.href='${configs.table_capitalize}List.html'"
	/>
</div>
<div id="isNotComeFromList">
	<input type="button" id="jump${configs.table_capitalize}Edit" value="Previous"
		onclick="location.href='${configs.table}Edit.html'" style="display: none;"/>
</div>
<div id="isNotRead">
<input type="button" id="doFinish" value="Finish" onclick="location.href='${configs.table}List.html'" />
</div>
</form>
</body></html>
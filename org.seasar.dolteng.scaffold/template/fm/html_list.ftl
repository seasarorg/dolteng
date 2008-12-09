<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:te="http://www.seasar.org/teeda/extension" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../../css/global.css"/>
</head>
<body>
<form id="${configs.table_capitalize}ListForm">
<input type="button" id="doCreate" value="Create" onclick="location.href='${configs.table}Edit.html'"/><br/>
<table border="1" class="tablebg">
	<thead>
		<tr>
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
			<th<#if mapping.isNumeric() = true> class="right"</#if>><span id="${mapping.javaFieldName}Label">${mapping.javaFieldName}</span></th>
    </#if>
</#list>
		</tr>
	</thead>
	<tbody id="${configs.table}Items">
		<tr id="${configs.table}Row" class="row_even">
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
			<td<#if mapping.isNumeric() = true> class="right"</#if>><span id="${mapping.javaFieldName}">${mapping.javaFieldName}</span></td>
    </#if>
</#list>
			<td><a id="go${configs.table_capitalize}Edit-edit" href="${configs.table}Edit.html?fixed_crudType=2${createPkeyLink(true)}">Edit</a>
			<a id="go${configs.table_capitalize}Confirm" href="${configs.table}Confirm.html?fixed_crudType=3${createPkeyLink(true)}">Delete</a>
			<a id="go${configs.table_capitalize}Confirm-confirm" href="${configs.table}Confirm.html?fixed_crudType=1${createPkeyLink(true)}">Inquire</a>
			</td>
		</tr>
	</tbody>
</table>
</form>
</body></html>
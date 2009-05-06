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

<#if isSelectedExisted() = true>
<s:form>
<html:hidden property="offset" />
<html:hidden property="count" />
<table border="1">
  <#list selectedColumnsMappings as condition>
	<tr>
		<th>${condition.javaFieldName}</th><td><html:text property="${condition.javaFieldName}" /></td>
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
		<td><s:link href="show<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>"> show </s:link></td>
		<td><s:link href="edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>"> edit </s:link></td>
		<td><s:link onclick="return confirm('delete OK?');" href="delete<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if><#if isVersionColumn(mapping) = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>">delete</s:link></td>
	</tr>
</c:forEach>

</table>
<#if isSelectedExisted() = true>
<table>
	<tr>
		<td>
			<#noparse>${f:h(totalNumber)}Items</#noparse>
		</td>
	</tr>
	<tr>
		<td>
			<#noparse>${f:h(currentPageIndex)}/${f:h(totalPageIndex)}</#noparse>
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
</s:form>
</#if>

<s:link href="create"> create new Object </s:link>
</body>
</html>
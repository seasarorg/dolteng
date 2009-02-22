<?xml version="1.0" encoding="UTF-8"?>
<m:mayaa xmlns:m="http://mayaa.seasar.org"
	xmlns:s="http://sastruts.seasar.org"
	xmlns:f="http://sastruts.seasar.org/functions"
	xmlns:bean="http://jakarta.apache.org/struts/tags-bean"
	xmlns:html="http://jakarta.apache.org/struts/tags-html"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions">
	<html:errors m:id="errors" />
<#if isSelectedExisted() = true>	
	<s:form m:id="form"></s:form>
	<html:hidden m:id="offset" property="offset" />
	<html:hidden m:id="count" property="count" />
<#list selectedColumnsMappings as condition>
	<html:text m:id="${condition.javaFieldName}" property="${condition.javaFieldName}" />
</#list>
</#if>
	<c:forEach m:id="items" var="e" varStatus="s" items="<#noparse>${</#noparse>${configs.table}Items<#noparse>}</#noparse>">
		<#noparse>
		<m:echo>
			<m:attribute name="style"
				value="background-color:${s.index %2 == 0 ? 'white' : 'aqua'}" />
			<m:doBody />
		</m:echo>
		</#noparse>
	</c:forEach>
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<m:write id="l_${mapping.javaFieldName}" value="<#noparse>${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse>" />
	</#if>
</#list>

	<s:link m:id="showLink" href="show<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>">
		<m:doBody />
	</s:link>
	<s:link m:id="editLink" href="edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>">
		<m:doBody />
	</s:link>
	<s:link m:id="deleteLink" href="delete<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if><#if isVersionColumn(mapping) = true><#noparse>/${e.</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>">
		<m:doBody />
	</s:link>
	<s:link m:id="createLink" href="create">
		<m:doBody />
	</s:link>
<#if isSelectedExisted() = true>	
	<#noparse>
	<m:write id="totalNumber" value="${totalNumber}" />
	<m:write id="currentPageIndex" value="${currentPageIndex}" />
	<m:write id="totalPageIndex" value="${totalPageIndex}" />
	<m:if m:id="prevPageVisible"  test="${isPrevPage == 'true' }" />
	<m:if m:id="prevPageDisabled" test="${isPrevPage == 'false' }" />
	<m:if m:id="nextPageVisible"  test="${isNextPage == 'true' }" />
	<m:if m:id="nextPageDisabled" test="${isNextPage == 'false' }" />
	</#noparse>
</#if>
</m:mayaa>
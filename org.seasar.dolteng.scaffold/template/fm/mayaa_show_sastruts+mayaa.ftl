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
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = false && isVersionColumn(mapping) = false>
	<m:write id="${mapping.javaFieldName}" value="<#noparse>${</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse>" />
	</#if>
</#list>

	<s:link m:id="editLink" href="edit<#list mappings as mapping><#if mapping.isPrimaryKey() = true><#noparse>/${</#noparse>${mapping.javaFieldName}<#noparse>}</#noparse></#if></#list>">
		<m:doBody />
	</s:link>
	<s:link m:id="listLink" href="/${configs.table}/">
		<m:doBody />
	</s:link>
</m:mayaa>
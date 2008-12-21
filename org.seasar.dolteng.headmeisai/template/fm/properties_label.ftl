<#list mappings as mapping>
${mapping.javaFieldName}=${mapping.javaFieldName}
</#list>

<#list meisaiColumnsMappings as mapping>
meisai${mapping.javaFieldName?cap_first}=meisai${mapping.javaFieldName?cap_first}
</#list>

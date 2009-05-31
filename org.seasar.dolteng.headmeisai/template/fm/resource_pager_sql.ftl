select * from ${configs.table_rdb}
/*BEGIN*/
 where
<#assign i = 0> 
<#assign setsuzokushi = "">
<#list selectedColumnsMappings as selectedColumnsMapping>
       <#if i != 0>
         <#assign setsuzokushi = "and">
       </#if>
       /*IF arg${selectedColumnsMapping.javaFieldName?cap_first} != null*/${setsuzokushi}
             ${selectedColumnsMapping.sqlColumnName} ${getS2DaoCondition("arg${selectedColumnsMapping.javaFieldName?cap_first}", "${getJavaClassName(selectedColumnsMapping)}")}
       /*END*/
       <#assign i = i + 1>
</#list>
/*END*/
 ORDER BY ${orderbyStringColumn}
 
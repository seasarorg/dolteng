select * from ${configs.table_rdb}
/*BEGIN*/
 where
<#assign i = 0>
<#list selectedColumnsMappings as selectedColumnsMapping>
       /*IF arg${selectedColumnsMapping.javaFieldName?cap_first} != null*/
       <#if i = 0>
         ${selectedColumnsMapping.sqlColumnName} ${getS2DaoCondition("arg${selectedColumnsMapping.javaFieldName?cap_first}", "${getJavaClassName(selectedColumnsMapping)}")}
       <#else>
         and ${selectedColumnsMapping.sqlColumnName} ${getS2DaoCondition("arg${selectedColumnsMapping.javaFieldName?cap_first}", "${getJavaClassName(selectedColumnsMapping)}")}
       </#if>
       /*END*/
       <#assign i = i + 1>
</#list>
/*END*/
 ORDER BY ${orderbyStringColumn}
 
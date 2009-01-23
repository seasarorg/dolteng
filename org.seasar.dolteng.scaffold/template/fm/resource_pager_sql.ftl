select * from ${configs.table_rdb}
/*BEGIN*/
 where
<#assign i = 0> 
<#list selectedColumnsMappings as selectedColumnsMapping>
       /*IF arg${selectedColumnsMapping.javaFieldName?cap_first} != null*/
           <#assign j = 0>
           <#assign setsuzokushi = "">
           <#list selectedColumnsMappings as condition>
             <#if i <= j><#break></#if>
             <#if j != 0>
               <#assign setsuzokushi = setsuzokushi + " || ">
             </#if>
             <#assign setsuzokushi = setsuzokushi + condition.javaFieldName + " != null">
             <#assign j = j + 1>
           </#list>
           <#if setsuzokushi != "">
           /*IF ${setsuzokushi}*/
             and
           /*END*/
           </#if>
             ${selectedColumnsMapping.sqlColumnName} LIKE concat(/*arg${selectedColumnsMapping.javaFieldName?cap_first}*/' ','%')
       /*END*/
       <#assign i = i + 1>
</#list>
/*END*/

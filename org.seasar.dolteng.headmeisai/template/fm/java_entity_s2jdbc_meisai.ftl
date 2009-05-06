package ${configs.rootpackagename}.${configs.entitypackagename};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
<#if configs.table_rdb.equalsIgnoreCase(configs.meisaitable_capitalize) = false>
import javax.persistence.Table;
</#if>

${getImportsInMeisai()}

@Entity
<#if configs.table_rdb.equalsIgnoreCase(configs.meisaitable_capitalize) = false>
@Table(name="${configs.meisaitable_rdb}")
</#if>
public class ${configs.meisaitable_capitalize} {

<#list meisaiColumnsMappings as mapping>
<#if mapping.isPrimaryKey() = true>
    @Id
    @GeneratedValue
<#elseif isVersionColumn(mapping) = true>
    @Version
</#if>
<#if mapping.isDate() = true>
    @Temporal(TemporalType.DATE)
</#if>
<#if mapping.sqlColumnName.equalsIgnoreCase(mapping.javaFieldName) = false>
    @Column(name="${mapping.sqlColumnName}")
</#if>
    public ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
}

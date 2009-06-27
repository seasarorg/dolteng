package ${configs.rootpackagename}.${configs.entitypackagename};

import javax.persistence.Entity;
<#if configs.table_rdb.equalsIgnoreCase(configs.table_capitalize) = false>
import javax.persistence.Table;
</#if>

${getJpaEntityImports()}

${getImports()}

@Entity
<#if configs.table_rdb.equalsIgnoreCase(configs.table_capitalize) = false>
@Table(name="${configs.table_rdb}")
</#if>
public class ${configs.table_capitalize} {

<#list mappings as mapping>
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
	private ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
	public ${configs.table_capitalize}() {
	}

<#list mappings as mapping>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return this.${mapping.javaFieldName};
	}

	public void set${mapping.javaFieldName?cap_first}(${getJavaClassName(mapping)} ${mapping.javaFieldName}) {
		this.${mapping.javaFieldName} = ${mapping.javaFieldName};
	}

</#list>
}
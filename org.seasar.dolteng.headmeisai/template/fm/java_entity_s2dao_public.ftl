package ${configs.rootpackagename}.${configs.entitypackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;
</#if>
${getImports()}

<#if isTigerResource() = true>
@Bean(table="${configs.table_rdb}")
</#if>
public class ${configs.table_capitalize} {

<#if isTigerResource() = false>
	public static final String TABLE = "${configs.table_rdb}";

</#if>
<#if isTigerResource() = true>
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = true>
    @Id(IdType.IDENTITY)
    </#if>
	public ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
<#else>
<#list mappings as mapping>
    <#if mapping.isPrimaryKey() = true>
    public static final String ${mapping.javaFieldName}_ID = "identity";
    </#if>
	public ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
</#if>
	public ${configs.table_capitalize}() {
	}
}
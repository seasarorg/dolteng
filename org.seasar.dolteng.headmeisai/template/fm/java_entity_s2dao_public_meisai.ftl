package ${configs.rootpackagename}.${configs.entitypackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;
</#if>
${getImports()}

<#if isTigerResource() = true>
@Bean(table="${configs.meisaitable_rdb}")
</#if>
public class ${configs.meisaitable_capitalize} {

<#if isTigerResource() = false>
	public static final String TABLE = "${configs.meisaitable_rdb}";

</#if>
<#list meisaiColumnsMappings as mapping>
    <#if mapping.isPrimaryKey() = true>
    @Id//(IdType.IDENTITY)
    </#if>
	public ${getJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
	public ${configs.meisaitable_capitalize}() {
	}
}
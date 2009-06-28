package ${configs.rootpackagename}.${configs.entitypackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
	<#if countPkeyInMeisai() = 1>
import org.seasar.dao.annotation.tiger.IdType;
    </#if>
</#if>

<#if isTigerResource() = true>
@Bean(table="${configs.meisaitable_rdb}")
</#if>
public class ${configs.meisaitable_capitalize} {

<#if isTigerResource() = false>
	public static final String TABLE = "${configs.meisaitable_rdb}";

</#if>
<#if isTigerResource() = true>
<#list meisaiColumnsMappings as mapping>
    <#if mapping.isPrimaryKey() = true>
	<#if countPkeyInMeisai() = 1>
    @Id(IdType.IDENTITY)
    <#else>
    @Id//(IdType.IDENTITY)
    </#if>
    </#if>
	public ${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
<#else>
<#list meisaiColumnsMappings as mapping>
    <#if mapping.isPrimaryKey() = true>
    public static final String ${mapping.javaFieldName}_ID = "identity";
    </#if>
	public ${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
</#if>
	public ${configs.meisaitable_capitalize}() {
	}
}
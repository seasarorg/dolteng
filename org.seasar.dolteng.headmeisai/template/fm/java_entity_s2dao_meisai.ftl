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
<#list meisaiColumnsMappings as mapping>
    <#if mapping.isPrimaryKey() = true>
	<#if countPkeyInMeisai() = 1>
    @Id(IdType.IDENTITY)
    <#else>
    @Id//(IdType.IDENTITY)
    </#if>
    </#if>
	private ${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName};

</#list>
	public ${configs.meisaitable_capitalize}() {
	}

<#list meisaiColumnsMappings as mapping>
	public ${getMeisaiJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return this.${mapping.javaFieldName};
	}

	public void set${mapping.javaFieldName?cap_first}(${getMeisaiJavaClassName(mapping)} ${mapping.javaFieldName}) {
		this.${mapping.javaFieldName} = ${mapping.javaFieldName};
	}

</#list>
}

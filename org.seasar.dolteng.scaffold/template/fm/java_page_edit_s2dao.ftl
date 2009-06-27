package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getNullableAndPrimaryKeyImports()}

<#if isTigerResource() = true>
	<#if isMappingsContainsRequired() = true>
import org.seasar.teeda.extension.annotation.validator.Required;
	</#if>
</#if>
import org.seasar.teeda.core.exception.AppFacesException;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Edit${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {

	public ${configs.table_capitalize}Edit${configs.pagesuffix}() {
	}
	
<#if isTigerResource() = true>
	public Class<?> initialize() {
<#else>
	public Class initialize() {
</#if>
		if(getCrudType() == CrudType.UPDATE) {
			${configs.table_capitalize} data = get${configs.table_capitalize}${configs.daosuffix}().selectById(${createPkeyMethodCallArgs()});
			if(data == null) {
				throw new AppFacesException("E0000001");
			}
			get${configs.table_capitalize}${configs.dxosuffix}().convert(data ,this);
		}
		return null;
	}
	
<#if isTigerResource() = true>
	public Class<?> prerender() {
<#else>
	public Class prerender() {
</#if>
		return null;
	}

<#list mappings as mapping>
<#if mapping.isNullable() = false && mapping.isPrimaryKey() = false>
<#if isTigerResource() = true>
	@Override
	@Required
<#else>
	public static final String ${mapping.javaFieldName}_TRequiredValidator = null;
</#if>
	public void set${mapping.javaFieldName?cap_first}(${getJavaClassName(mapping)} ${mapping.javaFieldName}) {
		super.set${mapping.javaFieldName?cap_first}(${mapping.javaFieldName});
	}

</#if>
</#list>
	public String getIsNotCreateStyle() {
		return getCrudType() == CrudType.CREATE ? "display: none;" : null;
	}
}
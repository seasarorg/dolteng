package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.validator.Required;

</#if>
import org.seasar.teeda.core.exception.AppFacesException;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};
import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.meisaitable_capitalize}Dto;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Edit${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {

	// detail information
	private int ${configs.meisaitable?uncap_first}Index;
	private ${configs.meisaitable_capitalize}Dto[] ${configs.meisaitable?uncap_first}Items;

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
		
		if (get${createHeadPkeyName()?cap_first}() != null) {
			${configs.meisaitable_capitalize}[] meisai =
				${configs.meisaitable_capitalize}Dao.selectBy${createHeadMeisaiPkeyByName()?cap_first}(get${createHeadPkeyName()?cap_first}());
			${configs.meisaitable?uncap_first}Items = ${configs.meisaitable_capitalize}Dto.convert(meisai);
		} else {
			${configs.meisaitable?uncap_first}Items = new ${configs.meisaitable?cap_first}Dto[5];
			for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
				${configs.meisaitable?cap_first}Dto dto = new ${configs.meisaitable?cap_first}Dto();
				<#if countPkeyInMeisai() = 2>
				//dto.meisai${createMeisaiPkeyName()?cap_first} = i + 1;
				dto.meisai${createMeisaiPkeyName()?cap_first} = ${castFromMeisaiToHead()};
				</#if>
				${configs.meisaitable?uncap_first}Items[i] = dto;
			}
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
	
	public int get${configs.meisaitable?cap_first}Index() {
		return ${configs.meisaitable?uncap_first}Index;
	}
	
	public void set${configs.meisaitable?cap_first}Index(int index) {
		${configs.meisaitable?uncap_first}Index = index;
	}
	
	public ${configs.meisaitable_capitalize}Dto[] get${configs.meisaitable?cap_first}Items() {
		return ${configs.meisaitable?uncap_first}Items;
	}
	
	public void set${configs.meisaitable?cap_first}Items(${configs.meisaitable_capitalize}Dto[] items) {
		${configs.meisaitable?uncap_first}Items = items;
	}
}

package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getDateAndNullableAndPrimaryKeyImports()}

<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.takeover.TakeOver;
import org.seasar.teeda.extension.annotation.takeover.TakeOverType;
	<#if isMappingsContainsDate() = true>
import org.seasar.teeda.extension.annotation.convert.DateTimeConverter;
	</#if>
	<#if isMappingsContainsRequired() = true>
import org.seasar.teeda.extension.annotation.validator.Required;
	</#if>
</#if>
import org.seasar.teeda.core.exception.AppFacesException;
import org.seasar.teeda.extension.util.LabelHelper;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};
import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.meisaitable_capitalize}Dto;
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}Confirm${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {
	
	// detail information
	private int ${configs.meisaitable?uncap_first}Index;
	private ${configs.meisaitable_capitalize}Dto[] ${configs.meisaitable?uncap_first}Items;

	private LabelHelper labelHelper;
	
	public ${configs.table_capitalize}Confirm${configs.pagesuffix}() {
	}
	
<#if isTigerResource() = true>
	public Class<?> initialize() {
<#else>
	public Class initialize() {
</#if>
		if(isComeFromList()) {
			${configs.table_capitalize} data = get${configs.table_capitalize}${configs.daosuffix}().selectById(${createPkeyMethodCallArgs()});
			if(data == null) {
				throw new AppFacesException("E0000001");
			}
			get${configs.table_capitalize}${configs.dxosuffix}().convert(data ,this);
		}
		
		if (getCrudType() != CrudType.CREATE && getCrudType() != CrudType.UPDATE && get${createHeadPkeyName()?cap_first}() != null) {
			${configs.meisaitable_capitalize}[] meisai =
				${configs.meisaitable_capitalize}Dao.selectBy${createHeadMeisaiPkeyByName()?cap_first}(get${createHeadPkeyName()?cap_first}());
			${configs.meisaitable?uncap_first}Items = ${configs.meisaitable_capitalize}Dto.convert(meisai);
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

<#if isTigerResource() = true>
	@TakeOver(type = TakeOverType.NEVER)
	public Class<?> doFinish() {
<#else>
	public static final String doFinish_TAKE_OVER = "type=never";
	public Class doFinish() {
</#if>
		switch(getCrudType()) {
			case CrudType.CREATE:
				get${configs.table_capitalize}${configs.daosuffix}().insert(get${configs.table_capitalize}${configs.dxosuffix}().convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].setMeisai${createHeadMeisaiPkeyByName()?cap_first}(get${createHeadPkeyName()?cap_first}());
					${configs.meisaitable_capitalize}Dao.insert(${configs.meisaitable?uncap_first}Items[i].convert());
				}
				break;
			case CrudType.UPDATE:
				get${configs.table_capitalize}${configs.daosuffix}().update(get${configs.table_capitalize}${configs.dxosuffix}().convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].setMeisai${createHeadMeisaiPkeyByName()?cap_first}(get${createHeadPkeyName()?cap_first}());
					${configs.meisaitable_capitalize}Dao.update(${configs.meisaitable?uncap_first}Items[i].convert());
				}
				break;
			case CrudType.DELETE:
				get${configs.table_capitalize}${configs.daosuffix}().delete(get${configs.table_capitalize}${configs.dxosuffix}().convert(this));
				for (int i = 0; i < ${configs.meisaitable?uncap_first}Items.length; i++) {
					${configs.meisaitable?uncap_first}Items[i].setMeisai${createHeadMeisaiPkeyByName()?cap_first}(get${createHeadPkeyName()?cap_first}());
					${configs.meisaitable_capitalize}Dao.delete(${configs.meisaitable?uncap_first}Items[i].convert());
				}
				break;
			default:
				break;
		}
		return ${configs.table_capitalize}List${configs.pagesuffix}.class;
	}
	
	public boolean isComeFromList() {
        return getCrudType() == CrudType.READ || getCrudType() == CrudType.DELETE;
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
<#if mapping.isDate() = true>
<#if isTigerResource() = true>
	@Override
	@DateTimeConverter
<#else>
	public static final String ${mapping.javaFieldName}_TDateTimeConverter = null;
</#if>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return super.get${mapping.javaFieldName?cap_first}();
	}

</#if>
</#list>
	public void setLabelHelper(LabelHelper labelHelper) {
		this.labelHelper = labelHelper;
	}
	
	public LabelHelper getLabelHelper() {
		return this.labelHelper;
	}
	
	public String getJump${configs.table_capitalize}EditStyle() {
		return isComeFromList() ? "display: none;" : "";
	}

	public String getDoFinishValue() {
        return labelHelper.getLabelValue(CrudType.toString(getCrudType()));
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
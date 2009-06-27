package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

import java.util.List;

${getDateAndSelectedImports()}

import org.seasar.teeda.extension.annotation.convert.DateTimeConverter;
import org.seasar.teeda.extension.annotation.takeover.TakeOver;

import ${configs.rootpackagename}.${configs.dtopackagename}.${configs.table_capitalize}Dto;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;

public class ${configs.table_capitalize}List${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {

	private List<${configs.table_capitalize}> ${configs.table}Items;

	private int ${configs.table}Index;

<#if isSelectedExisted() = true>
	<#list selectedColumnsMappings as selectedColumnsMapping>
	public ${getJavaClassName(selectedColumnsMapping)} text${selectedColumnsMapping.javaFieldName?cap_first};

	</#list>
</#if>

<#if isSelectedExisted() = true>
	public Integer offset;

	public Integer currentPageIndex;

	public Integer totalPageIndex;

	public Integer totalNumber;

	private int limit = 10;
</#if>

	public ${configs.table_capitalize}List${configs.pagesuffix}() {
	}

	public Class<?> initialize() {
		return null;
	}

	public Class<?> prerender() {
		<#if isSelectedExisted() = true>
		offset = ${configs.table}Index;

		${configs.table?cap_first}Dto dto = new ${configs.table?cap_first}Dto();
		dto.setMaxResults(limit);
		dto.setFirstResult(${configs.table}Index);
		setCondition(dto);
		${configs.table}Items = get${configs.table?cap_first}Service().findBy${configs.table?cap_first}(dto);

		calculatePageIndex();
		<#else>
		${configs.table}Items = get${configs.table_capitalize}${configs.servicesuffix}().findAll();
		</#if>
	
		return null;
	}

<#if isSelectedExisted() = true>
	private void setCondition(${configs.table?cap_first}Dto dto) {
	<#list selectedColumnsMappings as selectedColumnsMapping>
		<#if isDtoParameterLike("${getJavaClassName(selectedColumnsMapping)}") = true>
		if (text${selectedColumnsMapping.javaFieldName?cap_first} == null || text${selectedColumnsMapping.javaFieldName?cap_first}.length() == 0) {
			dto.set${selectedColumnsMapping.javaFieldName?cap_first}_${getDtoSuffix("${getJavaClassName(selectedColumnsMapping)}")}(text${selectedColumnsMapping.javaFieldName?cap_first});
		} else {
			dto.set${selectedColumnsMapping.javaFieldName?cap_first}_${getDtoSuffix("${getJavaClassName(selectedColumnsMapping)}")}(text${selectedColumnsMapping.javaFieldName?cap_first} + "%");
		}
		<#else>
		if (text${selectedColumnsMapping.javaFieldName?cap_first} != null) {
			dto.set${selectedColumnsMapping.javaFieldName?cap_first}_${getDtoSuffix("${getJavaClassName(selectedColumnsMapping)}")}(text${selectedColumnsMapping.javaFieldName?cap_first});
		}
		</#if>
	</#list>
	}

	public void calculatePageIndex() {
		${configs.table?cap_first}Dto dto = new ${configs.table?cap_first}Dto();
		dto.setMaxResults(Integer.MAX_VALUE);
		dto.setFirstResult(0);
		setCondition(dto);
		List<${configs.table?cap_first}> tmp = get${configs.table?cap_first}Service().findBy${configs.table?cap_first}(dto);
		totalNumber = tmp.size();

		currentPageIndex = offset/limit+1;
		totalPageIndex = totalNumber/limit;
		if (totalNumber%limit > 0) totalPageIndex++;
	}

	public Class<?> doRetrieve() {
		return null;
	}

	public Class<?> doGoFirstPage() {
		offset = 0;
		${configs.table}Index = offset;
		return null;
	}

	public Class<?> doGoPreviousPage() {
		${configs.table}Index = offset;
		if (${configs.table}Index - limit >= 0) {
			${configs.table}Index -= limit;
		}
		return null;
	}

	public Class<?> doGoNextPage() {
		${configs.table}Index = offset;
		${configs.table?cap_first}Dto dto = new ${configs.table?cap_first}Dto();
		dto.setMaxResults(Integer.MAX_VALUE);
		dto.setFirstResult(0);
		setCondition(dto);
		List<${configs.table?cap_first}> tmp = get${configs.table?cap_first}Service().findBy${configs.table?cap_first}(dto);
		if (${configs.table}Index + limit < tmp.size()) {
			${configs.table}Index += limit;
		}
		return null;
	}

	public Class<?> doGoLastPage() {
		calculatePageIndex();		
		offset = (totalPageIndex-1)*limit;
		${configs.table}Index = offset;
		return null;
	}

	public boolean isDoGoFirstPageDisabled() {
		return offset == 0;
	}

	public boolean isDoGoPreviousPageDisabled() {
		return isDoGoFirstPageDisabled();
	}

	public boolean isDoGoNextPageDisabled() {
		return currentPageIndex == totalPageIndex;
	}

	public boolean isDoGoLastPageDisabled() {
		return isDoGoNextPageDisabled();
	}
</#if>

	public String get${configs.table_capitalize}RowClass() {
		if (get${configs.table_capitalize}Index() % 2 == 0) {
			return "row_even";
		}
		return "row_odd";
	}

	@TakeOver(properties = "crudType")
	public Class<?> doCreate() {
		setCrudType(CrudType.CREATE);
		return ${configs.table_capitalize}Edit${configs.pagesuffix}.class;
		
	}
	
<#list mappings as mapping>
<#if mapping.isDate() = true>
	@Override
	@DateTimeConverter
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return super.get${mapping.javaFieldName?cap_first}();
	}

</#if>
</#list>
	public List<${configs.table_capitalize}> get${configs.table?cap_first}Items() {
		return this.${configs.table}Items;
	}

	public void set${configs.table?cap_first}Items(List<${configs.table_capitalize}> items) {
		this.${configs.table}Items = items;
	}
	
	public int get${configs.table_capitalize}Index() {
		return this.${configs.table}Index;
	}
	
	public void set${configs.table_capitalize}Index(int ${configs.table}Index) {
		this.${configs.table}Index = ${configs.table}Index;
	}
}
package ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.${configs.table};

${getImports()}
<#if isTigerResource() = true>
import org.seasar.teeda.extension.annotation.convert.DateTimeConverter;
import org.seasar.teeda.extension.annotation.takeover.TakeOver;

</#if>
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.subapplicationrootpackagename}.CrudType;
import ${configs.rootpackagename}.${configs.pagingpackagename}.${configs.table_capitalize}PagerCondition;

public class ${configs.table_capitalize}List${configs.pagesuffix} extends Abstract${configs.table_capitalize}${configs.pagesuffix} {

	private ${configs.table_capitalize}[] ${configs.table}Items;

	private int ${configs.table}Index;

<#if isSelectedExisted() = true>
	<#list selectedColumnsMappings as selectedColumnsMapping>
	private ${getJavaClassName(selectedColumnsMapping)} text${selectedColumnsMapping.javaFieldName?cap_first};

	</#list>
</#if>

<#if isSelectedExisted() = true>
	private Integer offset;

	private Integer currentPageIndex;

	private Integer totalPageIndex;

	private Integer totalNumber;

	private int limit = 10;
</#if>

	public ${configs.table_capitalize}ListPage() {
	}

	public Class initialize() {
		return null;
	}

	/*	
	public Class prerender() {
		${configs.table}Items = get${configs.table_capitalize}${configs.daosuffix}().selectAll();
		return null;
	}
	*/

	public Class prerender() {
		<#if isSelectedExisted() = true>
			<#if isTigerResource() = true>
		offset = ${configs.table}Index;
			<#else>
		offset = new Integer(${configs.table}Index);
			</#if>
	
		${configs.table?cap_first}PagerCondition dto = new ${configs.table?cap_first}PagerCondition();
		dto.setLimit(limit);
		dto.setOffset(${configs.table}Index);
	
		${configs.table}Items = get${configs.table_capitalize}${configs.daosuffix}().
			findBy${orderbyString}PagerCondition(
				${conditionCallParam}, dto);

			<#if isTigerResource() = true>
		totalNumber = dto.getCount();
			<#else>
		totalNumber = new Integer(dto.getCount());
			</#if>

		calculatePageIndex();
		<#else>
		${configs.table}Items = get${configs.table_capitalize}${configs.daosuffix}().selectAll();
		</#if>

		return null;
	}
	
<#if isSelectedExisted() = true>
	public void calculatePageIndex() {
	<#if isTigerResource() = true>
		currentPageIndex = offset/limit+1;
		totalPageIndex = totalNumber/limit;
		if (totalNumber%limit > 0) {
			totalPageIndex++;
		}
	<#else>
		currentPageIndex = new Integer(offset.intValue()/limit+1);
		totalPageIndex = new Integer(totalNumber.intValue()/limit);
		if (totalNumber.intValue()%limit > 0) {
			totalPageIndex = new Integer(totalPageIndex.intValue() + 1);
		}
	</#if>
	}
	
	public Class doRetrieve() {
		return null;
	}

	public Class doGoFirstPage() {
	<#if isTigerResource() = true>
		offset = 0;
		${configs.table}Index = offset;
	<#else>
		offset = new Integer(0);
		empIndex = offset.intValue();
	</#if>
		return null;
	}

	public Class doGoPreviousPage() {
	<#if isTigerResource() = true>
		${configs.table}Index = offset;
	<#else>
		${configs.table}Index = offset.intValue();
	</#if>
		if (${configs.table}Index - limit >= 0) {
			${configs.table}Index -= limit;
		}
		return null;
	}

	public Class doGoNextPage() {
	<#if isTigerResource() = true>
		${configs.table}Index = offset;
	<#else>
		${configs.table}Index = offset.intValue();
	</#if>
		prerender();
	<#if isTigerResource() = true>
		if (${configs.table}Index + limit < totalNumber) {
	<#else>
		if (${configs.table}Index + limit < totalNumber.intValue()) {
	</#if>
			${configs.table}Index += limit;
		}
		return null;
	}

	public Class doGoLastPage() {
		prerender();
	<#if isTigerResource() = true>
		offset = (totalPageIndex-1)*limit;
		${configs.table}Index = offset;
	<#else>
		offset = new Integer((totalPageIndex.intValue()-1)*limit);
		empIndex = offset.intValue();
	</#if>
		return null;
	}

	public boolean isDoGoFirstPageDisabled() {
	<#if isTigerResource() = true>
		return offset == 0;
	<#else>
		return offset.intValue() == 0;
	</#if>
	}

	public boolean isDoGoPreviousPageDisabled() {
		return isDoGoFirstPageDisabled();
	}

	public boolean isDoGoNextPageDisabled() {
	<#if isTigerResource() = true>
		return currentPageIndex == totalPageIndex;
	<#else>
		return currentPageIndex.intValue() == totalPageIndex.intValue();
	</#if>
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

<#if isTigerResource() = true>
	@TakeOver(properties = "crudType")
<#else>
	public static final String doCreate_TAKE_OVER = "properties='crudType'";
</#if>
	public Class doCreate() {
		setCrudType(CrudType.CREATE);
		return ${configs.table_capitalize}Edit${configs.pagesuffix}.class;
	}

<#list mappings as mapping>
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
	public ${configs.table_capitalize}[] get${configs.table?cap_first}Items() {
		return this.${configs.table}Items;
	}

	public void set${configs.table?cap_first}Items(${configs.table_capitalize}[] items) {
		this.${configs.table}Items = items;
	}
	
	public int get${configs.table_capitalize}Index() {
		return this.${configs.table}Index;
	}
	
	public void set${configs.table_capitalize}Index(int ${configs.table}Index) {
		this.${configs.table}Index = ${configs.table}Index;
	}

<#if isSelectedExisted() = true>
	<#list selectedColumnsMappings as selectedColumnsMapping>
	public ${getJavaClassName(selectedColumnsMapping)} getText${selectedColumnsMapping.javaFieldName?cap_first}() {
		return this.text${selectedColumnsMapping.javaFieldName?cap_first};
	}

	public void setText${selectedColumnsMapping.javaFieldName?cap_first}(${getJavaClassName(selectedColumnsMapping)} text${selectedColumnsMapping.javaFieldName?cap_first}) {
		this.text${selectedColumnsMapping.javaFieldName?cap_first} = text${selectedColumnsMapping.javaFieldName?cap_first};
	}
	</#list>

	public Integer getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public void setCurrentPageIndex(Integer currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
	
	public Integer getTotalPageIndex() {
		return totalPageIndex;
	}
	
	public void setTotalPageIndex(Integer totalPageIndex) {
		this.totalPageIndex = totalPageIndex;
	}
	
	public Integer getTotalNumber() {
		return totalNumber;
	}
	
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
</#if>
}
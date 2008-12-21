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
	
	public ${configs.table_capitalize}[] ${configs.table}Items;
	
	public int ${configs.table}Index;
	
	<#list selectedColumnsMappings as selectedColumnsMapping>
	public ${getJavaClassName(selectedColumnsMapping)} text${selectedColumnsMapping.javaFieldName?cap_first};
	
	</#list>

	public Integer offset;
	
	public Integer currentPageIndex;
	
	public Integer totalPageIndex;
	
	public Integer totalNumber;
	
	private int limit = 10;
	
	public ${configs.table_capitalize}ListPage() {
	}
	
	public Class initialize() {
		return null;
	}
	
	public Class prerender() {
		//${configs.table}Items = ${configs.table}${configs.daosuffix}.selectAll();
		offset = ${configs.table}Index;
		
		${configs.table?cap_first}PagerCondition dto = new ${configs.table?cap_first}PagerCondition();
		dto.setLimit(limit);
		dto.setOffset(${configs.table}Index);
		
		${configs.table}Items = ${configs.table}Dao.
		  findBy${orderbyString}PagerCondition(
		    ${conditionCallParam}, dto);
		    
		calculatePageIndex();
		
		return null;
	}
	
	public void calculatePageIndex() {
		totalNumber = ${configs.table}Dao.
		  countBy${orderbyString}PagerCondition(
		    ${conditionCallParam});
		
		currentPageIndex = offset/limit+1;
		totalPageIndex = totalNumber/limit;
		if (totalNumber%limit > 0) totalPageIndex++;
	}
	
	public Class doRetrieve() {
		return null;
	}
	
	public Class doGoFirstPage() {
		offset = 0;
		${configs.table}Index = offset;
		return null;
	}
	
	public Class doGoPreviousPage() {
		${configs.table}Index = offset;
		if (${configs.table}Index - limit >= 0) {
			${configs.table}Index -= limit;
		}
		return null;
	}
	  
	public Class doGoNextPage() {
		${configs.table}Index = offset;
		if (${configs.table}Index + limit < ${configs.table}Dao.
			countBy${orderbyString}PagerCondition(
				${conditionCallParam})) {
			${configs.table}Index += limit;
		}
		return null;
	}
	
	public Class doGoLastPage() {
		calculatePageIndex();		
		offset = (totalPageIndex-1)*limit;
		${configs.table}Index = offset;
		return null;
	}
	
	public boolean isFirstPage() {
		if (offset == 0) {
			return false;
		}
		return true;
	}
	
	public boolean isPreviousPage() {
		return isFirstPage();
	}
	
	public boolean isNextPage() {
		if (currentPageIndex == totalPageIndex) {
			return false;
		}
		return true;
	}
	
	public boolean isLastPage() {
		return isNextPage();
	}
	
	
	
	
	
	public String get${configs.table_capitalize}RowClass() {
		if (${configs.table}Index % 2 == 0) {
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
		crudType = CrudType.CREATE;
		return ${configs.table_capitalize}Edit${configs.pagesuffix}.class;
	}
	
<#list mappings as mapping>
<#if mapping.isDate() = true>
<#if isTigerResource() = true>
	@DateTimeConverter
<#else>
	public static final String ${mapping.javaFieldName}_TDateTimeConverter = null;
</#if>
	public ${getJavaClassName(mapping)} get${mapping.javaFieldName?cap_first}() {
		return ${mapping.javaFieldName};
	}

</#if>
</#list>
}
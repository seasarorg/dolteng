package ${configs.rootpackagename}.${configs.daopackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Arguments;
import org.seasar.dao.annotation.tiger.S2Dao;
</#if>
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};
import ${configs.rootpackagename}.${configs.pagingpackagename}.${configs.table_capitalize}PagerCondition;

<#if isTigerResource() = true>
@S2Dao(bean=${configs.table_capitalize}.class)
</#if>
public interface ${configs.table_capitalize}${configs.daosuffix} {
	public static final String findBy${orderbyString}PagerCondition = "ORDER BY ${orderbyStringColumn}";

<#if isTigerResource() = false>
	public Class BEAN = ${configs.table_capitalize}.class;

</#if>
	public ${configs.table_capitalize}[] selectAll();
	
	@Arguments({${conditionArguments}})
	public ${configs.table_capitalize}[] findBy${orderbyString}PagerCondition(
		${conditionParam}, ${configs.table_capitalize}PagerCondition dto);
	
	@Arguments({${conditionArguments}})
	public int countBy${orderbyString}PagerCondition(
		${conditionParam});
	
<#if 0 &lt; countPkeys()>
<#if isTigerResource() = true>
	@Arguments(${createPkeyMethodArgNames()})
<#else>
	public String selectById_ARGS = ${createPkeyMethodArgNames()};
</#if>
</#if>
	public ${configs.table_capitalize} selectById(${createPkeyMethodArgs()});
	
	public int insert(${configs.table_capitalize} ${configs.table});

	public int update(${configs.table_capitalize} ${configs.table});
	
	public int delete(${configs.table_capitalize} ${configs.table});
}
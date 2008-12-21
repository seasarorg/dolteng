package ${configs.rootpackagename}.${configs.daopackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Arguments;
import org.seasar.dao.annotation.tiger.S2Dao;
</#if>
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};

<#if isTigerResource() = true>
@S2Dao(bean=${configs.meisaitable_capitalize}.class)
</#if>
public interface ${configs.meisaitable_capitalize}${configs.daosuffix} {
<#if isTigerResource() = false>
	public Class BEAN = ${configs.meisaitable_capitalize}.class;

</#if>
	public ${configs.meisaitable_capitalize}[] selectAll();
		
<#if 0 &lt; countMeisaiPkeys()>
<#if isTigerResource() = true>
	@Arguments(${createMeisaiPkeyMethodArgNames()})
<#else>
	public String selectById_ARGS = ${createMeisaiPkeyMethodArgNames()};
</#if>
</#if>
	public ${configs.meisaitable_capitalize} selectById(${createMeisaiPkeyMethodArgs()});

<#if 0 &lt; countMeisaiPkeys()>
<#if isTigerResource() = true>
	@Arguments(${createHeadMeisaiPkeyMethodArgNames()})
<#else>
	public String selectById_ARGS = ${createHeadMeisaiPkeyMethodArgNames()};
</#if>
</#if>
	public ${configs.meisaitable_capitalize}[] selectBy${createHeadMeisaiPkeyByName()?cap_first}(${createHeadMeisaiPkeyMethodArgs(false)});
	
	public int insert(${configs.meisaitable_capitalize} ${configs.meisaitable});

	public int update(${configs.meisaitable_capitalize} ${configs.meisaitable});
	
	public int delete(${configs.meisaitable_capitalize} ${configs.meisaitable});
}
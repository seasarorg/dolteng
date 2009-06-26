package ${configs.rootpackagename}.${configs.pagingpackagename};

<#if isTigerResource() = true>
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;
</#if>
${getImports()}
import org.seasar.dao.pager.DefaultPagerCondition;

public class ${configs.table_capitalize}PagerCondition extends DefaultPagerCondition {

	private static final long serialVersionUID = 1L;

<#if isTigerResource() = false>
	public static final String TABLE = "${configs.table_rdb}";

</#if>
	public ${configs.table_capitalize}PagerCondition() {
	}
}
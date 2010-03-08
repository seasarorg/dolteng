package ${configs.rootpackagename}.${configs.servicepackagename};

import java.util.List;

<#if isTigerResource() = true>
import org.seasar.flex2.rpc.remoting.service.annotation.RemotingService;
</#if>

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

<#if isTigerResource() = true>
@RemotingService
</#if>
public class ${configs.table_capitalize}${configs.servicesuffix}  extends AbstractService<${configs.table_capitalize}> {
<#if isTigerResource() = false>
	public static final String REMOTING_SERVICE = "";
	
</#if>

    public ${configs.table_capitalize} findById(${createPkeyMethodArgs()}) {
        return select().id(${createPkeyMethodCallArgsCopy()}).getSingleResult();
    }

    public List<${configs.table_capitalize}> getAllItemsPaged(int startIndex,int numItems){
    	return findAllPaged("${createPkeyMethodCallArgsCopy()}", startIndex, numItems);
	}
	
	public void deleteItem(${createPkeyMethodArgs()}) {
		${configs.table_capitalize} ${configs.table} = findById(${createPkeyMethodCallArgsCopy()});
		 removeItem(${configs.table});
	}
}
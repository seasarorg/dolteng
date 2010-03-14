package ${configs.rootpackagename}.${configs.servicepackagename};

import java.util.List;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}${configs.servicesuffix}  extends AbstractService<${configs.table_capitalize}> {

    public ${configs.table_capitalize} findById(${createPkeyMethodArgs()}) {
        return select().id(${createPkeyMethodCallArgsCopy()}).getSingleResult();
    }
    
    public List<${configs.table_capitalize}> selectAllPaged(int startIndex,int numItems){
    	return findAllPaged("${createPkeyMethodCallArgsCopy()}", startIndex, numItems);
	}
	
	public int deleteItem(${createPkeyMethodArgs()}) {
		${configs.table_capitalize} ${configs.table} = findById(${createPkeyMethodCallArgsCopy()});
		return removeItem(${configs.table});
	}

}
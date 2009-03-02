package ${configs.rootpackagename}.${configs.servicepackagename};

import java.util.List;

import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.table_capitalize};

public class ${configs.table_capitalize}${configs.servicesuffix}  extends AbstractService<${configs.table_capitalize}> {

    public ${configs.table_capitalize} findById(${createPkeyMethodArgs()}) {
        return select().id(${createPkeyMethodCallArgsCopy()}).getSingleResult();
    }
    
	public int remove(${createPkeyMethodArgs()}) {
		${configs.table_capitalize} ${configs.table} = findById(${createPkeyMethodCallArgsCopy()});
		return delete(${configs.table});
	}
	
	public List<${configs.table_capitalize}> fill(){
		return selectAll();
	}
	
	public List<${configs.table_capitalize}> selectAll(){
		return findAll();
	}
		
}
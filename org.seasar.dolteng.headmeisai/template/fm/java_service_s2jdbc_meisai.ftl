package ${configs.rootpackagename}.${configs.servicepackagename};

import java.util.List;
import ${configs.rootpackagename}.${configs.entitypackagename}.${configs.meisaitable_capitalize};

public class ${configs.meisaitable_capitalize}${configs.servicesuffix} extends AbstractService<${configs.meisaitable_capitalize}> {

    public ${configs.meisaitable_capitalize} findById(${createPkeyMethodArgs()}) {
        return select().id(${createPkeyMethodCallArgsCopy()}).getSingleResult();
    }

//    public List<Emp> findByDeptId(String deptId) {
//    	return jdbcManager.from(Emp.class).
//    		where("DEPT_ID = ?", deptId).getResultList();
//    }
	public List<${configs.meisaitable_capitalize}> findBy${createHeadMeisaiPkeyByName()?cap_first}(String ${createHeadMeisaiPkeyByName()?uncap_first}){
		return jdbcManager.from(${configs.meisaitable_capitalize}.class).
			where("${configs.table_rdb}_${createMeisaiPkeySqlColumnName()} = ?", ${createHeadMeisaiPkeyByName()?uncap_first}).getResultList();
	}
}

package ${configs.rootpackagename}.${configs.servicepackagename};

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.GenericUtil;


/**
 * ServiceBase Class for AMF Client.
 * 
 * @author nod
 * @param <T>  Entity Class
 *           
 * 
 */
public abstract class AbstractService<T> {

	/**
	 * JDBCManager Instance.
	 */
	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * Entity Class.
	 */
	protected Class<T> entityClass;

	/**
	 * Path prefix by SQLFile  
	 */
	protected String sqlFilePathPrefix;

	/**
	 * Constractor.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public AbstractService() {
		Map<TypeVariable<?>, Type> map = GenericUtil
				.getTypeVariableMap(getClass());
		for (Class<?> c = getClass(); c != Object.class; c = c.getSuperclass()) {
			if (c.getSuperclass() == AbstractService.class) {
				Type type = c.getGenericSuperclass();
				Type[] arrays = GenericUtil.getGenericParameter(type);
				setEntityClass((Class<T>) GenericUtil.getActualClass(arrays[0],
						map));
				break;
			}
		}
	}

	/**
	 * Constractor.
	 * 
	 * @param entityClass
	 *            Entity Class
	 */
	public AbstractService(Class<T> entityClass) {
		setEntityClass(entityClass);
	}

	/**
	 * Returns AutoSelect
	 * 
	 * @return AutoSelect
	 */
	protected AutoSelect<T> select() {
		return jdbcManager.from(entityClass);
	}

	protected AutoSelect<T> selectPaged(int offset, int limit) {
		return jdbcManager.from(entityClass).orderBy("id").limit(limit).offset(
				offset);
	}

	/**
	 * Returns All Entity.
	 * 
	 * @return All Entity.
	 */
	public List<T> getAllItems() {
		return select().getResultList();
	}

	/**
	 * Returns AutoSelect with Conditions.
	 * 
	 * @param conditions
	 *           
	 * 
	 * @return Entity List
	 * @see AutoSelect#where(Map)
	 */
	public List<T> findByCondition(BeanMap conditions) {
		return select().where(conditions).getResultList();
	}

    public List<T> findAllPaged(String orderByItemName,int startIndex, int numItems){
    	return jdbcManager
    		.from(entityClass)
    		.orderBy(orderByItemName)
    		.limit(numItems)
    		.offset(startIndex)
    		.getResultList();
    }
    
    /**
     * Returns num of Search Count.
     * 
     * @return num of count
     */
    
	public long count() {
		return select().getCount();
	}

	public int insertItem(T entity) {
		return jdbcManager.insert(entity).execute();
	}

	public int updateItem(T entity) {
		return jdbcManager.update(entity).execute();
	}

	public int removeItem(T entity) {
		return jdbcManager.delete(entity).execute();
	}

	protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
			String path) {
		return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path);
	}

	protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
			String path, Object parameter) {
		return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path,
				parameter);
	}

	protected SqlFileUpdate updateBySqlFile(String path) {
		return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path);
	}


	protected SqlFileUpdate updateBySqlFile(String path, Object parameter) {
		return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path, parameter);
	}

	protected void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
		sqlFilePathPrefix = "META-INF/sql/"
				+ StringUtil.replace(entityClass.getName(), ".", "/") + "/";
	}
}
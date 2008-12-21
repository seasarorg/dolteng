package org.seasar.dolteng.headmeisai.factory;

import java.util.Map;

import org.seasar.dolteng.eclipse.model.RootModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory;
import org.seasar.dolteng.headmeisai.model.SAStrutsScaffoldModel;

/**
 * SAStruts専用のScaffoldModelを作成するFactory
 * 
 * @author newta
 */
public class SAStrutsScaffoldModelFactory extends ScaffoldModelFactory {

	@Override
	public RootModel createScaffoldModel(Map<String, String> configs,
			TableNode node) {

		return new SAStrutsScaffoldModel(configs, node, "jsp");
	}

	@Override
	public RootModel createScaffoldModel(Map<String, String> configs,
			TableNode node, Map<Integer, String[]> selectedColumns) {

		return new SAStrutsScaffoldModel(configs, node, "jsp");
	}

    /* (non-Javadoc)
     * @see org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory#createScaffoldModel(java.util.Map, org.seasar.dolteng.eclipse.model.impl.TableNode)
     */
    @Override
    public RootModel createScaffoldModel(Map<String, String> configs,
            TableNode node, Map<Integer, String[]> selectedColumns,
            String meisaiTableName, Map<Integer, String[]> meisaiColumns)
    {
		return new SAStrutsScaffoldModel(configs, node, "jsp");
    }
}

package org.seasar.dolteng.headmeisai.factory;

import java.util.Map;

import org.seasar.dolteng.eclipse.model.RootModel;
import org.seasar.dolteng.eclipse.model.impl.ScaffoldModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory;
import org.seasar.dolteng.headmeisai.model.SAStrutsScaffoldModel;

/**
 * SAStruts+Mayaa専用のScaffoldModelを作成するFactory
 * @author matobat
 */
public class SAStrutsMayaaScaffoldModelFactory extends ScaffoldModelFactory {

	@Override
	public RootModel createScaffoldModel(Map<String, String> configs,
			TableNode node) {
		
		return new SAStrutsScaffoldModel(configs, node, "html");
	}

	@Override
	public RootModel createScaffoldModel(Map<String, String> configs,
			TableNode node, Map<Integer, String[]> selectedColumns) {
		
		return new SAStrutsScaffoldModel(configs, node, "html");
	}

    /* (non-Javadoc)
     * @see org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory#createScaffoldModel(java.util.Map, org.seasar.dolteng.eclipse.model.impl.TableNode)
     */
    @Override
    public RootModel createScaffoldModel(Map<String, String> configs,
            TableNode node, Map<Integer, String[]> selectedColumns,
            String meisaiTableName, Map<Integer, String[]> meisaiColumns)
    {
		return new SAStrutsScaffoldModel(configs, node, "html");
    }
}

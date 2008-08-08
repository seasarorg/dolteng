package org.seasar.dolteng.scaffold.factory;

import java.util.Map;

import org.seasar.dolteng.eclipse.model.RootModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory;
import org.seasar.dolteng.scaffold.model.SAStrutsScaffoldModel;

/**
 * SAStruts専用のScaffoldModelを作成するFactory
 * @author newta
 */
public class SAStrutsScaffoldModelFactory extends ScaffoldModelFactory {

	@Override
	public RootModel createScaffoldModel(Map<String, String> configs,
			TableNode node) {
		
		return new SAStrutsScaffoldModel(configs, node);
	}
}

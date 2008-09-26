package org.seasar.dolteng.scaffold.factory;

import java.util.Map;

import org.seasar.dolteng.eclipse.model.RootModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;
import org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory;
import org.seasar.dolteng.scaffold.model.SAStrutsScaffoldModel;

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
}

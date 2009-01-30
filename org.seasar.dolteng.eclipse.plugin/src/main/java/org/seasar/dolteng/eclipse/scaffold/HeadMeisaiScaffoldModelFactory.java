/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dolteng.eclipse.scaffold;

import java.util.Map;

import org.seasar.dolteng.eclipse.model.RootModel;
import org.seasar.dolteng.eclipse.model.impl.HeadMeisaiScaffoldModel;
import org.seasar.dolteng.eclipse.model.impl.ScaffoldModel;
import org.seasar.dolteng.eclipse.model.impl.TableNode;

/**
 * デフォルトのScaffoldModelを作成する
 * @author newta
 */
public class HeadMeisaiScaffoldModelFactory extends ScaffoldModelFactory {

    /* (non-Javadoc)
     * @see org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory#createScaffoldModel(java.util.Map, org.seasar.dolteng.eclipse.model.impl.TableNode)
     */
    @Override
    public RootModel createScaffoldModel(Map<String, String> configs,
            TableNode node)
    {
        return new HeadMeisaiScaffoldModel(configs, node, null, null, null);
    }


    /* (non-Javadoc)
     * @see org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory#createScaffoldModel(java.util.Map, org.seasar.dolteng.eclipse.model.impl.TableNode)
     */
    @Override
    public RootModel createScaffoldModel(Map<String, String> configs,
            TableNode node, Map<Integer, String[]> selectedColumns)
    {
        return new HeadMeisaiScaffoldModel(configs, node, selectedColumns, null, null);
    }

    /* (non-Javadoc)
     * @see org.seasar.dolteng.eclipse.scaffold.ScaffoldModelFactory#createScaffoldModel(java.util.Map, org.seasar.dolteng.eclipse.model.impl.TableNode)
     */
    @Override
    public RootModel createScaffoldModel(Map<String, String> configs,
            TableNode node, Map<Integer, String[]> selectedColumns,
            String meisaiTableName, Map<Integer, String[]> meisaiColumns)
    {
        return new HeadMeisaiScaffoldModel(configs, node, selectedColumns, meisaiTableName, meisaiColumns);
    }
}

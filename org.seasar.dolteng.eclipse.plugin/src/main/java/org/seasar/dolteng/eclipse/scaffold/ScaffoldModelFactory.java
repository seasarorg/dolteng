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
import org.seasar.dolteng.eclipse.model.impl.TableNode;

/**
 * ScaffoldModelを作成するFactory
 * @author newta
 */
public abstract class ScaffoldModelFactory {

    /**
     * ScaffoldModelを作成します
     * @param configs
     * @param node
     * @return templeteに渡されるmodel
     */
    public abstract RootModel createScaffoldModel(Map<String, String> configs, TableNode node);


    /**
     * ScaffoldModelを作成します
     * @param configs
     * @param node
     * @param selectedColumns テーブル上の選択された列情報 Map<i, String[0]> に列名、Map<i, String[1]> に型
     * @return templeteに渡されるmodel
     */
    public abstract RootModel createScaffoldModel(Map<String, String> configs, TableNode node, 
            Map<Integer, String[]> selectedColumns);

    
    /**
     * ScaffoldModelを作成します
     * @param configs
     * @param node
     * @param selectedColumns テーブル上の選択された列情報 Map<i, String[0]> に列名、Map<i, String[1]> に型
     * @param meisaiTableName 明細テーブルの名前
     * @param meisaiColumns 明細テーブルの列情報
     * 
     * @return templeteに渡されるmodel
     */
    public abstract RootModel createScaffoldModel(Map<String, String> configs, TableNode node, 
            Map<Integer, String[]> selectedColumns, 
            String meisaiTableName, Map<Integer, String[]> meisaiColumns);


}

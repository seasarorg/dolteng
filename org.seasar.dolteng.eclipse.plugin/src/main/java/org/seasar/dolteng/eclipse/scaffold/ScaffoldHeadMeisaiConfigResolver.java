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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.seasar.dolteng.eclipse.Constants;
import org.seasar.dolteng.eclipse.DoltengCore;
import org.seasar.dolteng.eclipse.model.ScaffoldDisplay;
import org.seasar.eclipse.common.util.ExtensionAcceptor;

/**
 * @author taichi
 * 
 */
public class ScaffoldHeadMeisaiConfigResolver {

    protected IProject project;

    protected Map<String, ScaffoldHeadMeisaiConfig> configs = new HashMap<String, ScaffoldHeadMeisaiConfig>();

    public ScaffoldHeadMeisaiConfigResolver(IProject project) {
        this.project = project;
    }

    public void initialize() {
        // ここで拡張ポイントに対する処理をしているようです。
        ExtensionAcceptor.accept(Constants.ID_PLUGIN, "headmeisai",//"scaffold",
                new ExtensionAcceptor.ExtensionVisitor() {
                    public void visit(IConfigurationElement e) {
                        try {
                            System.out.println("-----------------------------------------------------");
                            System.out.println("-----------------------------------------------------");
                            System.out.println("-----------------------------------------------------");
                            System.out.println("e.getName = " + e.getName());
                            System.out.println("-----------------------------------------------------");
                            System.out.println("-----------------------------------------------------");
                            System.out.println("-----------------------------------------------------");
                            if ("templates".equals(e.getName())) {
                                ScaffoldConfigEnabler enabler = (ScaffoldConfigEnabler) e
                                        .createExecutableExtension("enablement");
                                if (enabler.enableFor(project)) {
                                    configs.put(e.getAttribute("id"),
                                            new ScaffoldHeadMeisaiConfig(e));
                                }
                            }
                        } catch (Exception ex) {
                            DoltengCore.log(ex);
                        }
                    }
                });
    }

    public ScaffoldHeadMeisaiConfig getConfig(String id) {
        return this.configs.get(id);
    }

    public ScaffoldDisplay[] getScaffolds() {
        Collection<ScaffoldHeadMeisaiConfig> configs = this.configs.values();
        return configs.toArray(new ScaffoldDisplay[configs.size()]);
    }
}

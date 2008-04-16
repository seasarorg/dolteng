/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.eclipse.common.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.seasar.eclipse.common.CommonPlugin;

/**
 * @author taichi
 * 
 */
public class LaunchConfigurationFactory {

    public static ILaunchConfiguration create(CreationHandler handler) {
        ILaunchConfiguration config = null;
        try {
            ILaunchManager manager = DebugPlugin.getDefault()
                    .getLaunchManager();
            ILaunchConfigurationType type = manager
                    .getLaunchConfigurationType(handler.getTypeName());
            ILaunchConfiguration[] configs = manager
                    .getLaunchConfigurations(type);
            for (ILaunchConfiguration element : configs) {
                if (element.getName().equals(handler.getConfigName())) {
                    if (handler.equals(element)) {
                        config = element;
                    } else {
                        element.delete();
                    }
                    break;
                }
            }
            if (config == null) {
                ILaunchConfigurationWorkingCopy copy = type.newInstance(null,
                        handler.getConfigName());
                handler.setUp(copy);
                config = copy.doSave();
            }
        } catch (CoreException e) {
            CommonPlugin.log(e);
        }
        return config;
    }

    public interface CreationHandler {

        String getTypeName();

        String getConfigName();

        boolean equals(ILaunchConfiguration config);

        void setUp(ILaunchConfigurationWorkingCopy config);
    }
}

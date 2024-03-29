/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.dolteng.core.template;

import java.io.OutputStream;

import org.seasar.dolteng.eclipse.model.RootModel;

/**
 * @author taichi
 * 
 */
public interface TemplateHandler {

    TemplateConfig[] getTemplateConfigs();

    RootModel getProcessModel(TemplateConfig config);

    OutputStream open(TemplateConfig config);

    void begin();

    void done();

    void fail(RootModel model, Exception e);

    void close(OutputStream stream);
}

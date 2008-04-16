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
package org.seasar.eclipse.common.util;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * @author y-komori
 * 
 */
public class FontManager {
    protected static final FontRegistry registry = new FontRegistry(Display
            .getCurrent(), true);

    public static Font get(final String name, final int height, final int style) {
        String descriptor = getDescriptor(name, height, style);
        Font font;
        if (registry.hasValueFor(descriptor)) {
            font = registry.get(descriptor);
        } else {
            FontData fontData = new FontData(name, height, style);
            font = new Font(Display.getCurrent(), fontData);
            registry.put(descriptor, new FontData[] { fontData });
        }
        return font;
    }

    protected static String getDescriptor(final String name, final int height,
            final int style) {
        return name + "_" + height + "_" + style;
    }
}

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

import java.net.URL;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.seasar.eclipse.common.CommonPlugin;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class WorkbenchUtil {

    public static void openUrl(String url) {
        try {
            openUrl(new URL(url), false);
        } catch (Exception e) {
            CommonPlugin.log(e);
        }
    }

    public static void openUrl(URL url, boolean maybeInternal) {
        openUrl(url, maybeInternal, "");
    }

    public static void openUrl(URL url, boolean maybeInternal, String browserId) {
        try {
            IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
                    .getBrowserSupport();
            IWebBrowser browser = null;
            if (maybeInternal && support.isInternalWebBrowserAvailable()) {
                int flag = IWorkbenchBrowserSupport.AS_EDITOR
                        | IWorkbenchBrowserSupport.LOCATION_BAR
                        | IWorkbenchBrowserSupport.NAVIGATION_BAR
                        | IWorkbenchBrowserSupport.STATUS
                        | IWorkbenchBrowserSupport.PERSISTENT;
                browser = support.createBrowser(flag, StringUtil
                        .isEmpty(browserId) ? "" : browserId, null, null);
            } else {
                browser = support.getExternalBrowser();
            }
            if (browser != null) {
                browser.openURL(url);
            }
        } catch (Exception e) {
            CommonPlugin.log(e);
        }
    }

    public static IWorkbenchWindow getWorkbenchWindow() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow result = workbench.getActiveWorkbenchWindow();
        if (result == null && 0 < workbench.getWorkbenchWindowCount()) {
            IWorkbenchWindow[] ws = workbench.getWorkbenchWindows();
            result = ws[0];
        }
        return result;
    }

}

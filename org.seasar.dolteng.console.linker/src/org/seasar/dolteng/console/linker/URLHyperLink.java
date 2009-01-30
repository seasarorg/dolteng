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
/**
 * 
 */
package org.seasar.dolteng.console.linker;

import java.net.URL;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.console.IHyperlink;

public class URLHyperLink implements IHyperlink {

	URL url;

	public URLHyperLink(URL url) {
		this.url = url;
	}

	public void linkActivated() {
		try {
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
					.getBrowserSupport();
			IWebBrowser browser = null;
			if (support.isInternalWebBrowserAvailable()) {
				int flag = IWorkbenchBrowserSupport.AS_EDITOR
						| IWorkbenchBrowserSupport.LOCATION_BAR
						| IWorkbenchBrowserSupport.NAVIGATION_BAR
						| IWorkbenchBrowserSupport.STATUS
						| IWorkbenchBrowserSupport.PERSISTENT;
				browser = support.createBrowser(flag, Activator.PLUGIN_ID,
						null, null);
			} else {
				browser = support.getExternalBrowser();
			}
			if (browser != null) {
				browser.openURL(this.url);
			}
		} catch (Exception e) {
			Activator.log(e);
		}
	}

	public void linkEntered() {
	}

	public void linkExited() {
	}
}
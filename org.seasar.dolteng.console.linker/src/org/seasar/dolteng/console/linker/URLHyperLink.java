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
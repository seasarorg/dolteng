package org.seasar.dolteng.console.anchor;

import java.net.URL;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class HttpPatternMatchListener implements IPatternMatchListenerDelegate {

	public void connect(TextConsole console) {
	}

	public void disconnect() {
	}

	public void matchFound(PatternMatchEvent event) {
		try {
			TextConsole console = (TextConsole) event.getSource();
			IDocument doc = console.getDocument();
			String s = doc.get(event.getOffset(), event.getLength());
			URL url = new URL(s);
			console.addHyperlink(new URLHyperLink(url), event.getOffset(),
					event.getLength());
		} catch (Exception e) {
			Activator.log(e);
		}
	}
}

package org.seasar.dolteng.console.linker.msgid;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;
import org.seasar.dolteng.console.linker.Activator;

public class MessageIdBasedLinker implements IPatternMatchListenerDelegate {

	public void connect(TextConsole console) {
	}

	public void disconnect() {
	}

	public void matchFound(PatternMatchEvent event) {
		try {
			TextConsole console = (TextConsole) event.getSource();
			IDocument doc = console.getDocument();
			String s = doc.get(event.getOffset(), event.getLength());
			String base = s.substring(2, s.length() - 5);
			String id = s.substring(1, s.length() - 1);
			IHyperlink link = Activator.create(base, id);
			if (link != null) {
				console
						.addHyperlink(link, event.getOffset(), event
								.getLength());
			}
		} catch (Exception e) {
			Activator.log(e);
		}
	}
}

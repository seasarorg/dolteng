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

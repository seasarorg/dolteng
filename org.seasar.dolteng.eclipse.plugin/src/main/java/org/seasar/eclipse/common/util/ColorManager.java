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

import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * {@link Color} オブジェクトを管理するためのユーティリティクラスです。<br />
 * 
 * @author y-komori
 */
public class ColorManager {
	private static ColorRegistry registry = new ColorRegistry();

	private static final String SHARP = "#";

	private static final String ZERO = "0";

	private ColorManager() {
	}

	/**
	 * カラーを登録します。<br />
	 * 
	 * @param symbolicName
	 *            カラー名称
	 * @param colorData
	 *            カラーデータ
	 * @see ColorRegistry#put(String, RGB)
	 */
	public static void putColor(final String symbolicName, final RGB colorData) {
		registry.put(symbolicName, colorData);
	}

	/**
	 * カラーを登録します。<br />
	 * カラー名称は <code>colorData</code> をもとに、<code>#RRGGBB</code> となります。<br />
	 * 
	 * @param colorData
	 *            カラーデータ
	 * @see ColorRegistry#put(String, RGB)
	 */
	public static void putColor(final RGB colorData) {
		StringBuffer symbolicName = new StringBuffer(7);
		symbolicName.append(SHARP).append(toFixedHexString(colorData.red))
				.append(toFixedHexString(colorData.green)).append(
						toFixedHexString(colorData.blue));
		putColor(symbolicName.toString(), colorData);
	}

	private static String toFixedHexString(final int value) {
		return (value < 0x10) ? ZERO + Integer.toHexString(value) : Integer
				.toHexString(value);
	}

	/**
	 * 指定された名称の {@link Color} オブジェクトを返します。<br />
	 * 
	 * @param symbolicName
	 *            カラー名称
	 * @return {@link Color} オブジェクト
	 * @see ColorRegistry#get(String)
	 */
	public static Color getColor(final String symbolicName) {
		Color color = registry.get(symbolicName);
		if (color == null) {
			String symbol = putColorByColorText(symbolicName);
			if (symbol != null) {
				color = registry.get(symbol);
			}
		}
		return color;
	}

	/**
	 * 指定された名称の {@link ColorDescriptor} オブジェクトを返します。<br />
	 * 
	 * @param symbolicName
	 *            カラー名称
	 * @return {@link ColorDescriptor} オブジェクト
	 * @see ColorRegistry#getColorDescriptor(String)
	 */
	public static ColorDescriptor getColorDescriptor(final String symbolicName) {
		ColorDescriptor color = registry.getColorDescriptor(symbolicName);
		if (color == null) {
			String symbol = putColorByColorText(symbolicName);
			if (symbol != null) {
				color = registry.getColorDescriptor(symbol);
			}
		}
		return color;
	}

	/**
	 * <code>#RRGGBB</code> 形式のテキストを元にカラーを登録します。<br />
	 * 登録時のカラー名称は、<code>#RRGGBB</code> (ただし、<code>RRGGBB</code>
	 * の部分は大文字に変換される)となります。
	 * 
	 * @param colorText
	 *            <code>#RRGGBB</code> 形式のテキスト
	 * @return 登録したカラー名称
	 */
	public static String putColorByColorText(final String colorText) {
		if (colorText.startsWith(SHARP) && colorText.length() == 7) {
			int red = Integer.parseInt(colorText.substring(1, 3), 16);
			int green = Integer.parseInt(colorText.substring(3, 5), 16);
			int blue = Integer.parseInt(colorText.substring(5, 7), 16);
			RGB rgb = new RGB(red, green, blue);
			String newName = colorText.toUpperCase();
			putColor(newName, rgb);

			return newName;
		}
        return null;
	}
}

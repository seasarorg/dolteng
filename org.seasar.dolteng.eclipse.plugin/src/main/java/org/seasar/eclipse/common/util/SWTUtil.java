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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link SWT} クラスの定数を扱うためのユーティリティクラスです。<br />
 * 
 * @author y-komori
 */
public class SWTUtil {
    private static final Map<String, Integer> constants = new HashMap<String, Integer>();

    private static final Map<String, Integer> colorConstats = new HashMap<String, Integer>();

    static {
        initialize();
    }

    private SWTUtil() {
    }

    private static synchronized void initialize() {
        Field[] fields = SWT.class.getFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())
                    && (field.getType() == Integer.TYPE)) {
                String name = field.getName();
                int constant = FieldUtil.getInt(field);
                constants.put(name, new Integer(constant));

                if (name.startsWith("COLOR_")) {
                    String colorName = name.substring(6);
                    colorConstats.put(colorName, new Integer(constant));
                }
            }
        }
    }

    /**
     * アンダースコアで区切られた文字列を、単語境界を大文字にした文字列に変換します。<br />
     * <p>
     * 【例】<br />
     * MOUSE_DOUBLE_CLICK -> mouseDoubleClick
     * </p>
     * 
     * @param name
     *            変換対象
     * @return 変換結果
     */
    public static String convertConstantName(final String name) {
        StringTokenizer st = new StringTokenizer(name, "_");
        StringBuilder builder = new StringBuilder("");
        while (st.hasMoreTokens()) {
            builder.append(StringUtil.capitalize(st.nextToken().toLowerCase()));
        }
        return builder.toString();
    }

    /**
     * {@link SWT} クラスの持つ定数を返します。<br>
     * 
     * @param name
     *            定数名
     * @return 値。存在しない定数名が指定された場合、<code>SWT.NONE</code>を返します。
     */
    public static int getSWTConstant(final String name) {
        int constant = SWT.NONE;
        Integer constantObj = constants.get(name);
        if (constantObj != null) {
            constant = constantObj.intValue();
        }
        return constant;
    }

    /**
     * カンマ区切りの定数からSWTのスタイルを計算します。<br>
     * 例えば以下のような入力に対して、本メソッドは
     * <code>SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER</code>の計算結果を
     * 戻り値として返します。<br>
     * {@link SWT} クラスに定義されていない定数が指定された場合、無視されます。
     * 
     * 入力例:<code>"HORIZONTAL, SHADOW_IN, CENTER"</code><br>
     * 
     * @param styles
     *            カンマ区切りの定数。
     * @param defaultStyle
     *            <code>styles</code> が <code>null</code> だった場合に返すデフォルト値。
     * @return スタイル値。 引数が <code>null</code> の場合は <code>defalutStyle</code>
     *         を返します。
     */
    public static int getStyle(final String styles, final int defaultStyle) {
        int result = 0;
        if (styles != null) {
            StringTokenizer st = new StringTokenizer(styles, ",");
            while (st.hasMoreTokens()) {
                String style = st.nextToken().trim();
                int constant = getSWTConstant(style);
                if (constant != SWT.NULL) {
                    result |= constant;
                }
            }
        } else {
            result = defaultStyle;
        }
        return result;
    }

    /**
     * カンマ区切りの定数からSWTのスタイルを計算します。<br>
     * 例えば以下のような入力に対して、本メソッドは
     * <code>SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER</code>の計算結果を
     * 戻り値として返します。<br>
     * {@link SWT} クラスに定義されていない定数が指定された場合、無視されます。
     * 
     * 入力例:<code>"HORIZONTAL, SHADOW_IN, CENTER"</code><br>
     * 
     * @param styles
     *            カンマ区切りの定数。
     * @return スタイル値。 引数が <code>null</code> の場合は <code>SWT.NONE</code>
     *         を返します。
     */
    public static int getStyle(final String styles) {
        return getStyle(styles, SWT.NONE);
    }

    /**
     * {@link Color} オブジェクトを生成します。<br>
     * <code>colorString</code> で指定された文字列から {@link Color} オブジェクトを生成します。<br>
     * <code>colorString</code> は #RGB 形式または <code>red</code>、<code>blue</code>
     * 等 {@link SWT} クラスの <code>COLOR_*</code>
     * 定数で用意された文字列が指定できます(いずれも、大文字・小文字どちらも使用可能)。<br>
     * 例: <code>#FF0000</code> を指定した場合、赤を表します。
     * 
     * @param colorString
     *            色を表す文字列。
     * @return <code>Color</code> オブジェクト
     */
    public static Color getColor(final String colorString) {
        Color color = ColorManager.getColor(colorString);
        if (color == null) {
            String symbolicName = colorString.toUpperCase();
            Integer constant = colorConstats.get(symbolicName);
            if (constant != null) {
                Display display = Display.getCurrent();
                if (display != null) {
                    color = display.getSystemColor(constant.intValue());
                    ColorManager.putColor(symbolicName, color.getRGB());
                    color.dispose();
                    color = ColorManager.getColor(symbolicName);
                }
            }
        }
        return color;
    }

    /**
     * 指定された {@link Shell} のイメージをキャプチャしてファイルへ保存します。<br />
     * 
     * @param shell
     *            キャプチャ対象の {@link Shell} オブジェクト
     * @param path
     *            保存先パス
     * @param format
     *            フォーマット({@link ImageLoader#save(String, int)} メソッドのドキュメントを参照)
     */
    public static void saveWindowImage(final Shell shell, final String path,
            final int format) {
        if (shell == null) {
            return;
        }

        Rectangle bounds = shell.getBounds();

        GC gc = new GC(shell.getDisplay(), SWT.BITMAP);
        Image image = new Image(shell.getDisplay(), bounds.width, bounds.height);
        gc.copyArea(image, bounds.x, bounds.y);
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] { image.getImageData() };
        loader.save(path, format);
        image.dispose();
        gc.dispose();
    }
}

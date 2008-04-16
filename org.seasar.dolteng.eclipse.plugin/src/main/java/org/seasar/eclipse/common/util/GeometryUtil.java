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

/**
 * @author y-komori
 * 
 */
public class GeometryUtil {
    /**
     * <code>value</code>の表す数値または割合を元に実際の値を計算します。<br>
     * <ul>
     * <li><code>value</code>が数値のみからなる場合、そのままint値に変換した値を返します。<br>
     * <li><code>value</code>が%で終わる数値を表す場合、<code>parentSize</code>に対する<code>value</code>の割合を返します。
     * </ul>
     * 
     * @param value
     *            計算対象
     * @param parentSize
     *            親サイズ
     * @return 計算結果
     * @throws NumberFormatException
     */
    public static int calcSize(final String value, final int parentSize) {
        int result;
        if (value.endsWith("%")) {
            int percent = Integer.parseInt(value.substring(0,
                    value.length() - 1));
            result = parentSize * percent / 100;
        } else {
            result = Integer.parseInt(value);
        }
        return result;
    }

    /**
     * <code>value</code>を元に実際の位置を計算します。<br>
     * <ul>
     * <li><code>value</code>が数値のみからなる場合、そのままint値に変換した値を返します。<br>
     * <li><code>value</code>が%で終わる数値を表す場合、<code>parentSize</code>に対する<code>value</code>の割合を返します。
     * <li><code>value</code>が top または left の場合、0を返します。
     * <li><code>value</code>が center または middle の場合、(<code>parentSize</code> -
     * <code>targetSize</code>) / 2 を返します。
     * <li><code>value</code>が bottom または right の場合、(<code>parentSize</code> -
     * <code>targetSize</code> を返します。
     * </ul>
     * 
     * @param value
     *            計算対象
     * @param parentSize
     *            親Boxサイズ
     * @param targetSize
     *            自Boxサイズ
     * @return 計算結果
     * @throws NumberFormatException
     */
    public static int calcPosition(final String value, final int parentSize,
            final int targetSize) {
        int position = 0;
        if ("top".equalsIgnoreCase(value) || "left".equalsIgnoreCase(value)) {
            position = 0;
        } else if ("center".equalsIgnoreCase(value)
                || "middle".equalsIgnoreCase(value)) {
            position = (parentSize - targetSize) / 2;
        } else if ("bottom".equalsIgnoreCase(value)
                || "right".equalsIgnoreCase(value)) {
            position = parentSize - targetSize;
        } else {
            position = calcSize(value, parentSize);
        }
        position = (position >= 0) ? position : 0;
        return position;
    }

}

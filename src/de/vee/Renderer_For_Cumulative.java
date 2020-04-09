/*
 * Dancing Numbers - Logistic growth applied to Covid-19
 *
 * A naive approach to predict the spread of virus
 *
 *
 * Copyright (c) 2020 V. Eitner
 *
 * This file is part of "Dancing Numbers".
 *
 * "Dancing Numbers" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "Dancing Numbers" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Dancing Numbers".  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.vee;

import de.vee.model.Input;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;

public class Renderer_For_Cumulative extends XYLineAndShapeRenderer {

    private final Input input;
    private final int mark;
    private final DefaultXYDataset dataset;

    Renderer_For_Cumulative(DefaultXYDataset dataset, Input input, int mark) {
        this.input = input;
        this.mark = mark;
        this.dataset = dataset;
    }

    private Paint getPaint(String key) {
        Paint paint = input.getColor();
        if (key.contains("deat")) paint = new Color(0x600060);
        else if (key.contains("prev")) paint = input.getColor1();
        else if (key.contains("cur")) paint = Color.black;
        return paint;
    }

    @Override
    public Paint getSeriesPaint(int series) {
        if (dataset != null) {
            String key = dataset.getSeriesKey(series).toString().toLowerCase();
            Paint paint = getPaint(key);
            if (paint != null) return paint;
        }
        return super.getSeriesPaint(series);
    }

    @Override
    public Paint getItemOutlinePaint(int series, int column) {
        String key = dataset.getSeriesKey(series).toString().toLowerCase();
        if (key.contains("rep")) {
            if (column >= mark) return Color.red;
        }
        return getPaint(key);
    }

    @Override
    public boolean getUseOutlinePaint() {
        return true;
    }

    @Override
    public boolean getDefaultShapesVisible() {
        return false;
    }

    @Override
    public Boolean getSeriesShapesVisible(int series) {
        return (dataset.getSeriesKey(series).toString().toLowerCase().contains("rep"));
    }

    @Override
    public Boolean getSeriesLinesVisible(int series) {
        return !(dataset.getSeriesKey(series).toString().toLowerCase().contains("rep"));
    }

    @Override
    public Paint getItemPaint(int series, int column) {
        String key = dataset.getSeriesKey(series).toString().toLowerCase();
        if (key.contains("rep")) {
            if (column >= mark) return Color.red;
        }
        return getPaint(key);
    }

    @Override
    public Font getSeriesItemLabelFont(int series) {
        Font f = super.getDefaultItemLabelFont();
        f = f.deriveFont(Font.BOLD, (int) (f.getSize() * 1.2));
        return f;
    }
}

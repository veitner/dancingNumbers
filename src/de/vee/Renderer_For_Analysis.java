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

import java.awt.*;

public class Renderer_For_Analysis extends Renderer_For_Derivative {

    Renderer_For_Analysis() {
        super(null, null);
        setDefaultShapesVisible(false);
        setUseOutlinePaint(true);
        setDefaultOutlinePaint(Color.black);
    }

    @Override
    public Paint getSeriesPaint(int series) {
        return Color.black;
    }

    @Override
    public Boolean getSeriesShapesVisible(int series) {
        return true;
    }

    @Override
    public Boolean getSeriesLinesVisible(int series) {
        return false;
    }

    @Override
    public Paint getItemPaint(int row, int column) {
        return Color.black;
    }

    @Override
    public boolean getItemShapeFilled(int series, int item) {
        return true;
    }

    @Override
    public Paint getItemFillPaint(int row, int column) {
        return Color.black;
    }

    @Override
    public Paint getItemOutlinePaint(int series, int column) {
        return Color.black;
    }


}

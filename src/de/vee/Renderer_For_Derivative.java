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
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;

public class Renderer_For_Derivative extends Renderer_For_Cumulative {

    Renderer_For_Derivative(DefaultXYDataset dataset, Input input) {
        super(dataset, input, Integer.MAX_VALUE);
        setUseOutlinePaint(true);
    }

    protected Paint getPaint(String key) {
        Paint paint = super.getPaint(key);
        if (key.contains("smooth")) paint = new Color(140, 202, 221);
        return paint;
    }

    @Override
    public Paint getSeriesOutlinePaint(int series) {
        return Color.black;
    }

/*
    @Override
    public Paint getItemOutlinePaint(int row, int column) {
        return Color.black;
    }
*/

/*    @Override
    public Paint getSeriesPaint(int series) {
        if (dataset != null) {
            if (dataset.getSeriesKey(series).toString().contains("death")) {
                return new Color(0x600060);
            }
        }
        return Color.black;
    }*/

    @Override
    public Boolean getSeriesShapesVisible(int series) {
        return false;
    }

}

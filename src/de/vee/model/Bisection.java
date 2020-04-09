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

package de.vee.model;

public class Bisection {
    public static int find(double[] x, double v) {
        int imin = 0;
        int imax = x.length - 1;
        int ihalf = x.length / 2;
        while (imin != ihalf) {
            double xx = x[ihalf];
            if (xx - v > 1E-9) {
                imax = ihalf;
                ihalf = (imin + ihalf) / 2;
            } else {
                imin = ihalf;
                ihalf = (int) ((imax + ihalf) / 2. + 0.5);
            }
        }
        return ihalf;
    }
}

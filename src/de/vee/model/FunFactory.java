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

public class FunFactory {
    public static LogisticFunc createFunction(double[] a, double N, double x0, double y0) {
//        return new Function1(a, N, x0, y0);
        return new Gompertz(a, N);
    }

    public static LogisticFunc createFunction(double[] a, double N, double[] x0, double[] y0) {
/*
        int m = 0;
        for (int i = 0; i < x0.length; i++) {
            m++;
            if (y0[i] > 0) break;
        }
        return new Function1(a, N, x0[m], y0[m]);
*/
        return new Gompertz(a, N);
    }
}

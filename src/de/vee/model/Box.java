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

import java.util.Locale;

import static edu.princeton.cs.ErrorFunction.erf;
import static edu.princeton.cs.Gaussian.Phi;

public class Box {
    private double a;
    private double b;
    private static final double sqrt2 = Math.sqrt(0.2e1);

    public Box(double a, double b) {
        super();
        this.a = a;
        this.b = b;
    }

    double psi(double x) {
        double t4 = erf(sqrt2 * x / 0.2e1);
        return (t4 + 0.1e1) / 0.2e1;
    }

    double f(double sigma, double t) {
        return Math.max(0, Phi((b - t) / sigma) - psi((a - t) / sigma));
//        return Math.max(0, psi((b - t) / sigma) - psi((a - t) / sigma));
//        return (psi((b - t) / sigma) - psi((a - t) / sigma));
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Box b = new Box(20, 50);
        for (int i = 0; i < 70; i++) {
            double t = i;
            double y = b.f(2, t);
            System.out.printf("%f %f\n", t, y);
        }
    }

}

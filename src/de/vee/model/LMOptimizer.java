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

public class LMOptimizer extends Model {

    LMOptimizer(double[] x, double[] y, double[] dr, Input input) {
        super(x, y, dr, input);
    }

 /*
   double[][] optimize() {
        RealMatrix weights = new DiagonalMatrix(new double[]{1,1,1,1,1,2,0.1,0.1,1,1});
        // least squares problem to solve : modeled radius should be close to target radius
        LeastSquaresProblem problem = new LeastSquaresBuilder().
                start(y).
                model(new EffectiveMy(type, x)).
                target(targetMyValues).
                weight(weights).
                parameterValidator(validator).
                lazyEvaluation(false).
                maxEvaluations(1000).
                maxIterations(1000).
                build();
        LeastSquaresOptimizer.Optimum optimum = new LevenbergMarquardtOptimizer().optimize(problem);

        for (int i = 0; i < optimum.getPoint().getDimension(); i++) {
            System.out.printf("%.2f %.0f\n", x[i], optimum.getPoint().getEntry(i));
        }
        System.out.println("RMS: " + optimum.getRMS());
        System.out.println("evaluations: " + optimum.getEvaluations());
        System.out.println("iterations: " + optimum.getIterations());

    }*/

    @Override
    public double getDeathRate(double needle) {
        return 0;
    }

    @Override
    public double getShift(double needle) {
        return 0;
    }

    @Override
    public double getP(double needle) {
        return 0;
    }
}

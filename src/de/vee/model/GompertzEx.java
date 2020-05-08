package de.vee.model;

public class GompertzEx extends Gompertz {
    GompertzEx(double[] a, double N) {
        super(a, N);
    }

    @Override
    public double evaluate(double x) {
        double t4 = Math.exp(-a[2] * x);
        double t6 = Math.exp(a[1] / a[2] * t4);
        double t8 = Math.exp(-a[0] * t6);
        return N * t8;
    }

    @Override
    public double derivative(double x) {
        double t4 = Math.exp(-a[2] * x);
        double t8 = Math.exp(a[1] / a[2] * t4);
        double t11 = Math.exp(-a[0] * t8);
        return N * a[0] * a[1] * t11 * t4 * t8;
    }

    @Override
    public double inflectionPoint(double[] x) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public double[] estimateInitialValues(double[] x, double[] y) {
        return new double[]{5, 0.5, 0.05};
    }

    public static double[][] getConstraints() {
        double min = 1;
        double max = 10;
        return new double[][]{
                {min, max},//a0_min,a0_max
                {min / 1000., max}, //a1_min,a1_max
                {min / 500., max / 50.}, //a2_min,a2_max
        };
    }

}

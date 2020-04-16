package de.vee.model;

import static de.vee.model.ErrorFunction.erf;

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
//        return Math.max(0, psi((b - t) / sigma) - psi((a - t) / sigma));
        return (psi((b - t) / sigma) - psi((a - t) / sigma));
    }

}

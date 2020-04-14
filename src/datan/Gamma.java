package datan;

/**
 * A class providing methods for computing the Gamma function and related functions
 *
 * @author Siegmund Brandt.
 */
public final class Gamma {
    static double PI = 3.141592654;
    static double RTWOPI = 2.506628275;
    static double C[] = {76.18009173, -86.50532033, 24.01409822, -1.231739516, 0.120858003E-2, -0.536382E-5};
    static double BIG = 1.E30;
    static double EPSILON = 1.E-8;

    /**
     * returns Gamma function
     */
    public static double gamma(double x) {
        double xx, xh, xgh, s, anum, g, res;
        boolean reflec;
        if (x >= 1.) {
            reflec = false;
            xx = x - 1.;
        } else {
            reflec = true;
            xx = 1. - x;
        }
        xh = xx + 0.5;
        xgh = xx + 5.5;
        s = 1.;
        anum = xx;
        for (int i = 0; i < 6; i++) {
            anum = anum + 1.;
            s = s + C[i] / anum;
        }
        s = s * RTWOPI;
        g = Math.pow(xgh, xh) * s / Math.exp(xgh);
        if (reflec) {
            res = PI * xx / (g * Math.sin(PI * xx));
        } else {
            res = g;
        }
        return res;
    }

    /**
     * returns natural logarithm of Gamma function
     */
    public static double logGamma(double x) {
        double xx, xh, xgh, s, anum, g, res;
        boolean reflec;
        if (x >= 1.) {
            reflec = false;
            xx = x - 1.;
        } else {
            reflec = true;
            xx = 1. - x;
        }
        xh = xx + 0.5;
        xgh = xx + 5.5;
        s = 1.;
        anum = xx;
        for (int i = 0; i < 6; i++) {
            anum = anum + 1.;
            s = s + C[i] / anum;
        }
        s = s * RTWOPI;
        g = xh * Math.log(xgh) + Math.log(s) - xgh;
        if (reflec) {
            res = Math.log(PI * xx) - g - Math.log(Math.sin(PI * xx));
        } else {
            res = g;
        }
        return res;
    }

    /**
     * returns binomial coefficient
     */
    public static double binomial(int n, int k) {
        double res;
        if (k == 0 || n == 0 || k == n) {
            res = 1.;
        } else {
            double an = n;
            double ak = k;
            double albinc = logGamma(an + 1.) - logGamma(ak + 1.) - logGamma(an - ak + 1.);
            res = Math.exp(albinc);
            //res = Math.rint(res);
            System.out.println("n = " + n + ", k = " + k + " an = " + an + ", ak = " + ak + ", res =" + res);
        }
        return res;
    }

    /**
     * returns incomplete Gamma function
     */
    public static double incompleteGamma(double a, double x) {
        double f, s, a0, a1, a2j, a2j1, b0, b1, b2j, b2j1, cf, cfnew, help, fnorm, anum, res;
        boolean loop;
        int iloop;
        double EPSILON = 1.E-8;
        double BIG = 500.;
        double aloggamma = logGamma(a);
        if (x <= a + 1.) {
            // series development
            f = 1. / a;
            s = f;
            anum = a;
            loop = true;
            iloop = 0;
            while (loop) {
                anum = anum + 1.;
                f = x * f / anum;
                s = s + f;
                iloop++;
                if (iloop > 100 || f < EPSILON) loop = false;
            }
            if (x < EPSILON) {
                res = 0.;
            } else {
                help = a * Math.log(x) - x - aloggamma;
                if (Math.abs(help) >= BIG) {
                    res = 0.;
                } else {
                    res = s * Math.exp(help);
                }
            }
        } else {
            // continued fraction
            a0 = 0.;
            b0 = 1.;
            a1 = 1.;
            b1 = x;
            cf = 1.;
            fnorm = 1.;
            loop = true;
            iloop = 0;
            int j = 1;
            while (loop) {
                a2j = j - a;
                a2j1 = j;
                b2j = 1.;
                b2j1 = x;
                a0 = (b2j * a1 + a2j * a0) * fnorm;
                b0 = (b2j * b1 + a2j * b0) * fnorm;
                a1 = b2j1 * a0 + a2j1 * a1 * fnorm;
                b1 = b2j1 * b0 + a2j1 * b1 * fnorm;
                if (b1 != 0.) {
                    // normalize and test for convergence
                    fnorm = 1. / b1;
                    cfnew = a1 * fnorm;
                    if (Math.abs(cfnew - cf) / cf < EPSILON) {
                        loop = false;
                    } else {
                        j++;
                        cf = cfnew;
                    }
                }
            }
            help = a * Math.log(x) - x - aloggamma;
            if (Math.abs(help) > BIG) {
                res = 1.;
            } else {
                res = 1. - Math.exp(help) * cf;
            }
        }
        return res;
    }

    /**
     * returns Beta function
     */
    public static double beta(double z, double w) {
        double res;
        if (w < EPSILON) {
            res = BIG;
        } else {
            res = Math.exp(logGamma(z) + logGamma(w) - logGamma(z + w));
        }
        return res;
    }

    /**
     * returns incomplete Beta function
     */
    public static double incompleteBeta(double aa, double bb, double xx) {
        boolean reflec;
        double xlim, a, b, x, a1, b1, a2, b2, fnorm, cf, rm, apl2m, d2m, d2m1, cfnew;
        double res;
        xlim = (aa + 1.) / (aa + bb + 1.);
        if (xx < xlim) {
            reflec = false;
            a = aa;
            b = bb;
            x = xx;
        } else {
            reflec = true;
            a = bb;
            b = aa;
            x = 1. - xx;
        }
        if (x < EPSILON) {
            cf = 0.;
        } else {
            a1 = 1.;
            b1 = 1.;
            a2 = 1.;
            b2 = 1. - (a + b) * x / (a + 1.);
            fnorm = 1. / b2;
            cf = a2 * fnorm;

            loop:
            for (int m = 1; m <= 100; m++) {
                rm = m;
                apl2m = a + 2. * rm;
                d2m = rm * (b - rm) * x / ((apl2m - 1.) * apl2m);
                d2m1 = -(a + rm) * (a + b + rm) * x / (apl2m * (apl2m + 1.));
                a1 = (a2 + d2m * a1) * fnorm;
                b1 = (b2 + d2m * b1) * fnorm;
                a2 = a1 + d2m1 * a2 * fnorm;
                b2 = b1 + d2m1 * b2 * fnorm;
                if (b2 != 0.) {
                    // renormalize and test for convergence
                    fnorm = 1. / b2;
                    cfnew = a2 * fnorm;
                    if (Math.abs(cf - cfnew) / cf < EPSILON) break loop;
                    cf = cfnew;
                }
            }
            cf = cf * Math.pow(x, a) * Math.pow(1. - x, b) / (a * beta(a, b));
        }
        if (reflec) res = 1. - cf;
        else res = cf;
        return res;
    }

}

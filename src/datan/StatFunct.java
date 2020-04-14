package datan;

/**
 * A class providing methods for computing statistical functions
 *
 * @author Siegmund Brandt.
 */
public final class StatFunct {
    static double PI = 3.141592654;
    static double RTWOPI = 2.506628275;
    static double BIG = 1.E30;
    static double EPSILON = 1.E-8;

    /**
     * returns probability of the binomal distribution
     */
    public static double binomial(int k, int n, double p) {
        double ak, an, albinc, alfact, arg, absarg, res;
        if (n < 1 || k < 0 || k > n) {
            res = 0.;
        } else {
            ak = k;
            an = n;
            albinc = Gamma.logGamma(an + 1.) - Gamma.logGamma(ak + 1.) - Gamma.logGamma(an - ak + 1.);
            if (p <= 0.) {
                if (p <= 0.) res = 1.;
                else res = 0.;
            } else if (p >= 1.) {
                if (k == n) res = 1.;
                else res = 0.;
            } else {
                alfact = Math.log(p) * ak + Math.log(1. - p) * (an - ak);
                arg = alfact + albinc;
                absarg = Math.abs(arg);
                if (absarg < EPSILON) {
                    res = 1.;
                } else if (absarg > BIG) {
                    res = 0.;
                } else {
                    res = Math.exp(arg);
                }
            }
        }
        return res;
    }

    /**
     * returns probability cumulative binomal distribution
     */
    public static double cumulativeBinomial(int k, int n, double p) {
        double res = 0.;
        if (k > 0) {
            for (int j = 0; j < k; j++) {
                res = res + binomial(j, n, p);
            }
        }
        return res;
    }

    /**
     * returns probability of the hypergeometric distribution
     */
    public static double hypergeometric(int k, int n, int kk, int nn) {
        double ak, akk, an, ann, al, all, a, b, c, res;
        an = n;
        ann = nn;
        ak = k;
        akk = kk;
        al = an - ak;
        all = ann - akk;
        if (ak <= akk && an <= ann && al <= all) {
            a = Gamma.logGamma(akk + 1.) - Gamma.logGamma(ak + 1.) - Gamma.logGamma(akk - ak + 1.);
            b = Gamma.logGamma(ann + 1.) - Gamma.logGamma(an + 1.) - Gamma.logGamma(ann - an + 1.);
            c = Gamma.logGamma(all + 1.) - Gamma.logGamma(al + 1.) - Gamma.logGamma(all - al + 1.);
            res = Math.exp(a + c - b);
        } else {
            res = 0.;
        }
        return res;
    }

    /**
     * returns cumulative hypergeometric distribution
     */
    public static double cumulativeHypergeometric(int k, int n, int kk, int nn) {
        double res = 0.;
        if (k > 0) {
            for (int j = 0; j < k; j++) {
                res = res + hypergeometric(j, n, kk, nn);
            }
        }
        return res;
    }

    /**
     * returns probability of the Poisson distribution
     */
    public static double poisson(int k, double lambda) {
        double res = Math.exp(-lambda);
        if (k > 0) {
            for (int j = 1; j <= k; j++) {
                res = res * lambda / (double) j;
            }
        }
        return res;
    }

    /**
     * returns cumulative Poisson distribution
     */
    public static double cumulativePoisson(int k, double lambda) {
        double res = 0.;
        if (k > 0) {
            for (int j = 0; j < k; j++) {
                res = res + poisson(j, lambda);
            }
        }
        return res;
    }

    /**
     * returns quantile of Poisson distribution
     */
    public static double quantilePoisson(int k, double p) {
        double res, x0, x1, xm, f0, f1, fm;
        // boundary of range
        if (p >= 1.) {
            res = BIG;
        } else if (p <= 0.) {
            res = 0.;
        }
        // normal range
        else {
            x0 = 0.;
            x1 = k;
            // bracket quantile
            f1 = p - cumulativePoisson(k, x1);
            double help = cumulativePoisson(k, x1);
            int icount = 0;
            while (f1 < 0. && icount < 10) {
                icount++;
                x1 = 2. * x1;
                f1 = p - cumulativePoisson(k, x1);
            }
            // find quantile
            res = 0.;  //
            loop:
            for (int i = 0; i < 2000; i++) {
                f0 = p - cumulativePoisson(k, x0);
                f1 = p - cumulativePoisson(k, x1);
                if (f0 == 0.) {
                    res = x0;
                    break loop;
                } else if (f1 == 0.) {
                    res = x1;
                    break loop;
                } else {
                    xm = 0.5 * (x0 + x1);
                    if (Math.abs(x0 - x1) > EPSILON) {
                        fm = p - cumulativePoisson(k, xm);
                        if (f0 * fm < 0.) x1 = xm;
                        else x0 = xm;
                    } else {
                        res = xm;
                        break loop;
                    }
                }
            }
        }
        return res;
    }

    /**
     * returns probability density of standard normal distribution
     */
    public static double standardNormal(double x) {
        double f = 0.3989422804014327;
        double res = f * Math.exp(-0.5 * x * x);
        return res;
    }

    /**
     * returns probability density of normal distribution
     */
    public static double normal(double x, double x0, double sigma) {
        double s1 = 1. / sigma;
        double u = (x - x0) * s1;
        double res = s1 * standardNormal(u);
        ;
        return res;
    }

    /**
     * returns cumulative standard normal distribution
     */
    public static double cumulativeStandardNormal(double x) {
        double arg = 0.5 * x * x;
        double s = 1.;
        if (x < 0.) s = -1.;
        double f = Gamma.incompleteGamma(0.5, arg);
        double res = 0.5 * (1. + s * f);
        return res;
    }

    /**
     * returns cumulative normal distribution
     */
    public static double cumulativeNormal(double x, double x0, double sigma) {
        double u = (x - x0) / sigma;
        double res = cumulativeStandardNormal(u);
        return res;
    }

    /**
     * returns quantile of standard normal distribution
     */
    public static double quantileStandardNormal(double p) {
        double res, x0, x1, xm, f0, f1, fm, pp;
        boolean pLessThanHalf = false;
        pp = p;
        // boundary of range
        if (p >= 1.) {
            res = BIG;
        } else if (p <= 0.) {
            res = 0.;
        }
        // normal range
        else {
            pp = Math.abs(p);
            if (p < 0.5) {
                pLessThanHalf = true;
                pp = 1. - p;
            }
            x0 = 0.;
            x1 = pp;
            // bracket quantile
            f1 = pp - cumulativeStandardNormal(x1);
            while (f1 > 0.) {
                x1 = 2. * x1;
                f1 = pp - cumulativeStandardNormal(x1);
            }
            // find quantile
            res = 0.;  //
            loop:
            for (int i = 0; i < 2000; i++) {
                f0 = pp - cumulativeStandardNormal(x0);
                f1 = pp - cumulativeStandardNormal(x1);
                if (f0 == 0.) {
                    res = x0;
                    break loop;
                } else if (f1 == 0.) {
                    res = x1;
                    break loop;
                } else {
                    xm = 0.5 * (x0 + x1);
                    if (Math.abs(x0 - x1) > EPSILON) {
                        fm = pp - cumulativeStandardNormal(xm);
                        if (f0 * fm < 0.) x1 = xm;
                        else x0 = xm;
                    } else {
                        res = xm;
                        break loop;
                    }
                }
            }
        }
        if (pLessThanHalf) res = -res;
        return res;
    }

    /**
     * returns quantile of normal distribution
     */
    public static double quantileNormal(double p, double x0, double sigma) {
        double u = quantileStandardNormal(p);
        double res = u * sigma + x0;
        return res;
    }

    /**
     * returns probability density of chi squared distribution
     */
    public static double chiSquared(double x, int n) {
        double an, b, c, e, res;
        res = 0.;
        if (x >= 0.) {
            if (x != 0. || n > 2) {
                an = n;
                b = Gamma.gamma(0.5 * an) * Math.sqrt(Math.pow(2., an));
                c = Math.sqrt(Math.pow(x, an - 2.));
                e = Math.exp(-0.5 * x);
                res = e * c / b;
            } else {
                if (n == 2) res = 0.5;
                if (n == 1) res = BIG;
            }
        }
        return res;
    }

    /**
     * returns cumulative chi squared distribution
     */
    public static double cumulativeChiSquared(double x, int n) {
        double a, res;
        res = 0.;
        if (x > 0.) {
            a = 0.5 * (double) n;
            res = Gamma.incompleteGamma(a, 0.5 * x);
        }
        return res;
    }

    /**
     * returns quantile of chi squared distribution
     */
    public static double quantileChiSquared(double p, int n) {
        double res, x0, x1, xm, f0, f1, fm;
        // boundary of range
        if (p >= 1.) {
            res = BIG;
        } else if (p <= 0.) {
            res = 0.;
        }
        // normal range
        else {
            x0 = 0.;
            x1 = p;
            // bracket quantile
            f1 = p - cumulativeChiSquared(x1, n);
            while (f1 > 0.) {
                x1 = 2. * x1;
                f1 = p - cumulativeChiSquared(x1, n);
            }
            // find quantile
            res = 0.;  //
            loop:
            for (int i = 0; i < 2000; i++) {
                f0 = p - cumulativeChiSquared(x0, n);
                f1 = p - cumulativeChiSquared(x1, n);
                if (f0 == 0.) {
                    res = x0;
                    break loop;
                } else if (f1 == 0.) {
                    res = x1;
                    break loop;
                } else {
                    xm = 0.5 * (x0 + x1);
                    if (Math.abs(x0 - x1) > EPSILON) {
                        fm = p - cumulativeChiSquared(xm, n);
                        if (f0 * fm < 0.) x1 = xm;
                        else x0 = xm;
                    } else {
                        res = xm;
                        break loop;
                    }
                }
            }
        }
        return res;
    }

    /**
     * returns probability density of F distribution
     */
    public static double fDistribution(double x, int f1, int f2) {
        double af1, af2, af12, af22, afs2, al;
        af1 = f1;
        af2 = f2;
        af12 = 0.5 * af1;
        af22 = 0.5 * af2;
        afs2 = af12 + af22;
        double res = 0.;
        if (x > 0.) {
            al = af12 * Math.log(af1 / af2) - afs2 * Math.log(1. + af1 * x / af2)
                    + (af12 - 1.) * Math.log(x) + Gamma.logGamma(afs2)
                    - Gamma.logGamma(af12) - Gamma.logGamma(af22);
            res = Math.exp(al);
        }
        return res;
    }

    /**
     * returns cumulative F distribution
     */
    public static double cumulativeFDistribution(double x, int f1, int f2) {
        double af1, af2, a, b, arg;
        af1 = f1;
        af2 = f2;
        arg = af2 / (af2 + af1 * x);
        a = 0.5 * af2;
        b = 0.5 * af1;
        double res = 1. - Gamma.incompleteBeta(a, b, arg);
        return res;
    }

    /**
     * returns quantile of F distribution
     */
    public static double quantileFDistribution(double p, int nf1, int nf2) {
        double res, x0, x1, xm, f0, f1, fm;
        // boundary of range
        if (p >= 1.) {
            res = BIG;
        } else if (p <= 0.) {
            res = 0.;
        }
        // normal range
        else {
            x0 = 0.;
            x1 = p;
            // bracket quantile
            f1 = p - cumulativeFDistribution(x1, nf1, nf2);
            while (f1 > 0.) {
                x1 = 2. * x1;
                f1 = p - cumulativeFDistribution(x1, nf1, nf2);
            }
            // find quantile
            res = 0.;  //
            loop:
            for (int i = 0; i < 2000; i++) {
                f0 = p - cumulativeFDistribution(x0, nf1, nf2);
                f1 = p - cumulativeFDistribution(x1, nf1, nf2);
                if (f0 == 0.) {
                    res = x0;
                    break loop;
                } else if (f1 == 0.) {
                    res = x1;
                    break loop;
                } else {
                    xm = 0.5 * (x0 + x1);
                    if (Math.abs(x0 - x1) > EPSILON) {
                        fm = p - cumulativeFDistribution(xm, nf1, nf2);
                        if (f0 * fm < 0.) x1 = xm;
                        else x0 = xm;
                    } else {
                        res = xm;
                        break loop;
                    }
                }
            }
        }
        return res;
    }

    /**
     * returns probability density of Student's distribution
     */
    public static double student(double x, int n) {
        double an, an2, sqan, arg, a, beta;
        an = n;
        an2 = 0.5 * an;
        sqan = Math.sqrt(an);
        arg = -an2 - 0.5;
        a = Math.pow((1. + x * x / an), arg);
        beta = Gamma.beta(0.5, an2);
        double res = a / (beta * sqan);
        return res;
    }

    /**
     * returns cumulative Student's distribution
     */
    public static double cumulativeStudent(double x, int n) {
        double an, an2, sqan, arg, a, res;
        an = n;
        an2 = 0.5 * an;
        arg = an / (an + x * x);
        a = Gamma.incompleteBeta(an2, 0.5, arg);
        if (x >= 0.) res = 1. - 0.5 * a;
        else res = 0.5 * a;
        return res;
    }

    /**
     * returns quantile of Student's distribution
     */
    public static double quantileStudent(double p, int n) {
        double res, x0, x1, xm, f0, f1, fm, pp;
        boolean pLessThanHalf = false;
        pp = p;
        // boundary of range
        if (p >= 1.) {
            res = BIG;
        } else if (p <= 0.) {
            res = 0.;
        }
        // normal range
        else {
            pp = Math.abs(p);
            if (p < 0.5) {
                pLessThanHalf = true;
                pp = 1. - p;
            }
            x0 = 0.;
            x1 = pp;
            // bracket quantile
            f1 = pp - cumulativeStudent(x1, n);
            while (f1 > 0.) {
                x1 = 2. * x1;
                f1 = pp - cumulativeStudent(x1, n);
            }
            // find quantile
            res = 0.;  //
            loop:
            for (int i = 0; i < 2000; i++) {
                f0 = pp - cumulativeStudent(x0, n);
                f1 = pp - cumulativeStudent(x1, n);
                if (f0 == 0.) {
                    res = x0;
                    break loop;
                } else if (f1 == 0.) {
                    res = x1;
                    break loop;
                } else {
                    xm = 0.5 * (x0 + x1);
                    if (Math.abs(x0 - x1) > EPSILON) {
                        fm = pp - cumulativeStudent(xm, n);
                        if (f0 * fm < 0.) x1 = xm;
                        else x0 = xm;
                    } else {
                        res = xm;
                        break loop;
                    }
                }
            }
        }
        if (pLessThanHalf) res = -res;
        return res;
    }


}

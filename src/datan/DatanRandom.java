package datan;

/**
 * A class providing methods for computing random numbers
 */
public final class DatanRandom {
    // constants for mlcg
    static final int M = 2147483563;
    static final int A = 40014;
    static final int Q = 53668;
    static final int R = 12211;
    static final double MINV = 4.656613E-10;
    static int x = 123456;
    // constants for ecuy
    static final int M1 = 2147483563;
    static final int M1MIN1 = 2147483562;
    static final int A1 = 40014;
    static final int Q1 = 53668;
    static final int R1 = 12211;
    static final int M2 = 2147483399;
    static final int A2 = 40692;
    static final int Q2 = 52774;
    static final int R2 = 3791;
    static final double M1INV = 4.656613E-10;
    static int x1 = 123456;
    static int x2 = 654321;
    static DatanMatrix b, d, dplus;
    static DatanVector rr, xx;

    /**
     * MLC generator which returns an array containing n random numbers.
     */
    public static double[] mlcg(int n) {
        double xminv = 4.656613E-10;
        double[] u = new double[n];
        for (int i = 0; i < n; i++) {
            int k = x / Q;
            x = A * (x - k * Q) - k * R;
            if (x < 0) x = x + M;
            u[i] = (double) x * MINV;
        }
        return u;
    }

    /**
     * sets seed for MLC generator.
     */
    public static void setSeedMlcg(int seed) {
        x = seed;
    }

    /**
     * gets current seed of MLC generator.
     */
    public static int getSeedMlcg() {
        return x;
    }

    /**
     * Combination of two MLC generators which returns an array containing n random numbers;
     * the method is also the basis for all other genrators in this class except mlcg.
     */
    public static double[] ecuy(int n) {
        double[] u = new double[n];
        for (int i = 0; i < n; i++) {
            // produce integer random number from first MLCG
            int k = x1 / Q1;
            x1 = A1 * (x1 - k * Q1) - k * R1;
            if (x1 < 0) x1 = x1 + M1;
            // produce integer random number from second MLCG
            k = x2 / Q2;
            x2 = A2 * (x2 - k * Q2) - k * R2;
            if (x2 < 0) x2 = x2 + M2;
            // combine
            int z = x1 - x2;
            if (z < 1) z = z + M1MIN1;
            // normalize and transform to double
            u[i] = (double) z * M1INV;

        }
        return u;
    }

    /**
     * sets seeds for combined generator Ecuy; seeds has 2 elements.
     */
    public static void setSeedsEcuy(int[] seeds) {
        x1 = seeds[0];
        x2 = seeds[1];
    }

    /**
     * gets current seeds of combined generator Ecuy; seeds has 2 elements.
     */
    public static int[] getSeedsEcuy() {
        int[] seeds = new int[2];
        seeds[0] = x1;
        seeds[1] = x2;
        return seeds;
    }

    /**
     * returns an array containing n random numbers following the standard normal distribution;
     * internally the method uses the combined generator Ecuy.
     */
    public static double[] standardNormal(int n) {
        double[] r = new double[n];
        double[] u;
        int i = 0;
        while (i < n) {
            u = ecuy(2);
            double v0 = 2. * u[0] - 1.;
            double v1 = 2. * u[1] - 1.;
            double s = v0 * v0 + v1 * v1;
            if (s < 1.) {
                double root = Math.sqrt(-2. * Math.log(s) / s);
                r[i] = v0 * root;
                if (i < n - 1) r[i + 1] = v1 * root;
                i = i + 2;
            }
        }
        return r;
    }

    /**
     * sets the covariance matrix of a multivariate normal distribution for
     * the subsequent use in the method multivariateNormal(DatanVector aa).
     */

    public static void setCovarianceMatrixForMultivariateNormal(DatanMatrix c) {
        int n = c.getNumberOfColumns();
        b = new DatanMatrix(c);
        b.choleskyInversion();
        d = b.choleskyDecomposition();
        dplus = d.pseudoInverse();
    }

    /**
     * returns a vector whose elements are distributed according to a multivariate normal distribution with mean aa
     * and covariance matrix c; the matrix c has to be set prior to using this method
     * by the method setCovarianceMatrixForMultivariateNormal(DatanMatrix c).
     */

    public static DatanVector multivariateNormal(DatanVector aa) {
        int n = aa.getNumberOfElements();
        double[] r = standardNormal(n);
        DatanVector rr = new DatanVector(r);
        xx = dplus.multiply(rr);
        xx = xx.add(aa);
        return xx;
    }


    /**
     * generates points on a line with measurement errors; equation of line is y=a*t + b;
     * array t will contain abscissa values beginning with t0 and incremented by dt;
     * array y will contain ordinate values with Gaussian error of width sigmay.
     */
    public static void line(double a, double b, double t0, double dt, double sigmay, double[] t, double[] y) {
        double[] r;
        for (int i = 0; i < t.length; i++) {
            t[i] = t0 + (double) i * dt;
            y[i] = a * t[i] + b;
            r = standardNormal(1);
            y[i] = y[i] + r[0] * sigmay;
        }
    }

    /**
     * generates a decay time; it is assumed that the source consists of a fraction a of nuclei
     * with mean life tau1 and a fraction (1-a) of nuclei of mean life tau2.
     */
    public static double radio(double a, double tau1, double tau2) {
        double t;
        double[] r;
        r = ecuy(3);
        if (r[0] < a) t = -tau1 * Math.log(r[1]);
        else t = -tau2 * Math.log(r[2]);
        return t;
    }


}

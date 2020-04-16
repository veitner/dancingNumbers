package datan;

/**
 * A class performing the least-squares fit of a nonlinear function
 * to a set of data,using Marquardt's method.
 *
 * @author Siegmund Brandt.
 */
public class LsqMar {
    static double EPSILON = 1.E-8, T = 1.E-15, LAMBD = 1.E-3;
    boolean converged;
    int n;
    int nst;
    int nstep;
    int r;
    protected int nred;
    int[] list, nsv;
    DatanVector c;
    protected DatanVector t;
    DatanVector x;
    DatanVector x1red;
    DatanVector x2red;
    DatanVector x1;
    DatanVector x2;
    DatanVector y;
    DatanVector deltay;
    DatanVector result;
    DatanMatrix a, cx;
    double[] mm;
    double lambda, m, mmin, m1, m2;  // minimum function
    boolean[] ok;
    boolean finalstep, newpoint;
    LsqFunction f;
    AuxDri ad;

    /**
     * @param t      vector, the elements of which are the controlled variables t_i
     * @param y      vector of the corresponding measured values.
     * @param deltay vector of measurement errors.
     * @param x      vector of first approximations of unknowns.
     * @param list   array containing the elements of a list specifying which of the n variables are fixed (list element = 0) and
     *               which are adjustable (list element = 1).
     * @param luf    user function which must be an extension of the abstract class DatanUserFunction.
     */
    public LsqMar(DatanVector t, DatanVector y, DatanVector deltay, DatanVector x, int[] list, LsqFunction luf) {
        this.t = t;
        this.y = y;
        this.deltay = deltay;
        this.x = x;
        this.list = list;
        n = t.getNumberOfElements();
        r = list.length;
        nred = 0;
        for (int i = 0; i < r; i++) {
            if (list[i] == 1) nred++;
        }
        nsv = new int[nred];
        ok = new boolean[1];
        mm = new double[1];
        f = luf;
        lambda = LAMBD;
// initial value of minimum function
        m = minimumFunction(x);
        nstep = 10000;
        if (nred == 0) {
// trivial case: all variables are fixed
            cx = new DatanMatrix(1, 1);
            cx.setElement(0, 0, 0.);
            converged = true;
        } else {
            ad = new AuxDri(f, t, y, list);
// iteration loop
            finalstep = false;
            loop:
            for (int istep = 0; istep < nstep; istep++) {
// matrix of derivatives a and vector c
                computeMatrixAndVector(x);
// final step                        
                if (finalstep) {
                    x1red = a.singularValueDecomposition(c, mm, 0., nsv, ok);
                    converged = ok[0];
                    if (!converged) break loop;
                    x1 = new DatanVector(r);
                    x1.putSubvector(x1red, list);
                    x1 = x1.add(x);
                    computeMatrixAndVector(x1);
                    cx = a.multiplyTransposedWith(a);
                    cx.choleskyInversion();
                    m = minimumFunction(x1);
                    break loop;
                }
// compute minimum function for two values of lambda
                x1red = new DatanVector(nred);
                x2red = new DatanVector(nred);
                a.marquardt(c, lambda, x1red, x2red, 0., ok);
                converged = ok[0];
                if (!converged) break loop;
                x1 = new DatanVector(r);
                x2 = new DatanVector(r);
                x1.putSubvector(x1red, list);
                x2.putSubvector(x2red, list);
                x1 = x1.add(x);
                x2 = x2.add(x);
                m1 = minimumFunction(x1);
                m2 = minimumFunction(x2);
// evaluate results
                if (m2 <= m + T) {
// reduce lambda and accept new point
                    lambda = 0.1 * lambda;
                    x = new DatanVector(x2);
                    mmin = m2;
                    newpoint = true;
                } else if (m2 > m + T && m1 < m + T) {
// keep current value of lambda and accept new point
                    x = new DatanVector(x1);
                    mmin = m1;
                    newpoint = true;
                } else {
// increase lambda and reject new point
                    lambda = 10. * lambda;
                    newpoint = false;
                }
                if (newpoint) {
// test for break-off criterion
                    if (Math.abs(m - mmin) < EPSILON * Math.abs(mmin) + T) {
                        finalstep = true;
                    } else {
                        m = mmin;
                    }
                }
            }
            if (converged) {
                x = new DatanVector(x1);
            } else {
                cx = new DatanMatrix(1);
                cx.setElement(0, 0, 0.);
            }
        }
        result = new DatanVector(x);
    }

    protected DatanMatrix getMatrixOfDerivatives(DatanVector x) {
        return ad.getMatrixOfDerivatives(x);
    }

    private void computeMatrixAndVector(DatanVector x) {
        a = getMatrixOfDerivatives(x);
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < nred; k++) {
                a.setElement(i, k, a.getElement(i, k) / deltay.getElement(i));
            }
        }
        c = new DatanVector(n);
        for (int i = 0; i < n; i++) {
            c.setElement(i, (y.getElement(i) - f.getValue(x, t.getElement(i))) / deltay.getElement(i));
        }
    }

    protected double minimumFunction(DatanVector x) {
        double mf = 0.;
        for (int i = 0; i < n; i++) {
            mf = mf + Math.pow((y.getElement(i) - f.getValue(x, t.getElement(i))) / deltay.getElement(i), 2.);
        }
        return mf;
    }

    /**
     * @return the vector of unknowns..
     */
    public DatanVector getResult() {
        return result;
    }

    /**
     * @return the covariance matrix ot the unknowns.
     */
    public DatanMatrix getCovarianceMatrix() {
        return cx;
    }

    /**
     * @return the value of the minimum function.
     */
    public double getChiSquare() {
        return m;
    }


    /**
     * @return true if iteration successful, false otherwise.
     */
    public boolean hasConverged() {
        return converged;
    }

}

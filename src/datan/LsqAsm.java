package datan;

/**
 * A class determining the asymmetric errors
 * for a set of parameters determined by the least square method using class LsqMar.
 *
 * @author Siegmund Brandt.
 */
public final class LsqAsm {
    static double EPS = 1.E-4, BIG = 1.E20;
    static int MAXSTEP = 1000;
    int[] list;
    double m, m1, mtarg, w;
    DatanVector t, y, deltay, x, x0;
    DatanMatrix cx;
    LsqFunction luf;
    int nstep = 20;
    boolean converged;

    /**
     * @param t      vector, the elements of which are the controlled variables t_i
     * @param y      vector of the corresponding measured values.
     * @param deltay vector of measurement errors.
     * @param x0     position of minimum.
     * @param cx     covariance matrix at x0.
     * @param m      value of minimum function.
     * @param list   array containing the elements of a list specifying which of the n variables are fixed (list element = 0) and
     *               which are adjustable (list element = 1).
     * @param luf    user function which must be an extension of the abstract class DatanUserFunction.
     */
    public LsqAsm(DatanVector t, DatanVector y, DatanVector deltay, DatanVector x0, int[] list, DatanMatrix cx,
                  double m, LsqFunction luf) {
        this.t = t;
        this.y = y;
        this.deltay = deltay;
        this.x0 = new DatanVector(x0);
        this.cx = new DatanMatrix(cx);
        this.m = m;
        this.w = 0.;
        this.luf = luf;
        this.list = list;
    }

    /**
     * @param t      vector, the elements of which are the controlled variables t_i
     * @param y      vector of the corresponding measured values.
     * @param deltay vector of measurement errors.
     * @param x0     position of minimum (all elements of list are set to 1).
     * @param cx     covariance matrix at x0.
     * @param m      value of minimum function.
     * @param w      probalility w for which cinfidence limits are computed. If w <= 0, then assymetry arrors are computed.
     * @param luf    user function which must be an extension of the abstract class DatanUserFunction.
     */
    public LsqAsm(DatanVector t, DatanVector y, DatanVector deltay, DatanVector x0, DatanMatrix cx,
                  double m, double w, DatanUserFunction luf) {
        this.t = t;
        this.y = y;
        this.deltay = deltay;
        this.x0 = new DatanVector(x0);
        this.cx = new DatanMatrix(cx);
        this.m = m;
        this.w = w;
        this.luf = luf;
        int[] list = new int[x0.getNumberOfElements()];
        for (int i = 0; i < x0.getNumberOfElements(); i++) {
            list[i] = 1;
        }
        this.list = list;
    }

    /**
     * @param w probalility w for which confidence limits are computed. If w <= 0, then assymetry arrors are computed.
     * @return the asymmetric errors. Element [i][0] is negativer deviation, element [i][1] is positive deviation
     * of the (non-fixed) parameter x[i].
     */
    public double[][] getAsymmetricErrors(double w) {
        DatanVector x, xr;
        int n = x0.getNumberOfElements();
        int nred = list.length;
        double[] dx = new double[nred];
        double[][] dxasy = new double[nred][2];
        double dxmin = BIG;
        double del, signum, xbig, xivar, xsmall;
        boolean enclosed;
// set target value of minimum function
        if (w <= 0.) mtarg = m + 1.;
        else mtarg = m + StatFunct.quantileChiSquared(w, nred);
        for (int i = 0; i < nred; i++) {
            if (list[i] == 1) {
                dx[i] = Math.sqrt(cx.getElement(i, i));
            } else {
                dx[i] = 0.;
            }
            dxmin = Math.min(dxmin, dx[i]);
            dxasy[i][0] = 0.;
            dxasy[i][1] = 0.;
        }
        dxmin = 0.1 * dxmin;
        converged = true;
        outerloop:
        for (int ivar = 0; ivar < n; ivar++) {
            if (list[ivar] != 0) {
// fix varaible ivar
                list[ivar] = 0;
                for (int isign = -1; isign <= 1; isign = isign + 2) {
                    signum = (double) isign;
                    del = dx[ivar];
                    double edge = 0.1 * del;
                    enclosed = false;
// set xsmall to x at minimum position
                    xsmall = x0.getElement(ivar);
                    xivar = xsmall + signum * del;
                    xbig = xivar;
                    innerloop:
                    for (int i = 0; i < nstep; i++) {
                        x = new DatanVector(x0);
                        x.setElement(ivar, xivar);
                        LsqMar lm = new LsqMar(t, y, deltay, x, list, luf);
                        converged = lm.hasConverged();
//               if(! converged) break outerloop;
                        xr = lm.getResult();
                        m1 = lm.getChiSquare();
// test for convergence
                        if (Math.abs(m1 - mtarg) < EPS) {
                            if (isign < 0) dxasy[ivar][0] = x0.getElement(ivar) - xr.getElement(ivar);
                            else dxasy[ivar][1] = -x0.getElement(ivar) + xr.getElement(ivar);
                            break innerloop;
                        } else {
                            if (!enclosed) {
// zero was not yet enclosed in last step
                                if (m1 > mtarg) {
// zero is now enclosed, perform first interval halving
                                    enclosed = true;
                                    xbig = xr.getElement(ivar);
                                    xivar = 0.5 * (xbig + xsmall);
                                } else {
// zero not enclosed, widen range
                                    del = 2. * del;
                                    xivar = xsmall + signum * del;
                                }
                            } else {
// continue interval halving
                                if (m1 > mtarg) xbig = xr.getElement(ivar);
                                else xsmall = xr.getElement(ivar);
                                xivar = 0.5 * (xbig + xsmall);
                            }
                        }
                    }
                }
// unfix variable ivar
                list[ivar] = 1;
            }
        }
        return dxasy;
    }

    /**
     * @return true if iteration successful, false otherwise.
     */
    public boolean hasConverged() {
        return converged;
    }

}

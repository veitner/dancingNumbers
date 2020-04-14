package datan;

/**
 * A class computing the Matrix A of derivatives needed int classes LsqNon and LsqMar
 *
 * @author Siegmund Brandt.
 */

public class AuxDri {


    private static double DELTA = 1.E-5;
    private static double CUT = 1.E-9;

    private LsqFunction f;
    private int r;
    private int nny;
    private int nred;
    private int[] list;
    private DatanVector t;
    DatanMatrix a;


    /**
     * @param luf  user function which must implement interface class LsqFunction.
     * @param t    vector of controlled variables.
     * @param y    vector of measured values.
     * @param list array containing the elements of a list specifying which of the n variables are fixed (list element = 0) and
     *             which are adjustable (list element = 1).
     */
    public AuxDri(LsqFunction luf, DatanVector t, DatanVector y, int[] list) {
        f = luf;
        this.list = list;
        this.t = t;
        nny = t.getNumberOfElements();
        r = list.length;
        nred = 0;
        for (int i = 0; i < r; i++) {
            if (list[i] == 1) nred++;
        }
        a = new DatanMatrix(nny, nred);
    }

    /**
     * @param xin position in n-space, where gradient is to be computed.
     * @return the matrix A of derivatives.
     */
    public DatanMatrix getMatrixOfDerivatives(DatanVector xin) {
        DatanVector x = new DatanVector(xin);
        for (int k = 0; k < nny; k++) {
            int il = -1;
            if (nred > 0) {
                for (int i = 0; i < r; i++) {
                    if (list[i] == 1) {
                        il++;
                        double arg = Math.abs(x.getElement(i));
                        if (arg < CUT) arg = CUT;
                        double del = DELTA;// * arg;
                        double sav = x.getElement(i);
                        x.setElement(i, sav + del);
                        double fp = f.getValue(x, t.getElement(k));
                        x.setElement(i, sav - del);
                        double fm = f.getValue(x, t.getElement(k));
                        a.setElement(k, il, (fp - fm) / (del + del));
                    }
                }
            }
        }
        return a;
    }
}

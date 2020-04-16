package subplex;

public class Common {
    /*
        struct {
            doublereal alpha, beta, gamma, delta, psi, omega;
            integer nsmin, nsmax, irepl, ifxsw;
            doublereal bonus, fstop;
            integer nfstop, nfxe;
            doublereal fxstat[4], ftest;
            logical minf, initx, newx;
        } usubc_;
    */
    double alpha, beta, gamma, delta, psi, omega;
    int nsmin, nsmax, irepl, ifxsw;
    double bonus, fstop;
    int nfstop, nfxe;
    double[] fxstat = new double[4];
    double ftest;
    boolean minf, initx, newx;
/*
    struct {
        doublereal fbonus, sfstop, sfbest;
        logical new__;
    } isubc_;
*/

    double fbonus, sfstop, sfbest;
    boolean new__;

    static Common instance;

    public static Common get() {
        if (instance == null) instance = new Common();
        return instance;
    }

    private Common() {
        super();
    }
}

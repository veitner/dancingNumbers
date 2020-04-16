package subplex;
/* fstats.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


/* Common Block Declarations */

/*
struct {
    double alpha, beta, gamma, delta, psi, omega;
    int nsmin, nsmax, irepl, ifxsw;
    double bonus, fstop;
    int nfstop, nfxe;
    double fxstat[4], ftest;
    boolean minf, initx, newx;
} usubc_;

#define usubc_1 usubc_
*/

import static java.lang.StrictMath.*;

class Fstats {

    /*
          subroutine fstats (fx,ifxwt,reset)
c
      int ifxwt
      double precision fx
      boolean reset
c
c                                         Coded by Tom Rowan
c                            Department of Computer Sciences
c                              University of Texas at Austin
c
c fstats modifies the common /usubc/ variables nfxe,fxstat.

     */
    /* Subroutine */
    static void fstats_(double fx, int ifxwt, boolean reset) {
        /* System generated locals */
        Common usubc_1 = Common.get();
        double d__1, d__2, d__3;

        /* Builtin functions */
//		double sqrt (double);

        /* Local variables */
        int nsv;
        double f1sv, fscale;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* fstats modifies the common /usubc/ variables nfxe,fxstat. */

        /* input */

        /*   fx     - most recent evaluation of f at best x */

        /*   ifxwt  - int weight for fx */

        /*   reset  - boolean switch */
        /*            = .true.  : initialize nfxe,fxstat */
        /*            = .false. : update nfxe,fxstat */

        /* common */



        /* local variables */



        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        if (reset) {
            usubc_1.nfxe = ifxwt;
            usubc_1.fxstat[0] = fx;
            usubc_1.fxstat[1] = fx;
            usubc_1.fxstat[2] = fx;
            usubc_1.fxstat[3] = 0.;
        } else {
            nsv = usubc_1.nfxe;
            f1sv = usubc_1.fxstat[0];
            usubc_1.nfxe += ifxwt;
            usubc_1.fxstat[0] += ifxwt * (fx - usubc_1.fxstat[0]) /
                    usubc_1.nfxe;
            usubc_1.fxstat[1] = max(usubc_1.fxstat[1], fx);
            usubc_1.fxstat[2] = min(usubc_1.fxstat[2], fx);
            /* Computing MAX */
            d__1 = abs(usubc_1.fxstat[1]);
            d__2 = abs(usubc_1.fxstat[2]);
            d__1 = max(d__1, d__2);
            fscale = max(d__1, 1.);
            /* Computing 2nd power */
            d__1 = usubc_1.fxstat[3] / fscale;
            /* Computing 2nd power */
            d__2 = (usubc_1.fxstat[0] - f1sv) / fscale;
            /* Computing 2nd power */
            d__3 = (fx - usubc_1.fxstat[0]) / fscale;
            usubc_1.fxstat[3] = fscale * sqrt(((nsv - 1) * (d__1 * d__1) + nsv * (
                    d__2 * d__2) + ifxwt * (d__3 * d__3)) / (usubc_1.nfxe - 1));
        }
    } /* fstats_ */

}
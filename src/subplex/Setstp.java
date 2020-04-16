package subplex;
/* setstp.f -- translated by f2c (version 20100827).
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
    logical minf, initx, newx;
} usubc_;

#define usubc_1 usubc_
*/

import static java.lang.StrictMath.*;
import static subplex.Dasum.dasum_;
import static subplex.Dscal.dscal_;

/* Table of constant values */
class Setstp {
    static int c__1 = 1;

	/*
	      subroutine setstp (nsubs,n,deltax,step)
c
      integer nsubs,n
      double precision deltax(n),step(n)
c
c                                         Coded by Tom Rowan
c                            Department of Computer Sciences
c                              University of Texas at Austin
c
c setstp sets the stepsizes for the corresponding components
c of the solution vector.
c

	 */

    /* Subroutine */
    static double setstp_(int nsubs, int n, double[] deltax,
                          double[] step, int step_offset) {

        Common usubc_1 = Common.get();
        /* System generated locals */
        int i__1;
        double d__1, d__2, d__3;

        /* Builtin functions */
//        double d_sign ( double *,double *);

        /* Local variables */
        int i__;
//        extern /* Subroutine */ int dscal_ ( int *,double *,double *,
//        int *);
//        extern double dasum_ ( int *,double *,int *);
        double stpfac;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* setstp sets the stepsizes for the corresponding components */
        /* of the solution vector. */

        /* input */

        /*   nsubs  - number of subspaces */

        /*   n      - number of components (problem dimension) */

        /*   deltax - vector of change in solution vector */

        /*   step   - stepsizes for corresponding components of */
        /*            solution vector */

        /* output */

        /*   step   - new stepsizes */

        /* common */



        /* local variables */



        /* subroutines and functions */

        /*   blas */
        /*   fortran */

        /* ----------------------------------------------------------- */

        /*     set new step */

        /* Parameter adjustments */
//        --step;
//        --deltax;

        /* Function Body */
        if (nsubs > 1) {
            /* Computing MIN */
            /* Computing MAX */
            d__3 = dasum_(n, deltax, 0, c__1) / dasum_(n, step, step_offset, c__1);
            d__1 = max(d__3, usubc_1.omega);
            d__2 = 1. / usubc_1.omega;
            stpfac = min(d__1, d__2);
        } else {
            stpfac = usubc_1.psi;
        }
        dscal_(n, stpfac, step, step_offset, c__1);

        /*     reorient simplex */

        i__1 = n;
        for (i__ = 1; i__ <= i__1; ++i__) {
            if (deltax[i__] != 0.f) {
                step[i__ + step_offset] = d_sign(step[i__ + step_offset], deltax[i__]);
            } else {
                step[i__ + step_offset] = -step[i__ + step_offset];
            }
            /* L10: */
        }
        return 0;
    } /* setstp_ */

    private static double d_sign(double a, double b) {
        return a * signum(b);
    }
}
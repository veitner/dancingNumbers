package subplex;
/* evalf.f -- translated by f2c (version 20100827).
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

struct {
    double fbonus, sfstop, sfbest;
    boolean new__;
} isubc_;

#define isubc_1 isubc_
*/

import static subplex.Fstats.fstats_;

class Evalf {
    /* Table of constant values */
    static int c__1 = 1;
    static boolean c_true = true;
    static boolean c_false = false;

    /*
          subroutine evalf (f,ns,ips,xs,n,x,sfx,nfe)
c
integer ns,n,nfe
integer ips(*)
double precision f,xs(*),x(n),sfx

     */
    /* Subroutine */
    static double evalf_(D_fp f, int ns, int[] ips, double[] xs,
                         int n, double[] x, double[] sfx, int[] nfe) {
        /* System generated locals */
        Common usubc_1 = Common.get();
        int i__1;

        /* Local variables */
        int i__;
        double fx;
        boolean newbst;
//        extern /* Subroutine */ int fstats_ (double *, int *, boolean *);



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* evalf evaluates the function f at a point defined by x */
        /* with ns of its components replaced by those in xs. */

        /* input */

        /*   f      - user supplied function f(n,x) to be optimized */

        /*   ns     - subspace dimension */

        /*   ips    - permutation vector */

        /*   xs     - double precision ns-vector to be mapped to x */

        /*   n      - problem dimension */

        /*   x      - double precision n-vector */

        /*   nfe    - number of function evaluations */

        /* output */

        /*   sfx    - signed value of f evaluated at x */

        /*   nfe    - incremented number of function evaluations */

        /* common */





        /* local variables */



        /* subroutines and functions */


        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//        --ips;
//        --xs;
//        --x;

        /* Function Body */
        i__1 = ns;
        for (i__ = 1; i__ <= i__1; ++i__) {
            x[ips[i__]] = xs[i__];
            /* L10: */
        }
        usubc_1.newx = usubc_1.new__ || usubc_1.irepl != 2;
//        fx = ( * f)(n, &x[1]);
        fx = f.evaluate(n, x);
        if (usubc_1.irepl == 0) {
            if (usubc_1.minf) {
                sfx[1] = fx;
            } else {
                sfx[1] = -fx;
            }
        } else if (usubc_1.new__) {
            if (usubc_1.minf) {
                sfx[1] = fx;
                newbst = fx < usubc_1.ftest;
            } else {
                sfx[1] = -fx;
                newbst = fx > usubc_1.ftest;
            }
            if (usubc_1.initx || newbst) {
                if (usubc_1.irepl == 1) {
                    fstats_(fx, c__1, c_true);
                }
                usubc_1.ftest = fx;
                usubc_1.sfbest = sfx[1];
            }
        } else {
            if (usubc_1.irepl == 1) {
                fstats_(fx, c__1, c_false);
                fx = usubc_1.fxstat[usubc_1.ifxsw - 1];
            }
            usubc_1.ftest = fx + usubc_1.fbonus * usubc_1.fxstat[3];
            if (usubc_1.minf) {
                sfx[1] = usubc_1.ftest;
                usubc_1.sfbest = fx;
            } else {
                sfx[1] = -usubc_1.ftest;
                usubc_1.sfbest = -fx;
            }
        }
        ++(nfe[1]);
        return sfx[1];
    } /* evalf_ */

}
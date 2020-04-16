package subplex;
/* subplx.f -- translated by f2c (version 20100827).
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

/* Table of constant values */

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.max;
import static subplex.Dcopy.dcopy_;
import static subplex.Evalf.evalf_;
import static subplex.Partx.partx_;
import static subplex.Setstp.setstp_;
import static subplex.Simplx.simplx_;
import static subplex.Sortd.sortd_;
import static subplex.Subopt.subopt_;

class Subplx {
    int c__1 = 1;
    int c__0 = 0;
/*
      subroutine subplx (f,n,tol,maxnfe,mode,scale,x,fx,nfe,
     *                   work,iwork,iflag)
c
      integer n,maxnfe,mode,nfe,iwork(*),iflag
      double precision f,tol,scale(*),x(n),fx,work(*)
c

 */

    /* Initialized data */

    Common usubc_1 = Common.get();

    double[] bnsfac    /* was [3][2] */ = {
            -1., -2., 0., 1., 0., 2.
    };

    /* System generated locals */
    int i__1;
    double d__1, d__2, d__3, d__4, d__5, d__6;

    /* Local variables */
    int i__, ns;
    double scl, dum;
    int ins;
    double sfx;
    boolean cmode;
    /*
            extern */
    /* Subroutine *//*
 int evalf_ (D_fp,int *,int *,double
				*,int *,double *,double *,int *),dcopy_(
        int *,double *,int *,double *,int *);
*/
    int istep;
    double xpscl;
    int nsubs, ipptr;
    /*
            extern */
    /* Subroutine *//*
 int sortd_ ( int *,double *,int *);
        int isptr;
        extern */
    /* Subroutine *//*
 int partx_ ( int *,int *,double *,
        int *,int *);
*/
    int insfnl, ifsptr;
    /*
            extern */
    /* Subroutine *//*
 int simplx_ (D_fp,int *,double *,
        int *,int *,int *,boolean *,double *,
        double *,int *,double *,double *,int *);
*/
    int insptr;
    /*
            extern */
    /* Subroutine *//*
 int subopt_ ( int *),setstp_( int *,
        int *,double *,double *);
*/
    int istptr;
    int isptr;
    private boolean l70 = false;
    private boolean l90 = false;



    /*                                         Coded by Tom Rowan */
    /*                            Department of Computer Sciences */
    /*                              University of Texas at Austin */

    /* subplx uses the subplex method to solve unconstrained */
    /* optimization problems.  The method is well suited for */
    /* optimizing objective functions that are noisy or are */
    /* discontinuous at the solution. */

    /* subplx sets default optimization options by calling the */
    /* subroutine subopt.  The user can override these defaults */
    /* by calling subopt prior to calling subplx, changing the */
    /* appropriate common variables, and setting the value of */
    /* mode as indicated below. */

    /* By default, subplx performs minimization. */

    /* input */

    /*   f      - user supplied function f(n,x) to be optimized, */
    /*            declared external in calling routine */

    /*   n      - problem dimension */

    /*   tol    - relative error tolerance for x (tol .ge. 0.) */

    /*   maxnfe - maximum number of function evaluations */

    /*   mode   - int mode switch with binary expansion */
    /*            (bit 1) (bit 0) : */
    /*            bit 0 = 0 : first call to subplx */
    /*                  = 1 : continuation of previous call */
    /*            bit 1 = 0 : use default options */
    /*                  = 1 : user set options */

    /*   scale  - scale and initial stepsizes for corresponding */
    /*            components of x */
    /*            (If scale(1) .lt. 0., */
    /*            abs(scale(1)) is used for all components of x, */
    /*            and scale(2),...,scale(n) are not referenced.) */

    /*   x      - starting guess for optimum */

    /*   work   - double precision work array of dimension .ge. */
    /*            2*n + nsmax*(nsmax+4) + 1 */
    /*            (nsmax is set in subroutine subopt. */
    /*            default: nsmax = min(5,n)) */

    /*   iwork  - int work array of dimension .ge. */
    /*            n + int(n/nsmin) */
    /*            (nsmin is set in subroutine subopt. */
    /*            default: nsmin = min(2,n)) */

    /* output */

    /*   x      - computed optimum */

    /*   fx     - value of f at x */

    /*   nfe    - number of function evaluations */

    /*   iflag  - error flag */
    /*            = -2 : invalid input */
    /*            = -1 : maxnfe exceeded */
    /*            =  0 : tol satisfied */
    /*            =  1 : limit of machine precision */
    /*            =  2 : fstop reached (fstop usage is determined */
    /*                   by values of options minf, nfstop, and */
    /*                   irepl. default: f(x) not tested against */
    /*                   fstop) */
    /*            iflag should not be reset between calls to */
    /*            subplx. */

    /* common */





    /* local variables */



    /* subroutines and functions */

    /*   blas */
    /*   fortran */

    /* data */

    /* Parameter adjustments */
//        --x;
//        --scale;
//        --work;
//        --iwork;

    /* Function Body */
    /* ----------------------------------------------------------- */

    /* Subroutine */ int subplx_(D_fp f, int n, double tol, int
            maxnfe, int mode, double[] scale, double[] x, double[]
                                         fx, int[] nfe, double[] work, int[] iwork, int[] iflag) {

        if (mode % 2 == 0) {

            /*       first call, check input */

            if (n < 1) {
                return gotoL120(iflag);
//	    goto L120;
            }
            if (tol < 0.) {
                return gotoL120(iflag);
//	    goto L120;
            }
            if (maxnfe < 1) {
                return gotoL120(iflag);
//	    goto L120;
            }
            if (scale[1] >= 0.) {
                i__1 = n;
                for (i__ = 1; i__ <= i__1; ++i__) {
                    xpscl = x[i__] + scale[i__];
                    if (xpscl == x[i__]) {
                        return gotoL120(iflag);
//		    goto L120;
                    }
                    /* L10: */
                }
            } else {
                scl = abs(scale[1]);
                i__1 = n;
                for (i__ = 1; i__ <= i__1; ++i__) {
                    xpscl = x[i__] + scl;
                    if (xpscl == x[i__]) {
                        return gotoL120(iflag);
//		    goto L120;
                    }
                    /* L20: */
                }
            }
            if (mode / 2 % 2 == 0) {
                subopt_(n);
            } else {
                if (usubc_1.alpha <= 0.) {
                    return gotoL120(iflag);
                }
                if (usubc_1.beta <= 0. || usubc_1.beta >= 1.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.gamma <= 1.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.delta <= 0. || usubc_1.delta >= 1.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.psi <= 0. || usubc_1.psi >= 1.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.omega <= 0. || usubc_1.omega >= 1.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.nsmin < 1 || usubc_1.nsmax < usubc_1.nsmin || n <
                        usubc_1.nsmax) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (n < ((n - 1) / usubc_1.nsmax + 1) * usubc_1.nsmin) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.irepl < 0 || usubc_1.irepl > 2) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.ifxsw < 1 || usubc_1.ifxsw > 3) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.bonus < 0.) {
                    return gotoL120(iflag);
//		goto L120;
                }
                if (usubc_1.nfstop < 0) {
                    return gotoL120(iflag);
//		goto L120;
                }
            }

            /*       initialization */

            istptr = n + 1;
            int isptr = istptr + n;
            ifsptr = isptr + usubc_1.nsmax * (usubc_1.nsmax + 3);
            insptr = n + 1;
            if (scale[1] > 0.) {
                dcopy_(n, scale, 1, c__1, work, 1, c__1);
                dcopy_(n, scale, 1, c__1, work, istptr, c__1);
            } else {
                dcopy_(n, new double[]{scl}, 0, c__0, work, 1, c__1);
                dcopy_(n, new double[]{scl}, 0, c__0, work, istptr, c__1);
            }
            i__1 = n;
            for (i__ = 1; i__ <= i__1; ++i__) {
                iwork[i__] = i__;
                /* L30: */
            }
            nfe[1] = 0;
            usubc_1.nfxe = 1;
            if (usubc_1.irepl == 0) {
                usubc_1.fbonus = 0.;
            } else if (usubc_1.minf) {
                usubc_1.fbonus = bnsfac[usubc_1.ifxsw - 1] * usubc_1.bonus;
            } else {
                usubc_1.fbonus = bnsfac[usubc_1.ifxsw + 2] * usubc_1.bonus;
            }
            if (usubc_1.nfstop == 0) {
                usubc_1.sfstop = 0.;
            } else if (usubc_1.minf) {
                usubc_1.sfstop = usubc_1.fstop;
            } else {
                usubc_1.sfstop = -usubc_1.fstop;
            }
            usubc_1.ftest = 0.;
            cmode = false;
            usubc_1.new__ = true;
            usubc_1.initx = true;
            double[] dum_ = {0, 0};
            double[] sfx_ = {sfx, sfx};
            evalf_((D_fp) f, c__0, iwork, dum_, n, x, sfx_, nfe);
            usubc_1.initx = false;
        } else {

            /*       continuation of previous call */

            if (iflag[1] == 2) {
                if (usubc_1.minf) {
                    usubc_1.sfstop = usubc_1.fstop;
                } else {
                    usubc_1.sfstop = -usubc_1.fstop;
                }
                cmode = true;
//	    goto L70;
                l70 = true;
//	    break;
            } else if (iflag[1] == -1) {
                cmode = true;
//	    goto L70;
                l70 = true;
            } else if (iflag[1] == 0) {
                cmode = false;
//	    goto L90;
                l90 = true;
            } else {
                return 0;
            }
        }

        /*     subplex loop */

//        L40:
        iflag[1] = 0;
        while (true) {
            if (!l90) {
                if (!l70) {
                    i__1 = n;
                    for (i__ = 1; i__ <= i__1; ++i__) {
                        work[i__] = abs(work[i__]);
                        /* L50: */
                    }
                    sortd_(n, work, iwork);
                    int[] nsubs_ = {0, 0};
                    partx_(n, iwork, work, nsubs_, iwork, insptr);
                    nsubs = nsubs_[1];
                    dcopy_(n, x, 1, c__1, work, 1, c__1);
                    ins = insptr;
                    insfnl = insptr + nsubs - 1;
                    ipptr = 1;

                    /*       simplex loop */
                }
                l70 = false;
                //        L60:
                while (true) {
                    ns = iwork[ins];
                    L70:
                    simplx_((D_fp) f, n, work, istptr, ns, iwork, ipptr, maxnfe, cmode, x, sfx, nfe, work, isptr, work, ifsptr, iflag);
                    cmode = false;
                    if (iflag[1] != 0) {
                        int res = gotoL110(fx, sfx);
                        return res;
                    }
                    if (ins < insfnl) {
                        ++ins;
                        ipptr += ns;
//	goto L60;
                    }
                    break;
                }
                /*       end simplex loop */

                i__1 = n;
                for (i__ = 1; i__ <= i__1; ++i__) {
                    work[i__] = x[i__] - work[i__];
                    /* L80: */
                }

                /*       check termination */
            }
            l90 = false;
            L90:
            istep = istptr;
            i__1 = n;
            for (i__ = 1; i__ <= i__1; ++i__) {
                /* Computing MAX */
                d__4 = abs(work[i__]);
                d__5 = abs(work[istep]) * usubc_1.psi;
                /* Computing MAX */
                d__6 = abs(x[i__]);
                if (max(d__4, d__5) / max(d__6, 1.) > tol) {
                    setstp_(nsubs, n, work, work, istptr);
//	    goto L40;
                    continue;
                }
                ++istep;
                /* L100: */
            }
            break;
        }

        /*     end subplex loop */


        iflag[1] = 0;
        L110:
        if (usubc_1.minf) {
            fx[1] = sfx;
        } else {
            fx[1] = -sfx;
        }
        return 0;

        /*     invalid input */


/*
        L120:
        iflag[1] = -2;
        return 0;
*/
    } /* subplx_ */

    int gotoL120(int iflag[]) {
        /*     invalid input */
        iflag[1] = -2;
        return 0;
    }

    int gotoL110(double[] fx, double sfx) {
        Common usubc_1 = Common.get();
        if (usubc_1.minf) {
            fx[1] = sfx;
        } else {
            fx[1] = -sfx;
        }
        return 0;

    }

    void doSimplexLoop(D_fp f, int n, int[] iwork, double[] work, int maxnfe, double[] x, int nfe, int[] iflag) {
    }
}
package subplex;/* simplx.f -- translated by f2c (version 20100827).
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

import java.util.Arrays;

import static java.lang.StrictMath.min;
import static subplex.Calcc.calcc_;
import static subplex.Dcopy.dcopy_;
import static subplex.Dist.dist_;
import static subplex.Evalf.evalf_;
import static subplex.Newpt.newpt_;
import static subplex.Order.order_;
import static subplex.Start.start_;

class Simplx {

    /* Table of constant values */

    static final boolean c_true = true;
    static final int c__1 = 1;
    static final boolean c_false = false;

	 /*
	       subroutine simplx (f,n,step,ns,ips,maxnfe,cmode,x,fx,
     *                   nfe,s,fs,iflag)
c
      integer n,ns,maxnfe,nfe,iflag
      integer ips(ns)
      double precision f,step(n),x(n),fx,s(ns,ns+3),fs(ns+1)
      logical cmode
c
c                                         Coded by Tom Rowan
c                            Department of Computer Sciences
c                              University of Texas at Austin
c
c simplx uses the Nelder-Mead simplex method to minimize the
c function f on a subspace.

	  */

    /* Subroutine */
    static int simplx_(D_fp f, int n, double[] step, int step_offset, int
            ns, int[] ips, int ips_offset, int maxnfe, boolean cmode, double[] x,
                       double fx, int[] nfe, double[] s, int soffset, double[] fs, int fsoffset, int[]
                               iflag) {
        Common usubc_1 = Common.get();
        Common isubc_1 = Common.get();
        /* System generated locals */
        int s_dim1, s_offset, i__1;
        double d__1, d__2;

        /* Local variables */
        int i__, j;
        double fc = 0, fe = 0;
        int ih = -1, il = 1;
        double fr;
        int is = 0;
        double dum, tol = 1.;
        int inew;
//		extern double dist_(int *, double *, double *);
        int npts;
/*
		extern */
        /* Subroutine *//*
 int calcc_ (int *, double *, int *,
				int *, boolean *, double *),evalf_(D_fp, int *,
				int *, double *, int *, double *, double *,
				int *);
*/
        int icent;
        boolean small = false;
/*
		extern */
        /* Subroutine *//*
 int order_ (int *, double *, int *,
				int *, int *);
*/
        int itemp;
/*
		extern */
        /* Subroutine *//*
 int dcopy_ (int *, double *, int *,
				double *, int *),newpt_(int *, double *,
				double *, double *, boolean *, double *, boolean *),
				start_(int *, double *, double *, int *, int *
						, double *, boolean *);
*/
        boolean updatc;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* simplx uses the Nelder-Mead simplex method to minimize the */
        /* function f on a subspace. */

        /* input */

        /*   f      - function to be minimized, declared external in */
        /*            calling routine */

        /*   n      - problem dimension */

        /*   step   - stepsizes for corresponding components of x */

        /*   ns     - subspace dimension */

        /*   ips    - permutation vector */

        /*   maxnfe - maximum number of function evaluations */

        /*   cmode  - boolean switch */
        /*            = .true.  : continuation of previous call */
        /*            = .false. : first call */

        /*   x      - starting guess for minimum */

        /*   fx     - value of f at x */

        /*   nfe    - number of function evaluations */

        /*   s      - double precision work array of dimension .ge. */
        /*            ns*(ns+3) used to store simplex */

        /*   fs     - double precision work array of dimension .ge. */
        /*            ns+1 used to store function values of simplex */
        /*            vertices */

        /* output */

        /*   x      - computed minimum */

        /*   fx     - value of f at x */

        /*   nfe    - incremented number of function evaluations */

        /*   iflag  - error flag */
        /*            = -1 : maxnfe exceeded */
        /*            =  0 : simplex reduced by factor of psi */
        /*            =  1 : limit of machine precision */
        /*            =  2 : reached fstop */

        /* common */





        /* local variables */



        /* subroutines and functions */

        /*   blas */
        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
/*
        --x;
        --step;
        --fs;
*/
        s_dim1 = ns;
        s_offset = 1 + s_dim1;
//        s -= s_offset;
//        --ips;

        /* Function Body */
        if (cmode) {
//	goto L50;
            tol = 1;
            updateFlag(iflag, fx, nfe, maxnfe, ns, s, ih, il, s_dim1, tol, small);
        } else {
            npts = ns + 1;
            icent = ns + 2;
            itemp = ns + 3;
            updatc = false;
            boolean[] small_ = {small, small};
            start_(n, x, step, step_offset, ns, ips, ips_offset, s, small_);
            small = small_[1];
            if (small) {
                iflag[1] = 1;
                return 0;
            }
            if (usubc_1.irepl > 0) {
                isubc_1.new__ = false;
                evalf_((D_fp) f, ns, ips, Arrays.copyOfRange(s, s_dim1 + 1, s.length), n, x, fs, nfe);
            } else {
                fs[1] = fx;
            }
            isubc_1.new__ = true;
            i__1 = npts;
            for (j = 2; j <= i__1; ++j) {
                evalf_((D_fp) f, ns, ips, Arrays.copyOfRange(s, j * s_dim1 + 1, s.length), n, x, fs,
                        nfe);
                /* L10: */
            }
            il = 1;
            int[] il_ = {il, il};
            int[] is_ = {is, is};
            int[] ih_ = {ih, ih};
            order_(npts, fs, il_, is_, ih_);
            il = il_[1];
            is = is_[1];
            ih = ih_[1];
            tol = usubc_1.psi * dist_(ns, Arrays.copyOfRange(s, ih * s_dim1 + 1, s.length), Arrays.copyOfRange(s, il * s_dim1 + 1, s.length));

            /*     main loop */

//        L20:
            while (true) {
                while (true) {
                    inew = 0;
                    calcc_(ns, Arrays.copyOfRange(s, s_offset, s.length), ih, inew, updatc, Arrays.copyOfRange(s, icent * s_dim1 + 1, s.length));
                    updatc = true;
                    inew = ih;

                    /*       reflect */

                    small_ = new boolean[]{small, small};
                    newpt_(ns, usubc_1.alpha, slice(s, icent * s_dim1 + 1), slice(s, ih * s_dim1 + 1),
                            c_true, slice(s, itemp * s_dim1 + 1), small_);
                    small = small_[1];
                    if (small) {
//	goto L40;
                        break;
                    }
                    double[] fr_ = {0, 0};
                    evalf_((D_fp) f, ns, ips, slice(s, itemp * s_dim1 + 1), n, x, fr_, nfe);
                    fr = fr_[1];
                    if (fr < fs[il]) {

                        /*         expand */

                        d__1 = -usubc_1.gamma;
                        small_ = new boolean[]{small, small};
                        newpt_(ns, d__1, slice(s, icent * s_dim1 + 1), slice(s, itemp * s_dim1 + 1),
                                c_true, slice(s, ih * s_dim1 + 1), small_);
                        small = small_[1];
                        if (small) {
//	    goto L40;
                            break;
                        }
                        double[] fe_ = {fe, fe};
                        fe = evalf_((D_fp) f, ns, ips, slice(s, ih * s_dim1 + 1), n, x, fe_, nfe);
                        fe = fe_[1];
                        if (fe < fr) {
                            fs[ih] = fe;
                        } else {
                            dcopy_(ns, s, itemp * s_dim1 + 1, c__1, s, ih * s_dim1 + 1,
                                    c__1);
                            fs[ih] = fr;
                        }
                    } else if (fr < fs[is]) {

                        /*         accept reflected point */

                        dcopy_(ns, s, itemp * s_dim1 + 1, c__1, s, ih * s_dim1 + 1, c__1);
                        fs[ih] = fr;
                    } else {

                        /*         contract */

                        if (fr > fs[ih]) {
                            d__1 = -usubc_1.beta;
                            small_ = new boolean[]{small, small};
                            newpt_(ns, d__1, slice(s, icent * s_dim1 + 1), slice(s, ih * s_dim1 + 1),
                                    c_true, slice(s, itemp * s_dim1 + 1), small_);
                            small = small_[1];
                        } else {
                            d__1 = -usubc_1.beta;
                            double[] dum_ = {0, 0};
                            newpt_(ns, d__1, slice(s, icent * s_dim1 + 1), slice(s, itemp * s_dim1 + 1),
                                    c_false, dum_, small_);
                            small = small_[1];

                        }
                        if (small) {
//	    goto L40;
                            break;
                        }
                        double[] fc_ = {fc, fc};
                        evalf_((D_fp) f, ns, ips, slice(s, itemp * s_dim1 + 1), n, x, fc_,
                                nfe);
                        fc = fc_[1];
                        /* Computing MIN */
                        d__1 = fr;
                        d__2 = fs[ih];
                        if (fc < min(d__1, d__2)) {
                            dcopy_(ns, s, itemp * s_dim1 + 1, c__1, s, ih * s_dim1 + 1, c__1);
                            fs[ih] = fc;
                        } else {

                            /*           shrink simplex */

                            i__1 = npts;
                            for (j = 1; j <= i__1; ++j) {
                                if (j != il) {
                                    d__1 = -usubc_1.delta;
                                    double[] dum_ = {0, 0};
                                    newpt_(ns, d__1, slice(s, il * s_dim1 + 1), slice(s, j * s_dim1 + 1),
                                            c_false, dum_, small_);
                                    small = small_[1];
                                    if (small) {
//			goto L40;
                                        break;
                                    }
                                    double[] fs_ = {fs[j], fs[j]};
                                    evalf_((D_fp) f, ns, ips, slice(s, j * s_dim1 + 1), n, x,
                                            fs_, nfe);
                                    fs[j] = fs_[1];
                                }
                                /* L30: */
                            }
                        }
                        updatc = false;
                    }

                    order_(npts, fs, il_, is_, ih_);
                    il = il_[1];
                    is = is_[1];
                    ih = ih_[1];
                    break;
                }

                /*       check termination */

                L40:
                if (usubc_1.irepl == 0) {
                    fx = fs[il];
                } else {
                    fx = isubc_1.sfbest;
                }

                L50:
                if (updateFlag(iflag, fx, nfe, maxnfe, ns, s, ih, il, s_dim1, tol, small)) {
                    break;
                }
/*
            if (usubc_1.nfstop > 0 && fx <= isubc_1.sfstop && usubc_1.nfxe >=
                    usubc_1.nfstop) {
                iflag[1] = 2;
                break;
            } else if (nfe >= maxnfe) {
                iflag[1] = -1;
                break;
            } else if (dist_(ns, slice(s, ih * s_dim1 + 1), slice(s, il * s_dim1 + 1)) <= tol ||
                    small) {
                iflag[1] = 0;
                break;
            } else {
                continue;
//	goto L20;
            }
*/
            }

            /*     end main loop, return best point */
        }
        i__1 = ns;
        for (i__ = 1; i__ <= i__1; ++i__) {
            x[ips[i__]] = s[i__ + il * s_dim1];
            /* L60: */
        }
        return 0;
    } /* simplx_ */

    static boolean updateFlag(int[] iflag, double fx, int[] nfe, int maxnfe, int ns, double[] s, int ih, int il, int s_dim1, double tol, boolean small) {

        Common usubc_1 = Common.get();
        Common isubc_1 = usubc_1;
        if (usubc_1.nfstop > 0 && fx <= isubc_1.sfstop && usubc_1.nfxe >=
                usubc_1.nfstop) {
            iflag[1] = 2;
            return true;
        } else if (nfe[1] >= maxnfe) {
            iflag[1] = -1;
            return true;
        } else if (dist_(ns, slice(s, ih * s_dim1 + 1), slice(s, il * s_dim1 + 1)) <= tol ||
                small) {
            iflag[1] = 0;
            return true;
        }
        return false;
    }

    static double[] slice(double[] a, int i) {
        return Arrays.copyOfRange(a, i, a.length);
    }
}
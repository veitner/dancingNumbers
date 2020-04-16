package subplex;
/* calcc.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


import java.util.Arrays;

import static subplex.Daxpy.daxpy_;
import static subplex.Dcopy.dcopy_;
import static subplex.Dscal.dscal_;

class Calcc {

    /* Table of constant values */

    static double[] c_b3 = {0.};
    static int c__0 = 0;
    static int c__1 = 1;
    static double[] c_b7 = {1.};

    /*
        subroutine calcc (ns,s,ih,inew,updatc,c)
        c
        int ns,ih,inew
        double precision s(ns,ns+3),c(ns)
        logical updatc
    */
    /* Subroutine */
    static int calcc_(int ns, double[] s, int ih, int inew, boolean updatc, double[] c__) {
        /* System generated locals */
        int s_dim1, s_offset, i__1;
        double d__1;

        /* Local variables */
        int i__, j;
/*
        extern */
        /* Subroutine *//*
 int dscal_ (int *, double *, double *,
                int *),dcopy_(int *, double *, int *, double
                *, int *), daxpy_(int *, double *, double *,
                int *, double *, int *);
*/



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* calcc calculates the centroid of the simplex without the */
        /* vertex with highest function value. */

        /* input */

        /*   ns     - subspace dimension */

        /*   s      - double precision work space of dimension .ge. */
        /*            ns*(ns+3) used to store simplex */

        /*   ih     - index to vertex with highest function value */

        /*   inew   - index to new point */

        /*   updatc - logical switch */
        /*            = .true.  : update centroid */
        /*            = .false. : calculate centroid from scratch */

        /*   c      - centroid of the simplex without vertex with */
        /*            highest function value */

        /* output */

        /*   c      - new centroid */

        /* local variables */


        /* subroutines and functions */

        /*   blas */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//        --c__;
        s_dim1 = ns;
        s_offset = 1 + s_dim1;
//        s -= s_offset;

        /* Function Body */
        if (updatc) {
            if (ih == inew) {
                return 0;
            }
            i__1 = ns;
            for (i__ = 1; i__ <= i__1; ++i__) {
                c__[i__] += (s[i__ + inew * s_dim1] - s[i__ + ih * s_dim1]) / ns;
                /* L10: */
            }
        } else {
            dcopy_(ns, c_b3, 1, c__0, c__, 1, c__1);
            i__1 = ns + 1;
            for (j = 1; j <= i__1; ++j) {
                if (j != ih) {

                    daxpy_(ns, c_b7, Arrays.copyOfRange(s, j * s_dim1 + 1, s.length), c__1, c__, c__1);
                }
                /* L20: */
            }
            d__1 = 1. / ns;
            dscal_(ns, d__1, c__, 0, c__1);
        }
        return 0;
    } /* calcc_ */

}
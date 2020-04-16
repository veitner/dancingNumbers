package subplex;
/* start.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


import static subplex.Dcopy.dcopy_;

class Start {
    /* Table of constant values */

    static int c__1 = 1;

    /* Subroutine */
    static void start_(int n, double[] x, double[] step, int step_offset,
                       int ns, int[] ips, int ips_offset, double[] s, boolean[] small) {
        /* System generated locals */
        int s_dim1, s_offset, i__1;

        /* Local variables */
        int i__, j;
//		extern /* Subroutine */ int dcopy_ (int *, double *, int *,
//				double *, int *);



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* start creates the initial simplex for simplx minimization. */

        /* input */

        /*   n      - problem dimension */

        /*   x      - current best point */

        /*   step   - stepsizes for corresponding components of x */

        /*   ns     - subspace dimension */

        /*   ips    - permutation vector */


        /* output */

        /*   s      - first ns+1 columns contain initial simplex */

        /*   small  - boolean flag */
        /*            = .true.  : coincident points */
        /*            = .false. : otherwise */

        /* local variables */


        /* subroutines and functions */

        /*   blas */
        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//		--ips;
//		--step;
//		--x;
        s_dim1 = ns;
        s_offset = 1 + s_dim1;
//		s -= s_offset;

        /* Function Body */
        i__1 = ns;
        for (i__ = 1; i__ <= i__1; ++i__) {
            s[i__ + s_dim1] = x[ips[i__ + ips_offset]];
            /* L10: */
        }
        i__1 = ns + 1;
        for (j = 2; j <= i__1; ++j) {
            dcopy_(ns, s, s_dim1 + 1, c__1, s, j * s_dim1 + 1, c__1);
            s[j - 1 + j * s_dim1] = s[j - 1 + s_dim1] + step[ips[j - 1 + ips_offset] + step_offset];
            /* L20: */
        }

        /* check for coincident points */

        i__1 = ns + 1;
        for (j = 2; j <= i__1; ++j) {
            if (s[j - 1 + j * s_dim1] == s[j - 1 + s_dim1]) {
//	    goto L40;
                small[1] = true;
                return;
            }
            /* L30: */
        }
        small[1] = false;

        /* coincident points */

//		L40:
//    small[1] = true;
//		return 0;
    } /* start_ */

}
package subplex;
/* dist.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;

class Dist {
    /*
          double precision function dist (n,x,y)
    c
          int n
          double precision x(n),y(n)
    c
    c                                         Coded by Tom Rowan
    c                            Department of Computer Sciences
    c                              University of Texas at Austin
    c
    c dist calculates the distance between the points x,y.

     */
    static double dist_(int n, double[] x, double[] y) {
        /* System generated locals */
        int i__1;
        double ret_val, d__1;

        /* Builtin functions */
//        double sqrt (double);

        /* Local variables */
        int i__;
        double sum, scale, absxmy;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* dist calculates the distance between the points x,y. */

        /* input */

        /*   n      - number of components */

        /*   x      - point in n-space */

        /*   y      - point in n-space */

        /* local variables */


        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//        --y;
//        --x;

        /* Function Body */
        absxmy = abs(x[1] - y[1]);
        if (absxmy <= 1.) {
            sum = absxmy * absxmy;
            scale = 1.;
        } else {
            sum = 1.;
            scale = absxmy;
        }
        i__1 = n;
        for (i__ = 2; i__ <= i__1; ++i__) {
            absxmy = abs(x[i__] - y[i__]);
            if (absxmy <= scale) {
                /* Computing 2nd power */
                d__1 = absxmy / scale;
                sum += d__1 * d__1;
            } else {
                /* Computing 2nd power */
                d__1 = scale / absxmy;
                sum = sum * (d__1 * d__1) + 1.;
                scale = absxmy;
            }
            /* L10: */
        }
        ret_val = scale * sqrt(sum);
        return ret_val;
    } /* dist_ */

}
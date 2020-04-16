package subplex;
/* dasum.f -- translated by f2c (version 20100827).
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

class Dasum {
    /*
          double precision function dasum(n,dx,incx)
c
c     takes the sum of the absolute values.
c     uses unrolled loops for increment equal to one.
c     jack dongarra, linpack, 3/11/78.
c     modified to correct problem with negative increment, 8/21/90.
c
      double precision dx(1),dtemp
      int i,incx,ix,m,mp1,n

     */
    static double dasum_(int n, double[] dx, int step_offset, int incx) {
        /* System generated locals */
        int i__1;
        double ret_val;

        /* Local variables */
        int i__, m, ix, mp1;
        double dtemp;


        /*     takes the sum of the absolute values. */
        /*     uses unrolled loops for increment equal to one. */
        /*     jack dongarra, linpack, 3/11/78. */
        /*     modified to correct problem with negative increment, 8/21/90. */


        /* Parameter adjustments */
//		--dx;

        /* Function Body */
        ret_val = 0.;
        dtemp = 0.;
        if (n <= 0) {
            return ret_val;
        }
        if (incx != 1) {
//		if (incx == 1){
//	goto L20;
//		}

            /*        code for increment not equal to 1 */

            ix = 1;
            if (incx < 0) {
                ix = (-(n) + 1) * incx + 1;
            }
            i__1 = n;
            for (i__ = 1; i__ <= i__1; ++i__) {
                dtemp += abs(dx[ix]);
                ix += incx;
                /* L10: */
            }
            ret_val = dtemp;
            return ret_val;

            /*        code for increment equal to 1 */


            /*        clean-up loop */
        } else {
//			L20:
            m = n % 6;
            if (m != 0) {
//		if (m == 0) {
//	goto L40;
//		}
                i__1 = m;
                for (i__ = 1; i__ <= i__1; ++i__) {
                    dtemp += abs(dx[i__]);
                    /* L30: */
                }
                if (n < 6) {
//	goto L60;
                    return dtemp;
                }
//					L40:
                mp1 = m + 1;
                i__1 = n;
                for (i__ = mp1; i__ <= i__1; i__ += 6) {
                    dtemp = dtemp + abs(dx[i__]) + abs(dx[i__ + 1]) + abs(dx[i__ + 2]) + abs(dx[i__
                            + 3]) + abs(dx[i__ + 4]) + abs(dx[i__ + 5]);
                    /* L50: */
                }

//				L60:
                ret_val = dtemp;
                return ret_val;
            } /* dasum_ */
        }
        return ret_val;
    }
}
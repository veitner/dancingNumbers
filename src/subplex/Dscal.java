package subplex;
/* dscal.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/

class Dscal {
    /*
          subroutine  dscal(n,da,dx,incx)
c
c     scales a vector by a constant.
c     uses unrolled loops for increment equal to one.
c     jack dongarra, linpack, 3/11/78.
c     modified to correct problem with negative increment, 8/21/90.
c
      double precision da,dx(1)

     */

    /* Subroutine */
    static void dscal_(int n, double da, double[] dx,
                       int x_offset, int incx) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__, m, ix, mp1;


        /*     scales a vector by a constant. */
        /*     uses unrolled loops for increment equal to one. */
        /*     jack dongarras, linpack, 3/11/78. */
        /*     modified to correct problem with negative increment, 8/21/90. */


        /* Parameter adjustments */
//        --dx;

        /* Function Body */
        if (n <= 0) {
            return;
        }
        if (incx != 1) {
//        if (incx == 1){
//	goto L20;
//        }

            /*        code for increment not equal to 1 */

            ix = 1;
            if (incx < 0) {
                ix = (-(n) + 1) * incx + 1;
            }
            ix += x_offset;
            i__1 = n;
            for (i__ = 1; i__ <= i__1; ++i__) {
                dx[ix] = da * dx[ix];
                ix += incx;
                /* L10: */
            }
            return;

            /*        code for increment equal to 1 */


            /*        clean-up loop */

        }
//        L20:
        m = n % 5;
        if (m != 0) {
//        if (m == 0) {
//	goto L40;
//        }
            i__1 = m;
            for (int i_ = 1; i_ <= i__1; ++i_) {
                i__ = i_ + x_offset;
                dx[i__] = da * dx[i__];
                /* L30: */
            }
            if (n < 5) {
                return;
            }
        }
//        L40:
        mp1 = m + 1;
        i__1 = n;
        for (int i_ = mp1; i_ <= i__1; i_ += 5) {
            i__ = i_ + x_offset;
            dx[i__] = da * dx[i__];
            dx[i__ + 1] = da * dx[i__ + 1];
            dx[i__ + 2] = da * dx[i__ + 2];
            dx[i__ + 3] = da * dx[i__ + 3];
            dx[i__ + 4] = da * dx[i__ + 4];
            /* L50: */
        }
    } /* dscal_ */

}
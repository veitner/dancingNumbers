package subplex;
/* dcopy.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/

class Dcopy {
    /*
  subroutine  dcopy(n,dx,incx,dy,incy)
c
c     copies a vector, x, to a vector, y.
c     uses unrolled loops for increments equal to one.
c     jack dongarra, linpack, 3/11/78.
c
  double precision dx(1),dy(1)
  int i,incx,incy,ix,iy,m,mp1,n

*/
    /* Subroutine */
    static void dcopy_(int n, double[] dx, int idx, int incx,
                       double[] dy, int idy, int incy) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__, m, ix, iy, mp1;


        /*     copies a vector, x, to a vector, y. */
        /*     uses unrolled loops for increments equal to one. */
        /*     jack dongarra, linpack, 3/11/78. */


        /* Parameter adjustments */
//        --dy;
//        --dx;

        /* Function Body */
        if (n <= 0) {
            return;
        }
        if (!(incx == 1 && incy == 1)) {
//        if (incx == 1 && incy == 1) {
//	goto L20;
//        }

            /*        code for unequal increments or equal increments */
            /*          not equal to 1 */

            ix = 1;
            iy = 1;
            if (incx < 0) {
                ix = (-(n) + 1) * incx + 1;
            }
            if (incy < 0) {
                iy = (-(n) + 1) * incy + 1;
            }
            i__1 = n;
            for (i__ = 1; i__ <= i__1; ++i__) {
                dy[iy + idy] = dx[ix + idx];
                ix += incx;
                iy += incy;
                /* L10: */
            }
            return;

            /*        code for both increments equal to 1 */


            /*        clean-up loop */
        }
//        L20:
        m = n % 7;
        if (m != 0) {
//        if (m == 0) {
//	goto L40;
//        }
            i__1 = m;
            for (i__ = 1; i__ <= i__1; ++i__) {
                dy[i__ + idy] = dx[i__ + idx];
                /* L30: */
            }
            if (n < 7) {
                return;
            }
        }
//        L40:
        mp1 = m + 1;
        i__1 = n;
        for (i__ = mp1; i__ <= i__1; i__ += 7) {
            dy[i__ + idy] = dx[i__ + idx];
            dy[i__ + 1 + idy] = dx[i__ + 1 + idx];
            dy[i__ + 2 + idy] = dx[i__ + 2 + idx];
            dy[i__ + 3 + idy] = dx[i__ + 3 + idx];
            dy[i__ + 4 + idy] = dx[i__ + 4 + idx];
            dy[i__ + 5 + idy] = dx[i__ + 5 + idx];
            dy[i__ + 6 + idy] = dx[i__ + 6 + idx];
            /* L50: */
        }
    } /* dcopy_ */

}
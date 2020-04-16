package subplex;
/* sortd.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/

class Sortd {

    /* Subroutine */
    static void sortd_(int n, double[] xkey, int[] ix) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__, ixi, ixip1, ilast, iswap, ifirst;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* sortd uses the shakersort method to sort an array of keys */
        /* in decreasing order. The sort is performed implicitly by */
        /* modifying a vector of indices. */

        /* For nearly sorted arrays, sortd requires O(n) comparisons. */
        /* for completely unsorted arrays, sortd requires O(n**2) */
        /* comparisons and will be inefficient unless n is small. */

        /* input */

        /*   n      - number of components */

        /*   xkey   - double precision vector of keys */

        /*   ix     - int vector of indices */

        /* output */

        /*   ix     - indices satisfy xkey(ix(i)) .ge. xkey(ix(i+1)) */
        /*            for i = 1,...,n-1 */

        /* local variables */


        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//		--ix;
//		--xkey;

        /* Function Body */
        ifirst = 1;
        iswap = 1;
        ilast = n - 1;
//		L10:
        while (ifirst <= ilast) {
            i__1 = ilast;
            for (i__ = ifirst; i__ <= i__1; ++i__) {
                ixi = ix[i__];
                ixip1 = ix[i__ + 1];
                if (xkey[ixi] < xkey[ixip1]) {
                    ix[i__] = ixip1;
                    ix[i__ + 1] = ixi;
                    iswap = i__;
                }
                /* L20: */
            }
            ilast = iswap - 1;
            i__1 = ifirst;
            for (i__ = ilast; i__ >= i__1; --i__) {
                ixi = ix[i__];
                ixip1 = ix[i__ + 1];
                if (xkey[ixi] < xkey[ixip1]) {
                    ix[i__] = ixip1;
                    ix[i__ + 1] = ixi;
                    iswap = i__;
                }
                /* L30: */
            }
            ifirst = iswap + 1;
//	goto L10;
        }
    } /* sortd_ */

}
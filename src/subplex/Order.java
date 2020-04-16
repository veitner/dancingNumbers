package subplex;
/* order.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/

class Order {

    /*
          subroutine order (npts,fs,il,is,ih)
c
      int npts,il,is,ih
      double precision fs(npts)
c
c                                         Coded by Tom Rowan
c                            Department of Computer Sciences
c                              University of Texas at Austin
c
c order determines the indices of the vertices with the
c lowest, second highest, and highest function values.

     */
    /* Subroutine */
    static void order_(int npts, double[] fs, int[] il,
                       int[] is, int[] ih) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__, j, il0;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* order determines the indices of the vertices with the */
        /* lowest, second highest, and highest function values. */

        /* input */

        /*   npts   - number of points in simplex */

        /*   fs     - double precision vector of function values of */
        /*            simplex */

        /*   il     - index to vertex with lowest function value */

        /* output */

        /*   il     - new index to vertex with lowest function value */

        /*   is     - new index to vertex with second highest */
        /*            function value */

        /*   ih     - new index to vertex with highest function value */

        /* local variables */


        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//		--fs;

        /* Function Body */
        il0 = il[1];
        j = il0 % npts + 1;
        if (fs[j] >= fs[il[1]]) {
            ih[1] = j;
            is[1] = il0;
        } else {
            ih[1] = il0;
            is[1] = j;
            il[1] = j;
        }
        i__1 = il0 + npts - 2;
        for (i__ = il0 + 1; i__ <= i__1; ++i__) {
            j = i__ % npts + 1;
            if (fs[j] >= fs[ih[1]]) {
                is[1] = ih[1];
                ih[1] = j;
            } else if (fs[j] > fs[is[1]]) {
                is[1] = j;
            } else if (fs[j] < fs[il[1]]) {
                il[1] = j;
            }
            /* L10: */
        }
    } /* order_ */

}
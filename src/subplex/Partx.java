package subplex;
/* partx.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


import static java.lang.StrictMath.min;

class Partx {

    /* Common Block Declarations */

/*
			struct

			{
				double alpha, beta, gamma, delta, psi, omega;
				int nsmin, nsmax, irepl, ifxsw;
				double bonus, fstop;
				int nfstop, nfxe;
				double fxstat[ 4],ftest;
				logical minf, initx, newx;
			}

			usubc_;

#
			define usubc_1
			usubc_
*/

    /*
          subroutine partx (n,ip,absdx,nsubs,nsvals)
    c
          int n,nsubs,nsvals(*)
          int ip(n)
          double precision absdx(n)
    c
    c                                         Coded by Tom Rowan
    c                            Department of Computer Sciences
    c                              University of Texas at Austin
    c
    c partx partitions the vector x by grouping components of
    c similar magnitude of change.

     */
    /* Subroutine */
    static void partx_(int n, int[] ip, double[] absdx,
                       int[] nsubs, int[] nsvals, int nsval_offset) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__;
        double as1, as2;
        int ns1, ns2;
        double gap;
        int nleft, nused;
        double as1max, gapmax, asleft;

        Common usubc_1 = Common.get();


        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* partx partitions the vector x by grouping components of */
        /* similar magnitude of change. */

        /* input */

        /*   n      - number of components (problem dimension) */

        /*   ip     - permutation vector */

        /*   absdx  - vector of magnitude of change in x */

        /*   nsvals - int array dimensioned .ge. int(n/nsmin) */

        /* output */

        /*   nsubs  - number of subspaces */

        /*   nsvals - int array of subspace dimensions */

        /* common */



        /* local variables */



        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//				--absdx;
//				--ip;
//				--nsvals;

        /* Function Body */
        nsubs[1] = 0;
        nused = 0;
        nleft = n;
        asleft = absdx[1];
        i__1 = n;
        for (i__ = 2; i__ <= i__1; ++i__) {
            asleft += absdx[i__];
            /* L10: */
        }
//        L20:
        as1max = 0;
        while (true) {
            if (nused < n) {
                ++(nsubs[1]);
                as1 = 0.;
                i__1 = usubc_1.nsmin - 1;
                for (i__ = 1; i__ <= i__1; ++i__) {
                    as1 += absdx[ip[nused + i__]];
                    /* L30: */
                }
                gapmax = -1.;
                i__1 = min(usubc_1.nsmax, nleft);
                for (ns1 = usubc_1.nsmin; ns1 <= i__1; ++ns1) {
                    as1 += absdx[ip[nused + ns1]];
                    ns2 = nleft - ns1;
                    if (ns2 > 0) {
                        if (ns2 >= ((ns2 - 1) / usubc_1.nsmax + 1) * usubc_1.nsmin) {
                            as2 = asleft - as1;
                            gap = as1 / ns1 - as2 / ns2;
                            if (gap > gapmax) {
                                gapmax = gap;
                                nsvals[nsubs[1] + nsval_offset] = ns1;
                                as1max = as1;
                            }
                        }
                    } else {
                        if (as1 / ns1 > gapmax) {
                            nsvals[nsubs[1] + nsval_offset] = ns1;
                            return;
                        }
                    }
                    /* L40: */
                }
                nused += nsvals[nsubs[1] + nsval_offset];
                nleft = n - nused;
                asleft -= as1max;
//	goto L20;
            }
        }
    } /* partx_ */

}
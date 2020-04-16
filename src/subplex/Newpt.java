package subplex;

/* newpt.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/

class Newpt {

    /*
          subroutine newpt (ns,coef,xbase,xold,new,xnew,small)
c
      int ns
      double precision coef,xbase(ns),xold(ns),xnew(*)
      boolean new,small
c
c                                         Coded by Tom Rowan
c                            Department of Computer Sciences
c                              University of Texas at Austin
c
c newpt performs reflections, expansions, contractions, and
c shrinkages (massive contractions) by computing:
c
c xbase + coef * (xbase - xold)
c
c The result is stored in xnew if new .eq. .true.,
c in xold otherwise.

     */
    /* Subroutine */
    static boolean newpt_(int ns, double coef, double[] xbase,
                          double[] xold, boolean new__, double[] xnew, boolean[] small) {
        /* System generated locals */
        int i__1;

        /* Local variables */
        int i__;
        boolean eqold;
        double xoldi;
        boolean eqbase;



        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* newpt performs reflections, expansions, contractions, and */
        /* shrinkages (massive contractions) by computing: */

        /* xbase + coef * (xbase - xold) */

        /* The result is stored in xnew if new .eq. .true., */
        /* in xold otherwise. */

        /* use :  coef .gt. 0 to reflect */
        /*        coef .lt. 0 to expand, contract, or shrink */

        /* input */

        /*   ns     - number of components (subspace dimension) */

        /*   coef   - one of four simplex method coefficients */

        /*   xbase  - double precision ns-vector representing base */
        /*            point */

        /*   xold   - double precision ns-vector representing old */
        /*            point */

        /*   new    - boolean switch */
        /*            = .true.  : store result in xnew */
        /*            = .false. : store result in xold, xnew is not */
        /*                        referenced */

        /* output */

        /*   xold   - unchanged if new .eq. .true., contains new */
        /*            point otherwise */

        /*   xnew   - double precision ns-vector representing new */
        /*            point if  new .eq. .true., not referenced */
        /*            otherwise */

        /*   small  - boolean flag */
        /*            = .true.  : coincident points */
        /*            = .false. : otherwise */

        /* local variables */


        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        /* Parameter adjustments */
//        --xold;
//        --xbase;
//        --xnew;

        /* Function Body */
        eqbase = true;
        eqold = true;
        if (new__) {
            i__1 = ns;
            for (i__ = 1; i__ <= i__1; ++i__) {
                xnew[i__] = xbase[i__] + coef * (xbase[i__] - xold[i__]);
                eqbase = eqbase && xnew[i__] == xbase[i__];
                eqold = eqold && xnew[i__] == xold[i__];
                /* L10: */
            }
        } else {
            i__1 = ns;
            for (i__ = 1; i__ <= i__1; ++i__) {
                xoldi = xold[i__];
                xold[i__] = xbase[i__] + coef * (xbase[i__] - xold[i__]);
                eqbase = eqbase && xold[i__] == xbase[i__];
                eqold = eqold && xold[i__] == xoldi;
                /* L20: */
            }
        }
        small[1] = eqbase || eqold;
        return small[1];
    } /* newpt_ */

}
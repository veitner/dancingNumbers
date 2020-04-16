package subplex;
/* subopt.f -- translated by f2c (version 20100827).
   You must link the resulting object file with libf2c:
	on Microsoft Windows system, link with libf2c.lib;
	on Linux or Unix systems, link with .../path/to/libf2c.a -lm
	or, if you install libf2c.a in a standard place, with -lf2c -lm
	-- in that order, at the end of the command line, as in
		cc *.o -lf2c -lm
	Source for libf2c is in /netlib/f2c/libf2c.zip, e.g.,

		http://www.netlib.org/f2c/libf2c.zip
*/


/* Common Block Declarations */

/*
struct {
    doublereal alpha, beta, gamma, delta, psi, omega;
    integer nsmin, nsmax, irepl, ifxsw;
    doublereal bonus, fstop;
    integer nfstop, nfxe;
    doublereal fxstat[4], ftest;
    logical minf, initx, newx;
} usubc_;

#define usubc_1 usubc_
*/

import static java.lang.StrictMath.min;

class Subopt {

    /* Subroutine */
    static void subopt_(int n) {
        Common usubc_1 = Common.get();

        /*                                         Coded by Tom Rowan */
        /*                            Department of Computer Sciences */
        /*                              University of Texas at Austin */

        /* subopt sets options for subplx. */

        /* input */

        /*   n      - problem dimension */

        /* common */




        /* subroutines and functions */

        /*   fortran */

        /* ----------------------------------------------------------- */

        /* *********************************************************** */
        /* simplex method strategy parameters */
        /* *********************************************************** */

        /* alpha  - reflection coefficient */
        /*          alpha .gt. 0 */

        usubc_1.alpha = 1.;

        /* beta   - contraction coefficient */
        /*          0 .lt. beta .lt. 1 */

        usubc_1.beta = .5;

        /* gamma  - expansion coefficient */
        /*          gamma .gt. 1 */

        usubc_1.gamma = 2.;

        /* delta  - shrinkage (massive contraction) coefficient */
        /*          0 .lt. delta .lt. 1 */

        usubc_1.delta = .5;

        /* *********************************************************** */
        /* subplex method strategy parameters */
        /* *********************************************************** */

        /* psi    - simplex reduction coefficient */
        /*          0 .lt. psi .lt. 1 */

        usubc_1.psi = .25;

        /* omega  - step reduction coefficient */
        /*          0 .lt. omega .lt. 1 */

        usubc_1.omega = .1;

        /* nsmin and nsmax specify a range of subspace dimensions. */
        /* In addition to satisfying  1 .le. nsmin .le. nsmax .le. n, */
        /* nsmin and nsmax must be chosen so that n can be expressed */
        /* as a sum of positive integers where each of these integers */
        /* ns(i) satisfies   nsmin .le. ns(i) .ge. nsmax. */
        /* Specifically, */
        /*     nsmin*ceil(n/nsmax) .le. n   must be true. */

        /* nsmin  - subspace dimension minimum */

        usubc_1.nsmin = min(2, n);

        /* nsmax  - subspace dimension maximum */

        usubc_1.nsmax = min(5, n);

        /* *********************************************************** */
        /* subplex method special cases */
        /* *********************************************************** */
        /* nelder-mead simplex method with periodic restarts */
        /*   nsmin = nsmax = n */
        /* *********************************************************** */
        /* nelder-mead simplex method */
        /*   nsmin = nsmax = n, psi = small positive */
        /* *********************************************************** */

        /* irepl, ifxsw, and bonus deal with measurement replication. */
        /* Objective functions subject to large amounts of noise can */
        /* cause an optimization method to halt at a false optimum. */
        /* An expensive solution to this problem is to evaluate f */
        /* several times at each point and return the average (or max */
        /* or min) of these trials as the function value.  subplx */
        /* performs measurement replication only at the current best */
        /* point. The longer a point is retained as best, the more */
        /* accurate its function value becomes. */

        /* The common variable nfxe contains the number of function */
        /* evaluations at the current best point. fxstat contains the */
        /* mean, max, min, and standard deviation of these trials. */

        /* irepl  - measurement replication switch */
        /* irepl  = 0, 1, or 2 */
        /*        = 0 : no measurement replication */
        /*        = 1 : subplx performs measurement replication */
        /*        = 2 : user performs measurement replication */
        /*              (This is useful when optimizing on the mean, */
        /*              max, or min of trials is insufficient. Common */
        /*              variable initx is true for first function */
        /*              evaluation. newx is true for first trial at */
        /*              this point. The user uses subroutine fstats */
        /*              within his objective function to maintain */
        /*              fxstat. By monitoring newx, the user can tell */
        /*              whether to return the function evaluation */
        /*              (newx = .true.) or to use the new function */
        /*              evaluation to refine the function evaluation */
        /*              of the current best point (newx = .false.). */
        /*              The common variable ftest gives the function */
        /*              value that a new point must beat to be */
        /*              considered the new best point.) */

        usubc_1.irepl = 0;

        /* ifxsw  - measurement replication optimization switch */
        /* ifxsw  = 1, 2, or 3 */
        /*        = 1 : retain mean of trials as best function value */
        /*        = 2 : retain max */
        /*        = 3 : retain min */

        usubc_1.ifxsw = 1;

        /* Since the current best point will also be the most */
        /* accurately evaluated point whenever irepl .gt. 0, a bonus */
        /* should be added to the function value of the best point */
        /* so that the best point is not replaced by a new point */
        /* that only appears better because of noise. */
        /* subplx uses bonus to determine how many multiples of */
        /* fxstat(4) should be added as a bonus to the function */
        /* evaluation. (The bonus is adjusted automatically by */
        /* subplx when ifxsw or minf is changed.) */

        /* bonus  - measurement replication bonus coefficient */
        /*          bonus .ge. 0 (normally, bonus = 0 or 1) */
        /*        = 0 : bonus not used */
        /*        = 1 : bonus used */

        usubc_1.bonus = 1.;

        /* nfstop = 0 : f(x) is not tested against fstop */
        /*        = 1 : if f(x) has reached fstop, subplx returns */
        /*              iflag = 2 */
        /*        = 2 : (only valid when irepl .gt. 0) */
        /*              if f(x) has reached fstop and */
        /*              nfxe .gt. nfstop, subplx returns iflag = 2 */

        usubc_1.nfstop = 0;

        /* fstop  - f target value */
        /*          Its usage is determined by the value of nfstop. */

        /* minf   - logical switch */
        /*        = .true.  : subplx performs minimization */
        /*        = .false. : subplx performs maximization */

        usubc_1.minf = true;
    } /* subopt_ */

}
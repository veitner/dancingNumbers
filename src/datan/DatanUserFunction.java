package datan;

/**
 * An abstract class which has to be extended by the user to yield a class computing a scalar function
 *
 * @author Siegmund Brandt.
 */

public abstract class DatanUserFunction implements LsqFunction {

    /**
     * @return the function value at a point in the Cartesian plane with the coordinates x, y.
     * Used in the drawing of a contour.
     */
    public double getValue(double x, double y) {
        return 0.;
    }

    /**
     * @return the function value for a given vector d and the variable t.
     * Used in in least squares problems.
     */
    public double getValue(DatanVector d, double t) {
        return 0.;
    }

    /**
     * @return the function value at a point in n-space given by the vector d.
     * Used in in minimization problems.
     */
    public double getValue(DatanVector d) {
        return 0.;
    }

    /**
     * @return the function value for the argument x.
     * Used in some cases in numerical computation of derivative or in finding zero of a function
     */
    public double getValue(double x) {
        return 0.;
    }

    /**
     * @return the function value of the constraint function number k with the arguments eta and x.
     * Used in LsqGen and LsqAsg
     */
    public double getValue(DatanVector eta, DatanVector x, int k) {
        return 0.;
    }
}


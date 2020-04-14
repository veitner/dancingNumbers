package datan;

public interface LsqFunction {
    /**
     * @return the function value for a given vector d and the variable t.
     * Used in in least squares problems.
     */
    double getValue(DatanVector d, double t);
}

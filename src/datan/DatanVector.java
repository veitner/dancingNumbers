package datan;


import java.util.Locale;

/**
 * A class representing a vector.
 *
 * @author Siegmund Brandt.
 */
public class DatanVector {

    /**
     * The vector elements.
     */
    double[] a;
    /**
     * Number of elements.
     */
    int n;

    /**
     * Creates a vector with n elements which are all zero.
     */
    public DatanVector(int n) {
        this.n = n;
        a = new double[n];
        for (int i = 0; i < n; i++) {
            setElement(i, 0.);
        }
    }

    public double[] toArray() {
        return a;
    }


    /**
     * Creates a vector with n elements from the array b.
     */
    public DatanVector(int n, double[] b) {
        this.n = n;
        a = new double[n];
        System.arraycopy(b, 0, a, 0, n);
    }

    /**
     * Creates a vector from the array b.
     */
    public DatanVector(double[] b) {
        n = b.length;
        a = new double[n];
        System.arraycopy(b, 0, a, 0, n);
    }

    /**
     * Creates a vector which is a copy of b.
     */
    public DatanVector(DatanVector b) {
        this.n = b.getNumberOfElements();
        a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = b.getElement(i);
        }
    }

    /**
     * Returns the number of elements.
     */
    public int getNumberOfElements() {
        return n;
    }

    /**
     * Returns elements i.
     */
    public double getElement(int i) {
        return a[i];
    }

    /**
     * Sets element i to in.
     */
    public void setElement(int i, double in) {
        a[i] = in;
    }

    /**
     * Returns sum with vector b.
     */
    public DatanVector add(DatanVector b) {
        DatanVector c = new DatanVector(n);
        for (int i = 0; i < n; i++) {
            c.setElement(i, getElement(i) + b.getElement(i));
        }
        return c;

    }

    /**
     * Returns original vector minus vector b.
     */
    public DatanVector sub(DatanVector b) {
        DatanVector c = new DatanVector(n);
        for (int i = 0; i < n; i++) {
            c.setElement(i, getElement(i) - b.getElement(i));
        }
        return c;

    }

    /**
     * Returns product with scalar s.
     */
    public DatanVector multiply(double s) {
        DatanVector c = new DatanVector(n);
        for (int i = 0; i < n; i++) {
            c.setElement(i, getElement(i) * s);
        }
        return c;

    }

    /**
     * Returns subvector defined by a list.
     *
     * @param list array with same number of elements as vector,
     *             element has to be 1 if corresponding element of vector is to become element of subvector.
     * @return the desired subvector.
     */
    public DatanVector getSubvector(int[] list) {
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (list[i] == 1) {
                k++;
            }
        }
        DatanVector c = new DatanVector(k);
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (list[i] == 1) {
                c.setElement(j, getElement(i));
                j++;
            }
        }
        return c;

    }

    /**
     * Replaces elements according to a list.
     *
     * @param list array with same number of elements as vector,
     *             element has to be 1 if corresponding element of original vector is to be replaced by element of subvector.
     */
    public void putSubvector(DatanVector b, int[] list) {
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (list[i] == 1) {
                setElement(i, b.getElement(k));
                k++;
            }
        }

    }

    /**
     * Returns scalar product with vector b.
     */
    public double dot(DatanVector b) {
        double res = 0.;
        for (int i = 0; i < n; i++) {
            res = res + getElement(i) * b.getElement(i);
        }
        return res;

    }

    /**
     * Returns Euclidean norm.
     */
    public double norm() {
        double res = dot(this);
        res = Math.sqrt(res);
        return res;

    }

    /**
     * Writes the DatanVector on standard output.
     */
    public void write() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append("   ").append(a[i]);
        }
        System.out.println(str);
    }

    /**
     * Writes the DatanVector onto a string, using 5 fraction digits.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append("   ").append(String.format(Locale.US, "%10.5f", a[i]));
        }
//      str = str + "\n";
        return str.toString();
    }

    /**
     * Writes the DatanVector onto a string.
     *
     * @param fmt formatstring, e.g., "%15.10f"
     */
    public String toString(String fmt) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            str.append("   ").append(String.format(Locale.US, fmt, a[i]));
        }
        return str.toString();
    }
}

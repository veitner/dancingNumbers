package datan;

/**
 * A class representing a histogram.
 *
 * @author Siegmund Brandt.
 */
public class Histogram {
    double[] hist;
    double x0, delx;
    int nx;
    int n;

    /**
     * Creates a histogram.
     *
     * @param x0   lower boundary of histogram
     * @param delx bin size
     * @param nx   number of bins
     */
    public Histogram(double x0, double delx, int nx) {
        this.x0 = x0;
        this.delx = delx;
        this.nx = nx;
        hist = new double[nx];
        for (int i = 0; i < nx; i++) {
            hist[i] = 0.;
        }
    }

    /**
     * Enters xin in the appropriate bin with a user-defined weight.
     */
    public void enter(double xin, double weight) {
        n = (int) ((xin - x0) / delx);
        if (n >= 0 && n < nx) hist[n] = hist[n] + weight;
    }

    /**
     * Enters xin in the appropriate bin with weight 1.
     */
    public void enter(double xin) {
        n = (int) ((xin - x0) / delx);
        if (n >= 0 && n < nx) hist[n] = hist[n] + 1.;
    }

    /**
     * @return the lower boundary
     */
    public double getLowerBoundary() {
        return x0;
    }

    /**
     * @return the bin size
     */
    public double getBinSize() {
        return delx;
    }

    /**
     * @return the number of bins
     */
    public int getNumberOfBins() {
        return nx;
    }

    /**
     * @return the contents of all nx bins
     */
    public double[] getContents() {
        return hist;
    }

    /**
     * @return the sum of the contents of all nx bins
     */
    public double getTotalContents() {
        double result = 0.;
        for (int i = 0; i < nx; i++) {
            result = result + hist[i];
        }
        return result;
    }

    /**
     * @return the contents of bin number i
     */
    public double getContentsAt(int i) {
        double result = hist[i];
        return result;
    }

    /**
     * Sets the contents of bin number i to the value in
     */
    public void setContentsAt(double in, int i) {
        hist[i] = in;
    }


}

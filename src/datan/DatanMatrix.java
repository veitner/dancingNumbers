package datan;


import java.util.Locale;


/**
 * A class representing a matrix.
 *
 * @author Siegmund Brandt.
 */
public class DatanMatrix {
    /**
     * The matrix elements.
     */
    double a[][];
    /**
     * Number of rows.
     */
    int m;
    /**
     * Number of columns.
     */
    int n;

    /**
     * Creates a matrix which is a copy of b.
     */
    public DatanMatrix(DatanMatrix b) {
        m = b.getNumberOfRows();
        n = b.getNumberOfColumns();
        a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = b.getElement(i, j);
            }
        }
    }

    /**
     * Creates an (n x n) unit matrix.
     */
    public DatanMatrix(int n) {
        this.n = n;
        this.m = n;
        this.a = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    a[i][j] = 1.;
                } else {
                    a[i][j] = 0.;
                }
            }
        }
    }

    /**
     * Creates an (n x m) matrix all elements of which contain a zero (null matrix).
     */
    public DatanMatrix(int m, int n) {
        this.n = n;
        this.m = m;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 0.;
            }
        }
    }


    /**
     * Creates a matrix the elements of which are given by a.
     */
    public DatanMatrix(double a[][]) {
        m = a.length;
        n = a[0].length;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = a[i][j];
            }
        }
    }

    /**
     * Creates a matrix with only one column the elements of which are given by a.
     */
    public DatanMatrix(double a[]) {
        m = a.length;
        n = 1;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            this.a[i][0] = a[i];
        }
    }


    /**
     * Creates a matrix with only one column representing vector v).
     */
    public DatanMatrix(DatanVector v) {
        m = v.getNumberOfElements();
        n = 1;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            this.a[i][0] = v.getElement(i);
        }
    }

    /**
     * Returns number of rows.
     */
    public int getNumberOfRows() {
        return m;
    }

    /**
     * Returns number of columns.
     */
    public int getNumberOfColumns() {
        return n;
    }

    /**
     * Returns element (i,j).
     */
    public double getElement(int i, int j) {
        return a[i][j];
    }

    /**
     * sets element (i,j) ti in.
     */
    public void setElement(int i, int j, double in) {
        a[i][j] = in;
    }

    /**
     * Returns the transposed matrix.
     */
    public DatanMatrix transpose() {
        DatanMatrix c = new DatanMatrix(n, m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c.setElement(j, i, a[i][j]);
            }
        }
        return c;
    }

    /**
     * Returns the sum with the matrix b.
     *
     * @param b the matrix to be added.
     * @return the desired sum.
     */
    public DatanMatrix add(DatanMatrix b) {
        DatanMatrix c = new DatanMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c.setElement(i, j, a[i][j] + b.getElement(i, j));
            }
        }
        return c;
    }

    /**
     * Returns the difference between this matrix and the matrix b.
     *
     * @param b the matrix to be added.
     * @return the desired difference.
     */
    public DatanMatrix sub(DatanMatrix b) {
        DatanMatrix c = new DatanMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c.setElement(i, j, a[i][j] - b.getElement(i, j));
            }
        }
        return c;
    }

    /**
     * Returns the product of this matrix and the scalar s.
     *
     * @param s scalar factor.
     * @return the desired product.
     */
    public DatanMatrix multiply(double s) {
        DatanMatrix c = new DatanMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c.setElement(i, j, getElement(i, j) * s);
            }
        }
        return c;
    }

    /**
     * Returns the vector obtained by multipying this matrix from the right with the (column) vector v.
     *
     * @param v the vector to be multiplied with.
     * @return the desired product.
     */
    public DatanVector multiply(DatanVector v) {
        DatanVector c = new DatanVector(m);
        for (int i = 0; i < m; i++) {
            double res = 0.;
            for (int r = 0; r < n; r++) {
                res = res + getElement(i, r) * v.getElement(r);
            }
            c.setElement(i, res);
        }
        return c;
    }

    /**
     * Returns the matrix obtained by multipying this matrix from the right with the matrix b.
     *
     * @param b the matrix to be multiplied with from the right.
     * @return the desired product.
     */
    public DatanMatrix multiply(DatanMatrix b) {
        int l = b.getNumberOfColumns();
        DatanMatrix c = new DatanMatrix(m, l);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < l; j++) {
                double res = 0.;
                for (int r = 0; r < n; r++) {
                    res = res + getElement(i, r) * b.getElement(r, j);
                }
                c.setElement(i, j, res);
            }
        }
        return c;
    }

    /**
     * Returns the matrix obtained by multipying this matrix from the right with the transposed of matrix b.
     *
     * @param b the matrix, the transposed of which is to be multiplied with from the right.
     * @return the desired product.
     */
    public DatanMatrix multiplyWithTransposed(DatanMatrix b) {
        int k = b.getNumberOfRows();
        DatanMatrix c = new DatanMatrix(m, k);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                double res = 0.;
                for (int r = 0; r < n; r++) {
                    res = res + getElement(i, r) * b.getElement(j, r);
                }
                c.setElement(i, j, res);
            }
        }
        return c;
    }

    /**
     * Returns the matrix obtained by multipying the transposed of this matrix from the right with the matrix b.
     *
     * @param b the matrix, with which the transposed is to be multiplied with from the right.
     * @return the desired product.
     */
    public DatanMatrix multiplyTransposedWith(DatanMatrix b) {
        int l = b.getNumberOfColumns();
        DatanMatrix c = new DatanMatrix(n, l);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                double res = 0.;
                for (int r = 0; r < m; r++) {
                    res = res + getElement(r, i) * b.getElement(r, j);
                }
                c.setElement(i, j, res);
            }
        }
        return c;

    }

    /**
     * Returns a submatrix.
     *
     * @param k  number of rows of the submatrix.
     * @param l  number of columnss of the submatrix.
     * @param m0 row nomber of starting position of submatrix in the original matrix.
     * @param n0 column starting position of submatrix in the original matrix.
     * @return the desired submatrix.
     */
    public DatanMatrix getSubmatrix(int k, int l, int m0, int n0) {
        DatanMatrix c = new DatanMatrix(k, l);
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < l; j++) {
                c.setElement(i, j, getElement(m0 + i, n0 + j));
            }
        }
        return c;
    }

    /**
     * Replaces a submatrix.
     *
     * @param m0 row nomber of starting position of submatrix in the original matrix.
     * @param n0 column starting position of submatrix in the original matrix.
     * @param b  the matrix to be placed there instead.
     */
    public void putSubmatrix(int m0, int n0, DatanMatrix b) {
        int k = b.getNumberOfRows();
        int l = b.getNumberOfColumns();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < l; j++) {
                setElement(i + m0, j + n0, b.getElement(i, j));
            }
        }
    }

    /**
     * Returns the column vector with column index k.
     *
     * @param k index of column.
     * @return the desired column vector.
     */
    public DatanVector getColumn(int k) {
        DatanVector c = new DatanVector(m);
        for (int i = 0; i < m; i++) {
            c.setElement(i, getElement(i, k));
        }
        return c;

    }

    /**
     * Replaces the column vector with column index k by (column) vector b.
     *
     * @param k number of column.
     * @param b vector replacing that column.
     */
    public void putColumn(int k, DatanVector b) {
        for (int i = 0; i < m; i++) {
            setElement(i, k, b.getElement(i));
        }
    }

    /**
     * Returns the row vector with row index k.
     *
     * @param k number of row.
     * @return the desired row vector.
     */
    public DatanVector getRow(int k) {
        DatanVector c = new DatanVector(n);
        for (int i = 0; i < n; i++) {
            c.setElement(i, getElement(k, i));
        }
        return c;

    }

    /**
     * Replaces the row vector with column index k by (row) vector b.
     *
     * @param k number of row.
     * @param b vector replacing that row.
     */
    public void putRow(int k, DatanVector b) {
        for (int i = 0; i < n; i++) {
            setElement(k, i, b.getElement(i));
        }
    }

    /**
     * Solves matrix equation a x = b where b is original matrix; after call it contains the solution x.
     *
     * @param a (n x n) matrix.
     */
    public void matrixEquation(DatanMatrix a) {
        int k, kk;
        double save, amax;
        for (k = 0; k < m - 1; k++) {

            amax = 0.;
            kk = k;
            for (int l = k; l < m; l++) {
                if (Math.abs(amax) < Math.abs(a.getElement(l, k))) {
                    amax = a.getElement(l, k);
                    kk = l;
                }
            }
            if (kk != k) {
                for (int j = k; j < m; j++) {
                    save = a.getElement(k, j);
                    a.setElement(k, j, a.getElement(kk, j));
                    a.setElement(kk, j, save);
                }
                for (int i = 0; i < n; i++) {
                    save = getElement(k, i);
                    setElement(k, i, getElement(kk, i));
                    setElement(kk, i, save);
                }
            }
            for (int i = k + 1; i < m; i++) {
                double fact = a.getElement(i, k) / a.getElement(k, k);
                for (int j = k + 1; j < m; j++) {
                    a.setElement(i, j, a.getElement(i, j) - a.getElement(k, j) * fact);
                }
                for (int j = 0; j < n; j++) {
                    setElement(i, j, getElement(i, j) - getElement(k, j) * fact);
                }
            }
        }
        for (int j = 0; j < n; j++) {
            setElement(m - 1, j, getElement(m - 1, j) / a.getElement(m - 1, m - 1));
            if (m > 1) {
                for (int i = m - 2; i >= 0; i--) {
                    for (int l = i + 1; l < m; l++) {
                        setElement(i, j, getElement(i, j) - a.getElement(i, l) * getElement(l, j));
                    }
                    setElement(i, j, getElement(i, j) / a.getElement(i, i));
                }
            }
        }
    }

    /**
     * Returns inverse of this matrix, which has to be squre and nonsingualar.
     */
    public DatanMatrix inverse() {
        DatanMatrix b = new DatanMatrix(m);
        b.matrixEquation(this);
        return b;
    }


    /**
     * Returns Cholesky decomposition of this matrix , which has to be symmetric positve definite.
     */
    public DatanMatrix choleskyDecomposition() {
        DatanMatrix u = new DatanMatrix(m, m);
        double s;
        for (int k = 0; k < m; k++) {
            s = 0.;
            for (int j = k; j < m; j++) {
                if (k > 0) {
                    s = 0.;
                    for (int l = 0; l <= k - 1; l++) {
                        s = s + u.getElement(l, k) * u.getElement(l, j);
                    }
                }
                u.setElement(k, j, getElement(k, j) - s);
                if (k == j) {
                    u.setElement(k, j, Math.sqrt(Math.abs(u.getElement(k, j))));
                } else {
                    u.setElement(k, j, u.getElement(k, j) / u.getElement(k, k));
                }
            }
        }
        return u;
    }

    /**
     * Returns product of this matrix (assumed to be of upper triangular form) with matrix a.
     */
    public DatanMatrix choleskyMultiply(DatanMatrix a) {
        n = a.getNumberOfColumns();
        DatanMatrix r = new DatanMatrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                r.setElement(i, k, 0.);
                for (int l = i; l < m; l++) {
                    r.setElement(i, k, r.getElement(i, k) + getElement(i, l) * a.getElement(l, k));
                }
            }
        }
        return r;
    }


    /**
     * Returns product of this matrix (assumed to be of upper triangular form) with vector a.
     */
    public DatanVector choleskyMultiply(DatanVector a) {
        DatanVector r = new DatanVector(m);
        for (int i = 0; i < m; i++) {
            r.setElement(i, 0.);
            for (int l = i; l < m; l++) {
                r.setElement(i, r.getElement(i) + getElement(i, l) * a.getElement(l));
            }
        }
        return r;
    }

    /**
     * Cholesky inversion (in place) of this matrix, which has to be positive definite symmetric.
     */
    public void choleskyInversion() {
        DatanMatrix u;
        // Step 1: Cholesky Decomposition
        u = choleskyDecomposition();
        for (int i = 0; i < m; i++) {
            // Step 2: Forward Substitution
            for (int l = i; l < m; l++) {
                if (l == i) {
                    setElement(m - 1, l, 1. / u.getElement(l, l));
                } else {
                    setElement(m - 1, l, 0.);
                    for (int k = i; k <= l - 1; k++) {
                        setElement(m - 1, l, getElement(m - 1, l) - u.getElement(k, l) * getElement(n - 1, k));
                    }
                    setElement(m - 1, l, getElement(m - 1, l) / u.getElement(l, l));
                }
            }
            // Step 3: Back Substitution
            for (int l = m - 1; l >= i; l--) {
                if (l == m - 1) {
                    setElement(i, l, getElement(m - 1, l) / u.getElement(l, l));
                } else {
                    setElement(i, l, getElement(m - 1, l));
                    for (int k = m - 1; k >= l + 1; k--) {
                        setElement(i, l, getElement(i, l) - u.getElement(l, k) * getElement(i, k));
                    }
                    setElement(i, l, getElement(i, l) / u.getElement(l, l));
                }
            }
        }
        // Fill lower triangle symmetrically
        if (m > 1) {
            for (int i = 0; i < m; i++) {
                for (int l = 0; l < i; l++) {
                    setElement(i, l, getElement(l, i));
                }
            }
        }
    }

    /**
     * Returns solution matrix x of matrix equation a x = b (where a is this matix and b the matrix in the argument of the metod)
     * using singular value decomposition;
     * this call with only one argument should be used in unproblematic cases only, since it does not provide a check
     * of convergence of the method and further information.
     */
    public DatanMatrix singularValueDecomposition(DatanMatrix b) {
        int[] nsv = {0};
        boolean[] ok = {false};
        DatanVector r = new DatanVector(m);
        return singularValueDecomposition(b, r, 0., nsv, ok);
    }

    /**
     * Returns solution vector x of matrix equation a x = b (where a is this matix) using singular value decomposition;
     *
     * @param b    vector b
     * @param frac fraction f; if smax is the largest singualr value, then all singular values < smin = f smax
     *             are set equal to zero. If frac = 0, then it is assumed to be 1.E-15.
     * @param r    array containing the squares of the residuals (available AFTER completion of the method)
     * @param nsv  nsv[0] contains the number of singular values not set equal to zero (available AFTER completion of the method)
     * @param ok   ok[0] = true if method has converged, = false otherwise (available AFTER completion of the method)
     */
    public DatanVector singularValueDecomposition(DatanVector b, double[] r, double frac, int[] nsv, boolean[] ok) {
        int l = 1;
        DatanMatrix bb = new DatanMatrix(b);
        DatanVector rr = new DatanVector(1);
        rr.setElement(0, r[0]);
        DatanMatrix xx;
        DatanVector x;
        xx = singularValueDecomposition(bb, rr, frac, nsv, ok);
        x = xx.getColumn(0);
        r[0] = rr.getElement(0);
        return x;
    }


    /**
     * Returns solution matrix x of matrix equation a x = b (where a is this matix) using singular value decomposition;
     *
     * @param b    matrix b
     * @param frac fraction f; if smax is the largest singualr value, then all singular values < smin = f smax
     *             are set equal to zero. If frac = 0, then it is assumed to be 1.E-15.
     * @param r    vector containing the squares of the residuals (available AFTER completion of the method)
     * @param nsv  nsv[0] contains the number of singular values not set equal to zero (available AFTER completion of the method)
     * @param ok   ok[0] = true if method has converged, = false otherwise (available AFTER completion of the method)
     */
    public DatanMatrix singularValueDecomposition(DatanMatrix b, DatanVector r, double frac, int[] nsv, boolean[] ok) {
        int l;
        l = b.getNumberOfColumns();
        // step 1: bidiagonalization of a
        DatanVector d = new DatanVector(m);
        DatanVector e = new DatanVector(m);
        DatanMatrix x = new DatanMatrix(n, l);
        sv1(b, d, e);
        // step 2: diagonalization of bidiagonal matrix
        ok[0] = sv2(b, d, e);
        // step 3: order singular values and perform permutation
        sv3(b, d);
        // step 4: singular value analysis
        sv4(b, d, x, r, frac, nsv);
        return x;
    }

    /**
     * Yields the solution vectors (x1 and x2) appearing in Marquardt's method to solve the matrix equation a x = b, where a is this matrix.
     *
     * @param bvec   vector b
     * @param frac   parameter f needed in singular value decomposition. If frac = 0, then it is assumed to be 1.E-15.
     * @param lambda Marquardt's parameter
     * @param x1     solution vector x1  (available AFTER completion of the method)
     * @param x2     solution vector x2  (available AFTER completion of the method)
     * @param ok     ok[0] = true if method has converged, = false otherwise (available AFTER completion of the method)
     */
    public void marquardt(DatanVector bvec, double lambda, DatanVector x1, DatanVector x2, double frac, boolean[] ok) {
        DatanMatrix b = new DatanMatrix(bvec);
        // step 1: bidiagonalization of a
        DatanVector d = new DatanVector(m);
        DatanVector e = new DatanVector(m);
        sv1(b, d, e);
        // step 2: diagonalization of bidiagonal matrix
        ok[0] = sv2(b, d, e);
        // step 3: order singular values and perform permutation
        sv3(b, d);
        // step 4: singular value analysis
        svm(b, d, lambda, x1, x2, frac);
    }

    /**
     * Returns the pseudoinverse of this matrix, using the singular value decomposition.
     *
     * @param frac fraction f; if smax is the largest singualr value, then all singular values < smin = f smax
     *             are set equal to zero. If frac = 0, then it is assumed to be 1.E-15.
     * @param r    vector containing the squares of the residuals (available AFTER completion of the method)
     * @param nsv  nsv[0] contains the number of singular values not set equal to zero (available AFTER completion of the method)
     * @param ok   ok[0] = true if method has converged, = false otherwise (available AFTER completion of the method)
     * @param d    vector, the elements of which are the diagiagonal elements of a square matrix D whose other elements are zero
     *             (available AFTER completion of the method)
     * @param u    u[0] orthoganal matrix (available AFTER completion of the method)
     * @param v    v[0] orthoganal matrix (available AFTER completion of the method); u D v(transposed) is equal to the original matrix
     */
    public DatanMatrix pseudoInverse(DatanVector r, double frac, int[] nsv, boolean[] ok, DatanVector d, DatanMatrix[] u, DatanMatrix[] v) {
        // step 0: create b as (m x m) unit matrix
        DatanMatrix b = new DatanMatrix(m);
        // step 1: bidiagonalization of a
        DatanVector e = new DatanVector(m);
        DatanMatrix x = new DatanMatrix(n, m);
        sv1(b, d, e);
        // step 2: diagonalization of bidiagonal matrix
        ok[0] = sv2(b, d, e);
        // step 3: order singular values and perform permutation
        sv3(b, d);
        v[0] = getSubmatrix(m, m, 0, 0);
        u[0] = b.transpose();
        // step 4: singular value analysis
        sv4(b, d, x, r, frac, nsv);
        return x;
    }

    /**
     * Returns the pseudoinverse of this matrix, using the singular value decomposition;
     * this call without argument should be used in unproblematic cases only, since it does not provide a check
     * of convergence of the method and further information.
     */
    public DatanMatrix pseudoInverse() {
        DatanMatrix b = new DatanMatrix(m);
        return singularValueDecomposition(b);
    }

    /**
     * Performs first step of singular value decomposition.
     */
    private void sv1(DatanMatrix b, DatanVector d, DatanVector e) {
        int l;
        DatanVector v, s;
        l = b.getNumberOfColumns();
        double ub[] = new double[2];
        double ubs[][] = new double[2][n];
        for (int i = 0; i < n; i++) {
            // set up Householder transformation Q(i)
            if (i < n - 1 || m > n) {
                v = getColumn(i);
                defineHouseholderTransformation(v, i, i + 1, ub);
                // apply Q(i) to a
                for (int j = i; j < n; j++) {
                    s = getColumn(j);
                    applyHouseholderTransformation(v, s, i, i + 1, ub);
                    putColumn(j, s);
                }
                // apply Q(i) to b
                for (int k = 0; k < l; k++) {
                    s = b.getColumn(k);
                    int nev = v.getNumberOfElements();
                    int nes = s.getNumberOfElements();
                    applyHouseholderTransformation(v, s, i, i + 1, ub);
                    b.putColumn(k, s);
                }
            }
            if (i < n - 2) {
                // set up Householder transformation H(i)
                v = getRow(i);
                defineHouseholderTransformation(v, i + 1, i + 2, ub);
                // save H(i)
                ubs[0][i] = ub[0];
                ubs[1][i] = ub[1];
                // apply H(i) to a
                for (int j = i; j < m; j++) {
                    s = getRow(j);
                    applyHouseholderTransformation(v, s, i + 1, i + 2, ub);
                    // save elements i+2, ... in row j of matrix a
                    if (j == i) {
                        for (int k = i + 2; k < n; k++) {
                            s.setElement(k, v.getElement(k));
                        }
                    }
                    putRow(j, s);
                }
            }
        }
        // copy diagonal of transformed matrix a to d and upper parallel to e
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                d.setElement(i, getElement(i, i));
                e.setElement(i, getElement(i - 1, i));
            }
        }
        d.setElement(0, getElement(0, 0));
        e.setElement(0, 0.);
        // construct matrix H = H(1) * H(2) * ... * H(n-1), H(n-1) = I
        for (int i = n - 1; i >= 0; i--) {
            v = getRow(i);
            for (int k = 0; k < n; k++) {
                setElement(i, k, 0.);
            }
            setElement(i, i, 1.);
            if (i < n - 2) {
                for (int k = i; k < n; k++) {
                    s = getColumn(k);
                    ub[0] = ubs[0][i];
                    ub[1] = ubs[1][i];
                    applyHouseholderTransformation(v, s, i + 1, i + 2, ub);
                    putColumn(k, s);
                }
            }
        }
    }

    /**
     * Performs second step of singular value decomposition.
     */
    private boolean sv2(DatanMatrix b, DatanVector d, DatanVector e) {
        int l, ll;
        DatanVector v, s;
        l = b.getNumberOfColumns();
        double bmx;
        int niter, nitermax;
        boolean ok, elzero, normalend;
        ok = true;
        elzero = true;
        nitermax = 10 * n;
        niter = 0;
        bmx = d.getElement(0);
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                bmx = Math.max(Math.abs(d.getElement(i)) + Math.abs(e.getElement(i)), bmx);
            }
        }
        for (int k = n - 1; k >= 0; k--) {
            int helpcount = 0;
            normalend = false;
            iterate:
            while (k != 0 && !normalend && helpcount < 100) {
                helpcount++;
                if ((bmx + d.getElement(k)) - bmx == 0.) {
                    // since d(k) == 0, perform Givens transform with result e(k) = 0.
                    s21(d, e, k);
                }
                // find ll (1 <= ll <= k) so that either e(ll) = 0 or d(ll-1 ) = 0.)
                // In the latter case transform e(ll) to zero. In both cases the
                // matrix splits and the bottom right minor begins with row ll.
                // If no such ll is found, set ll = 0.
                int iflag = 1;
                ll = 0;

                loop:

                for (int lll = k; lll >= 0; lll--) {
                    ll = lll;
                    if (ll == 0) {
                        elzero = false;
                        iflag = 1;
                        break loop;
                    } else if ((bmx - e.getElement(ll)) - bmx == 0.) {
                        elzero = true;
                        iflag = 2;
                        break loop;
                    } else if ((bmx + d.getElement(ll - 1)) - bmx == 0.) {
                        elzero = false;
                        iflag = 3;
                    }
                }
                if ((ll > 0) && !elzero) {
                    s22(b, d, e, k, ll);
                }
                if (ll != k) {
                    // one more QR pass with order k
                    s23(b, d, e, k, ll);
                    niter++;
                    // set flag indicating non-convergence
                    if (niter <= nitermax) {
                        continue iterate;
                    }
                    ok = false;
                }
                normalend = true;
            }
            if (d.getElement(k) < 0.) {
                // for negative singular values perform change of sign
                d.setElement(k, -d.getElement(k));
                for (int j = 0; j < n; j++) {
                    setElement(j, k, -getElement(j, k));
                }
            }
            // order is decreased by one in next pass
        }
        return ok;
    }


    /**
     * Method only used by sv2, treats specal case 1.
     */
    private void s21(DatanVector d, DatanVector e, int k) {
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        for (int i = k - 1; i >= 0; i--) {
            if (i == k - 1) {
                v[0] = d.getElement(i);
                v[1] = e.getElement(i + 1);
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                e.setElement(i + 1, v[1]);
            } else {
                v[0] = d.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                h = v[1];
            }
            if (i > 0) {
                v[0] = e.getElement(i);
                v[1] = 0.;
                applyGivensTransformation(v, cs);
                e.setElement(i, v[0]);
                h = v[1];
            }
            for (int j = 0; j < n; j++) {
                v[0] = getElement(j, i);
                v[1] = getElement(j, k);
                applyGivensTransformation(v, cs);
                setElement(j, i, v[0]);
                setElement(j, k, v[1]);
            }
        }
    }

    /**
     * Method only used by sv2, treats specal case 2.
     */
    private void s22(DatanMatrix b, DatanVector d, DatanVector e, int k, int ll) {
        int l;
        l = b.getNumberOfColumns();
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        for (int i = ll; i <= k; i++) {
            if (i == ll) {
                v[0] = d.getElement(i);
                v[1] = e.getElement(i);
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                e.setElement(i, v[1]);
            } else {
                v[0] = d.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                h = v[1];
            }
            if (i < k) {
                v[0] = e.getElement(i + 1);
                v[1] = 0.;
                // *****************************
                applyGivensTransformation(v, cs);
                e.setElement(i + 1, v[0]);
                h = v[1];
            }
            for (int j = 0; j < l; j++) {
                v[0] = b.getElement(i, j);
                v[1] = b.getElement(l - 1, j);
                applyGivensTransformation(v, cs);
                b.setElement(i, j, v[0]);
                b.setElement(l - 1, j, v[1]);
            }
        }
    }

    /**
     * Method only used by sv2, performs QR algorithm.
     */
    private void s23(DatanMatrix b, DatanVector d, DatanVector e, int k, int ll) {
        int l;
        l = b.getNumberOfColumns();
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        double g, f, t;
        f = ((d.getElement(k - 1) - d.getElement(k)) * (d.getElement(k - 1) + d.getElement(k))
                + (e.getElement(k - 1) - e.getElement(k)) * (e.getElement(k - 1) + e.getElement(k)))
                / (2. * e.getElement(k) * d.getElement(k - 1));
        if (Math.abs(f) > 1.E10) {
            g = Math.abs(f);
        } else {
            g = Math.sqrt(1. + f * f);
        }
        if (f >= 0.) {
            t = f + g;
        } else {
            t = f - g;
        }
        f = ((d.getElement(ll) - d.getElement(k)) * (d.getElement(ll) + d.getElement(k))
                + e.getElement(k) * (d.getElement(k - 1) / t - e.getElement(k))) / d.getElement(ll);
        for (int i = ll; i < k; i++) {
            if (i == ll) {
                // define R(ll)
                v[0] = f;
                v[1] = e.getElement(i + 1);
                defineGivensTransformation(v, cs);
            } else {
                // define R(i), i != ll
                v[0] = e.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                e.setElement(i, v[0]);
                h = v[1];
            }
            v[0] = d.getElement(i);
            v[1] = e.getElement(i + 1);
            applyGivensTransformation(v, cs);
            d.setElement(i, v[0]);
            e.setElement(i + 1, v[1]);
            v[0] = 0.;
            v[1] = d.getElement(i + 1);
            applyGivensTransformation(v, cs);
            h = v[0];
            d.setElement(i + 1, v[1]);
            for (int j = 0; j < n; j++) {
                v[0] = getElement(j, i);
                v[1] = getElement(j, i + 1);
                applyGivensTransformation(v, cs);
                setElement(j, i, v[0]);
                setElement(j, i + 1, v[1]);
            }
            // define T(i)
            v[0] = d.getElement(i);
            v[1] = h;
            defineAndApplyGivensTransformation(v, cs);
            d.setElement(i, v[0]);
            h = v[1];
            v[0] = e.getElement(i + 1);
            v[1] = d.getElement(i + 1);
            applyGivensTransformation(v, cs);
            e.setElement(i + 1, v[0]);
            d.setElement(i + 1, v[1]);
            if (i < k - 1) {
                v[0] = 0.;
                v[1] = e.getElement(i + 2);
                applyGivensTransformation(v, cs);
                e.setElement(i + 2, v[1]);
                h = v[0];
            }
            for (int j = 0; j < l; j++) {
                v[0] = b.getElement(i, j);
                v[1] = b.getElement(i + 1, j);
                applyGivensTransformation(v, cs);
                b.setElement(i, j, v[0]);
                b.setElement(i + 1, j, v[1]);
            }
        }
    }

    /**
     * Performs third step of singular value decomposition.
     */
    private void sv3(DatanMatrix b, DatanVector d) {
        double t;
        int l, k;
        l = b.getNumberOfColumns();
        boolean proceed = false;
        if (n > 1) {
            loop:
            for (int i = 1; i < n; i++) {
                if (d.getElement(i) > d.getElement(i - 1)) {
                    proceed = true;
                    break loop;
                }
            }
            if (proceed) {
                for (int i = 1; i < n; i++) {
                    t = d.getElement(i - 1);
                    k = i - 1;
                    for (int j = i; j < n; j++) {
                        if (t < d.getElement(j)) {
                            t = d.getElement(j);
                            k = j;
                        }
                    }
                    if (k != i - 1) {
                        // perform permutation on singular values
                        d.setElement(k, d.getElement(i - 1));
                        d.setElement(i - 1, t);
                        // perform permutation on matrix a
                        for (int j = 0; j < n; j++) {
                            t = getElement(j, k);
                            setElement(j, k, getElement(j, i - 1));
                            setElement(j, i - 1, t);
                        }
                        // perform permutation on matrix b
                        for (int j = 0; j < l; j++) {
                            t = b.getElement(k, j);
                            b.setElement(k, j, b.getElement(i - 1, j));
                            b.setElement(i - 1, j, t);
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs fourth step of singular value decomposition.
     */
    private void sv4(DatanMatrix b, DatanVector d, DatanMatrix x, DatanVector r, double frac, int[] nsv) {
        int l, kk;
        double fract, sinmin, sinmax, s1;
        final double EPSILON = 1.E-15;
        l = b.getNumberOfColumns();
        fract = Math.abs(frac);
        if (fract < EPSILON) fract = EPSILON;
        sinmax = 0.;
        for (int i = 0; i < n; i++) {
            sinmax = Math.max(sinmax, d.getElement(i));
        }
        sinmin = sinmax * fract;
        kk = n - 1;
        loop:
        for (int i = 0; i < n; i++) {
            if (d.getElement(i) <= sinmin) {
                kk = i - 1;
                break loop;
            }
        }
        for (int i = 0; i < m; i++) {
            if (i <= kk) {
                s1 = 1. / d.getElement(i);
                for (int j = 0; j < l; j++) {
                    b.setElement(i, j, b.getElement(i, j) * s1);
                }
            } else {
                for (int j = 0; j < l; j++) {
                    if (i == kk + 1) {
                        r.setElement(j, Math.pow(b.getElement(i, j), 2.));
                    } else {
                        r.setElement(j, r.getElement(j) + Math.pow(b.getElement(i, j), 2.));
                    }
                    if (i < n) b.setElement(i, j, 0.);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                x.setElement(i, j, 0.);
                for (int k = 0; k < n; k++) {
                    x.setElement(i, j, x.getElement(i, j) + getElement(i, k) * b.getElement(k, j));
                }
            }
        }
        nsv[0] = kk + 1;
    }

    /**
     * Performs singular value analysis and application of Marquardt method.
     */
    public void svm(DatanMatrix b, DatanVector d, double lambda, DatanVector x1, DatanVector x2, double frac) {
        DatanVector p1, p2;
        int l, kk;
        double lambda2, lambdap, lambdap2, den1, den2, fract, g, help, sinmin, sinmax;
        final double EPSILON = 1.E-15;
        l = b.getNumberOfColumns();
        fract = Math.abs(frac);
        sinmax = 0.;
        lambda2 = lambda * lambda;
        lambdap = lambda * 0.1;
        lambdap2 = lambdap * lambdap;
        for (int i = 0; i < n; i++) {
            sinmax = Math.max(sinmax, d.getElement(i));
        }
        sinmin = sinmax * fract;
        kk = n;
        p1 = new DatanVector(n);
        p2 = new DatanVector(n);
        loop:
        for (int i = 0; i < n; i++) {
            if (d.getElement(i) <= sinmin) {
                kk = i - 1;
                break loop;
            }
        }
        for (int i = 0; i < m; i++) {
            g = b.getElement(i, 0);
            if (i < kk) {
                help = Math.pow(d.getElement(i), 2.);
                den1 = 1. / (help + lambda2);
                den2 = 1. / (help + lambdap2);
                p1.setElement(i, d.getElement(i) * g * den1);
                p2.setElement(i, d.getElement(i) * g * den2);
            }
        }
        for (int i = 0; i < n; i++) {
            x1.setElement(i, 0.);
            x2.setElement(i, 0.);
            for (int k = 0; k < n; k++) {
                x1.setElement(i, x1.getElement(i) + getElement(i, k) * p1.getElement(k));
                x2.setElement(i, x2.getElement(i) + getElement(i, k) * p2.getElement(k));
            }
        }
    }


    /**
     * finds solution vector x for least squares problem with constraints.
     * The least squares problem is r^2 = (a x - b)^2 = min; the constraints are e x = b.
     * The original matrix is a.
     *
     * @param b    vector b
     * @param e    matrix e
     * @param frac parameter for singular value decomposition (normally 0.)
     * @param r    r[0] residual r^2 - available after application of method.
     * @param ok   ok[0] true if method converged (false otherwise) -  available after application of method.
     */

    public DatanVector leastSquaresWithConstraints(DatanVector b, DatanMatrix e, DatanVector d, DatanVector r, double frac, boolean[] ok) {
        DatanMatrix a2, p2;
        DatanVector s, v, x;
        x = new DatanVector(n);
        s = new DatanVector(n);
        int nsv[] = new int[1];
        int l, nminl;
        l = e.getNumberOfRows();
        nminl = n - l;
        double param[][] = new double[l][2];
// step 1
        for (int i = 0; i < l; i++) {
            v = e.getRow(i);
            defineHouseholderTransformation(v, i, i + 1, param[i]);
            for (int j = i; j < l; j++) {
                s = e.getRow(j);
                applyHouseholderTransformation(v, s, i, i + 1, param[i]);
                if (j == i && n - 1 > i) {
                    for (int k = i + 1; k < n; k++) {
                        s.setElement(k, v.getElement(k));
                    }
                }
                e.putRow(j, s);
            }
            for (int j = 0; j < m; j++) {
                s = getRow(j);
                applyHouseholderTransformation(v, s, i, i + 1, param[i]);
                putRow(j, s);
            }
        }
// step 2
        x.setElement(0, d.getElement(0) / e.getElement(0, 0));
        if (l > 1) {
            for (int j = 1; j < l; j++) {
                x.setElement(j, d.getElement(j));
                for (int k = 0; k < j - 1; k++) {
                    x.setElement(j, x.getElement(j) - e.getElement(j, k) * x.getElement(k));
                }
                x.setElement(j, x.getElement(j) / e.getElement(j, j));
            }
        }
// step 3
        for (int j = 0; j < m; j++) {
            for (int k = 0; k < l; k++) {
                b.setElement(j, b.getElement(j) - getElement(j, k) * x.getElement(k));
            }
        }
// step 4
        a2 = getSubmatrix(m, nminl, 0, l);
        DatanMatrix bmatr = new DatanMatrix(b);
        p2 = a2.singularValueDecomposition(bmatr, r, frac, nsv, ok);
        if (ok[0]) {
// step 5
            for (int i = 0; i < nminl; i++) {
                x.setElement(i + l, p2.getElement(i, 0));
            }
            for (int i = l - 1; i >= 0; i--) {
                v = e.getRow(i);
                applyHouseholderTransformation(v, x, i, i + 1, param[i]);
            }
        }
        return x;
    }


    /**
     * Defines Givens transformation.
     *
     * @param v  vector elements v1, v2.
     * @param cs contains (after application of method) the two parameters cs[0] = c and cs[1] = s.
     */

    public void defineGivensTransformation(double v[], double cs[]) {
        double w, q;
        if (Math.abs(v[0]) > Math.abs(v[1])) {
            w = v[1] / v[0];
            q = Math.sqrt(1. + w * w);
            cs[0] = 1. / q;
            if (v[0] < 0) {
                cs[0] = -cs[0];
            }
            cs[1] = cs[0] * w;
        } else {
            if (v[1] != 0.) {
                w = v[0] / v[1];
                q = Math.sqrt(1. + w * w);
                cs[1] = 1. / q;
                if (v[1] < 0) {
                    cs[1] = -cs[1];
                }
                cs[0] = cs[1] * w;
            } else {
                cs[0] = 1.;
                cs[1] = 0.;
            }
        }
    }

    /**
     * Defines Givens transformation and applies it to the defining vector.
     *
     * @param v  vector elements v1, v2; after application of method it contains the transformed elements.
     * @param cs contains (after application of method) the two parameters cs[0] = c and cs[1] = s.
     */

    public void defineAndApplyGivensTransformation(double v[], double cs[]) {
        double w, q;
        if (Math.abs(v[0]) > Math.abs(v[1])) {
            w = v[1] / v[0];
            q = Math.sqrt(1. + w * w);
            cs[0] = 1. / q;
            if (v[0] < 0) {
                cs[0] = -cs[0];
            }
            cs[1] = cs[0] * w;
            v[0] = Math.abs(v[0]) * q;
            v[1] = 0.;
        } else {
            if (v[1] != 0.) {
                w = v[0] / v[1];
                q = Math.sqrt(1. + w * w);
                cs[1] = 1. / q;
                if (v[1] < 0) {
                    cs[1] = -cs[1];
                }
                cs[0] = cs[1] * w;
                v[0] = Math.abs(v[1]) * q;
                v[1] = 0.;
            } else {
                cs[0] = 1.;
                cs[1] = 0.;
            }
        }
    }

    /**
     * Applies Givens Transformation to the two vector components  z.
     *
     * @param z  vector elements z1, z2; after application of method it contains the transformed elements.
     * @param cs the two parameters cs[0] = c and cs[1] = s defining the transformation.
     */
    public void applyGivensTransformation(double z[], double cs[]) {
        double w;
        w = z[0] * cs[0] + z[1] * cs[1];
        z[1] = -z[0] * cs[1] + z[1] * cs[0];
        z[0] = w;
    }


    /**
     * Defines a Householder Transformation.
     *
     * @param v     vector.
     * @param p     vector index.
     * @param l     vector index.
     * @param param parameters of transformation (after application of method): param[0] = up, param[1] = b.
     */
    public void defineHouseholderTransformation(DatanVector v, int p, int l, double param[]) {
        int ne = v.getNumberOfElements();
        double c = Math.abs(v.getElement(p));
        for (int i = l; i < ne; i++) {
            c = Math.max(Math.abs(v.getElement(i)), c);
        }
        if (c > 0.) {
            double c1 = 1. / c;
            double vpprim = Math.pow(v.getElement(p) * c1, 2.);
            for (int i = l; i < ne; i++) {
                vpprim = vpprim + Math.pow(v.getElement(i) * c1, 2.);
            }
            vpprim = c * Math.sqrt(Math.abs(vpprim));
            if (v.getElement(p) > 0.) {
                vpprim = -vpprim;
            }
            param[0] = v.getElement(p) - vpprim;
            param[1] = 1. / (vpprim * param[0]);
        }
    }

    /**
     * Applies Householder Transformation to Vector c.
     *
     * @param v     transformation defining vector.
     * @param c     vector to be transformed (contains transformed vector after application).
     * @param p     vector index.
     * @param l     vector index.
     * @param param parameters of transformation: param[0] = up, param[1] = b.
     */
    public void applyHouseholderTransformation(DatanVector v, DatanVector c, int p, int l, double param[]) {
        int ne = v.getNumberOfElements();
        double s = c.getElement(p) * param[0];
        for (int i = l; i < ne; i++) {
            s = s + c.getElement(i) * v.getElement(i);
        }
        s = s * param[1];
        c.setElement(p, c.getElement(p) + s * param[0]);
        for (int i = l; i < ne; i++) {
            c.setElement(i, c.getElement(i) + s * v.getElement(i));
        }
    }


    /**
     * Writes the matrix to standard output.
     */

    public void write() {
        String str = new String();
        for (int i = 0; i < m; i++) {
            str = "";
            for (int j = 0; j < n; j++) {
                str = str + "   " + String.format(Locale.US, "%10.5f", a[i][j]);
            }
            System.out.println(str);
        }
    }

    /**
     * Writes the matrix onto a string, using 5 fraction digits.
     */
    public String toString() {
        String str = new String("");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                str = str + "   " + String.format(Locale.US, "%10.5f", a[i][j]);
            }
            str = str + "\n";
        }
        return str;
    }

    /**
     * Writes the matrix onto a string.
     *
     * @param fmt formatstring, e.g., "%15.10f"
     */
    public String toString(String fmt) {
        String str = new String("");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                str = str + "  " + String.format(Locale.US, fmt, a[i][j]);
            }
            str = str + "\n";
        }
        return str;
    }


}

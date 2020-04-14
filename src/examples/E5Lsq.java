package examples;

import datan.*;
import datangraphics.DatanGraphics;
import datangraphics.GraphicsWithDataPointsAndPolyline;

import java.util.Locale;

/**
 * Example demonstrating the fit of a nonlinear function to data using LsqMar
 */
public class E5Lsq {
    private int n;
    private double[][] dxasy;
    private DatanVector t, x, y, deltay;
    private DatanMatrix cx;
    private LsqFunction lexp;
    private double mf;

    public E5Lsq() {
        String s = "Example demonstrating the fit of a nonlinear function to data using LsqMar";
        DatanFrame df = new DatanFrame(getClass().getName(), s);
// simulate data
        double[] xx = {10., 1.};
        x = new DatanVector(xx);
        n = 20;
        int[] seeds = {15, 211};
        DatanRandom.setSeedsEcuy(seeds);
        double[] tt = new double[n];
        double[] deltayy = DatanRandom.standardNormal(n);
        double[] yy = DatanRandom.ecuy(n);
        double delt = 2. / 21.;
        lexp = new LsqExp();
        df.writeLine("         t" + "           y" + "      deltay");
        for (int i = 0; i < n; i++) {
            tt[i] = (double) (i + 1) * delt;
            double sig = 2. * (.5 + yy[i]);
            yy[i] = lexp.getValue(x, tt[i]) + deltayy[i] * sig;
            deltayy[i] = sig;
            df.writeLine(String.format(Locale.US, "%10.5f", tt[i])
                    + "  " + String.format(Locale.US, "%10.5f", yy[i]) +
                    "  " + String.format(Locale.US, "%10.5f", deltayy[i]));
        }
// perform fit
        t = new DatanVector(tt);
        y = new DatanVector(yy);
        deltay = new DatanVector(deltayy);
        double[] x1 = {.1, .1};  // first approximation
        x = new DatanVector(x1);
        int[] list = {1, 1};
        LsqMar lm = new LsqMar(t, y, deltay, x, list, lexp);
// write results
        df.writeLine("\n Fit to exponential: First Approximation x = " + x.toString());
        mf = lm.getChiSquare();
        df.writeLine("Minimum function M = " + String.format(Locale.US, "%10.5f", mf));
        x = lm.getResult();
        df.writeLine("Result x = " + x.toString());
        cx = lm.getCovarianceMatrix();
        df.writeLine("CovarianceMatrix cx = ");
        df.writeLine(cx.toString());
// asymmetric errors
        LsqAsm la = new LsqAsm(t, y, deltay, x, list, cx, mf, lexp);
        dxasy = la.getAsymmetricErrors(0.);
        DatanMatrix as = new DatanMatrix(dxasy);
        df.writeLine("Asymmetic errors:");
        df.writeLine(as.toString());
        plotDataAndFittedCurve();
        plotParameterPlane();
    }


    protected void plotDataAndFittedCurve() {
// prepare data and fitted curve for plotting
        int npl = 1000;
        double[] xpl = new double[npl];
        double[] ypl = new double[npl];
        double[] datx = new double[n];
        double[] daty = new double[n];
        double[] datsx = new double[n];
        double[] datsy = new double[n];
        double[] datrho = new double[n];
        for (int i = 0; i < n; i++) {
            datx[i] = t.getElement(i);
            daty[i] = y.getElement(i);
            datsx[i] = 0.;
            datsy[i] = deltay.getElement(i);
            datrho[i] = 0.;
        }
        double del = (t.getElement(n - 1) - t.getElement(0) + 1.) / (double) (npl - 1);
        for (int i = 0; i < npl; i++) {
            xpl[i] = -.5 + t.getElement(0) + (double) i * del;
            ypl[i] = lexp.getValue(x, xpl[i]);
        }
// produce graphics
        String caption = "Fit to exponential";
        double scale = .2;
        GraphicsWithDataPointsAndPolyline gdp = new GraphicsWithDataPointsAndPolyline(getClass().getName(), "examples.E5Lsq.ps",
                xpl, ypl, 1, scale, datx, daty, datsx, datsy, datrho, "t", "y", caption);
    }


    protected void plotParameterPlane() {
        double x1 = x.getElement(0);
        double x2 = x.getElement(1);
        double dx1 = Math.sqrt(cx.getElement(0, 0));
        double dx2 = Math.sqrt(cx.getElement(1, 1));
        double rho = (cx.getElement(1, 0)) / (dx1 * dx2);
// prepare size of plot
        double xmin = x1 - 2. * dx1;
        double xmax = x1 + 2. * dx1;
        double ymin = x2 - 2. * dx2;
        double ymax = x2 + 2. * dx2;
        DatanGraphics.openWorkstation(getClass().getName(), getClass().getName() + "_ParameterPlane.eps");
        DatanGraphics.setFormat(0., 0.);
        DatanGraphics.setWindowInComputingCoordinates(xmin, xmax, ymin, ymax);
        DatanGraphics.setViewportInWorldCoordinates(.2, .9, .16, .86);
        DatanGraphics.setWindowInWorldCoordinates(-.414, 1., 0., 1.);
        DatanGraphics.setBigClippingWindow();
        DatanGraphics.chooseColor(2);
        DatanGraphics.drawFrame();
        DatanGraphics.drawScaleX("x_1");
        DatanGraphics.drawScaleY("x_2");
        DatanGraphics.drawBoundary();
        DatanGraphics.chooseColor(5);
// draw data point with errors (and correlation)
        DatanGraphics.drawDatapoint(1, 1., x1, x2, dx1, dx2, rho);
        DatanGraphics.chooseColor(2);
        DatanGraphics.drawCaption(1., "Parameter plane");
// draw confidence contour
        double fcont = mf + 1.;
        int nx = 100;
        int ny = 100;
        double dx = (xmax - xmin) / (int) nx;
        double dy = (ymax - ymin) / (int) ny;
        myContourUserFunction mcuf = new myContourUserFunction();
        DatanGraphics.setBigClippingWindow();
        DatanGraphics.chooseColor(1);
        DatanGraphics.drawContour(xmin, ymin, dx, dy, nx, ny, fcont, mcuf);
// draw asymmetric errors as horizontal and vertical bars
        DatanGraphics.chooseColor(3);
        double[] xpl = new double[2];
        double[] ypl = new double[2];
        for (int i = 0; i < 2; i++) {
            if (i == 0) xpl[0] = x1 - dxasy[0][0];
            else xpl[0] = x1 + dxasy[0][1];
            xpl[1] = xpl[0];
            ypl[0] = ymin;
            ypl[1] = ymax;
            DatanGraphics.drawPolyline(xpl, ypl);
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) ypl[0] = x2 - dxasy[1][0];
            else ypl[0] = x2 + dxasy[1][1];
            ypl[1] = ypl[0];
            xpl[0] = xmin;
            xpl[1] = xmax;
            DatanGraphics.drawPolyline(xpl, ypl);
        }
        DatanGraphics.closeWorkstation();
    }


    private class myContourUserFunction extends DatanUserFunction {

        public double getValue(double x1, double x2) {
            double r;
            double arg[] = {x1, x2};
            DatanVector a = new DatanVector(arg);
            r = 0.;
            for (int i = 0; i < n; i++) {
                double d = (y.getElement(i) - lexp.getValue(a, t.getElement(i))) / deltay.getElement(i);
                double s = d * d;
                r = r + s;
            }
            return r;
        }
    }


    private class LsqExp extends DatanUserFunction {
        public double getValue(DatanVector x, double t) {
            return x.getElement(0) * Math.exp(-(x.getElement(1) * t));
        }
    }

    public static void main(String s[]) {
        new E5Lsq();
    }

}

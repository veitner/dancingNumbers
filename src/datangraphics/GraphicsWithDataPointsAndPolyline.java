package datangraphics;


/**
 * A class producing a plot with data points and a polyline.
 *
 * @author Siegmund Brandt.
 */
public class GraphicsWithDataPointsAndPolyline {

    private int nmark, nws;
    private double[] xpl, ypl, datx, daty, datsx, datsy, datrho;
    private String textX, textY, caption, filename, frametitle;
    private double scalef, xa, xb, ya, yb, xd, yd;

    /**
     * Produces a plot with data points and a polyline.
     *
     * @param frametitle title of DatanGraphicsFrame; if frameTitle is blank the no DatanGraphicsFrame is initialized.
     * @param filename   name of Postscript file - has to end with either ".ps" (for postscript) or ".eps" (for encapsulated postscript).
     *                   If filename is blank, no file is initialized.
     * @param xpl        x coordinates of polyline
     * @param ypl        y coordinates of polyline
     * @param nmark      form of polymarker symbolizing the data point (1: open circle, 2: closed circle, 3: open sqare, 4: closed sqare, 5: open diamond, 6: closed diamond, 7: +, 8: x. 9: *)
     * @param scalef     scale factor; the diameter of the polymarker is 1% of the picture diagonal multiplied by this factor
     * @param datx       x coordinate of data points
     * @param daty       y coordinate of data points
     * @param datsx      errors in x; error bar is drawn only if deltax > 0 and if its size would exceed the radius if the polymarker
     * @param datsy      errors in y; error bar is drawn only if deltax > 0 and if its size would exceed the radius if the polymarker
     * @param datrho     correlations between errors; covariance ellipse is not drawn if rho = 0 (drawing can be forced by giving nmark a negative sign)
     * @param textX      text for x axis
     * @param textY      text for y axis
     * @param caption    text for caption of plot
     */
    public GraphicsWithDataPointsAndPolyline(String frametitle, String filename, double[] xpl, double[] ypl, int nmark, double scalef,
                                             double[] datx, double[] daty, double[] datsx, double[] datsy, double[] datrho,
                                             String textX, String textY, String caption) {

        this.xpl = xpl;
        this.ypl = ypl;
        this.nmark = nmark;
        this.scalef = scalef;
        this.datx = datx;
        this.daty = daty;
        this.datsx = datsx;
        this.datsy = datsy;
        this.datrho = datrho;
        this.textX = textX;
        this.textY = textY;
        this.caption = caption;
        this.filename = filename;
        this.frametitle = frametitle;

        produceGraphics();

    }

    private void produceGraphics() {
        DatanGraphics.openWorkstation(frametitle, filename);
// set scales
        xa = datx[0] - datsx[0];
        xb = datx[0] + datsx[0];
        ya = daty[0] - datsy[0];
        yb = daty[0] + datsy[0];
        for (int i = 1; i < datx.length; i++) {
            xa = Math.min(xa, datx[i] - datsx[i]);
            xb = Math.max(xb, datx[i] + datsx[i]);
            ya = Math.min(ya, daty[i] - datsy[i]);
            yb = Math.max(yb, daty[i] + datsy[i]);
        }
        xd = xb - xa;
        yd = yb - ya;
        xa = DatanGraphics.roundDown(xa - 0.1 * xd);
        xb = DatanGraphics.roundUp(xb + 0.1 * xd);
        ya = DatanGraphics.roundDown(ya - 0.1 * yd);
        yb = DatanGraphics.roundUp(yb + 0.1 * yd);
// draw background
        DatanGraphics.chooseColor(2);
        DatanGraphics.setWindowInComputingCoordinates(xa, xb, ya, yb);
        DatanGraphics.setWindowInWorldCoordinates(-0.214, 1.2, -0.1, 0.9);
        DatanGraphics.setViewportInWorldCoordinates(0.1, 1.1, 0.1, 0.75);
        DatanGraphics.setFormat(0., 0.);
        DatanGraphics.setBigClippingWindow();
        DatanGraphics.drawFrame();
        DatanGraphics.drawScaleX(textX);
        DatanGraphics.drawScaleY(textY);
        DatanGraphics.drawCaption(0., caption);
        DatanGraphics.drawBoundary();
        DatanGraphics.setSmallClippingWindow();
// draw data points
        DatanGraphics.chooseColor(1);
        for (int i = 0; i < datx.length; i++) {
            DatanGraphics.drawDatapoint(nmark, scalef, datx[i], daty[i], datsx[i], datsy[i], datrho[i]);
        }
// draw polyline
        DatanGraphics.setSmallClippingWindow();
        DatanGraphics.chooseColor(5);
        DatanGraphics.drawPolyline(xpl, ypl);
        DatanGraphics.closeWorkstation();
    }

}

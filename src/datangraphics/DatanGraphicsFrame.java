package datangraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**
 * A class representing a graphics frame.
 * <p>
 * Instances of this class are to be created only by method openWorkstation of class DatanGraphics.
 *
 * @author Siegmund Brandt.
 */
public class DatanGraphicsFrame extends JFrame {
    public static final long serialVersionUID = 1L;
//@SuppressWarnings("unchecked")

    int index, minWidth, minHeight;
    double hwratio;
    double root2 = Math.sqrt(2.);
    //Dimension mdim;
    JFrame frame;
    DatanCanvas canvas;
    AffineTransform af;
    BufferedImage bim;
    int length, numpolys;
    int bimw = 0, bimh = 0;
    Dimension Dmin, Dmax, DminInner, DmaxInner, dmin, dmax;
    //short[] buf_points = {1, 2, 10000, 20000, 20000, 20000};
    //int[] buf_poly = {0};
    //int[] numpolys = {1};
    int[] bufpol;
    short[] bufpts;

    public DatanGraphicsFrame(String frameTitle, String filename, int index, double formWidth, double formHeight, int numpolys, int[] bufpolys, short[] bufpoints) {
        int minWidth, minHeight, nww, nwh, nmwd, mxheight, hpos, wpos;
        this.numpolys = numpolys;
        String fullFrameTitle;
        bufpol = new int[numpolys];
        length = 0;
        for (int ipol = 0; ipol < numpolys; ipol++) {
            bufpol[ipol] = bufpolys[ipol];
            int noffs = bufpolys[ipol];
            length = length + 2 * bufpoints[noffs + 1] + 2;
        }
        bufpts = new short[length];
        for (int i = 0; i < length; i++) {
            bufpts[i] = bufpoints[i];
        }
        this.index = index;
        af = new AffineTransform();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        fullFrameTitle = frameTitle;
        if (!(filename.equals("") || filename.equals(" "))) {
            fullFrameTitle = fullFrameTitle + ". This plot is also written on file " + filename;
        }
        setTitle(fullFrameTitle);
        // size the frame
        pack();
        Dimension mdimw = getMinimumSize();
        minWidth = mdimw.width;
        minHeight = mdimw.height;
        Dimension mdim = getContentPane().getMinimumSize();
        Dmin = new Dimension(0, 0);
        DminInner = new Dimension(0, 0);
        DmaxInner = new Dimension(0, 0);
        hwratio = formHeight / formWidth;
        if (hwratio > 1) {
            double d = 0.5 * 32767. * (1. - 1. / hwratio);
            dmin = new Dimension((int) d, 0);
            dmax = new Dimension(32767 - (int) d, 32767);
        } else {
            double d = 0.5 * 32767. * (1. - hwratio);
            dmin = new Dimension(0, (int) d);
            dmax = new Dimension(32767, 32767 - (int) d);
        }
        Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
        mxheight = (int) ((float) screendim.height * 0.7);
        nmwd = mxheight;
        if (nmwd < mdim.width) nmwd = mdim.width;
        if (hwratio <= 1.0) {
            nww = mxheight;
            nwh = (int) Math.round((double) (nmwd - 2) * hwratio) + 2;
            if (nwh < 10) nwh = 10;
            nwh += mdim.height;
        } else {
            nwh = nmwd + mdim.height;
            nww = (int) Math.round((double) (nmwd - 2) / hwratio) + 2;
            if (nww < mdim.width) nww = mdim.width;
        }
        nww += mdimw.width - mdim.width;
        nwh += mdimw.height - mdim.height;
        setSize(nww, nwh);
        hpos = DatanGraphics.wlisttop + index * DatanGraphics.wlistvdel;
        if (hpos + nwh > DatanGraphics.screendim.height) hpos = DatanGraphics.screendim.height - nwh;
        wpos = DatanGraphics.wlisthdel * index;
        if (wpos + nww > DatanGraphics.screendim.width) wpos = DatanGraphics.screendim.width - nww;
        setLocation(new Point(wpos, hpos));
        canvas = new DatanCanvas();
        canvas.setBackground(DatanGraphics.ct[0]);
        getContentPane().add("Center", canvas);
        setVisible(true);
        toFront();
    }

    private class DatanCanvas extends Canvas {
        public static final long serialVersionUID = 1L;
        GeneralPath polyline;
        final int maxpolypoints = 200;

        public DatanCanvas() {
            polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, maxpolypoints);
        }

        public boolean isDoubleBuffered() {
            return true;
        }

        public void drawPolylines(Graphics2D g2) {
            int i, ipol, npoints, noffs;
// draw GeneralPath (polyline)
            for (ipol = 0; ipol < numpolys; ipol++) {
                noffs = bufpol[ipol];
                npoints = bufpts[noffs + 1];
                if (npoints > 1) {  //was 0, TS on 20.08.'12
                    polyline.moveTo(bufpts[noffs + 2], bufpts[noffs + 3]);
                    for (i = 1; i < npoints; i++) {
                        polyline.lineTo(bufpts[noffs + 2 * i + 2],
                                bufpts[noffs + 2 * i + 3]);
                    }
/* //TS on 20.08.'12
                if (npoints == 1) {
                    polyline.lineTo(bufpts[noffs+2], bufpts[noffs+3]);
                }
*/
                    g2.setStroke(DatanGraphics.strokes[bufpts[noffs] - 1]);
                    g2.setPaint(DatanGraphics.ct[bufpts[noffs]]);
                    g2.draw(polyline);
                    polyline.reset();
                }
            }
        }

        public AffineTransform affTransf(Dimension Dmin, Dimension Dmax,
                                         Dimension dmin, Dimension dmax) {
            float Xa, Xb, Ya, Yb, xa, xb, ya, yb, cx, cy, Xas, Yas;
            float r, R, XXa, XXb, YYa, YYb, m02, m12;
            Xa = (float) Dmin.width;
            Ya = (float) Dmin.height;
            Xb = (float) Dmax.width;
            Yb = (float) Dmax.height;
            xa = (float) dmin.width;
            ya = (float) dmax.height;
            xb = (float) dmax.width;
            yb = (float) dmin.height;
            r = Math.abs((yb - ya) / (xb - xa));
            R = Math.abs((Yb - Ya) / (Xb - Xa));

            if (r > R) {
                XXa = 0.5f * (Xb - Xa - (Xb - Xa) * R / r);
                XXb = 0.5f * (Xb - Xa + (Xb - Xa) * R / r);
                Xas = Xa;
//		Xa = XXa;
//		Xb = XXb;
                Xa = Xas + XXa;
                Xb = Xas + XXb;
            } else {
                YYa = 0.5f * (Yb - Ya - (Yb - Ya) * r / R);
                YYb = 0.5f * (Yb - Ya + (Yb - Ya) * r / R);
//		Ya = YYa;
//		Yb = YYb;
                Yas = Ya;
                Ya = Yas + YYa;
                Yb = Yas + YYb;
            }


            cx = (Xb - Xa) / (xb - xa);
            cy = (Yb - Ya) / (yb - ya);
            m02 = Xa - cx * xa + 0.5f;
            m12 = Ya - cy * ya + 0.5f;
            af.setTransform(cx, 0.f, 0.f, cy, m02, m12);
            return af;
        }


        public void paint(Graphics g) {
            float ffc, flw;
            int cw, ch;
            Graphics2D g2 = (Graphics2D) g;
            Dmax = canvas.getSize();

            cw = Dmax.width;
            ch = Dmax.height;

            if (bimw != cw || bimh != ch) {

                if (bimw != cw || bimh != ch) {
                    bim = (BufferedImage) createImage(cw, ch);
                }

                Graphics2D bimg2 = bim.createGraphics();

                bimg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                bimg2.setBackground(DatanGraphics.ct[0]);
                bimg2.clearRect(0, 0, cw, ch);

                DminInner.width = Dmin.width + 1;
                DminInner.height = Dmin.height + 1;
                DmaxInner.width = Dmax.width - 2;
                DmaxInner.height = Dmax.height - 2;

                bimg2.setTransform(affTransf(DminInner, DmaxInner, dmin, dmax));
                drawPolylines(bimg2);
                bimw = cw;
                bimh = ch;
                bimg2.dispose();
                bimg2 = null;
            }

            g2.drawImage(bim, 0, 0, this);

        }

    }


    public void dispose() {
        super.dispose();
        DatanGraphics.frames.setElementAt(null, index);
    }


    public int getIndex() {
        return index;
    }


}

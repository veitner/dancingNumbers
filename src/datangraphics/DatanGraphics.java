//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package datangraphics;

import datan.DatanUserFunction;
import datan.Histogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

/**
 * A final class with methods to produce graphics.
 *
 * @author Siegmund Brandt.
 */
public final class DatanGraphics {
    private static JFrame dGFrame;
    private static JFrame closeFrame;
    private static JOptionPane pane = new JOptionPane();
    private static PrintStream pr;
    private static int frameIndex;
    static Vector<DatanGraphicsFrame> frames;
    static int wlisttop;
    static int wlisthdel;
    static int wlistvdel;
    static Dimension screendim;
    static Color[] ct = new Color[]{new Color(0.0F, 0.0F, 0.9F), new Color(1.0F, 1.0F, 0.0F), new Color(1.0F, 1.0F, 1.0F), new Color(0.0F, 1.0F, 1.0F), new Color(1.0F, 0.784F, 0.0F), new Color(1.0F, 0.0F, 0.0F), new Color(0.753F, 0.753F, 0.753F), new Color(0.0F, 1.0F, 0.0F), new Color(1.0F, 0.0F, 1.0F)};
    private static float[] lw = new float[]{70.0F, 70.0F, 70.0F, 70.0F, 70.0F, 70.0F, 70.0F, 70.0F};
    static BasicStroke[] strokes;
    private static double[] framecoords;
    private static int istr;
    private static byte[] bstrng;
    private static byte[] bcmstr;
    private static boolean iqpsfp;
    private static int[] ixyc;
    private static Color[] ctab;
    private static float[] linw;
    private static double cdin;
    private static double root2;
    private static double[] qac;
    private static double[] qbc;
    private static double[] qdc;
    private static double[] qdci;
    private static double[] qaw2;
    private static double[] qbw2;
    private static double[] qdw2;
    private static double[] qdw2i;
    private static double[] qawp;
    private static double[] qbwp;
    private static double[] qdwp;
    private static double[] qdwpi;
    private static double[] qad;
    private static double[] qbd;
    private static double[] qdd;
    private static double[] qddi;
    private static double[] qform;
    private static double[] qadp;
    private static double[] qbdp;
    private static double[] qddp;
    private static double[] qddpi;
    private static double[] qadcl;
    private static double[] qbdcl;
    private static double[] papersize;
    private static double[] paperborders;
    private static double[] psOffsets;
    private static double[] plsizval;
    private static boolean rotatePlot;
    private static int iqplcl;
    private static int iqnplc;
    private static int iqnplm;
    private static double[] qoutx;
    private static double[] qouty;
    private static short[] bufPoints;
    private static int[] bufPoly;
    private static int numpolys;
    private static int bufPointsLengthFilled;
    private static int bufPointsLengthTotal;
    private static int bufPolyLengthFilled;
    private static int bufPolyLengthTotal;
    private static boolean open;
    private static boolean workstation1Open;
    private static boolean workstation2Open;
    private static String psFilename;
    private static String workstation1Title;
    private static int psModus;
    private static int[] k;
    private static int[] kk;
    private static int kkk;
    private static int[] ll;
    private static int[] lll;
    private static int[] mm;
    private static int[] m;
    private static final short[] iqlett;
    private static int id;
    private static int iga;
    private static int miga;
    private static int ko;
    private static int nuj;
    private static double alengt;
    private static double g1;
    private static double ga;
    private static double gr;
    private static double gz;
    private static byte[] c;
    private static double fg1;
    private static double fg2;
    private static double utx;
    private static double uty;
    private static double z;
    private static int[] idop;
    private static int[] j;
    private static int[] idat;
    private static double[] avin;
    private static double[] avnext;
    private static double[] rv;
    private static double[] av;
    private static double[] op;
    private static double[] gv;
    private static double[] gve;
    private static double[] rve;
    private static double[] wert2d;
    private static double xmovtx;
    private static double piqurt;
    private static boolean rnt;
    private static int nc;
    private static double an;
    private static double en;
    private static double atims;
    private static double eps;
    private static double atdel;
    private static double aneps;
    private static double eneps;
    private static double eta;
    private static double fixdef;
    private static double deldef;
    private static double ticdef;
    private static double grdef;
    private static int ntcdef;
    private static boolean numdef;
    private static boolean lscset;
    private static double[] lastPoint;
    private static double[] borderPoints;

    public DatanGraphics() {
    }

    private static void open() {
        if (!open) {
            open = true;
            workstation1Open = false;
            workstation2Open = false;
            dGFrame = new JFrame();
            setWindowInWorldCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setViewportInWorldCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setWindowInComputingCoordinates(0.0D, 0.0D, 0.0D, 0.0D);
            setFormat(0.0D, 0.0D);
            screendim = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame var0 = new JFrame();
            var0.setVisible(true);
            Point startPoint = var0.getLocation();
            wlisttop = startPoint.y + 1;
            Dimension var1 = var0.getSize();
            Dimension var2 = var0.getContentPane().getSize();
            wlisthdel = (var1.width - var2.width) / 2;
            if (wlisthdel < 2) {
                wlisthdel = 2;
            }

            wlistvdel = var1.height - var2.height - wlisthdel;
            var0.dispose();
            numpolys = 0;
            bufPointsLengthFilled = 0;
            bufPolyLengthFilled = 0;
            frames = new Vector<>();
            frameIndex = -1;
            bufPointsLengthTotal = 160000;
            bufPoints = new short[bufPointsLengthTotal];
            bufPolyLengthTotal = 1000;
            bufPoly = new int[bufPolyLengthTotal];
            qac[0] = 0.0D;
            qbc[0] = 1.0D;
            qac[1] = 0.0D;
            qbc[1] = 1.0D;
            qaw2[0] = 0.0D;
            qbw2[0] = 1.0D;
            qaw2[1] = 0.0D;
            qbw2[1] = 1.0D;
            qawp[0] = 0.0D;
            qbwp[0] = 1.0D;
            qawp[0] = 0.0D;
            iqplcl = 1;
        }

    }

    private static void close() {
        if (workstation1Open) {
            closeWorkstation();
        }

        if (!frames.isEmpty()) {
            closeFrame = new JFrame("Close graphics frame(s)");
            closeFrame.setSize(300, 100);
            closeFrame.setResizable(false);
            Container var0 = closeFrame.getContentPane();
            var0.setLayout(new FlowLayout());
            JButton var1 = new JButton("close graphics frame(s)");
            DatanGraphics.CloseButtonListener var2 = new DatanGraphics.CloseButtonListener();
            var1.addActionListener(var2);
            var0.add(var1);
            closeFrame.setVisible(true);
        }

        if (workstation2Open) {
            closeWorkstation();
        }

        open = false;
    }

    /**
     * Creates a new, initially invisible DatanGraphicsFrame with the specified title
     *
     * @param frameTitle title of DatanGraphicsFrame; if frameTitle is blank the no DatanGraphicsFrame is initialized.
     */
    public static void openWorkstation(String frameTitle) {
        openWorkstation(frameTitle, "");
    }

    /**
     * Creates a new, initially invisible DatanGraphicsFrame with the specified title. In addition, a file is initialized into which the graphics is written in Postscript format.
     *
     * @param frameTitle title of DatanGraphicsFrame; if frameTitle is blank the no DatanGraphicsFrame is initialized.
     * @param filename   name of Postscript file - has to end with either ".ps" (for postscript) or ".eps" (for encapsulated postscript). If filename is blank, no file is initialized.
     */
    public static void openWorkstation(String frameTitle, String filename) {
        boolean var2 = false;
        boolean var3 = false;
        if (!frameTitle.equals("") && !frameTitle.equals(" ")) {
            var2 = true;
        }

        if (!filename.equals("") && !filename.equals(" ")) {
            var3 = true;
        }

        if (!open) {
            open();
        }

        if (workstation1Open || workstation2Open) {
            JOptionPane.showMessageDialog(dGFrame, "Cannot open workstation; open already", "", 0);
        }

        if (var2) {
            workstation1Open = true;
            workstation1Title = frameTitle;
        }

        if (var3) {
            workstation2Open = true;
            String var4 = getExtension(filename);
            psModus = 0;
            if (var4.equals("ps")) {
                psModus = 0;
            } else if (var4.equals("eps")) {
                psModus = 1;
            } else {
                JOptionPane.showMessageDialog(dGFrame, "File extension must be .ps or .eps", "", 0);
            }
        }

        psFilename = filename;
    }

    /**
     * Finalizes graphics, displays DatanGraphicsFrame (if one was opened), and closes Postscript file (if one was opened).
     */
    public static void closeWorkstation() {
        if (workstation1Open) {
            showPlot();
            workstation1Open = false;
        }

        if (workstation2Open) {
            writePSFile(psFilename);
            workstation2Open = false;
        }

        numpolys = 0;
    }

    private static void showPlot() {
        flushPolyline();
        frameIndex = frames.size();
        boolean var0 = false;
        if (frames.contains(null)) {
            var0 = true;
            frameIndex = frames.indexOf(null);
        }

        DatanGraphicsFrame var1 = new DatanGraphicsFrame(workstation1Title, psFilename, frameIndex, qform[0], qform[1], numpolys, bufPoly, bufPoints);
        bufPointsLengthFilled = 0;
        if (var0) {
            frames.setElementAt(var1, frameIndex);
        } else {
            frames.addElement(var1);
        }

    }

    private static void writePSFile(String var0) {
        flushPolyline();
        FileOutputStream[] var2 = new FileOutputStream[1];
        PrintStream[] var3 = new PrintStream[1];
        if (openPrintStream(var0, var2, var3)) {
            pr = var3[0];
            exportPS(false);
            closePrintStream(var0, var2[0], pr);
            bufPointsLengthFilled = 0;
        }
    }

    private static boolean openPrintStream(String var0, FileOutputStream[] var1, PrintStream[] var2) {
        try {
            var1[0] = new FileOutputStream(var0);
        } catch (IOException var4) {
            JOptionPane.showMessageDialog(dGFrame, "Cannot open file (E)PS File\n", "Error in DatanGraphics", 0);
            System.out.println("Cannot open file (E)PS File\n" + var4.getMessage());
            return false;
        }

        var2[0] = new PrintStream(var1[0]);
        return true;
    }

    private static void closePrintStream(String var0, FileOutputStream var1, PrintStream var2) {
        var2.close();
        try {
            var1.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(dGFrame, String.format("Cannot close file (E)PS File: %s\n", var0), "Error in DatanGraphics", 0);
            System.out.println("Cannot close file\n" + ex.getMessage());
        }

    }

    private static void exportPS(boolean lwrelPS) {
        char[] var1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        float[] var3 = new float[3];
        short var14 = 1;
        int var18 = 0;
        int var19 = 0;
        framecoords[0] = qad[0];
        framecoords[1] = qad[1];
        framecoords[2] = qbd[0];
        framecoords[3] = qbd[1];
        calculatePlotSize();
        float var11 = 118.11024F * (float) (plsizval[0] / (framecoords[2] - framecoords[0] + 1.0D) + plsizval[1] / (framecoords[3] - framecoords[1] + 1.0D));
        float var12 = 64.0F * var11;
        if (psModus == 0) {
            pr.println("%!PS-Adobe-2.0");
            pr.println("initgraphics");
        } else {
            pr.println("%!PS-Adobe-2.0 EPSF-1.2");
            pr.println("%%Creator: DATAN");
            pr.print("%%CreationDate: ");
            pr.println();
            pr.println("%%DocumentFonts:");
            pr.println("%%Pages: 1");
            pr.print("%%BoundingBox: 0 0 ");
            pr.print((int) Math.ceil((framecoords[2] - framecoords[0]) * (double) var11 * 0.12D));
            pr.print(' ');
            pr.println((int) Math.ceil((framecoords[3] - framecoords[1]) * (double) var11 * 0.12D));
            pr.print("%%EndComments\n%%EndProlog\n%%Page: 1\n");
        }

        if (lwrelPS) {
            pr.print("/p {");
            pr.print(var12);
            pr.print(" mul setlinewidth\n");
        } else {
            pr.print("/p {2.3622 mul setlinewidth\n");
        }

        pr.print("moveto dup 0 eq {0 1}if {rlineto}repeat stroke}bind def\n");
        pr.print("/z { ");
        ctab[0].getRGBColorComponents(var3);

        int var6;
        for (var6 = 0; var6 < 3; ++var6) {
            pr.print(var3[var6]);
            pr.print(' ');
        }

        pr.print("setrgbcolor} def\n");
        if (lwrelPS) {
            pr.print("/t {");
            pr.print(var12);
            pr.print(" mul setlinewidth\n");
        } else {
            pr.print("/t {2.3622 mul setlinewidth\n");
        }

        pr.print("moveto dup 0 eq {0 1}if {rlineto}repeat gsave z closepath\nfill grestore stroke} bind def\n.12 .12 scale\n1 setlinecap\n1 setlinejoin\n");

        int var5;
        for (var5 = 0; var5 < 8; ++var5) {
            pr.print("/");
            pr.print(var1[var5]);
            pr.print(" { ");
            ctab[var5 + 1].getRGBColorComponents(var3);

            for (var6 = 0; var6 < 3; ++var6) {
                pr.print(var3[var6]);
                pr.print(' ');
            }

            pr.print("setrgbcolor ");
            pr.print(linw[var5]);
            pr.print("} def\n");
        }

        if (psModus == 0) {
            pr.print(psOffsets[0] * 236.22047424316406D);
            pr.print(" ");
            pr.print(psOffsets[1] * 236.22047424316406D);
            pr.println(" translate");
            if (rotatePlot) {
                pr.print(plsizval[1] * 236.22047424316406D);
                pr.println(" 0 translate");
                pr.println("90 rotate");
            }
        }

        short var15 = -1;
        int var16 = -1;
        int var17 = -1;
        outpst(1, 0);

        for (int var7 = 0; var7 < numpolys; ++var7) {
            int var8 = bufPoly[var7];
            short var9 = bufPoints[var8 + 1];
            int var13 = 0;
            if (var9 > 0) {
                var14 = bufPoints[var8];
                tracoords(bufPoints[var8 + 2], bufPoints[var8 + 3], var11, ixyc);
                var18 = ixyc[0];
                var19 = ixyc[1];
                boolean var24 = var14 == var15 && var18 == var16 && var19 == var17;

                for (var5 = 1; var5 < var9; ++var5) {
                    tracoords(bufPoints[var8 + 2 * var5 + 2], bufPoints[var8 + 2 * var5 + 3], var11, ixyc);
                    int var20 = ixyc[0];
                    int var21 = ixyc[1];
                    int var22 = var18 - var20;
                    int var23 = var19 - var21;
                    if (var22 != 0 || var23 != 0) {
                        ++var13;
                        outpst(0, var22);
                        outpst(0, var23);
                        var24 = false;
                        var18 = var20;
                        var19 = var21;
                    }

                    if ((var13 >= 50 || var5 == var9 - 1) && !var24) {
                        outpst(0, var13);
                        outpst(0, var20);
                        outpst(0, var21);
                        outpst(5, var14);
                        var24 = true;
                        var13 = 0;
                    }
                }
            }

            var15 = var14;
            var16 = var18;
            var17 = var19;
        }

        outpst(3, 0);
        if (psModus != 0) {
            pr.print("%%Trailer\n");
        }

        pr.print("showpage\n");
        pr.flush();
    }

    private static void calculatePlotSize() {
        double var0 = papersize[0] - paperborders[0] - paperborders[1];
        double var2 = papersize[1] - paperborders[2] - paperborders[3];
        double var4 = var2 / var0;
        double var6 = qform[1] / qform[0];
        plsizval[0] = qform[0];
        plsizval[1] = qform[1];
        rotatePlot = var6 > 1.0D && var4 < 1.0D || var6 < 1.0D && var4 > 1.0D;
        if (psModus == 0) {
            double var8;
            double var10;
            if (rotatePlot) {
                var8 = plsizval[1] / var0;
                var10 = plsizval[0] / var2;
            } else {
                var8 = plsizval[0] / var0;
                var10 = plsizval[1] / var2;
            }

            double var12 = Math.max(var8, var10);
            if (var12 > 1.0D) {
                plsizval[0] /= var12;
                plsizval[1] /= var12;
            }

            psOffsets[0] = paperborders[0];
            psOffsets[1] = paperborders[2];
            double[] var10000;
            if (!rotatePlot) {
                var10000 = psOffsets;
                var10000[0] += (var0 - plsizval[0]) * 0.5D;
                var10000 = psOffsets;
                var10000[1] += (var2 - plsizval[1]) * 0.5D;
            } else {
                var10000 = psOffsets;
                var10000[0] += (var0 - plsizval[1]) * 0.5D;
                var10000 = psOffsets;
                var10000[1] += (var2 - plsizval[0]) * 0.5D;
            }
        }

    }

    private static void outpst(int var0, int var1) {
        if (var0 == 1) {
            istr = 0;
        } else if (var0 == 3) {
            pr.write(bstrng, 0, istr);
            pr.println();
            istr = 0;
        } else {
            int var3 = inttobytes(var1, bcmstr, 12);
            int ilen = 12 - var3;
            int var2;
            if (var0 != 1 && var0 != 5) {
                if (istr + ilen >= 70) {
                    pr.write(bstrng, 0, istr);
                    pr.println();
                    istr = 0;
                }

                if (istr > 0) {
                    bstrng[istr] = 32;
                    ++istr;
                }

                for (var2 = 0; var2 < ilen; ++var2) {
                    bstrng[istr + var2] = bcmstr[var3 + var2];
                }

                istr += ilen;
            } else {
                if (var0 == 1) {
                    if (istr > 0) {
                        bstrng[istr] = 32;
                        ++istr;
                    }

                    for (var2 = 0; var2 < ilen; ++var2) {
                        bstrng[istr + var2] = bcmstr[var3 + var2];
                    }

                    istr += ilen;
                } else {
                    bstrng[istr] = 32;
                    bstrng[istr + 1] = (byte) (97 + var1 - 1);
                    istr += 2;
                }

                bstrng[istr] = 32;
                ++istr;
                if (iqpsfp) {
                    bstrng[istr] = 116;
                } else {
                    bstrng[istr] = 112;
                }

                ++istr;
            }
        }
    }

    private static void tracoords(short var0, short var1, float var2, int[] var3) {
        var3[0] = (int) ((float) ((double) var0 - framecoords[0]) * var2 + 0.5F);
        var3[1] = (int) ((float) ((double) var1 - framecoords[1]) * var2 + 0.5F);
    }

    private static int inttobytes(int var0, byte[] var1, int var2) {
        char[] var3 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int var5 = var0 >= 0 ? var0 : -var0;

        int var4;
        for (var4 = var2; var4 > 0 && var5 != 0; var5 /= 10) {
            --var4;
            int var6 = var5 % 10;
            var1[var4] = (byte) var3[var6];
        }

        if (var0 == 0) {
            --var4;
            var1[var4] = (byte) var3[0];
        } else if (var4 > 0 && var0 < 0) {
            --var4;
            var1[var4] = 45;
        }

        return var4;
    }

    /**
     * Establishes a window in computing coordiates. Remark: Unreasonable input values are replaced by values that are as reasonable as possible. E.g., if xacc == xbcc, then the method takes xbcc to be xacc + 1.
     *
     * @param xacc lower window edge in x
     * @param xbcc upper window edge in x
     * @param yacc lower window edge in y
     * @param ybcc upper window edge in y
     */
    public static void setWindowInComputingCoordinates(double xacc, double xbcc, double yacc, double ybcc) {
        qac[0] = xacc;
        qac[1] = yacc;
        qbc[0] = xbcc;
        qbc[1] = ybcc;
        if (qac[0] == qbc[0]) {
            qbc[0] = qac[0] + 1.0D;
        }

        if (qac[1] == qbc[1]) {
            qbc[1] = qac[1] + 1.0D;
        }

        setTransformations();
    }

    /**
     * Establishes a window in world coordiates. Remark: Unreasonable input values are replaced by values that are as reasonable as possible. E.g., if xapwc == xbpwc, then the method takes xbpwc to be xapwc + 1.
     *
     * @param xapwc lower window edge in x
     * @param xbpwc upper window edge in x
     * @param yapwc lower window edge in y
     * @param ybpwc upper window edge in y
     */
    public static void setWindowInWorldCoordinates(double xapwc, double xbpwc, double yapwc, double ybpwc) {
        if (xapwc > xbpwc) {
            qawp[0] = xbpwc;
            qbwp[0] = xapwc;
        } else {
            qawp[0] = xapwc;
            qbwp[0] = xbpwc;
        }

        if (yapwc > ybpwc) {
            qawp[1] = ybpwc;
            qbwp[1] = yapwc;
        } else {
            qawp[1] = yapwc;
            qbwp[1] = ybpwc;
        }

        if (qawp[0] == qbwp[0]) {
            qbwp[0] = qawp[0] + 1.0D;
        }

        if (qawp[1] == qbwp[1]) {
            qbwp[1] = qawp[1] + 1.0D;
        }

        setTransformations();
    }

    /**
     * Establishes a viewport in world coordiates. Remark: Unreasonable input values are replaced by values that are as reasonable as possible. E.g., if xawc == xbwc, then the method takes xbwc to be xawc + 1.
     *
     * @param xawc lower viewport edge in x
     * @param xbwc upper viewport edge in x
     * @param yawc lower viewport edge in y
     * @param ybwc upper viewport edge in y
     */
    public static void setViewportInWorldCoordinates(double xawc, double xbwc, double yawc, double ybwc) {
        qaw2[0] = xawc;
        qaw2[1] = yawc;
        qbw2[0] = xbwc;
        qbw2[1] = ybwc;
        if (qaw2[0] == qbw2[0]) {
            qbw2[0] = qaw2[0] + 1.0D;
        }

        if (qaw2[1] == qbw2[1]) {
            qbw2[1] = qaw2[1] + 1.0D;
        }

        setTransformations();
    }

    private static void setTransformations() {
        for (int var0 = 0; var0 < 2; ++var0) {
            qdc[var0] = qbc[var0] - qac[var0];
            qdci[var0] = 1.0D / qdc[var0];
            qdw2[var0] = qbw2[var0] - qaw2[var0];
            qdw2i[var0] = 1.0D / qdw2[var0];
            qdwp[var0] = qbwp[var0] - qawp[var0];
            qdwpi[var0] = 1.0D / qdwp[var0];
        }

        double var8 = qdwp[1] / qdwp[0];
        double var2 = qdd[1] / qdd[0];
        double var4;
        double var6;
        if (var2 > var8) {
            var4 = qdd[1] * var8 / var2;
            var6 = 0.5D * (qdd[1] - var4);
            qadp[1] = qad[1] + var6;
            qbdp[1] = qbd[1] - var6;
            qadp[0] = qad[0];
            qbdp[0] = qbd[0];
        } else {
            var4 = qdd[0] * var2 / var8;
            var6 = 0.5D * (qdd[0] - var4);
            qadp[0] = qad[0] + var6;
            qbdp[0] = qbd[0] - var6;
            qadp[1] = qad[1];
            qbdp[1] = qbd[1];
        }

        qddp[0] = qbdp[0] - qadp[0];
        qddp[1] = qbdp[1] - qadp[1];
        qddpi[0] = 1.0D / qddp[0];
        qddpi[1] = 1.0D / qddp[1];
    }

    private static void writeWindows() {
        System.out.println("c : " + qac[0] + " , " + qac[1] + " , " + qbc[0] + " , " + qbc[1] + " , " + qdc[0] + " , " + qdc[1] + " , " + qdci[0] + " , " + qdci[1]);
        System.out.println("w2 : " + qaw2[0] + " , " + qaw2[1] + " , " + qbw2[0] + " , " + qbw2[1] + " , " + qdw2[0] + " , " + qdw2[1] + " , " + qdw2i[0] + " , " + qdw2i[1]);
        System.out.println("wp : " + qawp[0] + " , " + qawp[1] + " , " + qbwp[0] + " , " + qbwp[1] + " , " + qdwp[0] + " , " + qdwp[1] + " , " + qdwpi[0] + " , " + qdwpi[1]);
        System.out.println("d : " + qad[0] + " , " + qad[1] + " , " + qbd[0] + " , " + qbd[1] + " , " + qdd[0] + " , " + qdd[1] + " , " + qddi[0] + " , " + qddi[1]);
        System.out.println("dp : " + qadp[0] + " , " + qadp[1] + " , " + qbdp[0] + " , " + qbdp[1] + " , " + qddp[0] + " , " + qddp[1] + " , " + qddpi[0] + " , " + qddpi[1]);
        System.out.println("clipping : " + qadcl[0] + " , " + qadcl[1] + " , " + qbdcl[0] + " , " + qbdcl[1]);
        System.out.println("form : " + qform[0] + " , " + qform[1]);
        System.out.println("framecoords : " + framecoords[0] + " , " + framecoords[1] + " , " + framecoords[2] + " , " + framecoords[3]);
    }

    /**
     * Chooses a color index, determining the color of all graphics elements created after this choice until it is changed.
     *
     * @param colorindex index in color table; has to be >=1, <=8 for method to be effective
     */
    public static void chooseColor(int colorindex) {
        flushPolyline();
        iqplcl = colorindex;
    }

    private static void move(double var0, double var2) {
        double var4 = transformSingleWorldToDevice(var0, 0);
        double var6 = transformSingleWorldToDevice(var2, 1);
        if (iqnplc > 1) {
            flushPolyline();
        }

        if (inClippingWindow(var4, var6)) {
            iqnplc = 1;
            qoutx[0] = var4;
            qouty[0] = var6;
        } else {
            iqnplc = 0;
        }

        lastPoint[0] = var4;
        lastPoint[1] = var6;
    }

    private static void draw(double var0, double var2) {
        double var5 = transformSingleWorldToDevice(var0, 0);
        double var7 = transformSingleWorldToDevice(var2, 1);
        boolean var9 = inClippingWindow(lastPoint[0], lastPoint[1]);
        boolean var10 = inClippingWindow(var5, var7);
        if (iqnplc >= iqnplm - 1) {
            flushPolyline();
            iqnplc = 1;
            qoutx[0] = lastPoint[0];
            qouty[0] = lastPoint[1];
        } else if (var9 && var10) {
            qoutx[iqnplc] = var5;
            qouty[iqnplc] = var7;
            ++iqnplc;
            lastPoint[0] = var5;
            lastPoint[1] = var7;
        } else {
            double[] var15;
            if (var9 && !var10) {
                var15 = borderPoint(lastPoint[0], lastPoint[1], var5, var7);
                qoutx[iqnplc] = var15[0];
                qouty[iqnplc] = var15[1];
                ++iqnplc;
                lastPoint[0] = var5;
                lastPoint[1] = var7;
            } else if (!var9 && var10) {
                var15 = borderPoint(var5, var7, lastPoint[0], lastPoint[1]);
                flushPolyline();
                iqnplc = 1;
                qoutx[0] = var15[0];
                qouty[0] = var15[1];
                qoutx[iqnplc] = var5;
                qouty[iqnplc] = var7;
                ++iqnplc;
                lastPoint[0] = var5;
                lastPoint[1] = var7;
            } else {
                if (lineCrossesClippingWindow(lastPoint[0], lastPoint[1], var5, var7, borderPoints)) {
                    flushPolyline();
                    iqnplc = 1;
                    qoutx[0] = borderPoints[0];
                    qouty[0] = borderPoints[1];
                    qoutx[iqnplc] = borderPoints[2];
                    qouty[iqnplc] = borderPoints[3];
                    ++iqnplc;
                }

                lastPoint[0] = var5;
                lastPoint[1] = var7;
            }
        }

    }

    private static boolean lineCrossesClippingWindow(double var0, double var2, double var4, double var6, double[] var8) {
        boolean var9 = false;
        if ((var0 >= qadcl[0] || var4 >= qadcl[0]) && (var0 <= qbdcl[0] || var4 <= qbdcl[0]) && (var2 >= qadcl[1] || var6 >= qadcl[1]) && (var2 <= qbdcl[1] || var6 <= qbdcl[1])) {
            if (qadcl[0] < var0 && var0 < qbdcl[0] && qadcl[0] < var4 && var4 < qbdcl[0]) {
                var8[1] = qadcl[1];
                var8[3] = qbdcl[1];
                var8[0] = var0 + (qadcl[1] - var2) * (var4 - var0) / (var6 - var2);
                var8[2] = var0 + (qbdcl[1] - var2) * (var4 - var0) / (var6 - var2);
                var9 = true;
            } else if (qadcl[1] < var2 && var2 < qbdcl[1] && qadcl[1] < var6 && var6 < qbdcl[1]) {
                var8[0] = qadcl[0];
                var8[2] = qbdcl[0];
                var8[1] = var2 + (qadcl[0] - var0) * (var6 - var2) / (var4 - var0);
                var8[3] = var2 + (qadcl[0] - var0) * (var6 - var2) / (var4 - var0);
                var9 = true;
            } else {
                int var10 = 0;
                var8[2 * var10] = var0 + (qadcl[1] - var2) * (var4 - var0) / (var6 - var2);
                var8[2 * var10 + 1] = qadcl[1];
                if (qadcl[0] <= var8[2 * var10] && var8[2 * var10] <= qbdcl[0]) {
                    ++var10;
                }

                var8[2 * var10] = var0 + (qbdcl[1] - var2) * (var4 - var0) / (var6 - var2);
                var8[2 * var10 + 1] = qbdcl[1];
                if (qadcl[0] <= var8[2 * var10] && var8[2 * var10] <= qbdcl[0]) {
                    ++var10;
                }

                if (var10 < 2) {
                    var8[2 * var10] = qadcl[0];
                    var8[2 * var10 + 1] = var2 + (qadcl[0] - var0) * (var6 - var2) / (var4 - var0);
                    if (qadcl[1] <= var8[2 * var10 + 1] && var8[2 * var10 + 1] <= qbdcl[1]) {
                        ++var10;
                    }
                }

                if (var10 < 2) {
                    var8[2 * var10] = qbdcl[0];
                    var8[2 * var10 + 1] = var2 + (qbdcl[0] - var0) * (var6 - var2) / (var4 - var0);
                    if (qadcl[1] <= var8[2 * var10 + 1] && var8[2 * var10 + 1] <= qbdcl[1]) {
                        ++var10;
                    }
                }

                if (var10 == 2) {
                    var9 = true;
                }
            }
        }

        return var9;
    }

    private static double[] borderPoint(double var0, double var2, double var4, double var6) {
        double[] var8 = new double[2];
        int var13 = regionOfExternalPoint(var4, var6);
        double var9;
        double var11;
        switch (var13) {
            case 1:
                var11 = qadcl[1];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                break;
            case 2:
                var11 = qbdcl[1];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                break;
            case 3:
                var9 = qadcl[0];
                var8[0] = var9;
                var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                break;
            case 4:
                var9 = qbdcl[0];
                var8[0] = var9;
                var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                break;
            case 5:
                var11 = qadcl[1];
                var9 = qadcl[0];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                if (var8[0] < var9) {
                    var8[0] = var9;
                    var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                }
                break;
            case 6:
                var11 = qbdcl[1];
                var9 = qadcl[0];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                if (var8[0] > var9) {
                    var8[0] = var9;
                    var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                }
                break;
            case 7:
                var11 = qadcl[1];
                var9 = qadcl[0];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                if (var8[0] < var9) {
                    var8[0] = var9;
                    var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                }
                break;
            case 8:
                var11 = qbdcl[1];
                var9 = qbdcl[0];
                var8[0] = var0 + (var11 - var2) * (var4 - var0) / (var6 - var2);
                var8[1] = var11;
                if (var8[0] > var9) {
                    var8[0] = var9;
                    var8[1] = var2 + (var9 - var0) * (var6 - var2) / (var4 - var0);
                }
        }

        return var8;
    }

    private static boolean inClippingWindow(double var0, double var2) {
        boolean var4 = false;
        if (qadcl[0] <= var0 && var0 <= qbdcl[0] && qadcl[1] <= var2 && var2 <= qbdcl[1]) {
            var4 = true;
        }

        return var4;
    }

    private static int regionOfExternalPoint(double var0, double var2) {
        byte var5;
        if (var0 < qadcl[0]) {
            if (var2 < qadcl[1]) {
                var5 = 5;
            } else if (var2 > qbdcl[1]) {
                var5 = 7;
            } else {
                var5 = 3;
            }
        } else if (var0 > qbdcl[0]) {
            if (var2 < qadcl[1]) {
                var5 = 6;
            } else if (var2 > qbdcl[1]) {
                var5 = 8;
            } else {
                var5 = 4;
            }
        } else if (var2 < qadcl[1]) {
            var5 = 1;
        } else if (var2 > qbdcl[1]) {
            var5 = 2;
        } else {
            var5 = 0;
        }

        return var5;
    }

    private static void drawLine(double[] var0, double[] var1, double var2) {
        double[] var10 = new double[2];
        double var6 = var1[0] - var0[0];
        double var8 = var1[1] - var0[1];
        double var4 = Math.sqrt(var6 * var6 + var8 * var8);
        int var12 = 0;
        if (var2 > 0.0D) {
            var12 = (int) (var4 / var2 + 0.5D);
        }

        int var13 = 2 * var12 + 1;
        var6 /= (double) var13;
        var8 /= (double) var13;
        int var11 = 1;
        var10[0] = var0[0];
        var10[1] = var0[1];
        move(var10[0], var10[1]);

        for (int var14 = 0; var14 < var13; ++var14) {
            var11 = -var11;
            var10[0] += var6;
            var10[1] += var8;
            if (var11 < 0) {
                draw(var10[0], var10[1]);
            } else {
                move(var10[0], var10[1]);
            }
        }

    }

    /**
     * Draws the contour line f(x,y) = c of a function f as a series of line segments in the x,y plane.
     *
     * @param x0    initial value
     * @param dx    interval width
     * @param nx    number of intervals for dividing the x axis
     * @param y0    same the y axis
     * @param dy    same the y axis
     * @param ny    same the y axis
     * @param fcont function value c of the contour line
     * @param cuf   user function computing f(x,y) which must be an extension of DatanUserFunction
     */

    public static void drawContour(double x0, double y0, double dx, double dy, int nx, int ny, double fcont, DatanUserFunction cuf) {
        double[] var33 = new double[500];
        setSmallClippingWindow();
        double var29 = 0.0D;
        double var31 = 0.0D;

        for (int var34 = 0; var34 < nx; ++var34) {
            double var13 = x0 + (double) var34 * dx;
            double var15 = var13 + dx;

            for (int var35 = 0; var35 < ny; ++var35) {
                double var17 = y0 + (double) var35 * dy;
                double var19 = var17 + dy;
                double var21;
                double var23;
                if (var35 == 0) {
                    var21 = cuf.getValue(var13, var17);
                    var23 = cuf.getValue(var15, var17);
                } else {
                    var21 = var29;
                    var23 = var31;
                }

                double var25;
                if (var34 != 0 && var35 < 500) {
                    var25 = var33[var35];
                } else {
                    var25 = cuf.getValue(var13, var19);
                }

                double var27 = cuf.getValue(var15, var19);
                if (var35 < 500) {
                    var33[var35] = var27;
                }

                var29 = var25;
                var31 = var27;
                drawContourForPixel(var13, var15, var17, var19, var21, var25, var23, var27, fcont);
            }
        }

        setBigClippingWindow();
    }

    private static void drawContourForPixel(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
        double[] var50 = new double[]{0.0D, 0.0D};
        double[] var51 = new double[]{0.0D, 0.0D};
        double var52 = 1.0E-27D;
        double var30 = 0.0D;
        double var32 = 0.0D;
        double var34 = 0.0D;
        double var36 = 0.0D;
        double var38 = 0.0D;
        double var40 = 0.0D;
        double var42 = 0.0D;
        double var44 = 0.0D;
        double var18 = var2 - var0;
        double var20 = var6 - var4;
        double var22 = var16 - var8;
        double var24 = var10 - var16;
        double var26 = var16 - var14;
        double var28 = var12 - var16;
        boolean var46;
        if (var22 * var28 < 0.0D) {
            var46 = false;
        } else {
            var46 = true;
            if (Math.abs(var22 + var28) < var52) {
                var30 = var0;
            } else {
                var30 = var0 + var18 * var22 / (var28 + var22);
            }

            var38 = var4;
        }

        boolean var47;
        if (var24 * var22 < 0.0D) {
            var47 = false;
        } else {
            var47 = true;
            var32 = var0;
            if (Math.abs(var22 + var24) < var52) {
                var40 = var4;
            } else {
                var40 = var4 + var20 * var22 / (var22 + var24);
            }
        }

        boolean var48;
        if (var26 * var24 < 0.0D) {
            var48 = false;
        } else {
            var48 = true;
            if (Math.abs(var26 + var24) < var52) {
                var34 = var0;
            } else {
                var34 = var0 + var18 * var24 / (var26 + var24);
            }

            var42 = var6;
        }

        boolean var49;
        if (var28 * var26 < 0.0D) {
            var49 = false;
        } else {
            var49 = true;
            var36 = var2;
            if (Math.abs(var28 + var26) < var52) {
                var44 = var4;
            } else {
                var44 = var4 + var20 * var28 / (var28 + var26);
            }
        }

        if (var46) {
            var50[0] = var30;
            var51[0] = var38;
            if (var47) {
                var50[1] = var32;
                var51[1] = var40;
                drawPolyline(var50, var51);
            }

            if (var48) {
                var50[1] = var34;
                var51[1] = var42;
                drawPolyline(var50, var51);
            }

            if (var49) {
                var50[1] = var36;
                var51[1] = var44;
                drawPolyline(var50, var51);
            }
        }

        if (var47) {
            var50[0] = var32;
            var51[0] = var40;
            if (var48) {
                var50[1] = var34;
                var51[1] = var42;
                drawPolyline(var50, var51);
            }

            if (var49) {
                var50[1] = var36;
                var51[1] = var44;
                drawPolyline(var50, var51);
            }
        }

        if (var48 && var49) {
            var50[0] = var34;
            var51[0] = var42;
            var50[1] = var36;
            var51[1] = var44;
            drawPolyline(var50, var51);
        }

    }

    /**
     * Draws a caption appearing just under the upper edge of the window in world coordinates.
     *
     * @param scalef scale factor; the typical length of a dash is 1% of the picture diagonal multiplied by this factor
     * @param string string containing the encoded text of the caption
     */
    public static void drawCaption(double scalef, String string) {
        double[] var3 = new double[]{0.0D, 0.0D};
        if (scalef <= 0.0D) {
            scalef = 1.0D;
        }

        double var4 = qbwp[0] - qawp[0];
        double var6 = qbwp[1] - qawp[1];
        scalef = 0.015D * scalef * Math.sqrt(var4 * var4 + var6 * var6);
        double var8 = qawp[0] + 0.5D * var4;
        double var10 = qawp[1] + 0.9D * var6;
        text(3, var8, var10, 1.0D, 0.0D, scalef, string, var3);
    }

    /**
     * Draws a text appearing at a desired position.
     *
     * @param x      computing coordinate left-hand corner
     * @param y      computing coordinate designating the lower left-hand corner of the rectangle surrounding the text
     * @param scalef scale factor; the typical length of a dash is 1% of the picture diagonal multiplied by this factor
     * @param string string containing the encoded text
     */
    public static void drawText(double x, double y, double scalef, String string) {
        double[] var7 = new double[]{0.0D, 0.0D};
        if (scalef <= 0.0D) {
            scalef = 1.0D;
        }

        scalef = 0.015D * scalef * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        double var8 = transformSingleComputingToWorld(x, 0);
        double var10 = transformSingleComputingToWorld(y, 1) + 0.5D * scalef;
        text(1, var8, var10, 1.0D, 0.0D, scalef, string, var7);
    }

    /**
     * Draws a histogram
     *
     * @param histogram the histogram to be drawn
     */
    public static void drawHistogram(Histogram histogram) {
        double[] var1 = new double[3];
        double[] var2 = new double[3];
        double[] var3 = new double[2];
        double[] var4 = new double[2];
        setSmallClippingWindow();
        double var5 = histogram.getLowerBoundary();
        double var7 = histogram.getBinSize();
        int var9 = histogram.getNumberOfBins();
        double[] var10 = histogram.getContents();

        for (int var11 = 0; var11 < var9; ++var11) {
            var1[0] = var5 + (double) var11 * var7;
            var1[1] = var1[0];
            var1[2] = var1[0] + var7;
            if (var11 == 0) {
                var2[0] = 0.0D;
            } else {
                var2[0] = var10[var11 - 1];
            }

            var2[1] = var10[var11];
            var2[2] = var2[1];
            drawPolyline(var1, var2);
        }

        var3[0] = var1[2];
        var4[0] = var2[2];
        var3[1] = var1[2];
        var4[1] = 0.0D;
        drawPolyline(var3, var4);
        setBigClippingWindow();
    }

    /**
     * Draws a data point with bars, and covariance ellipse
     *
     * @param nmark  form of polymarker symbolizing the data point (1: open circle, 2: closed circle, 3: open sqare, 4: closed sqare, 5: open diamond, 6: closed diamond, 7: +, 8: x. 9: *)
     * @param scalef scale factor; the diameter of the polymarker is 1% of the picture diagonal multiplied by this factor
     * @param x      x coordinate of data point
     * @param y      y coordinate of data point
     * @param deltax error in x; error bar is drawn only if deltax > 0 and if its siye would exceed the radius if the polymarker
     * @param deltay error in y; error bar is drawn only if deltax > 0 and if its siye would exceed the radius if the polymarker
     * @param rho    correlation between errors; covariance ellipse is not drawn if rho = 0 (drawing can be forced by giveng nmark a negative sign)
     */
    public static void drawDatapoint(int nmark, double scalef, double x, double y, double deltax, double deltay, double rho) {
        double[] var88 = new double[91];
        double[] var89 = new double[91];
        double var34 = scalef;
        if (scalef <= 0.0D) {
            var34 = 1.0D;
        }

        var34 = var34 * 0.01D * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        int var31 = Math.abs(nmark);
        boolean var40 = false;
        if (nmark < 0) {
            var40 = true;
        }

        if (var31 <= 1 || var31 > 9) {
            var31 = 1;
        }

        drawMark(var31, scalef, x, y);
        setSmallClippingWindow();
        double var32 = var34;
        if (var31 >= 7) {
            var32 = 0.0D;
        }

        if (var31 == 3 || var31 == 4) {
            var32 /= 1.414D;
        }

        double var57 = transformSingleComputingToWorld(x, 0);
        double var59 = transformSingleComputingToWorld(y, 1);
        double var82 = 0.0D;
        double var84 = 0.0D;
        double var78 = 0.0D;
        double var80 = 0.0D;
        boolean var38 = false;
        boolean var39 = false;
        if (deltax > 0.0D) {
            double var25 = var57 + var32;
            double var21 = var57 - var32;
            double var53 = x + deltax;
            var82 = transformSingleComputingToWorld(var53, 0);
            if (var82 > var25) {
                var38 = true;
            }

            if (var38) {
                move(var25, var59);
                draw(var82, var59);
                double var72 = var59 - var34;
                double var74 = var59 + var34;
                move(var82, var72);
                draw(var82, var74);
                double var49 = x - deltax;
                var78 = transformSingleComputingToWorld(var49, 0);
                move(var21, var59);
                draw(var78, var59);
                move(var78, var72);
                draw(var78, var74);
            }
        }

        if (deltay > 0.0D) {
            double var27 = var59 + var32;
            double var23 = var59 - var32;
            double var55 = y + deltay;
            var84 = transformSingleComputingToWorld(var55, 1);
            if (var84 > var27) {
                var39 = true;
            }

            if (var39) {
                move(var57, var27);
                draw(var57, var84);
                double var70 = var57 - var34;
                double var76 = var57 + var34;
                move(var70, var84);
                draw(var76, var84);
                double var51 = y - deltay;
                var80 = transformSingleComputingToWorld(var51, 1);
                move(var57, var23);
                draw(var57, var80);
                move(var70, var80);
                draw(var76, var80);
            }
        }

        if (deltax > 0.0D && deltay > 0.0D && Math.abs(rho) < 1.0D && var38 && var39) {
            double var68 = rho * deltax * deltay;
            if (var40 || Math.abs(rho) > 9.999999747378752E-5D) {
                double var15 = deltax * deltax;
                double var17 = deltay * deltay;
                double var86 = rho * rho;
                boolean var61 = false;
                if (Math.abs((deltax - deltay) / (deltax + deltay)) < 1.0E-4D) {
                    var61 = true;
                }

                double var36;
                if (var61) {
                    var36 = 0.7853981633974483D;
                } else {
                    var36 = 0.5D * Math.atan2(var68 * 2.0D, var15 - var17);
                }

                var34 = Math.sin(var36);
                double var29 = Math.cos(var36);
                double var47 = var34 * var34;
                double var41 = var29 * var29;
                double var13 = var15 * var17 * (1.0D - var86);
                double var19 = var17 * var41 - var68 * 2.0D * var34 * var29 + var15 * var47;
                double var43 = Math.sqrt(Math.abs(var13 / var19));
                var19 = var17 * var47 + var68 * 2.0D * var34 * var29 + var15 * var41;
                double var45 = Math.sqrt(Math.abs(var13 / var19));

                for (int var90 = 0; var90 < 91; ++var90) {
                    double var66 = (double) var90 * 0.06981D;
                    double var62 = var43 * Math.cos(var66);
                    double var64 = var45 * Math.sin(var66);
                    var88[var90] = x + var62 * var29 - var64 * var34;
                    var89[var90] = y + var62 * var34 + var64 * var29;
                }

                drawPolyline(var88, var89);
            }
        }

        setBigClippingWindow();
    }

    /**
     * Draws a polymarker
     *
     * @param nmark  form of polymarker (1: open circle, 2: closed circle, 3: open sqare, 4: closed sqare, 5: open diamond, 6: closed diamond, 7: +, 8: x. 9: *)
     * @param scalef scale factor; the diameter of the polymarker is 1% of the picture diagonal multiplied by this factor
     * @param xpm    x coordinate of polymarker
     * @param ypm    y coordinate of polymarker
     */
    public static void drawMark(int nmark, double scalef, double xpm, double ypm) {
        setSmallClippingWindow();
        double var12 = scalef;
        if (scalef <= 0.0D) {
            var12 = 1.0D;
        }

        var12 = var12 * 0.01D * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        int var9 = nmark;
        if (nmark < 1 || nmark > 9) {
            var9 = 1;
        }

        double var14 = transformSingleComputingToWorld(xpm, 0);
        double var16 = transformSingleComputingToWorld(ypm, 1);
        int var32;
        byte var7;
        double var24;
        double var26;
        double var28;
        double var30;
        if (var9 <= 6) {
            byte var8 = 1;
            if (var9 <= 2) {
                if (var9 == 2) {
                    var8 = 10;
                }

                var30 = 0.0D;
                var7 = 46;
            } else if (var9 != 3 && var9 != 4) {
                if (var9 == 6) {
                    var8 = 10;
                }

                var30 = 0.0D;
                var7 = 5;
            } else {
                if (var9 == 4) {
                    var8 = 10;
                }

                var30 = 0.785D;
                var7 = 5;
            }

            for (var32 = 1; var32 <= var8; ++var32) {
                double var22 = 6.283D / (double) (var7 - 1);
                double var10 = var12 * (1.0D - (double) (var32 - 1) / (double) var8);

                for (int var33 = 1; var33 <= var7; ++var33) {
                    var28 = var30 + (double) (var33 - 1) * var22;
                    var24 = var14 + var10 * Math.cos(var28);
                    var26 = var16 + var10 * Math.sin(var28);
                    if (var33 == 1) {
                        move(var24, var26);
                    } else {
                        draw(var24, var26);
                    }
                }
            }
        } else {
            if (var9 == 7) {
                var30 = 0.0D;
                var7 = 2;
            } else if (var9 == 8) {
                var30 = 0.785D;
                var7 = 2;
            } else {
                var30 = 0.0D;
                var7 = 4;
            }

            for (var32 = 1; var32 <= var7; ++var32) {
                var28 = var30 + (double) (var32 - 1) * 6.283D / ((double) var7 * 2.0D);
                double var18 = var12 * Math.cos(var28);
                double var20 = var12 * Math.sin(var28);
                var24 = var14 + var18;
                var26 = var16 + var20;
                move(var24, var26);
                var24 = var14 - var18;
                var26 = var16 - var20;
                draw(var24, var26);
            }
        }

        setBigClippingWindow();
    }

    /**
     * Draws a polyline.
     *
     * @param xpl x-coordinates of polyline
     * @param ypl y-coordinates of polyline
     */
    public static void drawPolyline(double[] xpl, double[] ypl) {
        setSmallClippingWindow();
        int var2 = xpl.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            double var4 = qaw2[0] + (xpl[var3] - qac[0]) * qdw2[0] * qdci[0];
            double var6 = qaw2[1] + (ypl[var3] - qac[1]) * qdw2[1] * qdci[1];
            if (var3 == 0) {
                move(var4, var6);
            } else {
                draw(var4, var6);
            }
        }

    }

    /**
     * Draws a broken polyline.
     *
     * @param ntype  line type (1: dashed, 2: dotted, 3 dot-dashed)
     * @param scalef scale factor; the typical length of a dash is 1% of the picture diagonal multiplied by this factor
     * @param xpl    x-coordinates of polyline
     * @param ypl    y-coordinates of polyline
     */
    public static void drawBrokenPolyline(int ntype, double scalef, double[] xpl, double[] ypl) {
        setSmallClippingWindow();
        double var32 = scalef;
        if (scalef < 0.0D) {
            var32 = 1.0D;
        }

        int var34 = ntype;
        if (ntype < 1 || ntype > 3) {
            var34 = 1;
        }

        var32 = 0.01D * var32 * Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1]));
        double var8;
        double var10;
        double var12;
        if (var34 == 1) {
            var8 = var32;
            var10 = var32;
            var12 = var32;
        } else if (var34 == 2) {
            var10 = 0.1D * var32;
            var8 = var10;
            var12 = var32;
        } else {
            var8 = var32;
            var10 = 0.1D * var32;
            var12 = 0.5D * var32;
        }

        double var20 = 0.0D;
        boolean var5 = true;
        double var14 = var8;
        var20 = 0.0D;
        double var24 = 0.0D;
        double var26 = 0.0D;
        boolean var7 = false;

        for (int var35 = 0; var35 < xpl.length; ++var35) {
            boolean var6 = true;

            while (var6) {
                double var28 = qaw2[0] + (xpl[var35] - qac[0]) * qdw2[0] * qdci[0];
                double var30 = qaw2[1] + (ypl[var35] - qac[1]) * qdw2[1] * qdci[1];
                if (var35 == 0) {
                    var24 = var28;
                    var26 = var30;
                    move(var28, var30);
                    var6 = false;
                } else {
                    double var16 = Math.sqrt((var28 - var24) * (var28 - var24) + (var30 - var26) * (var30 - var26));
                    double var22 = var20 + var16;
                    double var18 = var22 - var14;
                    if (var18 <= 0.0D) {
                        var20 += var16;
                        var6 = false;
                    } else {
                        var28 -= (var28 - var24) * var18 / var16;
                        var30 -= (var30 - var26) * var18 / var16;
                        var20 = 0.0D;
                        var6 = true;
                    }

                    if (var5) {
                        draw(var28, var30);
                    } else {
                        move(var28, var30);
                    }

                    var24 = var28;
                    var26 = var30;
                    if (var6) {
                        var5 = !var5;
                        if (var5) {
                            var7 = !var7;
                            if (var7) {
                                var14 = var10;
                            } else {
                                var14 = var8;
                            }
                        } else {
                            var14 = var12;
                        }
                    }
                }
            }
        }

        flushPolyline();
        setBigClippingWindow();
    }

    /**
     * Draws a frame around the computing coordinates window.
     */
    public static void drawBoundary() {
        double[] var0 = new double[5];
        double[] var1 = new double[5];
        var0[0] = qac[0];
        var1[0] = qac[1];
        var0[1] = qbc[0];
        var1[1] = var1[0];
        var0[2] = var0[1];
        var1[2] = qbc[1];
        var0[3] = var0[0];
        var1[3] = var1[2];
        var0[4] = var0[0];
        var1[4] = var1[0];
        drawPolylineInBigClippingWindow(var0, var1);
    }

    private static void drawPolylineInBigClippingWindow(double[] var0, double[] var1) {
        setBigClippingWindow();
        int var2 = var0.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            double var4 = qaw2[0] + (var0[var3] - qac[0]) * qdw2[0] * qdci[0];
            double var6 = qaw2[1] + (var1[var3] - qac[1]) * qdw2[1] * qdci[1];
            if (var3 == 0) {
                move(var4, var6);
            } else {
                draw(var4, var6);
            }
        }

    }

    private static void flushPolyline() {
        if (iqnplc > 0) {
            int var0;
            if (bufPolyLengthFilled == bufPolyLengthTotal) {
                int[] newBufPoly = new int[2 * bufPolyLengthTotal];

                for (var0 = 0; var0 < bufPolyLengthFilled; ++var0) {
                    newBufPoly[var0] = bufPoly[var0];
                }

                bufPoly = newBufPoly;
                bufPolyLengthTotal = 2 * bufPolyLengthTotal;
            }

            if (bufPointsLengthFilled + iqnplc + 2 > bufPointsLengthTotal) {
                short[] newBufPoints = new short[2 * bufPointsLengthTotal];

                for (var0 = 0; var0 < bufPointsLengthFilled; ++var0) {
                    newBufPoints[var0] = bufPoints[var0];
                }

                bufPoints = newBufPoints;
                bufPointsLengthTotal = 2 * bufPointsLengthTotal;
            }

            var0 = bufPointsLengthFilled;
            bufPoly[numpolys] = var0;
            ++numpolys;
            bufPoints[var0] = (short) iqplcl;
            bufPoints[var0 + 1] = (short) iqnplc;
            var0 += 2;

            for (int var1 = 0; var1 < iqnplc; ++var1) {
                bufPoints[var0] = (short) ((int) qoutx[var1]);
                bufPoints[var0 + 1] = (short) ((int) qouty[var1]);
                var0 += 2;
            }

            bufPolyLengthFilled = numpolys;
            bufPointsLengthFilled = var0;
            iqnplc = 0;
        }

    }

    private static void drawArrow(double[] var0, int var1, String var2, double var3) {
        double[] var15 = new double[2];
        double[] var16 = new double[4];
        double[] var17 = new double[]{0.0D, 0.0D};
        int var7 = Math.abs(var1);
        int var6;
        if (var7 == 1) {
            var6 = (3 + var1) / 2;
        } else {
            var6 = 2;
        }

        boolean var5 = false;

        int var18;
        for (var18 = 0; var18 < var2.length(); ++var18) {
            var5 = var5 || var2.charAt(var18) != ' ';
        }

        if (var5) {
            text(var6, var0[0], var0[1], 1.0D, 0.0D, var3, var2, var17);
            double var13;
            if (var1 < 1) {
                var13 = -1.0D;
            } else {
                var13 = 1.0D;
            }

            int var8 = 3 - var7;
            double var11 = 0.8D * var3;
            double var9;
            if (var7 == 1) {
                var15[1] = var0[1];
                var9 = Math.max((Math.abs(qbw2[0] - qaw2[0]) - Math.abs(var17[0] - var0[0])) * 0.25D, 0.0D);
                var15[0] = var17[0] - var13 * (var9 + var11);
            } else {
                var15[0] = var0[0] - Math.abs(var0[0] - var17[0]) * 0.5D;
                var9 = Math.max((Math.abs(qbw2[1] - qaw2[1]) - var3) * 0.25D, 0.0D);
                var15[1] = var17[1] - var13 * (var11 + var9 + var3 * 0.5D);
            }

            if (var9 <= 0.0D) {
                var11 = 0.0D;
            }

            if (var9 > 0.0D) {
                if (var11 > var9) {
                    var9 = var11;
                }

                for (var18 = 1; var18 <= 5; ++var18) {
                    var16[0] = var15[0];
                    var16[1] = var15[1];
                    if (var18 == 2 || var18 == 3 || var18 == 5) {
                        var16[var7 - 1] = var15[var7 - 1] + var13 * (var9 - var11);
                    }

                    if (var18 == 4) {
                        var16[var7 - 1] = var15[var7 - 1] + var13 * var9;
                    }

                    if (var18 == 3) {
                        var16[var8 - 1] = var15[var8 - 1] + var11 * 0.4D;
                    }

                    if (var18 == 5) {
                        var16[var8 - 1] = var15[var8 - 1] - var11 * 0.4D;
                    }

                    if (var18 == 1) {
                        move(var16[0], var16[1]);
                    } else {
                        draw(var16[0], var16[1]);
                    }

                    if (var18 == 2) {
                        var16[2] = var16[0];
                        var16[3] = var16[1];
                    }
                }

                draw(var16[2], var16[3]);
            }
        }

    }

    /**
     * Draws the axes of the computing-coordinate system as dashed lines within the window of computing coordiates.
     */
    public static void drawCoordinateCross() {
        setSmallClippingWindow();
        double[] var0 = new double[2];
        double[] var1 = new double[2];
        var0[0] = qac[0];
        var0[1] = qbc[0];
        var1[0] = 0.0D;
        var1[1] = 0.0D;
        if (qac[1] * qbc[1] < 0.0D) {
            drawBrokenPolyline(1, 1.0D, var0, var1);
        }

        var0[0] = 0.0D;
        var0[1] = 0.0D;
        var1[0] = qac[1];
        var1[1] = qbc[1];
        if (qac[0] * qbc[0] < 0.0D) {
            drawBrokenPolyline(1, 1.0D, var0, var1);
        }

        setBigClippingWindow();
    }

    /**
     * Draws a frame around the window in the world coordinate system.
     */
    public static void drawFrame() {
        move(qawp[0], qawp[1]);
        draw(qbwp[0], qawp[1]);
        draw(qbwp[0], qbwp[1]);
        draw(qawp[0], qbwp[1]);
        draw(qawp[0], qawp[1]);
    }

    /**
     * Determines the Format of the plot. Remarks: If plot too large for size of paper or screen frame, it is reduced in size to fit.
     *
     * @param width  width of plot in centimetres; if height=0, then plot has landscape A format with width being A number, e.g. (widhth=5, height=0) stands for landscape A5
     * @param height height of plot in centimetres; if = width=0, then plot has portrait A format with height being A number, e.g. (widhth=0, height=5) stands for portrait A5. If both width=0 and height=0, then the format is set to landscape A5.
     */
    public static void setFormat(double width, double height) {
        if (width == 0.0D && height == 0.0D) {
            width = 5.0D;
        }

        int idin;
        if (height == 0.0D) {
            idin = (int) width;
            if (idin < 1) {
                idin = 1;
            }

            if (idin > 10) {
                idin = 10;
            }

            qform[0] = cdin / Math.pow(root2, (double) idin);
            qform[1] = qform[0] / root2;
        } else if (width == 0.0D) {
            idin = (int) height;
            if (idin < 1) {
                idin = 1;
            }

            if (idin > 10) {
                idin = 10;
            }

            qform[1] = cdin / Math.pow(root2, (double) idin);
            qform[0] = qform[1] / root2;
        } else {
            qform[0] = width;
            qform[1] = height;
        }

        double rformt = qform[0] / qform[1];
        if (rformt > 1.0D) {
            qad[0] = 0.0D;
            qbd[0] = 32767.0D;
            qad[1] = 0.5D * (1.0D - 1.0D / rformt) * 32767.0D;
            qbd[1] = 0.5D * (1.0D + 1.0D / rformt) * 32767.0D;
        } else {
            qad[1] = 0.0D;
            qbd[1] = 32767.0D;
            qad[0] = 0.5D * (1.0D - rformt) * 32767.0D;
            qbd[0] = 0.5D * (1.0D + rformt) * 32767.0D;
        }

        for (int var4 = 0; var4 < 2; ++var4) {
            qdd[var4] = qbd[var4] - qad[var4];
            qddi[var4] = 1.0D / qdd[var4];
            qadp[var4] = qad[var4];
            qbdp[var4] = qbd[var4];
            qddp[var4] = qdd[var4];
            qddpi[var4] = qddi[var4];
        }

        setTransformations();
    }

    private static void writePolylineBuffer() {
        if (numpolys > 0) {
            int var0 = 0;

            for (int var1 = 0; var1 < numpolys; ++var1) {
                short var2 = bufPoints[var0 + 1];
                var0 += 2;

                for (int var3 = 0; var3 < var2; ++var3) {
                    var0 += 2;
                }
            }
        }

    }

    /**
     * Sets the clipping window to be the window of the computing coordinates.
     */
    public static void setSmallClippingWindow() {
        flushPolyline();
        qadcl[0] = transformSingleWorldToDevice(qaw2[0], 0);
        qadcl[1] = transformSingleWorldToDevice(qaw2[1], 1);
        qbdcl[0] = transformSingleWorldToDevice(qbw2[0], 0);
        qbdcl[1] = transformSingleWorldToDevice(qbw2[1], 1);
    }

    /**
     * Sets the clipping window to be the frame of the entire plot.
     */
    public static void setBigClippingWindow() {
        flushPolyline();
        qadcl[0] = qadp[0];
        qadcl[1] = qadp[1];
        qbdcl[0] = qbdp[0];
        qbdcl[1] = qbdp[1];
    }

    private void closeFrame(JFrame var1) {
        var1.dispose();
    }

    private static void text(int var0, double var1, double var3, double var5, double var7, double var9, String var11, double[] var12) {
        rv[0] = var5;
        rv[1] = var7;
        gv[0] = -var7;
        gv[1] = var5;
        double gvabs2 = gv[0] * gv[0] + gv[1] * gv[1];
        double gvabs = Math.sqrt(gv[0] * gv[0] + gv[1] * gv[1]);
        double rvabs2 = 0.0D;
        rvabs2 = rv[0] * rv[0] + rv[1] * rv[1];
        double rvabs = Math.sqrt(rv[0] * rv[0] + rv[1] * rv[1]);
        avin[0] = var1;
        avin[1] = var3;

        int var13;
        for (var13 = 0; var13 < 2; ++var13) {
            rve[var13] = rv[var13] / rvabs;
            gve[var13] = gv[var13] / gvabs;
        }

        gr = var9;
        c = var11.getBytes();
        grlett();

        for (var13 = 0; var13 < 2; ++var13) {
            if (var0 == 1) {
                av[var13] = avin[var13] - gr * 0.5D * gve[var13];
                op[var13] = avin[var13] + alengt * rve[var13];
            } else if (var0 == 2) {
                av[var13] = avin[var13] - gr * 0.5D * gve[var13] - alengt * rve[var13];
                op[var13] = avin[var13] - alengt * rve[var13];
            } else if (var0 == 3) {
                av[var13] = avin[var13] - alengt * 0.5D * rve[var13];
                op[var13] = avin[var13] + gr * 0.5D * gve[var13];
            } else {
                av[var13] = avin[var13] - gr * gve[var13] - alengt * 0.5D * rve[var13];
                op[var13] = avin[var13] - gr * 0.5D * gve[var13];
            }
        }

        var12[0] = op[0];
        var12[1] = op[1];

        for (var13 = 0; var13 < nuj; ++var13) {
            int nu = (int) ((double) j[var13] / 1000.0D + 0.05D);
            if (nu >= 0) {
                ga = gr;
            }

            if (nu == 1 || nu == 4) {
                ga = gr * fg1;
            }

            if (nu == 2 || nu == 3 || nu == 5 || nu == 6) {
                ga = gr * fg1 * fg2;
            }

            if (idop[var13 + 1] == 1) {
                g1 = ga;
            }

            if (nu >= 0) {
                gz = 0.0D;
            }

            if (nu >= 1) {
                gz = gr - gr * fg1 / 2.0D;
            }

            if (nu == 4) {
                gz = -(gr * fg1) / 2.0D;
            }

            if (nu == 2) {
                gz = gr + (gr * fg1 - gr * fg1 * fg2) / 2.0D;
            }

            if (nu == 3) {
                gz = gr - (gr * fg1 + gr * fg1 * fg2) / 2.0D;
            }

            if (nu == 5) {
                gz = (gr * fg1 - gr * fg1 * fg2) / 2.0D;
            }

            if (nu == 6) {
                gz = -(gr * fg1 + gr * fg1 * fg2) / 2.0D;
            }

            int var14;
            for (var14 = 0; var14 < 2; ++var14) {
                avnext[var14] = av[var14] + gz * gve[var14];
            }

            id = j[var13] - nu * 1000;
            grsymb();
            if (idop[var13 + 1] != 1) {
                if (idop[var13] == 1) {
                    ga = g1;
                }

                for (var14 = 0; var14 < 2; ++var14) {
                    double[] var10000 = av;
                    var10000[var14] += ga * rve[var14] * ((double) iga / utx + z / utx - (double) miga / utx);
                }
            }
        }

    }

    private static void grlett() {
        int var1 = c.length;
        nuj = 0;
        int na1 = 1;
        int na5 = 0;

        int no;
        for (no = 0; no < var1; ++no) {
            idop[no] = 0;
            j[no] = 0;
        }

        alengt = 0.0D;

        int nur;
        for (nur = 0; nur < var1; ++nur) {
            int iascii = c[nur];
            int na2 = 0;

            int nul;
            for (nul = 0; nul < 3; ++nul) {
                if (iascii == k[nul]) {
                    na2 = nul + 1;
                }
            }

            boolean var0;
            if (na2 != 0) {
                na1 = na2;
                var0 = false;
            } else {
                var0 = true;
            }

            if (var0) {
                int na4 = 0;

                for (nul = 0; nul < 3; ++nul) {
                    if (iascii == kk[nul]) {
                        na4 = nul + 1;
                    }
                }

                if (na4 != 0) {
                    if (na4 == 2 && na5 == 0) {
                        na5 = 2;
                    }

                    na5 += na4;
                    if (na4 == 3) {
                        na5 = 0;
                    }

                    if (na5 > 6) {
                        na5 = 0;
                    }

                    var0 = false;
                } else {
                    var0 = true;
                }
            }

            if (var0) {
                if (iascii == kkk) {
                    idop[nuj] = 1;
                    var0 = false;
                } else {
                    var0 = true;
                }
            }

            if (var0) {
                int na3 = 63;

                for (nul = 0; nul < 94; ++nul) {
                    if (iascii == m[nul]) {
                        na3 = nul + 1;
                    }
                }

                ++nuj;
                if (na1 == 1) {
                    j[nuj - 1] = mm[na3 - 1];
                }

                if (na1 == 2) {
                    j[nuj - 1] = ll[na3 - 1];
                }

                if (na1 == 3) {
                    j[nuj - 1] = lll[na3 - 1];
                }

                id = j[nuj - 1];
                int[] var10000 = j;
                int var10001 = nuj - 1;
                var10000[var10001] += na5 * 1000;
                var0 = idop[nuj - 1] != 1;
            }

            if (var0) {
                grleng();
                if (na5 == 0) {
                    alengt += gr * ((double) iga / utx + z / utx - (double) miga / utx);
                } else if (na5 != 1 && na5 != 4) {
                    if (na5 == 2 || na5 == 3 || na5 >= 5) {
                        alengt += gr * fg1 * fg2 * ((double) iga / utx + z / utx - (double) miga / utx);
                    }
                } else {
                    alengt += gr * fg1 * ((double) iga / utx + z / utx - (double) miga / utx);
                }
            }
        }

    }

    private static void grsymb() {
        move(avnext[0], avnext[1]);
        grleng();

        for (int var0 = 0; var0 < ko; ++var0) {
            int var1 = (int) ((double) Math.abs(idat[var0]) / 100.0D + 0.1D - (double) miga);
            int var2 = (int) ((double) Math.abs(idat[var0]) - (double) (var1 + miga) * 100.0D);

            for (int var3 = 0; var3 < 2; ++var3) {
                wert2d[var3] = avnext[var3] + (double) var1 / utx * rve[var3] * ga;
                wert2d[var3] = wert2d[var3] + (double) var2 / uty * gve[var3] * ga - gve[var3] * 10.0D * ga / uty;
            }

            if (idat[var0] < 0) {
                move(wert2d[0], wert2d[1]);
            } else {
                draw(wert2d[0], wert2d[1]);
            }
        }

    }

    private static void grleng() {
        int var0;
        for (var0 = 0; var0 < 25; ++var0) {
            idat[var0] = iqlett[var0 + (id - 1) * 25];
        }

        miga = 0;
        ko = 0;

        for (var0 = 0; var0 < 25 && idat[var0] != -999; ++var0) {
            ++ko;
        }

        if (ko != 0) {
            int iga1 = Math.abs(idat[0]);
            int miga1 = Math.abs(idat[0]);

            int ih;
            for (ih = 0; ih < ko; ++ih) {
                if (Math.abs(idat[ih]) > iga1) {
                    iga1 = Math.abs(idat[ih]);
                }

                if (Math.abs(idat[ih]) < miga1) {
                    miga1 = Math.abs(idat[ih]);
                }
            }

            iga = (int) ((double) iga1 / 100.0D);
            miga = (int) ((double) miga1 / 100.0D);
        } else {
            iga = 19;
        }

    }

    private static double[] transformWorldToComputing(double[] var0) {
        double[] var1 = new double[2];

        for (int var2 = 0; var2 < 2; ++var2) {
            var1[var2] = qac[var2] + (var0[var2] - qaw2[var2]) * qdc[var2] * qdw2i[var2];
        }

        return var1;
    }

    private static double[] transformComputingToWorld(double[] var0) {
        double[] var1 = new double[2];

        for (int var2 = 0; var2 < 2; ++var2) {
            var1[var2] = qaw2[var2] + (var0[var2] - qac[var2]) * qdw2[var2] * qdci[var2];
        }

        return var1;
    }

    private static double transformSingleWorldToComputing(double var0, int var2) {
        return qac[var2] + (var0 - qaw2[var2]) * qdc[var2] * qdw2i[var2];
    }

    private static double transformSingleComputingToWorld(double var0, int var2) {
        return qaw2[var2] + (var0 - qac[var2]) * qdw2[var2] * qdci[var2];
    }

    private static double transformSingleWorldToDevice(double var0, int var2) {
        return qadp[var2] + (var0 - qawp[var2]) * qddp[var2] * qdwpi[var2];
    }

    private static void ladder(double[] var0, double[] var1, int var2, int var3, int var4, double var5, double var7, int var9, double var12, double var14, boolean var16) {
        double[] var63;
        double[] var64;
        double[] var65 = new double[2];
        double[] var66 = new double[2];
        double[] var67 = new double[2];
        double[] var68 = new double[2];
        boolean[] var77 = new boolean[1];
        double[] var78 = new double[1];
        int[] var79 = new int[3];
        NumberFormat var80 = NumberFormat.getNumberInstance(Locale.US);
        var80.setGroupingUsed(false);
        var80.setMaximumIntegerDigits(9);
        NumberFormat var81 = NumberFormat.getNumberInstance(Locale.US);
        var81.setGroupingUsed(false);
        var80.setMaximumIntegerDigits(9);
        var81.setMaximumIntegerDigits(9);
        var81.setMaximumFractionDigits(5);
        NumberFormat var82 = NumberFormat.getNumberInstance(Locale.US);
        if (var82 instanceof DecimalFormat) {
            ((DecimalFormat) var82).applyPattern("0.#####E0");
        }

        int var37 = var9;
        double var42 = var7;
        xmovtx = 0.0D;
        if (var9 <= 0) {
            var37 = 1;
        }

        var63 = transformComputingToWorld(var0);
        var64 = transformComputingToWorld(var1);
        int var21;
        if (var2 == 1 && Math.abs(var4) == 2) {
            var21 = (14 - var4) / 4;
            var65[0] = 1.0D;
            var65[1] = 0.0D;
        } else {
            var21 = (3 - var4) / 2;
            var65[0] = 1.0D;
            var65[1] = 0.0D;
        }

        for (int var24 = 0; var24 < 2; ++var24) {
            var67[var24] = var63[var24];
            var68[var24] = var64[var24];
        }

        double var48 = (Math.abs(var0[var2 - 1]) + Math.abs(var1[var2 - 1])) * 0.5D;
        int var38;
        if (var48 > 0.0D) {
            var38 = 5 - (int) (Math.log(var48) / Math.log(10.0D) + 0.5D);
        } else {
            var38 = 5;
        }

        boolean var19;
        if (var38 >= 0 && var38 <= 9) {
            var80.setMaximumFractionDigits(var38);
            var19 = true;
        } else {
            var19 = false;
            var80.setMaximumFractionDigits(0);
        }

        double var54 = Math.abs(var1[var2 - 1] - var0[var2 - 1]);
        int var39;
        if (var54 != 0.0D) {
            var39 = (int) Math.rint(Math.log(var54) / Math.log(10.0D) - 0.5D);
        } else {
            var39 = 0;
        }

        double var50 = Math.pow(10.0D, (double) var39);
        int var23 = 0;
        if (var7 > 0.0D) {
            var23 = (int) (var54 / var7) + 1;
        }

        String var71;
        if (var7 <= 0.0D || var23 > 50) {
            double var52 = var54 / var50;
            if (var52 <= 1.5D) {
                var42 = var50 * 0.2D;
            } else if (var52 <= 3.5D) {
                var42 = var50 * 0.5D;
            } else if (var52 <= 7.5D) {
                var42 = var50;
            } else {
                var42 = var50 * 2.0D;
            }

            double var46 = Math.abs(transformSingleWorldToComputing(var14, var2 - 1) - transformSingleWorldToComputing(0.0D, var2 - 1));
            int var20;
            if (var21 != 1 && var21 != 2) {
                double var83 = Math.max(Math.abs(var0[var2 - 1]), Math.abs(var1[var2 - 1]));
                int var34 = (int) (Math.log(var83) / Math.log(10.0D));
                int var22;
                int var33;
                double var44;
                if (var16 && var48 > 0.05D) {
                    var33 = Math.max(var34, 0) + 3;
                    var44 = Math.abs(var5 - Math.rint(var5));
                    var22 = Math.max(-((int) (Math.log(var42) / Math.log(10.0D))), 0);
                    var71 = var80.format(var44);
                } else {
                    var33 = 7;
                    if (Math.abs(var34) >= 9) {
                        ++var33;
                    }

                    var44 = Math.pow(10.0D, (double) var34);
                    if (var42 <= var44 * 0.99999D) {
                        ++var33;
                    }

                    var44 = Math.abs(Math.rint(var5 / var44) - var5 / var44);
                    var71 = var81.format(var44);
                    var22 = 0;
                }

                int var85 = var71.length();
                var22 = Math.max(var85, var22);
                var33 += var22;
                var20 = (int) (var46 * 0.8D * (double) var33 / var42) + 1;
            } else {
                var20 = (int) (var46 * 1.4D / var42) + 1;
            }

            var37 = var20;
            var42 *= (double) var20;
        }

        if (var12 > 0.0D || var14 > 0.0D) {
            byte var87 = 0;

            while (true) {
                do {
                    boolean var17;
                    int var32;
                    int var35;
                    int var36;
                    double var56;
                    double var58;
                    double var60;
                    String var70;
                    do {
                        do {
                            if (!scale(var0[var2 - 1], var1[var2 - 1], var5, var42, var37, var77, var78, var79) || var87 >= 10) {
                                return;
                            }

                            var17 = var77[0];
                            var58 = var78[0];
                            var32 = var79[0];
                            var36 = var79[1];
                            var35 = var79[2];
                            var60 = Math.abs(var58);
                            if (var32 == 1) {
                                var56 = var12;
                            } else {
                                var56 = var12 * 0.5D;
                            }

                            var67[var2 - 1] = transformSingleComputingToWorld(var58, var2 - 1);
                            var68[var2 - 1] = var67[var2 - 1];
                            if (var12 > 0.0D) {
                                var68[Math.abs(var3) - 1] = var67[Math.abs(var3) - 1] + var56 * (double) (var3 / Math.abs(var3));
                                drawLine(var67, var68, 0.0D);
                            }

                            var68[Math.abs(var3) - 1] = var67[Math.abs(var3) - 1];
                        } while (var14 <= 0.0D);
                    } while (var32 != 1);

                    var68[Math.abs(var4) - 1] = var67[Math.abs(var4) - 1] + var56 * 2.0D * (double) (var4 / Math.abs(var4));
                    if (var16 && var19) {
                        if (var17 && (var48 > 0.05000000074505806D || var60 < var48 * 9.999999747378752E-6D) && var60 <= 500.0D) {
                            if (var35 == 1) {
                                if (var36 == 0) {
                                    var70 = "0";
                                } else {
                                    var71 = var81.format((long) var36);
                                    var70 = var71 + "&p";
                                    if (var36 == 1) {
                                        var70 = "&p";
                                    } else if (var36 == -1) {
                                        var70 = "-&p";
                                    }
                                }
                            } else {
                                var71 = var81.format((long) var36);
                                var70 = var71 + "&p";
                                var71 = var81.format((long) var35);
                                var70 = var70 + "@/" + var71;
                                if (var36 == 1) {
                                    var70 = "&p@/" + var71;
                                } else if (var36 == -1) {
                                    var70 = "-&p@/" + var71;
                                }
                            }
                        } else if (Math.abs(var58 - Math.rint(var58)) < Math.max(Math.abs(var1[var2 - 1] - var0[var2 - 1]) * 1.0E-5D, 1.0E-4D) && var48 > 0.05D) {
                            var71 = var81.format((long) ((int) Math.rint(var58)));
                            var70 = var71;
                        } else {
                            var71 = var80.format(var58);
                            var70 = var71;
                        }
                    } else {
                        if (var60 <= var48 * 9.999999747378752E-6D) {
                            var58 = 0.0D;
                        }

                        var71 = var82.format(var58);
                        new StringBuffer(var71.length());
                        StringBuffer var75 = new StringBuffer(var71.length());
                        StringBuffer var76 = new StringBuffer(var71.length());

                        int var84;
                        for (var84 = 0; var84 < var71.length() && var71.charAt(var84) != 'E'; ++var84) {
                            var75.append(var71.charAt(var84));
                        }

                        String var72 = var75.toString();

                        for (var84 = var71.indexOf(69) + 1; var84 < var71.length(); ++var84) {
                            var76.append(var71.charAt(var84));
                        }

                        String var73 = var76.toString();
                        if (var73.charAt(0) == '0') {
                            var70 = var72;
                        } else {
                            var70 = "%" + var72 + "*10^" + var73;
                        }
                    }

                    text(var21, var68[0], var68[1], var65[0], var65[1], var14, var70, var66);
                } while (var21 != 1 && var21 != 2);

                xmovtx = Math.max(Math.abs(var66[0] - var64[0]), xmovtx);
                var68[Math.abs(var4) - 1] = var67[Math.abs(var4) - 1];
            }
        }
    }

    private static boolean scale(double var0, double var2, double var4, double var6, int var8, boolean[] var9, double[] var10, int[] var11) {
        boolean var19 = false;
        if (!rnt) {
            nc = 0;
            an = Math.min(var0, var2);
            en = Math.max(var0, var2);
            int var15 = (int) Math.rint((var4 - an) / var6) + 1;
            atims = var4 - (double) var15 * var6;
            atdel = var6 / (double) var8;
            eps = (en - an) * 0.001D;
            aneps = an - eps;
            eneps = en + eps;
            eta = Math.max((en - an) * 1.0E-5D, 1.0E-4D);
        }

        byte var18 = 0;
        int var17 = 0;

        double var12;
        byte var16;
        boolean var20;
        do {
            var20 = false;
            rnt = atims < eneps;
            var12 = atims;
            if (atims > aneps && rnt) {
                var19 = true;
            }

            if (nc != 0 && nc != var8) {
                var16 = 0;
            } else {
                var16 = 1;
                nc = 0;
                if (Math.abs(var12) < 1000000.0D) {
                    int var14 = (int) Math.rint(var12 / piqurt);
                    if (Math.abs(var12 - (double) var14 * piqurt) <= eta) {
                        var20 = true;
                        if (var14 - 4 * (var14 / 4) == 0) {
                            var17 = var14 / 4;
                            var18 = 1;
                        } else if (var14 - 2 * (var14 / 2) == 0) {
                            var17 = var14 / 2;
                            var18 = 2;
                        } else {
                            var17 = var14;
                            var18 = 4;
                        }
                    }
                }
            }

            atims += atdel;
            ++nc;
        } while (rnt && !var19);

        if (!var19) {
            nc = 0;
        }

        var9[0] = var20;
        var10[0] = var12;
        var11[0] = var16;
        var11[1] = var17;
        var11[2] = var18;
        return var19;
    }

    /**
     * Draws an axis in x-direction. Remark: Tick marks from the upper and lower edges of the computing-coordinate window are drawn inside the window. Below the lower edge, numbers are displayed which label some of these marks. It is recommended to first call the method drawBoundary() to display lines on the window edges. The ticks and numbers are created automatically based on the choice of world and computing coordinates; they can be determined explicitely by the user by calling setParametersForScale(...) before calling this method.
     *
     * @param string encoded text appearing at arrow tip accompanying the scale
     */
    public static void drawScaleX(String string) {
        double[] var7 = new double[2];
        double[] var8 = new double[2];
        double[] var9 = new double[2];
        var7[0] = qac[0];
        var7[1] = qac[1];
        var8[0] = qbc[0];
        var8[1] = qac[1];
        double var4 = Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1])) * 0.014999999664723873D;
        double var2;
        if (lscset) {
            var2 = grdef * var4;
            var4 = ticdef * var4;
            ladder(var7, var8, 1, 2, -2, fixdef, deldef, ntcdef, var4, var2, numdef);
        } else {
            var2 = var4;
            ladder(var7, var8, 1, 2, -2, 0.0D, 0.0D, 0, var4, var4, true);
        }

        byte var1;
        if (qbc[0] - qac[0] >= 0.0D) {
            var1 = 1;
        } else {
            var1 = -1;
        }

        var9[0] = Math.max(qaw2[0], qbw2[0]);
        var9[1] = Math.min(qaw2[1], qbw2[1]) - 2.0D * var4 - 3.0D * var2;
        drawArrow(var9, var1, string, var2);
        var7[1] = qbc[1];
        var8[1] = qbc[1];
        if (lscset) {
            ladder(var7, var8, 1, -2, -2, fixdef, deldef, ntcdef, var4, 0.0D, numdef);
        } else {
            ladder(var7, var8, 1, -2, -2, 0.0D, 0.0D, 0, var4, 0.0D, true);
        }

        lscset = false;
    }

    /**
     * Draws an axis in y-direction. Remark: Tick marks from the left and right edges of the computing-coordinate window are drawn inside the window. To the left of the left edge, numbers are displayed which label some of these marks. It is recommended to first call the method drawBoundary() to display lines on the window edges. The ticks and numbers are created automatically based on the choice of world and computing coordinates; they can be determined explicitely by the user by calling setParametersForScale(...) before calling this method.
     *
     * @param string encoded text appearing at arrow tip accompanying the scale
     */
    public static void drawScaleY(String string) {
        double[] var7 = new double[2];
        double[] var8 = new double[2];
        double[] var9 = new double[2];
        var7[0] = qac[0];
        var7[1] = qac[1];
        var8[0] = qac[0];
        var8[1] = qbc[1];
        double var4 = Math.sqrt((qbwp[0] - qawp[0]) * (qbwp[0] - qawp[0]) + (qbwp[1] - qawp[1]) * (qbwp[1] - qawp[1])) * 0.014999999664723873D;
        double var2;
        if (lscset) {
            var2 = grdef * var4;
            var4 = ticdef * var4;
            ladder(var7, var8, 2, 1, -1, fixdef, deldef, ntcdef, var4, var2, numdef);
        } else {
            var2 = var4;
            ladder(var7, var8, 2, 1, -1, 0.0D, 0.0D, 0, var4, var4, true);
        }

        byte var1;
        if (qbc[1] - qac[1] >= 0.0D) {
            var1 = 2;
        } else {
            var1 = -2;
        }

        var9[0] = Math.min(qaw2[0], qbw2[0]) - 2.0D * var2 - xmovtx;
        var9[1] = Math.max(qaw2[1], qbw2[1]) - 0.5D * var2;
        drawArrow(var9, var1, string, var2);
        var7[0] = qbc[0];
        var8[0] = qbc[0];
        if (lscset) {
            ladder(var7, var8, 2, -1, -1, fixdef, deldef, ntcdef, var4, 0.0D, numdef);
        } else {
            ladder(var7, var8, 2, -1, -1, 0.0D, 0.0D, 0, var4, 0.0D, true);
        }

        lscset = false;
    }

    /**
     * Sets user defined properties for the scale drawn by the next call of drawScaleX or drawScaleY.
     *
     * @param fix         value (in computing coordinates) which one wants to mark and label with a number on the axis
     * @param del         distance between labeled tick marks
     * @param ntic        number of intervals with smaller tic marks between two labeled tick marks
     * @param ticscale    scale factor for tic length
     * @param numberscale scale factor for numbers
     * @param lnum        defines wether the numbers on the axes are shown ind decimal (lnum=true) or exponential form (lnum=false)
     */
    public static void setParametersForScale(double fix, double del, int ntic, double ticscale, double numberscale, boolean lnum) {
        lscset = true;
        fixdef = fix;
        deldef = del;
        ntcdef = ntic;
        if (ticscale > 0.0D) {
            ticdef = ticscale;
        } else {
            ticdef = 1.0D;
        }

        if (numberscale > 0.0D) {
            grdef = numberscale;
        } else {
            grdef = 1.0D;
        }

        numdef = lnum;
    }

    /**
     * Sets paper size to A4 format and all boders to 0.5 cm.
     */
    public static void setStandardPaperSizeAndBorders() {
        papersize[0] = 21.02241D;
        papersize[1] = 29.730177D;
        paperborders[0] = 0.5D;
        paperborders[1] = 0.5D;
        paperborders[2] = 0.5D;
        paperborders[3] = 0.5D;
    }

    /**
     * Sets paper size and distances of used area from paper edge; if not called A4 format is assumed with 0.5 cm for all boders.
     *
     * @param paperwidth   - width of paper in cm
     * @param paperheight  - height of paper in cm
     * @param borderleft   - distance of used area from left edge in cm
     * @param borderright  - distance of used area from right edge in cm
     * @param bordertop    - distance of used area from top edge in cm
     * @param borderbottom - distance of used area from bottom edge in cm
     */

    public static void setPaperSizeAndBorders(double paperwidth, double paperheight, double borderleft, double borderright, double bordertop, double borderbottom) {
        papersize[0] = paperwidth;
        papersize[1] = paperheight;
        paperborders[0] = borderleft;
        paperborders[1] = borderright;
        paperborders[2] = borderbottom;
        paperborders[3] = bordertop;
    }

    /**
     * Assigns standard color and linewidth values to all color indices for screen plots; also assigns standard color to screen plot background.
     * <br>
     * Background: Color(0.f, 0.f, .9f)
     * <br>
     * color index 1: Color(1.f, 1.f, 0.f), line width 2.f
     * <br>
     * color index 2: Color(1.f, 1.f, 1.f), line width 2.f
     * <br>
     * color index 3: Color(0.f, 1.f, 1.f), line width 2.f
     * <br>
     * color index 4: Color(1.f, 0.784f, 0.f), line width 2.f
     * <br>
     * color index 5: Color(1.f, 0.f, 0.f), line width 2.f
     * <br>
     * color index 6: Color(0.753f, 0.753f, 0.753f), line width 2.f
     * <br>
     * color index 7: Color(0.f, 1.f, 0.f), line width 2.f
     * <br>
     * color index 8: Color(1.f, 0.f, 1.f), line width 2.f
     */
    public static void setStandardScreenColors() {
        iqpsfp = true;
        ct[0] = new Color(0.0F, 0.0F, 0.9F);
        ct[1] = new Color(1.0F, 1.0F, 0.0F);
        ct[2] = new Color(1.0F, 1.0F, 1.0F);
        ct[3] = new Color(0.0F, 1.0F, 1.0F);
        ct[4] = new Color(1.0F, 0.784F, 0.0F);
        ct[5] = new Color(1.0F, 0.0F, 0.0F);
        ct[6] = new Color(0.753F, 0.753F, 0.753F);
        ct[7] = new Color(0.0F, 1.0F, 0.0F);
        ct[8] = new Color(1.0F, 0.0F, 1.0F);
        lw[0] = 70.0F;
        lw[1] = 70.0F;
        lw[2] = 70.0F;
        lw[3] = 70.0F;
        lw[4] = 70.0F;
        lw[5] = 70.0F;
        lw[6] = 70.0F;
        lw[7] = 70.0F;

        for (int var0 = 0; var0 < 8; ++var0) {
            strokes[var0] = new BasicStroke(lw[var0], 1, 1);
        }

    }

    /**
     * Assigns color and linewidth to a color indesx for screen plots.
     *
     * @param index     color index (>=1, <=8)
     * @param color     color to be assigned
     * @param linewidth line width (standard value is 2.f)
     *                  s
     */
    public static void setScreenColor(int index, Color color, float linewidth) {
        if (index > 0 && index <= 8) {
            flushPolyline();
            ct[index] = color;
            lw[index - 1] = 35.0F * linewidth;
            strokes[index - 1] = new BasicStroke(lw[index - 1], 1, 1);
        }

    }

    /**
     * Assigns a color to the background of screen plots.
     *
     * @param color background color
     */
    public static void setScreenBackground(Color color) {
        iqpsfp = false;
        ct[0] = color;
    }

    /**
     * Assigns standard color and linewidth values to all color indices for Postscript plots.
     * <br>
     * Background: Color.white
     * <br>
     * color index 1: Color(0.f, 0.f, 1.f), line width 2.f
     * <br>
     * color index 2: Color(0.f, 0.f, 0.f), line width 2.f
     * <br>
     * color index 3: Color(0.f, 1.f, 0.f), line width 2.f
     * <br>
     * color index 4: Color(1.f, 0.f, 0.f), line width 2.f
     * <br>
     * color index 5: Color(1.f, 0.f, 0.f), line width 2.f
     * <br>
     * color index 6: Color(.70f, 0.f, 0.75f), line width 2.f
     * <br>
     * color index 7: Color(0.f, 1.f, 0.f), line width 2.f
     * <br>
     * color index 8: Color(1.f, 0.f, 1.f), line width 2.f
     */
    public static void setStandardPSColors() {
        ctab[0] = Color.white;
        ctab[1] = new Color(0.0F, 0.0F, 1.0F);
        ctab[2] = new Color(0.0F, 0.0F, 0.0F);
        ctab[3] = new Color(0.0F, 1.0F, 0.0F);
        ctab[4] = new Color(1.0F, 0.0F, 0.0F);
        ctab[5] = new Color(1.0F, 0.0F, 0.0F);
        ctab[6] = new Color(0.7F, 0.0F, 0.75F);
        ctab[7] = new Color(0.0F, 1.0F, 0.0F);
        ctab[8] = new Color(1.0F, 0.0F, 1.0F);

        for (int var0 = 0; var0 < 8; ++var0) {
            linw[var0] = 2.0F;
        }

    }

    /**
     * Assigns color and linewidth to a color indesx for Postscript plots plots.
     *
     * @param index     color index (>=1, <=8)
     * @param color     color to be assigned
     * @param linewidth line width (standard value is 2.f)
     */
    public static void setPSColor(int index, Color color, float linewidth) {
        if (index > 0 && index <= 8) {
            ctab[index] = color;
            linw[index - 1] = linewidth;
        }

    }

    /**
     * Assigns standard color and linewidth values to all color indices for Postscript plots in black-and-white.
     * <br>
     * Background: Color.white
     * <br>
     * color index 1 to 8: Color.black, line width 2.f
     */
    public static void setStandardPSColorsBlackAndWhite() {
        iqpsfp = false;
        ctab[0] = Color.white;

        for (int var0 = 1; var0 < 9; ++var0) {
            ctab[var0] = Color.black;
            linw[var0 - 1] = 2.0F;
        }

    }

    /**
     * Returns a number with the sign of x and somewhat larger than x with only few significant digits; method is used for automatic determination of window in computing coordinates by some classes in package datangraphics which yield a complete plot.
     *
     * @param x number to round up
     * @return number with the sign of x and somewhat larger than x
     */
    static double roundUp(double x) {
        double var8 = 1.0D;
        if (x < 0.0D) {
            var8 = -1.0D;
        }

        x = Math.abs(x);
        double var10;
        if (x == 0.0D) {
            var10 = 0.0D;
        } else {
            double var14 = Math.log10(x);
            double var2 = Math.floor(var14);
            double var4 = Math.pow(10.0D, var2);
            double var6 = Math.ceil(10.0D * x / var4);
            int var12 = (int) var6;
            int var13 = var12 % 10;
            if (var8 == 1.0D) {
                ++var6;
            } else {
                --var6;
            }

            var10 = var8 * var6 * 0.1D * var4;
        }

        return var10;
    }

    /**
     * Returns a number with the sign of x and somewhat smaller than x with only few significant digits; method is used for automatic determination of window in computing coordinates by some classes in package datangraphics which yield a complete plot.
     *
     * @param x number to round down
     * @return number with the sign of x and somewhat smaller than x
     */
    static double roundDown(double x) {
        double var8 = 1.0D;
        if (x < 0.0D) {
            var8 = -1.0D;
        }

        x = Math.abs(x);
        double var10;
        if (x == 0.0D) {
            var10 = 0.0D;
        } else {
            double var14 = Math.log10(x);
            double var2 = Math.floor(var14);
            double var4 = Math.pow(10.0D, var2);
            double var6 = Math.ceil(10.0D * x / var4);
            int var12 = (int) var6;
            int var13 = var12 % 10;
            if (var8 == 1.0D) {
                --var6;
            } else {
                ++var6;
            }

            var10 = var8 * var6 * 0.1D * var4;
        }

        return var10;
    }

    private static String getExtension(String var0) {
        String var1 = null;
        int var2 = var0.lastIndexOf(46);
        if (var2 > 0 && var2 < var0.length() - 1) {
            var1 = var0.substring(var2 + 1).toLowerCase();
        }

        return var1;
    }

    static {
        strokes = new BasicStroke[]{
                new BasicStroke(lw[0], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[1], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[2], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[3], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[4], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[5], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[6], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
                new BasicStroke(lw[7], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        };
        framecoords = new double[4];
        bstrng = new byte[80];
        bcmstr = new byte[12];
        iqpsfp = false;
        ixyc = new int[2];
        ctab = new Color[]{
                new Color(1.0F, 1.0F, 0.902F),
                new Color(0.0F, 0.0F, 1.0F),
                new Color(0.0F, 0.0F, 0.0F),
                new Color(0.0F, 1.0F, 0.0F),
                new Color(1.0F, 0.0F, 0.0F),
                new Color(1.0F, 0.0F, 0.0F),
                new Color(0.7F, 0.0F, 0.75F),
                new Color(0.0F, 1.0F, 0.0F),
                new Color(1.0F, 0.0F, 1.0F)
        };
        linw = new float[]{
                2.0F,
                2.0F,
                2.0F,
                2.0F,
                2.0F,
                2.0F,
                2.0F,
                2.0F
        };
        cdin = 118.92D;
        root2 = 1.4142D;
        qac = new double[2];
        qbc = new double[2];
        qdc = new double[2];
        qdci = new double[2];
        qaw2 = new double[2];
        qbw2 = new double[2];
        qdw2 = new double[2];
        qdw2i = new double[2];
        qawp = new double[2];
        qbwp = new double[2];
        qdwp = new double[2];
        qdwpi = new double[2];
        qad = new double[]{0.0D, 0.0D};
        qbd = new double[]{32767.0D, 32767.0D};
        qdd = new double[]{32767.0D, 32767.0D};
        qddi = new double[]{3.05185E-5D, 3.05185E-5D};
        qform = new double[2];
        qadp = new double[2];
        qbdp = new double[2];
        qddp = new double[2];
        qddpi = new double[2];
        qadcl = new double[2];
        qbdcl = new double[2];
        papersize = new double[]{21.02241D, 29.730177D};
        paperborders = new double[]{0.5D, 0.5D, 0.5D, 0.5D};
        psOffsets = new double[2];
        plsizval = new double[2];
        iqnplm = 200;
        qoutx = new double[iqnplm];
        qouty = new double[iqnplm];
        open = false;
        workstation1Open = false;
        workstation2Open = false;
        k = new int[]{64, 38, 37};
        kk = new int[]{94, 95, 35};
        kkk = 34;
        ll = new int[]{1, 96, 96, 4, 96, 96, 95, 62, 165, 3, 108, 12, 109, 14, 60, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 156, 29, 153, 97, 96, 33, 34, 56, 177, 37, 184, 176, 40, 41, 41, 43, 179, 45, 46, 186, 181, 178, 48, 182, 52, 47, 96, 185, 180, 183, 58, 6, 96, 32, 96, 96, 95, 91, 175, 93, 94, 122, 123, 143, 125, 128, 142, 124, 127, 130, 130, 131, 132, 133, 134, 145, 137, 129, 138, 139, 140, 79, 96, 144, 135, 141, 126};
        lll = new int[]{1, 96, 96, 4, 96, 96, 7, 63, 147, 148, 11, 173, 155, 14, 5, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 161, 146, 162, 94, 96, 166, 34, 159, 154, 37, 38, 150, 40, 152, 42, 43, 175, 151, 46, 167, 167, 49, 163, 169, 92, 168, 168, 157, 56, 164, 58, 59, 96, 61, 96, 96, 95, 91, 175, 93, 94, 170, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 171, 80, 81, 82, 83, 84, 172, 86, 87, 88, 89, 90};
        mm = new int[]{1, 96, 96, 4, 96, 96, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 96, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 96, 61, 96, 96, 95, 91, 175, 93, 94, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
        m = new int[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 123, 124, 125, 126, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
        iqlett = new short[]{-810, 811, 911, 910, 810, -835, 935, 918, 818, 835, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -535, 835, 828, 535, -1335, 1535, 1528, 1335, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -313, 833, -1013, 1533, -1828, 328, -1518, 18, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1228, 1129, 830, 530, 229, 127, 125, 224, 523, 823, 1122, 1220, 1218, 1016, 815, 515, 216, 118, -735, 710, -999, 0, 0, 0, 0, 1535, -34, 135, 335, 434, 432, 331, 131, 32, 34, -1410, 1210, 1111, 1113, 1214, 1414, 1513, 1511, 1410, -999, 0, 0, 0, 0, 0, -1810, 530, 533, 835, 1335, 1533, 1530, 518, 513, 810, 1310, 1815, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -534, 434, 435, 535, 532, 430, 329, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1535, 1233, 1030, 925, 920, 1015, 1212, 1510, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 333, 530, 625, 620, 515, 312, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -118, 1518, -1313, 323, -825, 810, -313, 1323, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -118, 1518, -825, 810, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -210, 110, 111, 211, 207, 105, 4, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, 1518, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -811, 911, 910, 810, 811, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 1535, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 635, 834, 933, 1031, 1127, 1117, 1014, 912, 811, 610, 510, 311, 212, 114, 17, 27, -999, 0, 0, 0, -26, 635, 610, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -28, 130, 333, 535, 835, 1034, 1232, 1228, 1124, 10, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -31, 234, 535, 835, 1034, 1231, 1228, 1125, 1024, 524, -1024, 1222, 1320, 1316, 1213, 1011, 710, 410, 211, 13, -999, 0, 0, 0, 0, -835, 14, 1314, -1020, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1235, 35, 24, 525, 825, 1024, 1222, 1319, 1316, 1213, 1111, 810, 510, 311, 13, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1232, 1134, 935, 535, 334, 132, 29, 17, 114, 212, 311, 510, 910, 1111, 1212, 1316, 1320, 1222, 1123, 924, 524, 323, 121, 18, -999, -31, 35, 1335, 410, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -535, 233, 30, 28, 423, 823, 1228, 1230, 1033, 735, 535, -423, 18, 15, 212, 510, 710, 1012, 1215, 1218, 823, -999, 0, 0, 0, -12, 111, 310, 710, 1011, 1113, 1217, 1232, 1034, 835, 535, 234, 30, 26, 124, 322, 521, 721, 1022, 1223, -999, 0, 0, 0, 0, -819, 820, 920, 919, 819, -811, 911, 910, 810, 811, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -210, 110, 111, 211, 207, 105, 4, -119, 120, 220, 219, 119, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1510, 18, 1523, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -23, 1523, -1515, 15, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 1518, 23, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -333, 535, 1035, 1333, 1328, 823, 820, -811, 911, 910, 810, 811, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1510, 310, 13, 23, 325, 1325, 1523, 1518, 1315, 515, 318, 520, 820, 1018, 815, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 635, 1210, -219, 1019, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 735, 934, 1132, 1229, 1227, 1125, 1024, 723, 23, -723, 1121, 1218, 1216, 1012, 911, 710, 10, -999, 0, 0, 0, 0, 0, -1131, 1033, 934, 735, 535, 334, 233, 131, 27, 17, 114, 212, 311, 510, 710, 911, 1012, 1114, -999, 0, 0, 0, 0, 0, 0, -10, 35, 735, 1033, 1229, 1218, 1114, 1012, 811, 710, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 1235, -23, 923, -10, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 1235, -23, 923, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1131, 1033, 934, 735, 535, 334, 233, 131, 27, 17, 114, 212, 311, 510, 710, 911, 1012, 1114, 1217, 1221, 821, -999, 0, 0, 0, -10, 35, -1235, 1210, -23, 1223, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 610, -310, 335, -35, 635, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -14, 112, 310, 410, 611, 712, 815, 835, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, -19, 1235, -424, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 10, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 718, 1435, 1410, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 1210, 1235, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 735, 934, 1033, 1131, 1227, 1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 27, -999, 0, 0, 0, -10, 35, 735, 1034, 1232, 1329, 1327, 1224, 1022, 721, 21, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 735, 934, 1033, 1131, 1227, 1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 27, -618, 1210, -999, 0, -10, 35, 735, 1034, 1232, 1329, 1327, 1224, 1022, 721, 21, -721, 1310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1232, 1034, 735, 635, 234, 132, 29, 27, 125, 324, 823, 1021, 1119, 1217, 1215, 1112, 810, 510, 211, 112, 14, -999, 0, 0, 0, -610, 635, -35, 1235, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 17, 114, 212, 311, 510, 610, 811, 912, 1014, 1117, 1135, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 610, 1235, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 410, 828, 1210, 1635, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1210, -10, 1235, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 623, 1235, -623, 610, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1235, 10, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -910, 310, 335, 935, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1510, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -510, 1110, 1135, 535, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -810, 835, 1030, 530, 835, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1523, 23, 525, 520, 23, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -828, 1028, 1330, 1333, 1035, 835, 533, 530, 828, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -1025, 1011, 1110, -999, 0, 0, 0, 0, 0, 0, 0, -35, 10, -23, 124, 325, 625, 824, 922, 1019, 1016, 913, 811, 610, 310, 11, 12, -999, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -1035, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -18, 1118, 1120, 1023, -999, 0, 0, 0, 0, 0, 0, -735, 535, 334, 232, 210, -26, 626, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -1025, 1006, 904, 603, 303, 4, -999, 0, 0, 0, 0, -35, 10, -23, 124, 325, 525, 724, 823, 921, 910, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -110, 125, -31, 29, 229, 231, 31, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -525, 509, 407, 306, 105, 5, -431, 429, 629, 631, 431, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 10, -13, 1024, -619, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 12, 111, 310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 10, -23, 124, 325, 525, 724, 823, 810, -823, 924, 1125, 1325, 1524, 1623, 1610, -999, 0, 0, 0, 0, 0, 0, 0, 0, -25, 10, -23, 124, 325, 525, 724, 823, 921, 910, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -425, 224, 122, 19, 16, 113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 625, 425, -999, 0, 0, 0, 0, 0, 0, 0, -25, 3, -23, 124, 325, 625, 824, 922, 1019, 1016, 913, 811, 610, 310, 11, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1025, 1003, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -999, 0, 0, 0, 0, 0, 0, 0, 0, -25, 10, -23, 124, 325, 525, 724, 823, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1223, 1124, 825, 525, 324, 223, 121, 120, 219, 518, 818, 1117, 1215, 1213, 1011, 810, 510, 211, 113, -999, 0, 0, 0, 0, 0, -23, 823, -431, 412, 511, 710, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -810, 825, -812, 711, 510, 310, 211, 112, 14, 25, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 510, 1025, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 410, 721, 1010, 1425, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 1010, -10, 1025, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, 203, 304, 1025, -25, 615, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 1025, 10, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1535, 1335, 1033, 1025, 823, 1020, 1013, 1310, 1510, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1310, 1320, -1325, 1335, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -835, 1035, 1333, 1325, 1523, 1320, 1313, 1010, 810, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, 320, 820, 1316, 1816, 2118, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -534, 634, 635, 535, 532, 630, 729, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1030, 833, 333, 30, 28, 325, 825, 1023, 1020, 818, 318, 20, 23, 325, -818, 1015, 1013, 810, 310, 13, -999, 0, 0, 0, 0, -520, 823, 810, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -20, 323, 1023, 1320, 10, 1310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -23, 1323, 818, 1315, 810, 310, 13, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1010, 1023, 315, 1315, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1323, 323, 318, 820, 1318, 1313, 810, 310, 13, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1323, 323, 318, 1318, 1313, 810, 510, 318, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -323, 1323, 310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -310, 315, 1318, 1323, 323, 318, 1315, 1310, 310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -313, 510, 1010, 1313, 1320, 1023, 523, 320, 318, 1318, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -313, 510, 1010, 1313, 1320, 1023, 523, 320, 313, -1323, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -118, 1518, -810, 825, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -318, 1318, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -533, 835, 823, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -33, 335, 1035, 1333, 23, 1323, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1335, 830, 1328, 823, 323, 325, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 1035, 328, 1328, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1335, 335, 330, 833, 1330, 1325, 823, 323, 25, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1335, 335, 330, 1330, 1325, 823, 523, 330, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -335, 1335, 323, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -323, 328, 1330, 1335, 335, 330, 1328, 1323, 323, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -325, 523, 1023, 1325, 1333, 1035, 535, 333, 330, 1330, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -325, 523, 1023, 1325, 1333, 1035, 535, 333, 325, -1335, 23, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -318, 1318, -813, 823, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -318, 1318, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1225, 1013, 811, 610, 410, 211, 113, 16, 19, 122, 224, 425, 625, 824, 1022, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, -3, 28, 131, 334, 535, 735, 934, 1033, 1131, 1129, 1127, 1025, 924, 623, 223, -623, 922, 1120, 1218, 1215, 1011, 710, 510, 212, -999, -24, 125, 225, 324, 422, 710, 1225, -710, 703, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -626, 325, 224, 122, 19, 16, 113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 725, 130, 133, 434, 1134, -999, 0, 0, 0, -35, 34, 133, 332, 432, 1035, -431, 229, 127, 23, 17, 113, 311, 710, 810, 1009, 1006, 805, 5, -999, 0, 0, 0, 0, 0, -25, 125, 224, 321, 310, -321, 524, 725, 925, 1124, 1221, 1202, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 123, 21, 20, 119, 318, 618, -318, 117, 16, 14, 112, 211, 410, 710, 911, 1012, -999, 0, 0, 0, -21, 124, 225, 325, 422, 413, 511, 610, 810, 1111, 1213, 1229, 1033, 835, 635, 434, 431, 529, 728, 1527, -999, 0, 0, 0, 0, -25, 13, 111, 310, 410, 611, 713, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 10, -925, 725, 18, 710, 910, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 235, 334, 1110, -10, 621, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1011, 910, 811, 825, -812, 711, 510, 310, 211, 112, 14, 25, -14, 2, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 225, 10, 311, 512, 714, 1020, 1125, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 34, 132, 231, 331, 1035, -331, 230, 128, 126, 325, 1029, -325, 123, 20, 14, 211, 711, 910, 1009, 1007, 906, 705, 5, -999, -425, 224, 122, 19, 16, 113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 625, 425, -999, 0, 0, 0, 0, 0, 0, 0, -25, 1125, -410, 425, -825, 811, 910, 1010, 1111, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -425, 225, 122, 19, 16, 113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 625, 425, -16, 7, 105, 304, 904, 1003, -999, 0, -325, 224, 122, 19, 17, 113, 211, 310, 710, 811, 913, 1017, 1019, 922, 824, 725, 325, -725, 1125, -999, 0, 0, 0, 0, 0, -625, 613, 711, 910, 1010, 1212, -125, 1125, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1225, 1212, 1111, 910, 710, 611, 512, 414, 424, 325, 225, 124, 22, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -225, 123, 20, 17, 114, 212, 510, 710, 1012, 1114, 1217, 1220, 1123, 925, 725, 624, 602, -999, 0, 0, 0, 0, 0, 0, 0, -25, 225, 1005, 1205, -5, 1225, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1225, 1212, 1111, 910, 710, 611, 512, 414, 424, 325, 225, 124, 22, -825, 802, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, -325, 123, 20, 16, 113, 211, 410, 510, 611, 713, 720, -713, 811, 910, 1010, 1211, 1313, 1416, 1420, 1323, 1125, -999, 0, 0, 0, -23, 1523, -18, 1518, -13, 1513, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -23, 1523, 1025, 1020, 1523, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -25, 1510, -1525, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, 1318, -523, 525, 825, 823, 523, -513, 813, 810, 510, 513, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -20, 1520, -15, 1515, -1325, 310, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -20, 1520, -828, 813, -1510, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -13, 310, 510, 1335, 1535, 1833, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -23, 1223, 1422, 1520, 1515, 1413, 1212, 12, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 820, 1510, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, 1818, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1523, 323, 122, 20, 15, 113, 312, 1512, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -20, 810, 835, 1535, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -15, 18, 320, 820, 1813, 2313, 2515, 2518, 2320, 1820, 813, 313, 15, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1835, 1810, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1520, 1323, 1023, 813, 313, 15, 20, 323, 823, 1013, 1313, 1515, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1528, 20, 1513, -1510, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1510, 10, -13, 1520, 28, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1323, 1025, 1028, 1330, 1530, 1828, 1825, 1523, 1323, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 635, 1210, -219, 1019, -635, 437, 639, 837, 635, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -835, 810, 1015, 515, 810, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 635, 1210, -219, 1019, -337, 334, -937, 934, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -337, 334, 535, 735, 934, 937, -934, 1033, 1131, 1227, 1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 27, 131, 233, 334, -999, -35, 17, 114, 212, 311, 510, 610, 811, 912, 1014, 1117, 1135, -337, 334, -737, 734, -999, 0, 0, 0, 0, 0, 0, 0, 0, 510, -310, 333, 535, 1035, 1333, 1330, 1025, 1318, 1310, 810, 513, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1023, 924, 725, 425, 224, 122, 19, 16, 113, 211, 410, 710, 911, 1012, -1025, 1011, 1110, -333, 330, -733, 730, -999, 0, 0, 0, -425, 224, 122, 19, 16, 113, 211, 410, 610, 811, 913, 1016, 1019, 922, 824, 625, 425, -333, 330, -733, 730, -999, 0, 0, 0, -810, 825, -812, 711, 510, 310, 211, 112, 14, 25, -233, 230, -633, 630, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -506, 813, 1313, 506, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -530, 533, -1030, 1033, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 35, 1035, 1034, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 535, 1010, 10, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -27, 131, 233, 334, 535, 735, 934, 1033, 1131, 1227, 1217, 1114, 1012, 911, 710, 510, 311, 212, 114, 17, 27, -223, 1023, -999, 0, -310, 10, -110, 635, 1110, -910, 1210, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -35, 1035, -323, 723, -10, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -210, 235, -810, 835, -35, 1035, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1035, 35, 523, 10, 1010, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -610, 631, 434, 235, 135, 34, -631, 834, 1035, 1135, 1234, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -430, 229, 127, 24, 22, 118, 216, 415, 615, 816, 918, 1022, 1024, 927, 829, 630, 430, -435, 635, -535, 510, -410, 610, -999, 0, -35, 27, 124, 222, 321, 520, 620, 821, 922, 1024, 1127, 1135, -435, 635, -535, 510, -410, 610, -999, 0, 0, 0, 0, 0, 0, -210, 510, 515, 316, 119, 23, 27, 131, 334, 535, 735, 934, 1131, 1227, 1223, 1119, 916, 715, 710, 1010, -999, 0, 0, 0, 0, -310, 10, 3, 303, 310, -301, 13, 0, 300, 301, -999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        fg1 = 0.6D;
        fg2 = 0.6D;
        utx = 25.0D;
        uty = 25.0D;
        z = 8.0D;
        idop = new int[100];
        j = new int[100];
        idat = new int[25];
        avin = new double[2];
        avnext = new double[2];
        rv = new double[2];
        av = new double[2];
        op = new double[2];
        gv = new double[2];
        gve = new double[2];
        rve = new double[2];
        wert2d = new double[2];
        piqurt = 0.7853982D;
        rnt = false;
        nc = 0;
        an = 0.0D;
        en = 0.0D;
        atims = 0.0D;
        eps = 0.0D;
        atdel = 0.0D;
        aneps = 0.0D;
        eneps = 0.0D;
        eta = 0.0D;
        lastPoint = new double[2];
        borderPoints = new double[4];
    }

    private static class CloseButtonListener implements ActionListener {
        private CloseButtonListener() {
        }

        public void actionPerformed(ActionEvent var1) {
//            System.out.println("AL: close button activated");

            for (int var2 = 0; var2 < DatanGraphics.frames.size(); ++var2) {
                if (DatanGraphics.frames.elementAt(var2) != null) {
                    DatanGraphics.frames.elementAt(var2).dispose();
                }
            }

            DatanGraphics.closeFrame.dispose();
        }
    }
}

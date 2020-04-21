/*
 * Dancing Numbers - Logistic growth applied to Covid-19
 *
 * A naive approach to predict the spread of virus
 *
 *
 * Copyright (c) 2020 V. Eitner
 *
 * This file is part of "Dancing Numbers".
 *
 * "Dancing Numbers" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * "Dancing Numbers" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Dancing Numbers".  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package de.vee;

import de.vee.model.DeathRate;
import de.vee.model.Input;
import de.vee.model.LogisticFunc;
import de.vee.model.Model;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static de.vee.model.Bisection.find;
import static de.vee.model.Convolve.smooth;
import static de.vee.model.FunFactory.createFunction;
import static de.vee.model.ModelFactory.createModel;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Frames {
    private final static DefaultXYDataset dataset = new DefaultXYDataset();
    private static double TODAY = 75;
    private static final int CHART_WIDTH = 1280;//(int) Math.round(1.8 * 640);
    private static final int CHART_HEIGHT = 960;//(int) Math.round(1.8 * 480);
    private static final int REPEAT_FRAME = 15;
    private final int id;
    private Input input;

    private static void clearDataset() {
        for (int i = dataset.getSeriesCount() - 1; i >= 0; i--) {
            dataset.removeSeries(dataset.getSeriesKey(i));
        }
    }

    private Frames(Input input, int id) {
        this.input = input;
        this.id = id;
    }

    private void createFrames(double xmax, boolean adjustY, boolean markLast) {
        double[][] d = input.getData();
        TODAY = d[0][d[0].length - 1] + 1;
        TODAY += 5 - (int) TODAY % 5;
        Model m = step(input, null, adjustY, markLast);
        analyse(input, m);
        analyseDerivative(input, m, Math.max(xmax, TODAY), false, markLast);
        estimateICU(input, m, Math.max(xmax, TODAY));
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        Map<Integer, String> regions = new HashMap<>();
//        regions.put(97, "World");
//        regions.put(1, "Europe");
//        regions.put(2, "Italy");
//        regions.put(3, "Spain");
//        regions.put(4, "France");
//        regions.put(5, "Germany");
//        regions.put(6, "Poland");
//        regions.put(7, "Austria");
//        regions.put(8, "Switzerland");
//        regions.put(9, "Greece");
//        regions.put(10, "Croatia");
//        regions.put(44, "Brazil");
        regions.put(43, "United_States_of_America");

        Map<Integer, double[]> delta = new HashMap<>();
        delta.put(1, new double[]{1e-5, 0.5, 0.001, 1e-3});
        delta.put(5, new double[]{0.05, 1, 0.05, 0.5});
        Map<Integer, double[][]> constraints = new HashMap<>();
        constraints.put(5, new double[][]
                {{1e-7, 0.25},//a0_min,a0_max
                        {1e-6, 5e3}, //a1_min,a1_max
                        {1e-6, 9e-1}, //a2_min,a2_max
                        {0.5, 20}, //a3_min,a3_max
                });
        Map<Integer, Boolean> inflectionPoint = new HashMap<>();
        inflectionPoint.put(1, true);
//        inflectionPoint.put(5, true);
        Map<Integer, double[]> slices = new HashMap<>();
//        slices.put(2, new double[]{62, 71, 79});
//        slices.put(2, new double[]{64, 72, 81, 89}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
//        slices.put(2, new double[]{62, 82, 89}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
        slices.put(2, new double[]{62, 82}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
        slices.put(3, new double[]{70, 77});
        slices.put(4, new double[]{70, 84});
        slices.put(5, new double[]{/*52, 60,*/ 67, 76, 84});
        slices.put(9, new double[]{82});
        slices.put(43, new double[]{/*52, 60,*/ 76, 84});
        try {
            new Info(CHART_WIDTH, CHART_HEIGHT).applyTemplatesAndSave(regions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Integer key : regions.keySet()) {
            String region = regions.get(key);
            Input input = Input.get(region, 1e6);
            if (delta.containsKey(key)) {
                input = input.withDelta(delta.get(key));
            }
            if (constraints.containsKey(key)) {
                input = input.withConstraints(constraints.get(key));
            }
            if (inflectionPoint.containsKey(key)) {
                input = input.withInflectionPoint(inflectionPoint.get(key));
            }
            if (slices.containsKey(key)) {
                input = input.withSlice(slices.get(key));
            }
            Frames frame = new Frames(input, key);
            frame.createFrames(360, true, false);
        }

        if (args.length < 100) return;


        Input input = Input.get("China", 1428E6)
                .withDelta(new double[]{0.1, 0.2, 0.1, 0.005, 0.1, 0.2})
                .withInitial(new double[]{1.E-5, 17, 14.E-2, 0.04, 10, 0.1})
                .withFileName("china_g.dat")
                .nullifyData();
        Frames frame = new Frames(input, 0);
/*
        frame.step(input.clone().withSuffix("0").nullifyData(), input.clone().withSuffix("1").nullifyData(), false, false);
        frame.createFrames(TODAY, false, true);
*/

//too early
        /*
        input = Input.get("World", 447E6)
                .withDelta(new double[]{0.1, 0.2, 0.1, 0})
//                .withInitial(new double[]{1.E-5,17,14.E-2,14})
                .withYMax(5E5);

        frame = new Frames(input, 1);
        frame.createFrames(360, true, false);

        if (args.length < 100) return;

        input = Input.get("Europe", 447E6)
                .withInitial(new double[]{0.01, 2, 0.05, 1, 0.1, 4, 0.5})
                .withDelta(new double[]{1e-5, 0.5, 0.001, 1e-3, 1e-2, 1e-1, 1e-2})
                .withYMax(5E5)
                .withInflectionPoint(false);
        frame = new Frames(input, 1);
        frame.createFrames(360, true, false);
        //     if (args.length < 100) return;

        input = Input.get("Italy", 60E6)
                .withInflectionPoint(false);
        frame = new Frames(input, 2);
        frame.createFrames(360, true, false);

//        if (args.length < 100) return;
        input = Input.get("Spain", 46.7E6)
                .withInflectionPoint(false);
        frame = new Frames(input, 3);
        frame.createFrames(360, true, false);

//        if (args.length < 100) return;

        input = Input.get("France", 67E6)
                .withInflectionPoint(false);
        frame = new Frames(input, 4);
        frame.createFrames(360, true, false);

//        if (args.length < 100) return;

        input = Input.get("Germany", (long) 84E6).withDelta(new double[]{0.05, 1, 0.05, 0.5, 0.02, 1, 0.2}).withConstraints(new double[][]
                {
                        {1e-7, 0.25},//a0_min,a0_max
                        {1e-6, 5e3}, //a1_min,a1_max
                        {1e-6, 9e-1}, //a2_min,a2_max
                        {0.5, 20}, //a3_min,a3_max
                        {1e-3, 0.3}, //percentage_min,percentage_max
                        {0.1, 14},//shift_min,shift_max
                        {0.05, 3.} //p_min,p_max
                }
        );

        frame = new Frames(input, 5);
//        frame.step(input.clone().withSuffix("1").nullifyData(), input.clone().withSuffix("2").nullifyData(), false, false); //creates an adjusted dataset
        frame.createFrames(360, true, false);

//      if (args.length < 100) return;

        input = Input.get("Poland", 38.66E6).withInflectionPoint(false);
        frame = new Frames(input, 6);
        frame.createFrames(360, true, false);


        input = Input.get("Austria", 8847037).withInflectionPoint(false);
        frame = new Frames(input, 8);
        frame.createFrames(360, true, false);

        input = Input.get("Switzerland", 8516543).withInflectionPoint(false);
        frame = new Frames(input, 9);
        frame.createFrames(360, true, false);

        input = Input.get("Greece", 10727668).withInflectionPoint(false);
        frame = new Frames(input, 10);
        frame.createFrames(360, true, false);
        input = Input.get("Croatia", 4089400).withInflectionPoint(false);
        frame = new Frames(input, 11);
        frame.createFrames(360, true, false);

        input = Input.get("Slovenia", 2067372).withInflectionPoint(true);
        frame = new Frames(input, 12);
        frame.createFrames(360, true, false);
*/


//        /*
//        m=step("0071", input, null, true, false);
        input = Input.get("United_States_of_America", 327.2e6).withInflectionPoint(false);
        frame = new Frames(input, 7);
        frame.createFrames(360, true, false);
//*/

    }

    private boolean chartExists(String prefix, int postfix) {
//        File file = new File(String.format("gr/%s_p_%04d.png", prefix, postfix));
//        return !file.exists();
        return false;
    }

    private void analyseDerivative(Input input, Model model, double xmax, boolean scale, boolean markLast) {
        boolean deaths = true;
        String prefix = String.format("0%02d3_5", id);
        System.out.println(input.getTitle() + ": Processing (analyseDerivative) ....");
        clearDataset();
        double[][] dd = input.getData();
        double[] x = Arrays.copyOf(dd[0], dd[0].length);
        double[] y = Arrays.copyOf(dd[1], dd[1].length);
        double[] dr = null;
        if (dd.length > 2) {
            dr = Arrays.copyOf(dd[2], dd[2].length); //deaths
        }
        int count = x.length;

        double[][] a = model.getResult();
        int start = model.getStart();
        JFreeChart chart = null;

        //prepare reported data
        double[] dy1 = new double[count];
        double[] dx1 = new double[count];
        double[] dry1 = null;
        dx1[0] = x[0] + 0.5;
        dy1[0] = y[0];
        for (int i = 1; i < dy1.length; i++) {
            dy1[i] = Math.max(y[i] - y[i - 1], 0);
            dx1[i] = x[i] + 0.5; //shift because of "integration"
        }
        if (dr != null) {
            dry1 = new double[count];
            dy1[0] = dr[0];
            for (int i = 1; i < dry1.length; i++) {
                dry1[i] = Math.max(dr[i] - dr[i - 1], 0);
            }
        }
        double[][] xy1 = smooth(dx1, dy1);


        for (int l = Math.max(start, count - 15); l < count; l++) { //last 2 weeks only
            if (chartExists(prefix, l)) continue;
            LogisticFunc g = createFunction(a[l], input.getSlice(x[l]), input.getPopulationSize());
            int n = (int) (xmax * 4) + 1;
            double[] dx = new double[n];
            double[] dy = new double[n];
            double max = 0;
            double xd = 0;
            double ddx = xmax / (n - 1);
            int imax = -1;
            for (int i = 0; i < dx.length; i++) {
                dx[i] = xd;
                dy[i] = g.derivative(xd);
                xd += ddx;
            }
            for (int i = 0; i < dx.length; i++) {
                if (dy[i] > max) {
                    max = dy[i];
                    imax = i;
                }
            }
            if (scale) {
                for (int i = 0; i < dx.length; i++) {
                    dy[i] /= max;
                }
            }

            dataset.addSeries("New infections per time (model)", createSeries(dx, dy, dx[dx.length - 1] + 1));

            addMaximum("infections", imax, dx, dy);

            double[][] result = model.getResult();
            double deathRate;// = result[im][4];
            double shift;// = result[im][5];
            deathRate = model.getDeathRate(max);
            shift = model.getShift(max);
            double p = model.getP(max);

            double[] dy1d = DeathRate.getRate(dx, g, deathRate, shift, p);

            dataset.addSeries("deaths per time (model)", createSeries(dx, dy1d, dx[dx.length - 1] + 1));

            addMaximum("deaths per time", -1, dx, dy1d);

            dataset.addSeries("Smoothed infections per time (model)", createSeries(xy1[0], xy1[1], dx1[l]));

            chart = ChartFactory.createXYLineChart(
                    String.format("Rate of new infections per time for %s", input.getName()),
                    "Days since 01/20/2020",
                    scale ? "Relative number of new infections per time" : "Rate of new infections per time",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, false, false);

            int day = (int) Math.round(x[l]);
            chart.addSubtitle(new TextTitle(getDateStringLong(day)));

            XYPlot plot = chart.getXYPlot();

            //    renderer.setLabelGenerator(generator);

            plot.setRenderer(new Renderer_For_Derivative(dataset, input));

            DefaultXYDataset ds = new DefaultXYDataset();


            if (dry1 != null) {
                ds.addSeries("New deaths per time (reported)", createSeries(dx1, dry1, dx1[l]));
            }

            double[][] infections = createSeries(dx1, dy1, dx1[l]);
            ds.addSeries("New infections per time (reported)", infections);

            XYBarDataset dsb = new XYBarDataset(ds, 1.);
            plot.setDataset(1, dsb);
            final XYBarRenderer renderer = new XYBarRenderer() {
                Paint getPaint(int series) {
                    Paint paint = Color.gray;
                    String key = ds.getSeriesKey(series).toString().toLowerCase();
                    if (key.contains("death")) paint = new Color(0xC080C0);
                    return paint;

                }

                @Override
                public Paint getItemPaint(int series, int column) {
                    if (markLast) {
                        if (column > 60) return Color.red;
                    }
                    return getPaint(series);
                }

                @Override
                public Paint getSeriesPaint(int series) {
                    return getPaint(series);
                }
            };

            renderer.setShadowVisible(false);
            renderer.setBarPainter(new StandardXYBarPainter());

            plot.setRenderer(1, renderer);

            adjustPlot(plot, 0, xmax);
            saveChart(chart, prefix, l, true);
        }
        if (chart != null) {
            for (int i = count; i < count + REPEAT_FRAME; i++) {
                saveChart(chart, prefix, i, true); //several frames to pause
            }
        }

    }

    private void addMaximum(String key, int imax, double[] x, double[] y) {
        int max = imax;
        double dmax = -1;
        if (max < 0) {
            for (int i = 0; i < x.length; i++) {
                if (y[i] > dmax) {
                    dmax = y[i];
                    max = i;
                }
            }
        }

        if ((max > 0) && (max < y.length - 2)) {
            double[] mx = new double[2];
            double[] my = new double[2];
            mx[0] = x[max];
            mx[1] = x[max];
            my[0] = 0;
            my[1] = y[max] + 10;
            dataset.addSeries("Maximum of " + key, createSeries(mx, my, mx[mx.length - 1] + 1));
        }
    }

    private void estimateICU(Input input, Model model, double xmax) {
        String prefix = String.format("0%02d4_5", id);

        System.out.println(input.getTitle() + ": Processing (estimateICU) ....");
        clearDataset();
        double[][] dd = input.getData();
        double[] x = Arrays.copyOf(dd[0], dd[0].length);
        int count = x.length;

        double[][] a = model.getResult();
        int start = model.getStart();
        JFreeChart chart = null;
        for (int l = count - 1; l < count; l++) {
            if (chartExists(prefix, l)) continue;
            LogisticFunc g = createFunction(a[l], input.getSlice(x[l]), input.getPopulationSize());

            int n = (int) (xmax * 4) + 1;
            double[] x1 = new double[n];
            double xd = 0;
            double ddx = xmax / (n - 1);
            for (int i = 0; i < n; i++) {
                x1[i] = xd;
                xd += ddx;
            }
            double[] params = model.getResult()[l];
            int m = params.length - 1;
            double[] deaths = DeathRate.getRate(x1, g, params[m - 2], params[m - 1], params[m]);

            chart = ChartFactory.createXYLineChart(
                    String.format("Estimated demand on ICU of model for %s", input.getName()),
                    "Days since 01/20/2020",
                    "Number of required ICU",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, false, false);

            int day = (int) Math.round(x[l]);
            chart.addSubtitle(new TextTitle(getDateStringLong(day)));

            XYPlot plot = chart.getXYPlot();

            //      plot.setRenderer(new Renderer_For_Derivative(dataset));


            double[] p = {3, 5, 8};
            double[] days = {8, 13, 21};

            for (int i = 0; i < p.length; i++) {//percentage
                for (int j = 0; j < days.length; j++) {//days
//                    /*
                    int k = x1.length;
                    double[] y1 = new double[k];
                    for (int q = 0; q < k; q++) {
                        int r = 0;
                        while ((x1[q + r] - x1[q]) < days[j]) {
                            y1[q] += deaths[q + r] * ddx;
                            r++;
                            if (q + r >= k) break;
                        }
                        y1[q] *= p[i];
                    }
                    dataset.addSeries(String.format("%d ICU per death required for %d days", (int) Math.round(p[i]), (int) Math.round(days[j])), createSeries(x1, y1, x1[x1.length - 1] + 1));
//                    */
//                    double[] y11 = Convolve.eval(x1, deaths, p[i], days[j], 0.5);
//                    dataset.addSeries(String.format("%d ICU per death required for %d days", (int) Math.round(p[i]), (int) Math.round(days[j])), createSeries(x1, y11, x1[x1.length - 1] + 1));
                }
            }

            adjustPlot(plot, 0, xmax);
            saveChart(chart, prefix, l, true);
        }
        if (chart != null) {
            for (int i = count; i < count + 3 * REPEAT_FRAME; i++) {
                saveChart(chart, prefix, i, true); //several frames to pause
            }
        }
    }

    private void analyse(Input input, Model model) {
        System.out.println(input.getTitle() + ": Processing (analyse) ....");
        String prefix = String.format("0%02d2", id);
        clearDataset();
        double[][] dd = input.getData();
        double[] x = Arrays.copyOf(dd[0], dd[0].length);

        double[][] a = model.getResult();
        int start = model.getStart();

        clearDataset();
        for (int l = 0; l < 3; l++) {
            double[][] d = new double[2][x.length];
            for (int j = start + 1; j < x.length; j++) {
                int k = Math.max(0, a[j].length - 4) + l;
                int km = Math.max(0, a[j - 1].length - 4) + l;
                double delta = (a[j][k] - a[j - 1][km]) / a[j - 1][km];
                if (delta < -1) delta = -1;
                if (delta > 1) delta = 1;
                d[0][j] = x[j];
                d[1][j] = delta;
            }
            int m = 0;
            while (d[0][m] < 1) {
                m++;
            }
            d[0] = Arrays.copyOfRange(d[0], m, d[0].length);
            d[1] = Arrays.copyOfRange(d[1], m, d[1].length);
            dataset.addSeries(String.format("Parameter %d", l), d);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                String.format("Parameter of model for %s", input.getName()),
                "Days since 01/20/2020",
                "Rel. change in parameter",
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false);

        XYPlot plot = chart.getXYPlot();
        adjustPlot(plot, 1., TODAY);

        plot.getRangeAxis().setRange(new Range(-1, 1));

        LegendItemCollection lic = plot.getLegendItems();
        for (int i = 0; i < lic.getItemCount(); i++) {
            LegendItem l = lic.get(i);
            l.setLabelPaint(Color.black);
            l.setFillPaint(Color.black);
            l.setShapeVisible(true);
            l.setLineVisible(false);
            l.setOutlinePaint(Color.black);
        }

        plot.setRenderer(new Renderer_For_Analysis());

        for (int i = 0; i < REPEAT_FRAME; i++) {
            saveChart(chart, prefix, i, false); //several frames to pause the display
        }

    }

    private Model step(Input input0, Input input1, boolean adjustY, boolean markLast) {
        String prefix = String.format("0%02d1_1", id);
        if (input1 != null) {
            prefix = String.format("0%02d1_0", id);
        }
        clearDataset();
        System.out.println(input0.getTitle() + ": Processing (step) ....");
        double[][] dd = input0.getData();
        double[] x = Arrays.copyOf(dd[0], dd[0].length);
        double[] y = Arrays.copyOf(dd[1], dd[1].length);
        double[] dr = null;
        if (dd.length > 2) {
            dr = Arrays.copyOf(dd[2], dd[2].length);
        }
        double[] x1 = null;
        double[] y1 = null;
        double[] dr1 = null;
        if (input1 != null) {
            dd = input1.getData();
            x1 = Arrays.copyOf(dd[0], dd[0].length);
            y1 = Arrays.copyOf(dd[1], dd[1].length);
            if (dd.length > 2) {
                dr1 = Arrays.copyOf(dd[2], dd[2].length);
            }
        }
        Model m;

        m = createModel(x, y, dr, input0);
        int start = m.getStart();
        double[][] a = m.getResult();
        double[][] a1 = null;

        Model m1 = null;
        if (x1 != null) {
            m1 = createModel(x1, y1, dr1, input1);
            a1 = m1.getResult();
        }

        LogisticFunc gp = null;
        LogisticFunc gp1 = null;

        for (int j = 0; j < start; j++) {
            if (chartExists(prefix, j)) continue;

            double omax = input0.getYmax();
            if (adjustY) {
                if (y[j] < 1000) input0.setYMax(1000.);
            }
            createChart(prefix, gp, null, gp1, null, input0, input1, x, y, m, x1, y1, (int) (Math.round(x[j])), false, 100000);
            input0.setYMax(omax);
        }

        double[] v0;
        double[] v1 = null;
        double xmax = 0.;
        boolean first = true;
        int count = x.length;
        if (x1 != null) count += x1.length;
        int xmaxo = (int) Math.round(x[start]);
        LogisticFunc g = null;
        LogisticFunc g1 = null;
        for (int l = start; l < count; l++) {
            if (first) {
                if (l >= x.length) {
                    int k = 0;
                    while (x1[k] < xmax) {
                        if (k + 1 >= x1.length) break;
                        k++;
                    }
                    l = k;
                    xmax = x1[l];
                    first = false;
                } else {
                    xmax = x[l];
                }
            } else {
                if (l >= x1.length) break;
                xmax = x1[l];
            }


            if (first) {
                v0 = a[l];
            } else {
                v0 = a[a.length - 1];
                v1 = a1[l];
            }
            if (chartExists(prefix, l)) continue;

            double omax = input0.getYmax();
            if (adjustY) {
                if (y[l] < 1000) input0.setYMax(1000.);
                else if (y[l] < 10000) input0.setYMax(10000.);
                else if (y[l] < 100000) input0.setYMax(100000.);
                else if (y[l] < 200000) input0.setYMax(200000.);
                else if (y[l] < 500000) input0.setYMax(500000.);
                else if (y[l] < 1E6) input0.setYMax(1E6);
                else if (y[l] < 1E7) input0.setYMax(1E7);
                else if (y[l] < 1E8) input0.setYMax(1E8);
            }
            if (g != null) {
                xmaxo += 1;
                while (xmaxo < xmax) {
                    createChart(prefix, gp, g, gp1, g1, input0, input1, x, y, m, x1, y1, xmaxo, false, 100000);
                    xmaxo += 1;
                }
            }
            xmaxo = (int) Math.round(xmax);
            g = createFunction(v0, input.getSlice(x[l]), input0.getPopulationSize());
            if (!first) {
                g1 = createFunction(v1, input.getSlice(x[l]), input1.getPopulationSize());
            }
            int num = 100000;
            if (markLast) { //begin of a new wave? //todo: calculate the distance to the model and mark
                if (x1 != null) {
                    if (xmax > x1[0]) {
                        num = x1.length - 3;
                    }
                } else {
                    num = x.length - 3;
                }
            }
            createChart(prefix, gp, g, gp1, g1, input0, input1, x, y, m, x1, y1, (int) Math.round(xmax), l == count - 1, num);

            input0.setYMax(omax);
            gp = g;
            gp1 = g1;

            if ((g1 != null) && (m1 != null)) {
                if (x1.length > x.length / 4) {
                    File f = new File("s_" + input.getFileName());
                    try {
                        FileWriter fw = new FileWriter(f);
                        for (int k = 0; k < x.length; k++) {
                            //todo: blabla
                            double ny = g.evaluate(x[k] - 0.5);
                            double y2 = g1.evaluate(x[k]);
                            if (ny > 0.5) { //overflow otherwise
                                double z = (y[k] - ny) / ny;
                                y2 += z * y2;
                            } else {
                                y2 = y[k];
                            }
                            String deaths = "";
                            if (dr != null) {
                                if (k > 0) {
                                    double de = dr[k] - dr[k - 1];
                                    if (de > 0) deaths = String.format("%.0f", de);
                                }
                            }
                            fw.write(String.format("%.0f %.0f %s\n", x[k] + 1, y2, deaths)); //offset - see input
                        }
                        for (int i = 0; i < x1.length; i++) {
                            String deaths = "";
                            if (x1[i] < x[x.length - 1]) continue;
                            if (dr1 != null) {
                                if (i > 0) {
                                    double de = dr1[i] - dr1[i - 1];
                                    if (de > 0) deaths = String.format("%.0f", de);
                                }
                            }
                            fw.write(String.format("%.0f %.0f %s\n", x1[i] + 1, y1[i], deaths)); //shift of one - see readData
                        }

                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }




               /* if (l == x1.length - 1) { //create an artifical dataset
                    try {
                        FileWriter fw = new FileWriter(new File(input.fileName));
                        double b1 = (y1[1] - y1[0]) / (x1[1] - x1[0]);
                        double xx = y1[0] + b1 * (x[x.length - 1] - x1[0]);
//                        double xx = g1.evaluate(x[x.length - 1]);
                        double yy = y[y.length - 1];
                        double rd = (xx - yy) / yy;
                        for (int i = 0; i < x.length; i++) {
                            String deaths = "";
                            if (dr != null) {
                                if (i > 0) {
                                    double de = dr[i] - dr[i - 1];
                                    if (de > 0) deaths = String.format("%.0f", de);
                                }
                            }
                            double ynew = y[i] * (1. + rd);
                            fw.write(String.format("%.0f %.0f %s\n", x[i] + 1, ynew, deaths)); //shift of one - see readData
                        }
                        for (int i = 0; i < x1.length; i++) {
                            String deaths = "";
                            if (dr1 != null) {
                                if (i > 0) {
                                    double de = dr1[i] - dr1[i - 1];
                                    if (de > 0) deaths = String.format("%.0f", de);
                                }
                            }
                            fw.write(String.format("%.0f %.0f %s\n", x1[i] + 1, y1[i], deaths)); //shift of one - see readData
                        }
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                */
            }
        }
        return m;
    }

    private void createChart(String prefix, LogisticFunc gp, LogisticFunc g, LogisticFunc gp1, LogisticFunc g1, Input input, Input input1, double[] x, double[] y, Model model, double[] x1, double[] y1, int day, boolean pause, int mark) {
        String dateStringLong = getDateStringLong(day);
        int xmax = (int) Math.round(Math.min(x[x.length - 1], day));
        dataset.addSeries("Infections reported", createSeries(x, y, xmax));
        int xmax1 = 0;
        if (input1 != null) {
            xmax1 = (int) Math.round(Math.min(x1[x1.length - 1], day));
            if (x1[0] <= day)
                dataset.addSeries("Infections reported", createSeries(x1, y1, xmax1));
        }
        if (gp != null) {
            dataset.addSeries("Infections previous model", gp.getSeriesData(xmax + 1));
        }
        if (g != null) {
            dataset.addSeries("Infections current model", g.getSeriesData(xmax + 2));
        }
        if (gp1 != null) {
            dataset.addSeries("Infections previous model", gp1.getSeriesData(xmax1 + 1));
        }
        if (g1 != null) {
            dataset.addSeries("Infections current model", g1.getSeriesData(xmax1 + 2));
        }

        double[][] data = input.getData();
        if (data.length > 2) {
            double[] dx = data[0];
            double[] deaths = data[2];
            dataset.addSeries("Deaths reported", createSeries(dx, deaths, xmax));
            if (g != null) {
                double[] dy1d = DeathRate.getDeaths(dx, g, model.getDeathRate(xmax), model.getShift(xmax), model.getP(xmax));
                dataset.addSeries("Deaths current model", createSeries(dx, dy1d, xmax + 2));
            }
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                String.format("Reported cases for %s", input.getName()),
                "Days since 01/20/2020",
                "Number of reported cases",
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false);


        chart.addSubtitle(new TextTitle(dateStringLong)); //chart.setSubtitles(subTitles);
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new Renderer_For_Cumulative(dataset, input, mark));


        adjustPlot(plot, input.getYmax(), TODAY);

        saveChart(chart, prefix, day, false);

        if (pause) {
            for (int i = day + 1; i < day + REPEAT_FRAME; i++) {
                saveChart(chart, prefix, i, false); //several frames to pause the display
            }
        }


    }

    private void adjustPlot(XYPlot plot, double ymax, double xmax) {

        JFreeChart chart = plot.getChart();
        TextTitle tt = chart.getTitle();
        Font tf = tt.getFont();
        TextTitle newTitle = new TextTitle(tt.getText(), tf.deriveFont(Font.BOLD, (int) (tf.getSize() * 1.5)));
        chart.setTitle(newTitle);

        XYItemRenderer renderer = plot.getRenderer();

        BasicStroke stroke = new BasicStroke(2.5f);

        for (int i = 0; i < plot.getSeriesCount(); i++) {
            renderer.setSeriesStroke(i, stroke);
        }

        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setBackgroundPaint(new Color(255, 255, 255, 255));
        plot.setDomainGridlinePaint(new Color(128, 128, 128));
        plot.setRangeGridlinePaint(new Color(128, 128, 128));

        Font font = plot.getDomainAxis().getLabelFont();
        font = font.deriveFont(Font.BOLD, (int) (font.getSize() * 1.2));
        Font font1 = font.deriveFont(Font.BOLD, (int) (font.getSize() * 1.2));

        plot.getDomainAxis().setLabelFont(font);
        plot.getRangeAxis().setLabelFont(font);

        ValueAxis axis = plot.getRangeAxis();
        axis.setLabelFont(font1);
        axis.setTickLabelFont(font);
        axis.setLabelPaint(Color.black);
        axis.setTickLabelPaint(Color.black);
        axis = plot.getDomainAxis();
        axis.setLabelFont(font1);
        axis.setTickLabelFont(font);
        axis.setLabelPaint(Color.black);
        axis.setTickLabelPaint(Color.black);

        font = font.deriveFont(Font.BOLD);
        LegendItemCollection lic = plot.getLegendItems();
        for (int i = 0; i < lic.getItemCount(); i++) {
            LegendItem l = lic.get(i);
            l.setLabelFont(font);
            l.setLabelPaint(Color.black);
        }

        Font fontLarger = font.deriveFont(Font.BOLD, (int) (font.getSize() * 1.2));
        for (int i = 0; i < chart.getSubtitleCount(); i++) {
            Title t = chart.getSubtitle(i);
            if (t instanceof TextTitle) {
                TextTitle tit = (TextTitle) t;
                tit.setFont(fontLarger);
                tit.setPaint(Color.black);
            }
            if (t instanceof LegendTitle) {
                LegendTitle tit = (LegendTitle) t;
                tit.setItemFont(font);
                tit.setItemPaint(Color.black);
            }
        }


        if (Math.abs(ymax) > 0.) {
            plot.getRangeAxis().setRange(new Range(0, ymax));
        }
        plot.getDomainAxis().setRange(new Range(0, xmax));

        //remove legend for series containing max
        LegendItemCollection legend = plot.getLegendItems();
        Iterator iterator = legend.iterator();
        while (iterator.hasNext()) {
            LegendItem item = (LegendItem) iterator.next();
            if (item.getLabel().toLowerCase().contains("maximum"))
                iterator.remove();
        }
        plot.setFixedLegendItems(legend);
    }

    /**
     * Embeds a textual watermark over a source image to produce
     * a watermarked one.
     *
     * @param text            The text to be embedded as watermark.
     * @param sourceImageFile The source image file.
     * @param destImageFile   The output image file.
     */
    private void addTextWatermark(String text, File sourceImageFile, File destImageFile) {
        try {
            BufferedImage sourceImage = ImageIO.read(sourceImageFile);
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.RED);
            g2d.rotate(Math.toRadians(-34.));
            g2d.setFont(new Font("Arial", Font.BOLD, 64));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;

            // paints the textual watermark
            g2d.drawString(text, centerX - (int) rect.getWidth() / 4, centerY + (int) rect.getWidth() / 4);
            g2d.rotate(-Math.toRadians(-34.));

            ImageIO.write(sourceImage, "png", destImageFile);
            g2d.dispose();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveChart(JFreeChart chart, String prefix, int postfix, boolean waterMark) {
        File file = new File(String.format("gr/%s_p_%04d.png", prefix, postfix));
        try {
            ChartUtils.saveChartAsPNG(file, chart, CHART_WIDTH, CHART_HEIGHT);
            if (waterMark) {
                File f2 = new File(file.getName() + ".wm");
                addTextWatermark("Beware: Prediction based on simple model", file, f2);
                Files.copy(f2.toPath(), file.toPath(), REPLACE_EXISTING);
                Files.delete(f2.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double[][] createSeries(double[] x, double[] y, double xmax) {
        int n = find(x, xmax) + 1;
        double[][] data = new double[2][n];
        for (int i = 0; i < n; i++) {
            data[0][i] = x[i];
            data[1][i] = y[i];
        }
        return data;
    }

    private String getDateStringLong(int day) {
        DateFormat df = new SimpleDateFormat("dd_MM");
        Date date = new Date();
        String dateString = df.format(date);
        df = new SimpleDateFormat("MM/dd/Y");
        Calendar c = Calendar.getInstance();
        c.set(2020, Calendar.JANUARY, 20, 12, 0);
        Date start = c.getTime();
        date = new Date(start.getTime() + day * 24 * 60 * 60 * 1000L);
        return df.format(date) + ", day " + ((date.getTime() - start.getTime()) / 1000L / 60 / 60 / 24);
    }

}

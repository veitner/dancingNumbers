package de.vee;

import de.vee.model.DeathRate;
import de.vee.model.Input;
import de.vee.model.LogisticFunc;
import de.vee.model.Model;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;

import java.awt.*;

import static de.vee.model.FunFactory.createFunction;

public class FrameOfRate extends AbstractFrame {
    private double xmax;
    private boolean scale;
    private boolean markLast;

    FrameOfRate(Input input, Model m, int id, double xmax, boolean scale, boolean markLast) {
        super(input, m, id);
        this.xmax = xmax;
        this.scale = scale;
        this.markLast = markLast;
    }

    void doCreateFrames() {
        String prefix = String.format("0%02d3_5", id);

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
//        double[][] xy1 = smooth(dx1, dy1);


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

            DefaultXYDataset dataset = new DefaultXYDataset();

            dataset.addSeries("New infections per time (model)", createSeries(dx, dy, dx[dx.length - 1] + 1));

            addMaximum(dataset, "infections", imax, dx, dy);

            double[][] result = model.getResult();
            double deathRate;// = result[im][4];
            double shift;// = result[im][5];
            deathRate = model.getDeathRate(max);
            shift = model.getShift(max);
            double p = model.getP(max);

            double[] dy1d = DeathRate.getRate(dx, g, deathRate, shift, p);

            dataset.addSeries("deaths per time (model)", createSeries(dx, dy1d, dx[dx.length - 1] + 1));

            addMaximum(dataset, "deaths per time", -1, dx, dy1d);

//            dataset.addSeries("Smoothed infections per time (model)", createSeries(xy1[0], xy1[1], dx1[l]));

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
        duplicateChart(chart, prefix, count, count + REPEAT_FRAME, true);

    }

    private void addMaximum(DefaultXYDataset dataset, String key, int imax, double[] x, double[] y) {
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
}

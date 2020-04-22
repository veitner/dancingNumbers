package de.vee;

import de.vee.model.DeathRate;
import de.vee.model.Input;
import de.vee.model.LogisticFunc;
import de.vee.model.Model;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;

import static de.vee.model.FunFactory.createFunction;

class FrameOfICU extends AbstractFrame {

    FrameOfICU(Input input, Model m, int id) {
        super(input, m, id);
    }

    void createFrames(double xmax) {
        super.createFrames();

        String prefix = String.format("0%02d4_5", id);

        int count = x.length;

        double[][] a = model.getResult();

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
}

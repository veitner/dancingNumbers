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
import org.jfree.data.xy.DefaultXYDataset;

import static de.vee.model.FunFactory.createFunction;

class FrameOfCumulativeData extends AbstractFrame {
    boolean adjustY;
    boolean markLast;

    FrameOfCumulativeData(Input input, Model m, int id, boolean adjustY, boolean markLast) {
        super(input, m, id);
        this.adjustY = adjustY;
        this.markLast = markLast;
    }

    @Override
    void doCreateFrames() {
        String prefix = String.format("0%02d1_1", id);

        int start = model.getStart();
        double[][] a = model.getResult();

        LogisticFunc gp = null;

        for (int j = 0; j < start; j++) {
            if (chartExists(prefix, j)) continue;

            double omax = input.getYmax();
            if (adjustY) {
                if (y[j] < 1000) input.setYMax(1000.);
            }
            createChart(prefix, gp, null, (int) (Math.round(x[j])), false, 100000);
            input.setYMax(omax);
        }

        double[] v0;
        double xmax = 0.;
        int count = x.length;
        int xmaxo = (int) Math.round(x[start]);
        LogisticFunc g = null;
        for (int l = start; l < count; l++) {
            xmax = x[l];


            v0 = a[l];
            if (chartExists(prefix, l)) continue;

            double omax = input.getYmax();
            if (adjustY) {
                if (y[l] < 1000) input.setYMax(1000.);
                else if (y[l] < 10000) input.setYMax(10000.);
                else if (y[l] < 100000) input.setYMax(100000.);
                else if (y[l] < 200000) input.setYMax(200000.);
                else if (y[l] < 500000) input.setYMax(500000.);
                else if (y[l] < 1E6) input.setYMax(1E6);
                else if (y[l] < 1E7) input.setYMax(1E7);
                else if (y[l] < 1E8) input.setYMax(1E8);
            }
            if (g != null) {
                xmaxo += 1;
                while (xmaxo < xmax) {
                    createChart(prefix, gp, g, xmaxo, false, 100000);
                    xmaxo += 1;
                }
            }
            xmaxo = (int) Math.round(xmax);
            g = createFunction(v0, input.getSlice(x[l]), input.getPopulationSize());
            int num = 100000;
            if (markLast) { //begin of a new wave? //todo: calculate the distance to the model and mark
                num = x.length - 3;
            }
            createChart(prefix, gp, g, (int) Math.round(xmax), l == count - 1, num);

            input.setYMax(omax);
            gp = g;





               /* if (l == x1.length - 1) { //create an artifical dataset - "correct" data for china
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

    private void createChart(String prefix, LogisticFunc gp, LogisticFunc g, int day, boolean pause, int mark) {
        String dateStringLong = getDateStringLong(day);
        int xmax = (int) Math.round(Math.min(x[x.length - 1], day));
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Infections reported", createSeries(x, y, xmax));

        if (g != null) {
            dataset.addSeries("Infections current model", g.getSeriesData(xmax + 2));
        }

        if (gp != null) {
            dataset.addSeries("Infections previous model", gp.getSeriesData(xmax + 1));
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
            duplicateChart(chart, prefix, day + 1, day + REPEAT_FRAME, false);
        }


    }
}

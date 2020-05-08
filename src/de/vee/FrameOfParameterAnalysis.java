package de.vee;

import de.vee.model.Input;
import de.vee.model.Model;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;
import java.util.Arrays;

public class FrameOfParameterAnalysis extends AbstractFrame {

    FrameOfParameterAnalysis(Input input, Model m, int id) {
        super(input, m, id);
    }

    @Override
    void doCreateFrames() {
        String prefix = String.format("0%02d2", id);

        double[][] a = model.getResult();
        int start = model.getStart();

//        clearDataset();
        DefaultXYDataset dataset = new DefaultXYDataset();
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

        duplicateChart(chart, prefix, 0, REPEAT_FRAME, false);

    }
}

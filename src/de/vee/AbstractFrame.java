package de.vee;

import de.vee.model.Input;
import de.vee.model.Model;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static de.vee.model.Bisection.find;
import static de.vee.model.ModelFactory.createModel;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class AbstractFrame {
    final static DefaultXYDataset dataset = new DefaultXYDataset();
    final Input input;
    static double TODAY;
    static final int CHART_WIDTH = 1280;//(int) Math.round(1.8 * 640);
    static final int CHART_HEIGHT = 960;//(int) Math.round(1.8 * 480);
    static final int REPEAT_FRAME = 15;
    final int id;
    final double[] x;
    final double[] y;
    final double[] dr;
    final Model model;

    /**
     * @param input the input data
     * @param model if null it will be created
     * @param id    identifier for name of frames
     */
    AbstractFrame(Input input, Model model, int id) {
        this.id = id;
        this.input = input;
        double[][] data = input.getData();
        double today = data[0][data[0].length - 1] + 1;
        TODAY = today + 5 - (int) today % 5;

        x = data[0];
        y = data[1];
        if (data.length > 2) {
            dr = data[2];
        } else {
            dr = null;
        }

        if (model == null) {
            this.model = createModel(x, y, dr, input);
        } else {
            this.model = model;
        }
    }


    void createFrames() {
        System.out.println(input.getTitle() + String.format(": Processing (%s::createFrames) ....", getClass().getName()));
        clearDataset();
    }

    static void clearDataset() {
        for (int i = dataset.getSeriesCount() - 1; i >= 0; i--) {
            dataset.removeSeries(dataset.getSeriesKey(i));
        }
    }

    boolean chartExists(String prefix, int postfix) {
//        File file = new File(String.format("gr/%s_p_%04d.png", prefix, postfix));
//        return !file.exists();
        return false;
    }

    String getDateStringLong(int day) {
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

    double[][] createSeries(double[] x, double[] y, double xmax) {
        int n = find(x, xmax) + 1;
        double[][] data = new double[2][n];
        for (int i = 0; i < n; i++) {
            data[0][i] = x[i];
            data[1][i] = y[i];
        }
        return data;
    }

    void adjustPlot(XYPlot plot, double ymax, double xmax) {

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

    void saveChart(JFreeChart chart, String prefix, int postfix, boolean waterMark) {
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
}

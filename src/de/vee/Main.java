package de.vee;

import de.vee.data.CSVInput;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;

import static de.vee.Info.IMAGES_FOLDER;
import static java.awt.BorderLayout.NORTH;

public class Main extends JFrame {

    public Main() throws HeadlessException {
        super();
        JPanel c = new JPanel(new BorderLayout());
        Border padding = BorderFactory.createEmptyBorder(12, 7, 12, 7);
        c.setBorder(padding);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.BASELINE_LEADING;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridy = 0;
        constraints.gridx = 0;

        JTextArea ta = new JTextArea();

        PrintStream ps = System.out;
        System.setOut(new PrintStream(new StreamCapturer("", new Consumer() {
            @Override
            public void appendText(String text) {
                ta.append(text);
                ta.setCaretPosition(ta.getDocument().getLength());
            }
        }, ps)));


        Map<Integer, double[]> slices = new HashMap<>();
        JTextField tfSubepidemic = new JTextField();

        inputPanel.add(new JLabel("Select Region"), constraints);
        JComboBox<String> cb = new JComboBox<>();
/*
        cb.addItem("Europe");
        cb.addItem("World");
*/
        CSVInput input = new CSVInput();
        try {
            List<String> entries = input.getRegions();
            for (String entry : entries) {
                cb.addItem(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = cb.getSelectedIndex() + 1;
                double[] subepidemics = slices.get(index);
                if (subepidemics != null) {
                    StringBuilder s = new StringBuilder();
                    for (double subepidemic : subepidemics) {
                        int value = (int) subepidemic;
                        s.append(value).append(" ");
                    }
                    tfSubepidemic.setText(s.toString());
                } else {
                    tfSubepidemic.setText("");
                }
            }
        });


        for (int i = 0; i < cb.getItemCount(); i++) {
            int key = i + 1;
            String region = cb.getItemAt(i);
            if (region.toLowerCase().contains("china")) {
                slices.put(key, new double[]{30, 61});
            } else if (region.toLowerCase().contains("south_africa")) {
                slices.put(key, new double[]{72, 88});
            } else if (region.toLowerCase().contains("italy")) {
//                    input = input.withSlice(new double[]{65, 81});
            } else if (region.toLowerCase().contains("iran")) {
                slices.put(key, new double[]{67, 108, 114});
            } else if (region.toLowerCase().contains("states_of_america")) {
                slices.put(key, new double[]{83});
            } else if (region.toLowerCase().contains("german")) {
                slices.put(key, new double[]{98});
            } else if (region.toLowerCase().contains("united_kingdom")) {
                slices.put(key, new double[]{106});
            }
        }
        constraints.gridy += 1;
        inputPanel.add(cb, constraints);

        JButton buttonGo = new JButton("Go");
        buttonGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ta.setText("");
                java.util.Map<Integer, String> regions = new HashMap<>();
                Map<Integer, double[]> delta = new HashMap<>();
                Map<Integer, double[][]> constraints = new HashMap<>();
                Map<Integer, Boolean> inflectionPoint = new HashMap<>();
                Object o = cb.getSelectedItem();
                if (o != null) {
                    int key = cb.getSelectedIndex() + 1;
                    regions.put(key, o.toString());
                    String s = tfSubepidemic.getText();
                    StringTokenizer st = new StringTokenizer(s);
                    int count = st.countTokens();
                    if (count > 0) {
                        double[] values = new double[count];
                        for (int i = 0; i < count; i++) {
                            values[i] = Double.parseDouble(st.nextToken());
                        }
                        slices.put(key, values);
                    } else {
                        slices.remove(key);
                    }
                }
                try {
                    new Info(Frames.CHART_WIDTH, Frames.CHART_HEIGHT).applyTemplatesAndSave(regions);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Frames.process(regions, delta, constraints, inflectionPoint, slices);

            }
        });

        constraints.gridy += 1;
        JLabel lbl = new JLabel("Add sub-epidemic at day");
//        lbl.setToolTipText("for an intro to sub-epidemics watch https://www.youtube.com/watch?v=mugPlpYEYrQ&feature=youtu.be");
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=mugPlpYEYrQ&feature=youtu.be"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lbl.setText("<html>Add <a href=\"\">sub-epidemic</a> at day</html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbl.setText("Add sub-epidemic at day");
            }

        });
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        inputPanel.add(lbl, constraints);

        constraints.gridy += 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tfSubepidemic, constraints);

        constraints.gridx += 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 4;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
/*
        JPanel pnl = new JPanel(new GridLayout(3, 1));
        pnl.add(new Box(BoxLayout.X_AXIS));
        pnl.add(buttonGo);
        pnl.add(new Box(BoxLayout.X_AXIS));
*/
        inputPanel.add(buttonGo, constraints);

        c.add(inputPanel, NORTH);

        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(80, 160));
        sp.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0), new BevelBorder(BevelBorder.LOWERED)));
        c.add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton buttonResult = new JButton("Open output folder");
        buttonResult.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(IMAGES_FOLDER));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        buttonPanel.add(buttonResult);
        JButton buttonCleanUp = new JButton("Cleanup output folder");
        buttonCleanUp.addActionListener(e -> {
            try {
                new Info(Frames.CHART_WIDTH, Frames.CHART_HEIGHT).setupOutputDirectory();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        buttonPanel.add(buttonCleanUp);
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> {
            setVisible(false);
            System.exit(0);
        });
        buttonPanel.add(btnExit);

        c.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(c);
    }

    public interface Consumer {
        public void appendText(String text);
    }

    public class StreamCapturer extends OutputStream {

        private StringBuilder buffer;
        private String prefix;
        private Consumer consumer;
        private PrintStream old;

        public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
            if (!"".equals(prefix)) {
                this.prefix = "[" + prefix + "] ";
            } else {
                this.prefix = "";
            }
            buffer = new StringBuilder(128);
            buffer.append(prefix);
            this.old = old;
            this.consumer = consumer;
        }

        @Override
        public void write(int b) {
            char c = (char) b;
            String value = Character.toString(c);
            buffer.append(value);
            if (value.equals("\n")) {
                consumer.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                buffer.append(prefix);
            }
            old.print(c);
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Main m = new Main();
        try {
            new Info(Frames.CHART_WIDTH, Frames.CHART_HEIGHT).setupOutputDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.setTitle("Dancing Numbers");
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.pack();
        m.setLocationRelativeTo(null);
        m.setVisible(true);
    }
}

package de.vee;

import de.vee.data.CSVInput;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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


        inputPanel.add(new JLabel("Select Region"), constraints);
        JComboBox<String> cb = new JComboBox<>();
        cb.addItem("World");
        cb.addItem("Europe");
        CSVInput input = new CSVInput();
        try {
            List<String> regions = input.getRegions();
            for (String entry : regions) {
                cb.addItem(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                Map<Integer, double[]> slices = new HashMap<>();
                Object o = cb.getSelectedItem();
                if (o != null) {
                    regions.put(cb.getSelectedIndex() + 1, o.toString());
                }
                try {
                    new Info(Frames.CHART_WIDTH, Frames.CHART_HEIGHT).applyTemplatesAndSave(regions);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Frames.process(regions, delta, constraints, inflectionPoint, slices);
                    }
                };
                new Thread(r).start();
            }
        });
/*
//too much effort for nothing ...
        constraints.gridy+=1;
        JLabel label = new JLabel("Slices");
        inputPanel.add(label,constraints);
        constraints.gridy+=1;
        JTextField tf = new JTextField();
        constraints.fill = GridBagConstraints.BOTH;
        inputPanel.add(tf, constraints);
        constraints.fill = GridBagConstraints.NONE;
*/

        constraints.gridx += 1;
        inputPanel.add(buttonGo, constraints);
        c.add(inputPanel, NORTH);

        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(80, 160));
        sp.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0), new BevelBorder(BevelBorder.LOWERED)));
        c.add(sp, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
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

package datan;


import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * A class providing a JFrame with input and output possibilities for Datan examples.
 *
 * @author Siegmund Brandt.
 */
public class DatanFrame extends JFrame {
    JTextArea ta;
    JPanel panel;
    String frametitle, frameheader, filename, filnam;
    boolean openfile;
    public Container c;
    FileOutputStream out; // declare a file output object
    PrintStream p; // declare a print stream object

    /**
     * Creates a JFrame with a scroll pane for output and opens a file for a copy of that output
     *
     * @param frametitle  tile for the frame
     * @param frameheader short header (single line) characterizing the example and displayed at the top of the frame
     * @param filename    name of file with a copy of the output; if blank the file will be named <frametitle>.out.txt
     */
    public DatanFrame(String frametitle, String frameheader, String filename) {
        // super(frametitle);
        this.frametitle = frametitle;
        this.frameheader = frameheader;
        this.filename = filename;
        openfile = true;
        if (filename == "" || filename == " ") {
            filnam = frametitle + ".out.txt";
        } else {
            filnam = filename;
        }
        try {
            out = new FileOutputStream(filnam);
// Connect print stream to the output stream
            p = new PrintStream(out);
        } catch (Exception e) {
            System.err.println("Error opening file");
            System.out.println("Error opening file");
        }
        setTitle(frametitle + " - output is also written on file " + filnam);
        createPanel();
    }

    /**
     * Creates a JFrame with a scroll pane for output
     *
     * @param frametitle  tile for the frame
     * @param frameheader short header (single line) characterizing the example and displayed at the top of the frame
     */
    public DatanFrame(String frametitle, String frameheader) {
        super(frametitle);
        this.frametitle = frametitle;
        this.frameheader = frameheader;
        openfile = false;
        createPanel();
    }

    private void createPanel() {
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        JLabel label = new JLabel();
        label.setForeground(Color.blue);
        label.setText(" \n" + frameheader + "\n ");
        c.add(new JLabel(" "));
        c.add(label);
        c.add(new JLabel(" "));
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        c.add(panel);
        ta = new JTextArea(35, 55);
        Font myFont = new Font("Monospaced", Font.PLAIN, 12);
        ta.setFont(myFont);
        JScrollPane scrollText = new JScrollPane(ta);
        c.add(scrollText);
        pack();
        setLocation(new Point(100, 0));
        setVisible(true);
    }

    /**
     * Adds a JComponent to the Frame which may be used for interactive input or steering of example program
     */
    public void add(JComponent obj) {
        panel.add(Box.createHorizontalGlue());
        panel.add(obj);
        panel.add(Box.createHorizontalGlue());
        pack();
    }

    /**
     * Writes one line of output
     */
    public void writeLine(String str) {
        ta.append(str + "\n");
        if (openfile) {
            try {
                p.println(str);
            } catch (Exception e) {
                System.err.println("Error writing to file");
                System.out.println("Error writing to file");
            }
        }
    }

    /**
     * Writes several line of output
     */
    public void writeLines(String[] str) {
        for (int i = 0; i < str.length; i++) {
            ta.append(str[i] + "\n");
            if (openfile) {
                try {
                    p.println(str[i]);
                } catch (Exception e) {
                    System.err.println("Error writing to file");
                    System.out.println("Error writing to file");
                }
            }
        }
    }

}

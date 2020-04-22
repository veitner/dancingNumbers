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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

class Info {
    private static final int MAX = 9;
    private final int width;
    private final int height;

    private float getTextHeight(Graphics2D g, String string,
                                Font font) {//from  w  w  w.  j av  a  2s.c om
        LineMetrics lm = font.getLineMetrics(string,
                g.getFontRenderContext());
        return lm.getHeight();
    }

    private float getTextWidth(Graphics2D g, String string,
                               Font font) {/*from www  . ja  v a 2  s .c om*/
        return g.getFontMetrics(font).stringWidth(string);
    }

    private int getIndexOfLongestLine(List<String> text) {
        int nm = 0;
        int lo = 0;
        for (int i = 0; i < text.size(); i++) {
            String t = text.get(i);
            if (t.length() > lo) {
                lo = t.length();
                nm = i;
            }
        }
        return nm;
    }

    private void breakLines(List<String> text, Graphics2D g2, int width) {
        while (true) {
            int i = getIndexOfLongestLine(text);
            String t = text.get(i);
            if (getTextWidth(g2, t, g2.getFont()) > width) {
                String t1 = t;
                String t2 = "";
                while (getTextWidth(g2, t1, g2.getFont()) > width) {
                    int n = t1.lastIndexOf(" ");
                    if (n < 0) return;
                    t1 = t.substring(0, n + 1).trim();
                    t2 = t.substring(n).trim();
                }
                text.set(i, t1);
                if (!t2.trim().equals("")) {
                    text.add(i + 1, t2);
                }
            } else return;
        }
    }

    private void saveAsPng(String fileName, List<String> text, boolean center, boolean hilight, boolean small) throws IOException {
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(Color.white);
            g2.fillRect(0, 0, width, height);
            g2.setPaint(Color.black);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (small) {
//                g2.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 24));
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30));
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36));
            }
            if (hilight) {
                g2.setPaint(Color.red);
            }
//            g2.drawRect(0,0,width-1,height-1);
            int left = width / 70;
            int top = left;
//            g2.setFont(g2.getFont().deriveFont(24.f));

            breakLines(text, g2, width - 2 * left);

            int dh = (int) (1.2 * getTextHeight(g2, "Abc", g2.getFont()));
            for (int i = 0; i < text.size(); i++) {
                String t = text.get(i);
                if (!center) {
                    float w = getTextWidth(g2, t, g.getFont());
                    left = (int) (width - w) / 2;
                }
                g2.drawString(t, left, top);
                top += dh;
            }
        }
        ImageIO.write(img, "png", new File(fileName));
    }

    private void duplicate() {
        duplicate("0%02da2_0_0.txt", "0%02d1_0_0.txt");
        duplicate("0%02da3_2_0.txt", "0%02d1_0_0.txt");
        duplicate("0%02da3_3_0.txt", "0%02d1_0_0.txt");
        duplicate("0%02da4_0_0.txt", "0%02d1_0_0.txt");
    }

    private List<String> readText(File f) throws IOException {
        List<String> text = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        while ((line = br.readLine()) != null) {
            text.add(line);
        }
        return text;
    }

    public void recursiveDeleteDirectory(String directory) throws IOException {
        File fdir = new File(directory);
        Path path = fdir.toPath();
        if (fdir.exists()) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        try {
            Thread.sleep(300); //wait a bit to allow the fs to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Path result = Files.createDirectories(path);
            if (!result.toFile().exists()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Files.createDirectories(path);
            }
        } catch (AccessDeniedException ade) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Files.createDirectories(path);

        }

    }

    private void setupOutputDirectory() throws IOException {
        recursiveDeleteDirectory("gr");
        recursiveDeleteDirectory("rep1");
        String[] template = new String[]{
                "0000_0_0.txt",
                "0000_1_0.txt",
                "9999_1_0.txt",
                "9999_2_0.txt",
        };
        for (String tpl : template) {
            File fsrc = new File("rep/" + tpl);
            File ftrg = new File("rep1/" + tpl);
            Files.copy(fsrc.toPath(), ftrg.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void applyTemplate(String region, int id) throws IOException {
        String template = "000a1_0_0.tpl";
        File ftpl = new File("rep/" + template);
        List<String> text = readText(ftpl);
        File ftrg = new File(String.format("rep1/0%02d1_0_0.txt", id));
        FileWriter fw = new FileWriter(ftrg);
        for (String line : text) {
            if (line.contains("{$region}")) {
                line = line.replace("{$region}", region.replaceAll("_", " "));
            }
            fw.write(line + "\n");
        }
        fw.flush();
        fw.close();
    }

    private void duplicate(String what, String test) {
        String src = String.format(what, 0);
        File fsrc = new File("rep/" + src);
        for (int i = 1; i <= 10 * MAX; i++) {
            File ftest = new File("rep1/" + String.format(test, i));
            if (!ftest.exists()) continue;
            String trg = String.format(what, i).replace("a", "");
            File ftrg = new File("rep1/" + trg);
            try {
                Files.copy(fsrc.toPath(), ftrg.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void save() throws IOException {
        duplicate();
        Path path = new File("gr").toPath();
        Files.createDirectories(path);
        path = new File("rep1/out").toPath();
        Files.createDirectories(path);
        saveTextAsPng("0000_0_0", false, false, false);
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                for (int k = 0; k < MAX; k++) {
                    for (int l = 0; l < MAX; l++) {
                        if ((k == 0) && (l == 0)) continue; //title frame
                        String base = String.format("0%d%d%d_%d_0", i, j, k, l);
                        File f = new File("rep1/" + base + ".txt");
                        if (f.exists()) {
                            saveTextAsPng(base, true, l == 2, true);
                        }
                    }
                }
            }
        }
//        saveTextAsPng("9999_0_0", false, false);
        saveTextAsPng("9999_1_0", false, false, false);
        saveTextAsPng("9999_2_0", false, false, true);
    }

    private void saveTextAsPng(String fileName, boolean center, boolean hilight, boolean smaller) throws IOException {
        List<String> text = new ArrayList<>();
        File f = new File("rep1/" + fileName + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        while ((line = br.readLine()) != null) {
            text.add(line);
        }
        saveAsPng("rep1/out/" + fileName + ".png", text, center, hilight, smaller);
        saveAsPng("gr/" + fileName + ".png", text, center, hilight, smaller);
    }

    Info(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void applyTemplatesAndSave(Map<Integer, String> regions) throws IOException {
        setupOutputDirectory();
        for (Integer key : regions.keySet()) {
            String region = regions.get(key);
            applyTemplate(region, key);
        }
        save();
    }

}

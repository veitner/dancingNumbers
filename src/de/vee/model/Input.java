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

package de.vee.model;

import de.vee.data.CSVInput;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class Input implements Cloneable {
    double[] slice = {};
    private String name;
    private String title = "";
    private String suffix = "";
    //china and europe
    //double[] v =new double[]{1.E-5,17,14.E-2,14};
    //double[] dels = {0.1, 0.2, 0.1, 0}; //china and europe

    double[] v = {0.4, 20, 0.05};
    double[] v2 = {0.15, 4, 0.5};
    double[] dels = {0.05, 1, 0.05};
    double[] dels2 = {0.01, 1, 0.02};
    double N;
    private String fileName;
    private Color color = Color.BLACK;
    private Color color1 = Color.GRAY;
    private double ymax = 100000;
    private double[][] data = null;
    boolean withInflectionPoint = false;
    private double[][] constraints = FunFactory.getConstraints();


    private double[][] constraints2 = {
            {1e-3, 0.3}, //percentage_min,percentage_max
            {0.1, 21},//shift_min,shift_max
            {0.05, 3.} //p_min,p_max
    };

    @Override
    protected Input clone() {
        Input copy;
        copy = new Input(name, title, suffix, N).withDelta(dels).withInitial(v);
        copy.fileName = fileName;
        if (data != null) {
            copy.data = new double[data.length][];
            for (int i = 0; i < data.length; i++) {
                copy.data[i] = Arrays.copyOf(data[i], data[i].length);
            }
        }
        return copy;
    }

    public Input(String name, double popSize) {
        this(name, "", "", popSize);
    }

    public Input(String name, String title, double popSize) {
        this(name, title, "", popSize);
    }

    public Input(String name, String title, String suffix, double popSize) {
        super();
        this.name = name;
        this.title = title;
        this.suffix = suffix;
        this.fileName = getName().toLowerCase() + ".dat";
        this.N = popSize;
    }

    public String getName() {
        String n = name;
        if (n.toLowerCase().contains("world")) n = "the " + n;
        if (n.toLowerCase().contains("unite")) n = "the " + n;
        return n + suffix;
    }

    public String getTitle() {
        if (title.length() < 1) return getName();
        return title;
    }

    private String read(String fileName) {
        Locale.setDefault(Locale.ENGLISH);
        StringBuilder data = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
//                line = replace(line);
                if (!line.contains("#")) {
                    if (i != 0) data.append("\n");
                    i++;
                    data.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    private double[][] readData(String fileName) {
        System.out.println("Reading data for " + name + " from file " + fileName);
        String data = read(fileName);
        StringTokenizer st = new StringTokenizer(data);
        int count = 0;
        while (st.hasMoreTokens()) {
            st.nextToken("\n");
            count++;
        }
        st = new StringTokenizer(data);

        double[] x = new double[count];
        double[] y = new double[count];
        double[] dy = new double[count];
        double[] ddy = new double[count];
        int i = 0;
        while (st.hasMoreTokens()) {
            String line = st.nextToken("\n");
            StringTokenizer st1 = new StringTokenizer(line);
            x[i] = Double.valueOf(st1.nextToken(" ")) - 1.;
            y[i] = Double.valueOf(st1.nextToken(" "));
            if (st1.hasMoreTokens())
                ddy[i] = Double.valueOf(st1.nextToken());
            if (st1.hasMoreTokens())
                dy[i] = Double.valueOf(st1.nextToken());
            i++;
        }
        dy[0] = ddy[0];
        double sum = ddy[0];
        for (i = 1; i < x.length; i++) {
            sum += ddy[i];
            if (Math.abs(dy[i] - sum) > 1E-9) {
//                System.out.printf("%.0f: %.0f vs. %.0f, mortality: %.3f\n", x[i], dy[i], sum, sum / y[i]);
                if (dy[i] < sum) {
                    dy[i] = sum;
                }
            }
            System.out.printf("%.0f %.0f %.0f %.1f%%\n", x[i], dy[i], y[i], 100. * dy[i] / y[i]);
        }
        double[][] d = new double[3][];
        d[0] = x;
        d[1] = y;
        d[2] = dy;
        return d;
    }


    public Input withInitial(double[] initial) {
        if (initial.length != v.length) throw new ArrayStoreException("wrong length in 'withInitial'!");
        for (int i = 0; i < initial.length - 1; i++) {
            if (initial[i] < 0) continue;
            v[i] = initial[i];
        }
        return this;
    }

    public Input withInitial2(double[] initial) {
        if (initial.length != v2.length) throw new ArrayStoreException("wrong length in 'withInitial2'!");
        for (int i = 0; i < initial.length - 1; i++) {
            if (initial[i] < 0) continue;
            v2[i] = initial[i];
        }
        return this;
    }

    public Input withDelta(double[] delta) {
        if (delta.length != dels.length) throw new ArrayStoreException("wrong length in 'withDelta'!");
        for (int i = 0; i < delta.length - 1; i++) {
            if (delta[i] < 0) continue;
            dels[i] = delta[i];
        }
        return this;
    }

    public Input withDelta2(double[] delta) {
        if (delta.length != dels2.length) throw new ArrayStoreException("wrong length in 'withDelta2'!");
        for (int i = 0; i < delta.length - 1; i++) {
            if (delta[i] < 0) continue;
            dels2[i] = delta[i];
        }
        return this;
    }

    public Input withConstraints(double[][] constraints) {
        if (constraints.length != this.constraints.length)
            throw new ArrayStoreException("wrong length in 'withConstraints'!");
        for (int i = 0; i < constraints.length - 1; i++) {
            if (constraints[i].length < 1) continue;
            this.constraints[i] = constraints[i];
        }
        return this;

    }

    public Input withConstraints2(double[][] constraints) {
        if (constraints.length != this.constraints2.length)
            throw new ArrayStoreException("wrong length in 'withConstraints2'!");
        for (int i = 0; i < constraints.length - 1; i++) {
            if (constraints[i].length < 1) continue;
            this.constraints2[i] = constraints[i];
        }
        return this;
    }

    public Input withInflectionPoint(boolean b) {
        this.withInflectionPoint = b;
        return this;
    }

    public double[][] getData() {
        if (data == null) data = readData(fileName);
        return data;
    }

    public static Input get(String key, double popSize) {
        return get(key, "", "", popSize, new double[0], new double[0]);
    }

    static Input get(String key, double popSize, double[] initial, double[] delta) {
        return get(key, "", "", popSize, initial, delta);
    }

    static Input get(String key, String title, String suffix, double popSize, double[] initial, double[] delta) {
        Input input;
        try {
            input = new CSVInput().inputFor(key);
        } catch (IOException e) {
            input = new Input(key, title, suffix, popSize);
        }
        if (initial.length >= 4) {
            input.v = initial;
        }
        if (delta.length >= 4) {
            input.dels = delta;
        }
        return input;
    }

    static Input CHINA() {
        Input r = new Input("China", "Synthetic data", 1428E6);
        r.v[0] = 1.E-5;
        r.v[1] = 17;
        r.v[2] = 14.E-2;
        r.v[3] = 14;
        r.dels[0] = 0.1;
        r.dels[1] = 0.2;
        r.dels[2] = 0.1;
        r.dels[3] = 0;
        r.fileName = r.name.toLowerCase() + "_g.dat";
        r.setColor(Color.BLACK);
        r.color1 = Color.gray;
        return r;
    }

    static Input CHINA0() {
        Input r = CHINA();
        r.suffix = "0";
        r.title = "";
        r.fileName = r.getName().toLowerCase() + ".dat";
        return r;
    }

    static Input CHINA1() {
        Input r = Input.CHINA();
        r.suffix = "1";
        r.title = "";
        r.fileName = r.getName().toLowerCase() + ".dat";
        return r;
    }

    public Input withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    Input withSuffix(String suffix) {
        this.suffix = suffix;
        this.fileName = getName().toLowerCase() + ".dat";
        return this;
    }

    public Input nullifyData() {
        this.data = null;
        return this;
    }

    Input withColor(Color color) {
        setColor(color);
        return this;
    }

    private void setColor(Color c) {
        this.color = c;
        this.color1 = c.brighter().brighter().brighter();
    }

    public Input withYMax(double yMax) {
        this.ymax = yMax;
        return this;
    }

    public void setPopulationSize(double size) {
        this.N = size;
    }

    public double getPopulationSize() {
        return this.N;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public Paint getColor() {
        return color;
    }

    public Paint getColor1() {
        return color1;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYMax(double max) {
        this.ymax = max;
    }

    public String getFileName() {
        return fileName;
    }

    public double[][] getConstraints() {
        return constraints;
    }

    public double[][] getConstraintsForDeathRate() {
        return constraints2;
    }

    public Input withSlice(double[] slice) {
        this.slice = slice;
        return this;
    }

    public double[] getSlice(double x) {
        int nslice = 0;
        for (double u : slice) {
            if (x > u + SuperPose.SIGMA) {
                nslice++;
            }
        }
        return Arrays.copyOf(slice, nslice);
    }
}

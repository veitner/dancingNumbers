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

import de.vee.model.Input;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Frames extends AbstractFrame {

    protected Frames(Input input, int id) {
        super(input, null, id);
    }

    private void createFrames(double xmax, boolean adjustY, boolean markLast) {
        FrameOfCumulativeData cd = new FrameOfCumulativeData(input, model, id);
        cd.createFrames(adjustY, markLast);
        FrameOfParameterAnalysis pa = new FrameOfParameterAnalysis(input, model, id);
        pa.createFrames();
        FrameOfRate rate = new FrameOfRate(input, model, id);
        rate.createFrames(Math.max(xmax, TODAY), false, markLast);
        FrameOfICU icu = new FrameOfICU(input, model, id);
        icu.createFrames(Math.max(xmax, TODAY));
        System.out.println("\nDONE!");
    }

    public static void process(Map<Integer, String> regions, Map<Integer, double[]> delta, Map<Integer, double[][]> constraints, Map<Integer, Boolean> inflectionPoint, Map<Integer, double[]> slices) {
        for (Integer key : regions.keySet()) {
            String region = regions.get(key);
            Input input = Input.get(region, 1e6);
            if (delta.containsKey(key)) {
                input = input.withDelta(delta.get(key));
            }
            if (constraints.containsKey(key)) {
                input = input.withConstraints(constraints.get(key));
            }
            if (inflectionPoint.containsKey(key)) {
                input = input.withInflectionPoint(inflectionPoint.get(key));
            }
            if (slices.containsKey(key)) {
                input = input.withSlice(slices.get(key));
            } else {
                if (region.toLowerCase().contains("china")) {
                    input = input.withSlice(new double[]{30, 61});
                } else if (region.toLowerCase().contains("south_africa")) {
                    input = input.withSlice(new double[]{72, 88});
                }
            }
            Frames frame = new Frames(input, key);
            frame.createFrames(360, true, false);
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        Map<Integer, String> regions = new HashMap<>();
        regions.put(97, "World");
//        regions.put(1, "Europe");
//        regions.put(2, "Italy");
//        regions.put(3, "Spain");
//        regions.put(4, "France");
//        regions.put(5, "Germany");
//        regions.put(6, "Poland");
//        regions.put(7, "Austria");
//        regions.put(8, "Switzerland");
//        regions.put(9, "Greece");
//        regions.put(10, "Croatia");
//        regions.put(44, "Brazil");
//        regions.put(43, "United_States_of_America");

        Map<Integer, double[]> delta = new HashMap<>();
        delta.put(1, new double[]{1e-5, 0.5, 0.001});
        delta.put(5, new double[]{0.05, 1, 0.05});
        Map<Integer, double[][]> constraints = new HashMap<>();
        constraints.put(5, new double[][]
                {{1e-7, 0.25},//a0_min,a0_max
                        {1e-6, 5e3}, //a1_min,a1_max
                        {1e-6, 9e-1}, //a2_min,a2_max
                });
        Map<Integer, Boolean> inflectionPoint = new HashMap<>();
        inflectionPoint.put(1, true);
//        inflectionPoint.put(5, true);
        Map<Integer, double[]> slices = new HashMap<>();
//        slices.put(2, new double[]{62, 71, 79});
//        slices.put(2, new double[]{64, 72, 81, 89}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
//        slices.put(2, new double[]{62, 82, 89}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
//        slices.put(2, new double[]{62, 82}); //lockdown on 03/21 and partial release on 04/14 (due to news, but the numbers show a change around 04/10)
//        slices.put(3, new double[]{70, 77});
//        slices.put(4, new double[]{70, 84});
//        slices.put(5, new double[]{/*52, 60,*/ 67, 76, 84});
        slices.put(5, new double[]{/*52, 60,*/ 86}); //90: reopening of shops
//        slices.put(9, new double[]{82});
//        slices.put(43, new double[]{/*52, 60,*/ 76, 84});
        try {
            Info info = new Info(CHART_WIDTH, CHART_HEIGHT);
            info.setupOutputDirectory();
            info.applyTemplatesAndSave(regions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        process(regions, delta, constraints, inflectionPoint, slices);


/*
        Input input = Input.get("China", 1428E6)
                .withDelta(new double[]{0.1, 0.2, 0.1, 0.005, 0.1, 0.2})
                .withInitial(new double[]{1.E-5, 17, 14.E-2, 0.04, 10, 0.1})
                .withFileName("china_g.dat") //china_g is a synthetic dataset - used to test the model at the beginning
                .nullifyData();
        Frames frame = new Frames(input, 0);
        frame.createFrames(input.clone().withSuffix("0").nullifyData(), input.clone().withSuffix("1").nullifyData(), false, false);
        frame.createFrames(TODAY, false, true);
*/
    }


}

package de.vee.model;

public class ModelFactory {
    public static Model createModel(double[] x, double[] y, double[] dr, Input input) {
        return new AmoebaModel(x, y, dr, input);
//        return new LMModel(x, y, dr, input);
    }
}

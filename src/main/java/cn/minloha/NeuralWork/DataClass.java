package cn.minloha.NeuralWork;

import cn.minloha.Type.Matrix;
import cn.minloha.Type.Vector;

import java.text.DecimalFormat;

public class DataClass {

    private final Vector weight;
    private final Double bias;

    public DataClass(Vector weight, double bias) {
        this.weight = weight;
        this.bias = bias;
    }

    public Vector getWeight() {
        return weight;
    }

    public Double getBias() {
        return bias;
    }

    public String toString(){
        return "W:" + weight.toString() +"\n"+"B:"+ bias + "\n";
    }
}

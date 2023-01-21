package cn.minloha.NeuralWork;

import cn.minloha.Type.Matrix;
import cn.minloha.Type.Vector;

import java.util.ArrayList;
import java.util.List;

public class Function {

    public Double Sigmoid(double x){
        return 1/(1+Math.exp((1-x)/4));
    }

    public Vector toOnes(Vector output){
        List<Double> res = new ArrayList<>();double sum = 0;
        for(double j : output.getAsList()) sum += j;
        for(double k : output.getAsList()){
            res.add(k/sum);
        }
        return new Vector(res);
    }

    public Double CovLoss(Vector y1,Vector y2){
        // \frac{1}{2}\sum_i(y2_i-y1_i)^2
        List<Double> temp = new ArrayList<>();
        for(int d = 0;d<y1.getDimens();d++){
            temp.add(y2.getAsList().get(d)-y1.getAsList().get(d));
        }
        Vector a = new Vector(temp);
        Double res = Vector.Multiplicate(a,a)/2;
        return res;
    }
}

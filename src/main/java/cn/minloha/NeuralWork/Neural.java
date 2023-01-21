package cn.minloha.NeuralWork;

import cn.minloha.Type.Matrix;
import cn.minloha.Type.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 神经节点对象
public class Neural {
    private Vector weight;
    private Double bias;
    private Double z;

    private Vector nabla_w; // out/weight
    private Double nabla_b; // out/b
    private Vector nabla_input; // out/x
    private final Double lita;

    private Double sig;

    private final Function itemFunction = new Function();

    public Neural(int inputdim,double lita){
        this.lita = lita; List<Double> a = new ArrayList<>();
        for(int x = 0;x<inputdim;x++){
            a.add(0.1);
        }
        this.weight = new Vector(a);
        this.bias = 0.1;
    }

    public Neural(DataClass d,Double lita){
        this.weight = d.getWeight();
        this.bias = d.getBias();
        this.lita = lita;
    }

    public void changeDataclass(DataClass dataClass){
        this.weight = new Vector();this.bias = 0.0;
        this.weight = dataClass.getWeight();
        this.bias = dataClass.getBias();
    }

    public Double getZ(){
        return this.z;
    }

    public Vector getWeight(){
        return this.weight;
    }

    public Double getBias() {
        return bias;
    }

    public Vector getNabla(){
        return this.nabla_input;
    }

    public DataClass getDataClass(){
        DataClass c = new DataClass(this.weight,this.bias);
        return c;
    }

    public Double forward(Vector in){
        this.z = Vector.Multiplicate(this.weight,in) + this.bias;
        double k = itemFunction.Sigmoid(z);
        this.sig = k * (1-k);
        this.nabla_w = in;
        this.nabla_input = this.weight;
        this.bias = 1.0;
        return k;
    }

    public Double backward_w(Vector delta,boolean code,int t,Vector out,Vector in){
        double de;
        if(code) de = delta.getAsList().get(t) * this.sig;
        else de = this.sig * Vector.Multiplicate(delta,out);
        Vector kk = Vector.Multiplicate(de,in);

        this.weight = Vector.add(this.weight,Vector.Multiplicate(-1*lita,kk));
        return de;
    }

    public void backward_b(Vector delta,boolean code,int t,Vector out){
        double de;
        if(code) de = delta.getAsList().get(t) * this.sig;
        else de = this.sig * Vector.Multiplicate(delta,out);
        this.bias -= de;
    }
}

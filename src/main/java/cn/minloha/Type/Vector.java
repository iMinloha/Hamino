package cn.minloha.Type;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Vector {
    private final List<Double> saveVector = new ArrayList<>();


    public Vector(double ...l){
        for(double num : l){
            saveVector.add(num);
        }
    }

    public Double dot(double k){
        double result = 0;
        for(double db : this.saveVector){
            result += db * k;
        }
        return result;
    }

    public Vector(List<Double> list){
        saveVector.addAll(list);
    }


    public double getDieLength(){
        double len = 0.0;
        synchronized (new ReentrantLock()){
            for(double num : this.saveVector){
                len = num*num + len;
            }
            len = Math.sqrt(len);
        }
        return len;
    }


    public int getDimens(){
        return this.saveVector.size();
    }



    public Vector changeDimens(int dimen) {
        if(dimen-this.saveVector.size()>0){
            for(int d=0;d<dimen-this.saveVector.size();d++){
                this.saveVector.add(0.0);
            }
        }
        return new Vector(this.saveVector);
    }


    public Vector changeDimens(int dimen,double ...num) {
        List<Double> cpyList = this.saveVector;
        int len = this.saveVector.size();
        if(dimen-this.saveVector.size()>0){
            for(int d=0;d<dimen-this.saveVector.size();d++){
                this.saveVector.add(num[d]);
            }
        }
        return new Vector(this.saveVector);
    }


    public static Double Multiplicate(Vector v1,Vector v2) {
        double Mul = 0.0;
        synchronized (new ReentrantLock()) {
            if (v1.getDimens() > v2.getDimens()) {
                v2.changeDimens(v1.getDimens());
                for (int dnum = 0; dnum < v1.getDimens(); dnum++) {
                    Mul += v2.getAsList().get(dnum) * v1.getAsList().get(dnum);
                }
            } else if (v1.getDimens() == v2.getDimens()) {
                for (int dnum = 0; dnum < v1.getDimens(); dnum++) {
                    Mul += v2.getAsList().get(dnum) * v1.getAsList().get(dnum);
                }
            } else {
                v1.changeDimens(v2.getDimens());
                for (int dnum = 0; dnum < v1.getDimens(); dnum++) {
                    Mul += v2.getAsList().get(dnum) * v1.getAsList().get(dnum);
                }
            }
        }
        return Mul;
    }

    public static Vector Multiplicate(double k,Vector v1){
        List<Double> back = new ArrayList<>();
        for(double num : v1.getAsList()){
            back.add(num*k);
        }
        return new Vector(back);
    }

    public Boolean vecCompare(Vector v){
        boolean back = true;
        for(int n=0;n<this.saveVector.size();n++){
            if(this.saveVector.get(n)!=v.getAsList().get(n)){
                back = false;
            }
        }
        return back;
    }

    public static Matrix L_dv_dv(Vector x,Vector y){
        Object[][] m= new Object[y.getDimens()][x.getDimens()];
        for(int d1=0;d1<y.getDimens();d1++){
            for(int d2=0;d2<x.getDimens();d2++){
                m[d1][d2] = 2*y.getAsList().get(d1)-2*x.getAsList().get(d2);
            }
        }
        return new Matrix(m);
    }

    public List<Double> getAsList(){
        return this.saveVector;
    }

    public Matrix getAsMatrix(){
        Object[][] Max = new Object[this.saveVector.size()][1];
        for(int n=0;n<saveVector.size();n++){
            Max[n][0] = saveVector.get(n);
        }
        return new Matrix(Max);
    }

    public static void addElement(Vector vec1,Vector vec2){
        for(Double o : vec2.getAsList()){
            vec1.getAsList().add(o);
        }
    }

    public static Vector add(Vector v1,Vector v2){
        List<Double> back = new ArrayList<>();
        for(int t = 0; t<v1.getDimens();t++){
            back.add(v1.getAsList().get(t)+v2.getAsList().get(t));
        }
        return new Vector(back);
    }

    public Matrix reWrite(int times){
        List<Double> ls = new ArrayList<>();
        for(int oo = 0 ;oo<times;oo++){
            ls.addAll(this.saveVector);
        }
        return new Matrix(ls,this.saveVector.size(),times);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        int clocktime = 0;
        for(double num : this.saveVector){
            clocktime++;
            if(clocktime==this.saveVector.size()){
                DecimalFormat df = new DecimalFormat("######0.0000000000");
                String t = df.format(num);
                stringBuilder.append(t);
            }else {
                DecimalFormat df = new DecimalFormat("######0.0000000000");
                String t = df.format(num);
                stringBuilder.append(t).append(",");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}

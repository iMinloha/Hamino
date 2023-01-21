package cn.minloha.NeuralWork;

import cn.minloha.ModelLoader.ANNmodel;
import cn.minloha.ModelLoader.ModelLoad;
import cn.minloha.Type.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetWork {

    private final Function itemFunction = new Function();
    private Vector nabla_outer;
    private double lita = 0.7;
    private List<Integer> st = new ArrayList<>();

    private class Linear{
        private final List<Neural> neuralList = new ArrayList<>();
        private Vector out;
        private Vector input;


        private Linear(int nums,int inputDimens){
            for(int h = 0;h<nums;h++){
                neuralList.add(new Neural(inputDimens,0.2));
            }
        }

        private Linear(List<Neural> n){
            this.neuralList.addAll(n);
        }

        // 前向传播的同时就把权重矩阵和偏置向量拼接出来
        public Vector forward(Vector in){
            List<Double> res = new ArrayList<>();
            List<Double> nore = new ArrayList<>();
            for(Neural nn : this.neuralList){
                res.add(nn.forward(in));
                nore.add(nn.getZ());
            }
            this.input = in;
            this.out = new Vector(nore);
            return new Vector(res);
        }

        public Vector backward(Vector delta,boolean lastone){
            List<Double> ch = new ArrayList<>();
            // nums k
            for(int k = this.neuralList.size() - 1;k>=0;k--){
                Neural nnn = neuralList.get(k);
                double sb = nnn.backward_w(delta,lastone,k,this.out,this.input);
                nnn.backward_b(delta,lastone,k,this.out);
                ch.add(sb);
            }
            return new Vector(ch);
        }
    }


    // LinkedList进行管理
    private final List<Linear> net = new ArrayList<>();

    public void showNN(){
        System.out.println("net shape is:");
        for(Linear l : net){
            System.out.print(l.neuralList.size() + "\t");
        }
        System.out.println();
        for(Linear l : net){
            System.out.print("(" + l.neuralList.size() + "," + l.neuralList.get(0).getWeight().getAsList().size() + ")\t");
        }
        System.out.println();
    }

    public NetWork(int inputDimens,int ...nums){
        List<Integer> rk = new ArrayList<>();
        rk.add(inputDimens);
        for(int n : nums) rk.add(n);
        for(int k = 0;k<rk.size()-1;k++){
            net.add(new Linear(rk.get(k+1),rk.get(k)));
        }
        st = rk;
    }


    // forward
    public double forward(Vector input,Vector except){
        Vector in = input;
        for(Linear l : this.net){
            in = l.forward(in);
        }
        System.out.println("[Hamino]:train output " + in);
        this.nabla_outer = Vector.add(in,Vector.Multiplicate(-1,except));
        return itemFunction.CovLoss(except,in);
    }

    public Vector forward(Vector input){
        Vector in = input;
        for(Linear l : this.net){
            in = l.forward(in);
        }
        return in;
    }

    // backward
    public void backward(){
        Vector update = this.nabla_outer;
        List<Double> kkk = new ArrayList<>();
        for(int m = net.size()-1; m>=0; m--){
            boolean lastone = (m == net.size()-1);
            update = net.get(m).backward(update,lastone);
        }
    }

    public void saveModel(String FilePath) throws IOException {
        File f = new File(FilePath);
        if(f.exists())f.delete(); f.createNewFile();
        List<DataClass> params = new ArrayList<>();
        for(Linear n : net)
            for(Neural nn : n.neuralList) params.add(nn.getDataClass());
        ModelLoad modelLoad = new ANNmodel(params);
        modelLoad.saveModel(FilePath);
    }

    public void loadModel(String modelPath) throws IOException {
        ModelLoad modelLoad = new ANNmodel(new ArrayList<>());
        modelLoad.LoadingInFile(modelPath);List<Neural> ln = new ArrayList<>();
        List<DataClass> params = (List<DataClass>) modelLoad.getParams();
        for(DataClass dc : params) ln.add(new Neural(dc,this.lita));int times = 0;
        for(Linear l : net)
            for(Neural n : l.neuralList){
                n.changeDataclass(params.get(times));
                times++;
            }
    }
}

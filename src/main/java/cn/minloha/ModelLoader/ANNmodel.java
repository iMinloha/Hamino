package cn.minloha.ModelLoader;

import cn.minloha.NeuralWork.DataClass;
import cn.minloha.Type.Vector;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ANNmodel implements ModelLoad{
    private final List<DataClass> status;

    public ANNmodel(List<DataClass> status){
        this.status = status;
    }

    @Override
    public void LoadingInFile(String Filepath) throws IOException {
        // 使用新的线程完成工作
        synchronized (new ReentrantLock()) {
            // 初始化列表和矩阵宽度
            // 读取文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Filepath));
            String text = bufferedReader.readLine();
            List<Double> m = new ArrayList<>(); double k = 0.0;
            // 读到内容
            while (text != null) {
                String head = text.isEmpty()?" ":text.substring(0,1);
                // get all double numbers
                Pattern pattern = Pattern.compile("([0-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    // add into list
                    if(head.equals("W")){
                        m.add(Double.parseDouble(matcher.group()));
                    }else if(head.equals("B")){
                        k = Double.parseDouble(matcher.group());
                    }
                }
                text = bufferedReader.readLine();
                if(head.equals("}")){
                    status.add(new DataClass(new Vector(m),k));
                    m = new ArrayList<>(); k = 0.0;
                }
            }
            bufferedReader.close();
        }
        System.gc();
    }

    @Override
    public List<?> getParams() {
        return this.status;
    }

    @Override
    public void saveModel(String modelpath) throws IOException {
        File modelFile = new File(modelpath);
        if(!modelFile.exists()) modelFile.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(modelpath));
        for(DataClass m : this.status){
            bufferedWriter.write("{\n");
            bufferedWriter.write(m.toString());
            bufferedWriter.write("}\n");
        }
        bufferedWriter.close();
        System.gc();
    }


    @Test
    public void testModel() throws IOException {
        /*
        Object[][] ma = new Object[][]{{1,2,3},{1,3,2},{3,2,1}};
        Object[][] mb = new Object[][]{{1,2,3},{1,3,2},{3,2,1}};
        Matrix m1 = new Matrix(ma);
        Matrix m2 = new Matrix(mb);
        this.status.add(m1);
        this.status.add(m2);
        saveModel("C:\\Users\\28211\\Desktop\\aaa.dat");
        */
        LoadingInFile("C:\\Users\\28211\\Desktop\\neural.data");
        for(DataClass m : status){
            System.out.println(m);
        }
    }

}

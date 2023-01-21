package cn.minloha.ModelLoader;

import cn.minloha.Type.Vector;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MLmodel implements ModelLoad{
    private final List<Vector> status = new ArrayList<>();

    @Override
    public void LoadingInFile(String filepath) throws IOException {
        synchronized (new ReentrantLock()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
            String text = bufferedReader.readLine();
            while (text != null) {
                List<Double> list = new ArrayList<>();
                Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    list.add(Double.valueOf(matcher.group()));
                }
                if(list.size()>0){
                    Vector vector = new Vector(list);
                    status.add(vector);
                }
                text = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        System.gc();
    }

    @Test
    public void testModel() throws IOException {
        status.add(new Vector(2.3212,3.333,2.5423));
        saveModel("C:\\Users\\28211\\Desktop\\ml.vec");
    }

    @Override
    public List<?> getParams() {
        return this.status;
    }

    @Override
    public void saveModel(String modelpath) throws IOException {
        File file = new File(modelpath);
        if(!file.exists()) file.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("{" + "\n");
        for (Vector vector : status) {
            bufferedWriter.write(vector.toString() + "\n");
        }
        bufferedWriter.write("}");
        bufferedWriter.close();
    }
}

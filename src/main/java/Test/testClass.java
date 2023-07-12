package Test;

import cn.minloha.NeuralWork.NetWork;
import org.junit.Test;

import java.io.IOException;

public class testClass {
    @Test
    public void test() throws IOException {
        NetWork netWork = new NetWork(6,11,9,8,6,4,3);
        netWork.showNN();
        netWork.loadModel("C:\\Users\\28211\\Desktop\\normal.data");
        netWork.showNN();
    }
}

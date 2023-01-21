package cn.minloha.ModelLoader;

import cn.minloha.Type.Matrix;

import java.io.IOException;
import java.util.List;

public interface ModelLoad {
    public void LoadingInFile(String Filepath) throws IOException;
    public List<?> getParams();
    public void saveModel(String modelpath) throws IOException;
}

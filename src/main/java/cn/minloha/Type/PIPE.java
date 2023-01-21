package cn.minloha.Type;

import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.List;

public class PIPE {
    private static PIPE instance = new PIPE();
    private PIPE(){};
    private Boolean saveModel;
    private List<Zombie> enList;

    private List<String> pipe = new ArrayList<>();

    public static PIPE getInstance(){
        return instance;
    }

    public void setPipe(List<String> pipe){
        this.pipe = pipe;
    }

    public List<String> getPipe() {
        return pipe;
    }

    public List<Zombie> getEnList(){
        return this.enList;
    }

    public void setEnList(List<Zombie> enList) {
        this.enList = enList;
    }

    public Boolean getSaveModel() {
        return saveModel;
    }

    public void setSaveModel(Boolean saveModel) {
        this.saveModel = saveModel;
    }
}

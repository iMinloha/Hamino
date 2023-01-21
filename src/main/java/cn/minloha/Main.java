package cn.minloha;

import cn.minloha.GetCommander.Train;
import cn.minloha.GetEvent.PVPEvent;
import cn.minloha.Type.PIPE;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class Main extends JavaPlugin implements Listener{

    @Override
    public void onLoad(){
        this.getLogger().info("Server\"" + this.getServer().getServerId() + "\" is runing");
        FileConfiguration configuration = this.getConfig();
    }

    @Override
    public void onEnable(){
        saveDefaultConfig();
        this.getLogger().info("Load all algorithm");
        // 注册事件
        Bukkit.getPluginManager().registerEvents(new PVPEvent(), this);
        getCommand("hamino").setExecutor(new Train());
        reloadConfig();
        this.getLogger().info("Load configuration");
    }

    @Override
    public void onDisable(){
        List<Zombie> zl = PIPE.getInstance().getEnList();
        if(!zl.isEmpty())
            for(Zombie z : zl){
                System.out.println("[Hamino]:kill test target->" + z.getName());
                z.remove();
            }
        System.gc();
        this.getLogger().info("Kill all entity target");
        this.getLogger().info("Disable plugin");
    }
}
package cn.minloha.GetEvent;

import cn.minloha.Main;
import cn.minloha.NeuralWork.NetWork;
import cn.minloha.Type.Vector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MonitorEvent implements Listener {

    NetWork net = new NetWork(6,11,9,8,6,4,3);

    private List<String> statues = new ArrayList<>();

    private Boolean isNormal;

    private void InitModels(){
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        String filepath = Main.getPlugin(Main.class).getDataFolder().getAbsolutePath() + "\\";
        List<String> filelist = (List<String>) config.getList("models");
        isNormal = new File(filepath + filelist.get(0)).exists();
        for(String name : filelist){
            if((new File(filepath + name)).exists()){
                statues.add(filepath + name);
            }
        }
    }

    @EventHandler
    public void DamagePlayer(EntityDamageByEntityEvent event) throws IOException {
        FileConfiguration config = Main.getPlugin(Main.class).getConfig(); InitModels();
        String filepath = Main.getPlugin(Main.class).getDataFolder().getAbsolutePath() + "\\";

        if (event.getEntity().getType() == EntityType.PLAYER &&
                event.getDamager().getType() == EntityType.PLAYER){
            // 玩家和玩家PVP
            Player p1 = (Player) event.getEntity();  // M
            Player p2 = (Player) event.getDamager(); // S

            Location p1_lo = p1.getLocation();
            Location p2_lo = p2.getLocation();

            List<Double> re = new ArrayList<>();
            Location a_lo = p2.getTargetBlock((Set<Material>) null, 10).getLocation();

            re.add(p1_lo.getX() - p2_lo.getX());
            re.add(a_lo.getX() / 2);
            re.add(p1_lo.getY() - p2_lo.getY());
            re.add(a_lo.getY() / 2);
            re.add(p1_lo.getZ() - p2_lo.getZ());
            re.add(a_lo.getZ() / 2);

            // load different type models
            // we need loss function
            if(isNormal) {
                net.loadModel(this.statues.get(0));

                // (1,0,0)
                Vector ec = Vector.setOne(0,config.getList("models").size());
                double resss  = 1 - net.forward(new Vector(re),ec);
                if(resss > 0 && resss < 1 && resss > config.getDouble("confidence"))
                    p2.sendMessage("[Hamino]:" + "In the calculation, you are [" + config.getList("categories").get(0) + "]");
                else{
                    for(int k = 1;k<this.statues.size()-1;k++) {
                        net.loadModel(this.statues.get(k));
                        // (0,...,1,...0)
                        Vector ex = Vector.setOne(k,config.getList("models").size());
                        System.out.println("ex" + ex);
                        resss  = 1 - net.forward(new Vector(re),ex);
                        if(resss > 0 && resss < 1 && resss > config.getDouble("confidence"))
                            p2.sendMessage("[Hamino]:" + "In the calculation, you are [" + config.getList("categories").get(k) + "]");
                    }
                }
                System.out.println(resss);
            }
            else Bukkit.getLogger().info("Not have normal model!");
        }
    }
}

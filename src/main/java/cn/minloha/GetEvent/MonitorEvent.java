package cn.minloha.GetEvent;

import cn.minloha.Main;
import cn.minloha.NeuralWork.NetWork;
import cn.minloha.Type.Vector;
import org.bukkit.Bukkit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MonitorEvent implements Listener {

    NetWork net = new NetWork(6,11,9,8,6,4,3);

    private String statues;

    private Boolean isNormal;

    private void InitModels(){
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        String filepath = Main.getPlugin(Main.class).getDataFolder().getAbsolutePath() + "\\";
        statues = filepath + config.getString("models");
        isNormal = (new File(statues)).exists();
    }

    @EventHandler
    public void DamagePlayer(EntityDamageByEntityEvent event) throws IOException {
        FileConfiguration config = Main.getPlugin(Main.class).getConfig(); InitModels();

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

            // Calculate with files available
            if(isNormal) {
                net.loadModel(this.statues);
                // Calculate the group, just use a neural network to calculate it
                Vector exp = net.forward(new Vector(re));
                for(int m = 0;m<exp.getAsList().size();m++){
                    if(exp.getAsList().get(m) > config.getDouble("confidence"))
                        p2.sendMessage("[Hamino]:" + "In the calculation, you are [" + config.getList("categories").get(m) + "]");
                }
                System.out.println(exp);
            }
            else Bukkit.getLogger().info("Not have model!");
        }
    }
}

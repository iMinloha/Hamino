package cn.minloha.GetEvent;

import cn.minloha.Main;
import cn.minloha.NeuralWork.NetWork;
import cn.minloha.Type.PIPE;
import cn.minloha.Type.Vector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PVPEvent implements Listener {
    NetWork net = new NetWork(6,11,9,8,6,4,3);

    private PIPE pipe = PIPE.getInstance();
    private boolean one = true;

    @EventHandler
    public void DamageTarget(EntityDamageByEntityEvent event) throws IOException {
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();
        String filepath = Main.getPlugin(Main.class).getDataFolder().getAbsolutePath() + "\\";

        if(event.getEntity().getName().contains("Target") &&
                event.getEntity().getType().equals(EntityType.ZOMBIE)){
            event.setDamage(0);
            if(event.getDamager().getType() == EntityType.PLAYER) {
                Player player = (Player) event.getDamager();
                Entity monster = event.getEntity();
                int kick = 0;
                // test status PVP
                Location m_location = event.getEntity().getLocation();
                Location p_location = player.getLocation();
                List<Double> differ = new ArrayList<>();
                Location fouce = player.getTargetBlock((Set<Material>) null, 10).getLocation();

                // ====================
                differ.add(m_location.getX() - p_location.getX());
                differ.add(fouce.getX() - p_location.getX()/2);
                differ.add(m_location.getY() - p_location.getY());
                differ.add(fouce.getY() - p_location.getY()/2);
                differ.add(m_location.getZ() - p_location.getZ());
                differ.add(fouce.getZ() - p_location.getZ()/2);

                // Neural network
                List<Double> e = new ArrayList<>();
                for(String k : (List<String>) config.getList("categories")){
                    if(k.equals(pipe.getPipe().get(1))) e.add(1.0);
                    else e.add(0.0);
                }
                Vector except = new Vector(e);

                List<String> modelList = (List<String>) config.getList("models"); int index = 0;
                for(String o : modelList){
                    if(except.getAsList().get(index) == 1) { filepath += o;break; }
                    index++;
                }
                if(new File(filepath).exists() && one){net.loadModel(filepath); one = false;}

                double accessy = 1-net.forward(new Vector(differ), except);
                Player admin = Bukkit.getPlayer(pipe.getPipe().get(0));
                admin.sendMessage("{" + monster.getName() + "-> " + player.getName() + "} accuracy:" + accessy);
                net.backward();
                if(kick % 10 == 0) net.saveModel(filepath);
                kick++;
            }
        }
    }

    private void addData(Double s) throws IOException {
        String filepath = "C:\\Users\\28211\\Desktop\\re.txt";
        BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(filepath, true))));
        bufferedWriter.write(String.valueOf(s));
        bufferedWriter.write("\n");
        bufferedWriter.close();
    }
}

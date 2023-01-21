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
        } else if (event.getEntity().getType() == EntityType.PLAYER &&
                event.getDamager().getType() == EntityType.PLAYER){
            // 玩家和玩家PVP
            Player p1 = (Player) event.getEntity();  // M
            Player p2 = (Player) event.getDamager(); // S

            Location p1_lo = p1.getLocation();
            Location p2_lo = p2.getLocation();

            List<Double> re = new ArrayList<>();

            Location a_lo = p2.getTargetBlock((Set<Material>) null, 10).getLocation();

            re.add(p1_lo.getX() - p2_lo.getX());
            re.add(a_lo.getX() / 10);
            re.add(p1_lo.getY() - p2_lo.getY());
            re.add(a_lo.getY() / 10);
            re.add(p1_lo.getZ() - p2_lo.getZ());
            re.add(a_lo.getZ() / 10);

            List<Double> belief = net.forward(new Vector(re)).getAsList();

            int index = 0;double temp = 0.0;
            for(int m = 0;m<belief.size();m++){
                if(belief.get(m)>temp) {temp = belief.get(m);index=m;}
            }
            p2.sendMessage(ChatColor.RED + "[Hamino]:you are " + config.getList("categories").get(index).toString() + ",Similarity:" + belief.get(index));
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

package cn.minloha.GetCommander;

import cn.minloha.Main;
import cn.minloha.Type.PIPE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Train implements CommandExecutor{

    private final List<Zombie> eneityList = new ArrayList<>();
    public List<Zombie> getEneityList() {
        return eneityList;
    }

    public void sendHelpList(Player player){
        player.sendMessage(ChatColor.AQUA + "----------->[" + ChatColor.RED + "Hamino" + ChatColor.AQUA + "]<-----------");
        player.sendMessage("\tHamino is a PVP anti cheating using computational neural network. You can use relevant instructions to train your model!");
        player.sendMessage("parameter list:");
        player.sendMessage("\t help : get help");
        player.sendMessage("\t train : training parameters");
        player.sendMessage("\t test : test model");
    }

    public void sendTrainHelp(Player player){
        player.sendMessage(ChatColor.AQUA + "----------->[" + ChatColor.RED + "Hamino" + ChatColor.AQUA + "]<-----------");
        player.sendMessage("\tHamino is a PVP anti cheating using computational neural network. You can use relevant instructions to train your model!");
        player.sendMessage("parameter list:");
        player.sendMessage("\tstart categories: generate dummy training and save grouping");
        player.sendMessage("\tstop: stop training");
        player.sendMessage("\tplayername1 playername2: observe the pvp of two players and train them");
    }

    public void summonZombie(Player player){
        PIPE pipe = PIPE.getInstance();
        pipe.setSaveModel(false);
        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation().add(new Vector(3,0,3)), EntityType.ZOMBIE);
        zombie.setHealth(20);
        Random random = new Random();
        String name = "Target" + random.nextInt();
        zombie.setCustomName(name);
        zombie.setCustomNameVisible(true);
        zombie.setBaby(false);
        zombie.setCanPickupItems(true);
        // 延长燃烧时间
        zombie.setFireTicks(0);

        ItemStack head = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack body = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta itemMeta = head.getItemMeta();

        itemMeta.addEnchant(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"),20,true);
        head.setItemMeta(itemMeta); body.setItemMeta(itemMeta);
        player.getWorld().dropItem(player.getLocation().add(new Vector(3,1,3)), head);
        player.getWorld().dropItem(player.getLocation().add(new Vector(3,1,3)), body);
        eneityList.add(zombie);
        pipe.setEnList(eneityList);
    }

    public void killAllZombie(Player player){
        PIPE pipe = PIPE.getInstance();
        pipe.setSaveModel(true);
        if(eneityList.size() == 0){
            player.sendMessage("[Hamino]:No target to kill");
        }else {
            for (int index = 0; index < eneityList.size(); index++) {
                eneityList.get(index).remove();
            }
            eneityList.removeAll(eneityList);
            player.sendMessage("[Hamino]: Training has been stopped to generate a model for you");
        }
    }

    public void alertStart(Player player){
        player.sendMessage(ChatColor.AQUA + "----------->[" + ChatColor.RED + "Hamino" + ChatColor.AQUA + "]<-----------");
        player.sendMessage("\tHamino is a PVP anti cheating using computational neural network. You can use relevant instructions to train your model!");
        player.sendMessage("parameter list:");
        player.sendMessage("\t start categories(only one category): Training according to the grouping of configuration files");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = Main.getPlugin(Main.class).getConfig();

        if(sender instanceof Player){
            Player adminstor = (Player) sender;
            if(args.length>0)
                switch (args[0]){
                    case "train": {
                        if(args.length>1) switch (args[1]){
                            case "start": {
                                if (args.length == 3) {
                                    List<String> l = new ArrayList<>();
                                    l.add(adminstor.getName());
                                    l.add(args[2]);
                                    PIPE pipe = PIPE.getInstance();
                                    pipe.setPipe(l);
                                    summonZombie(adminstor); break;
                                } else {
                                    alertStart(adminstor);
                                }}
                            case "stop": killAllZombie(adminstor);break;
                            default: sendTrainHelp(adminstor);break;
                        }
                        else sendTrainHelp(adminstor);break;
                    }
                    case "help": sendHelpList(adminstor);break;
                    case "test": break;
                    default: sendHelpList(adminstor);break;
                }
            else sendHelpList(adminstor);
        }else {
            System.out.println("[Hamino]:this command only client can run");
        }
        return false;
    }

}

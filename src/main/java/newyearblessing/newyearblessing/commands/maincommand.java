package newyearblessing.newyearblessing.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class maincommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        Plugin plugin = newyearblessing.newyearblessing.NewyearBlessing.getProvidingPlugin(newyearblessing.newyearblessing.NewyearBlessing.class);
        String messagefront = plugin.getConfig().getString("message-front");
        String messagenohd = plugin.getConfig().getString("message-nohd");
        
        if(strings.length == 0)
        {
            String messageerror = plugin.getConfig().getString("message-error");
            commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.RED+messageerror);
            return false;
        }
        if(strings.length == 2)
        {
            String Message0 = strings[0];
            /*写入祝福*/
            if (Message0.equals("add"))
            {
                /*检查月*/
                int month = plugin.getConfig().getInt("use-month");
                Calendar calendar = Calendar.getInstance();
                int month_now = calendar.get(Calendar.MONTH) + 1;
                if(month!=month_now)
                {
                    if (month!=0) {
                        String messagetime = plugin.getConfig().getString("message-notime");
                        commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messagetime);
                        return false;
                    }
                }

                /*更新文件*/
                Plugin plugin2 = newyearblessing.newyearblessing.NewyearBlessing.getPlugin(newyearblessing.newyearblessing.NewyearBlessing.class);
                String Message1 = strings[1];
                String player = commandSender.getName();
                List<String> blessing = plugin.getConfig().getStringList("Blessing");
                for (String command_ : blessing) {
                    if(command_.equals(player)){
                        String messagealready = plugin.getConfig().getString("message-already");
                        commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.RED+messagealready);
                        return false;
                    }
                }
                blessing.add(Message1+" §7——"+player);
                blessing.add(player);
                plugin.getConfig().set("Blessing", blessing);
                plugin.saveConfig();
                String messageup = plugin.getConfig().getString("message-upload");
                commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.GREEN+messageup);
                /*给予奖励*/
                String reward=plugin.getConfig().getString("reward");
                Player players=(Player) commandSender;
                reward = PlaceholderAPI.setPlaceholders(players,reward);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),reward);
                /*更新hd*/
                if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                    commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messagenohd);
                    
                    return false;
                }
                if(!plugin.getConfig().getLocation("local").equals("null")) {
                    HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin2); // The API instance for your plugin
                    api.deleteHolograms();
                    api.deleteHolograms();
                    Hologram hologram = api.createHologram(plugin.getConfig().getLocation("local"));
                    TextHologramLine textLine;
                    int i = 0;
                    for (String command_ : blessing) {
                        i++;
                        if (i == 1) textLine = hologram.getLines().appendText(command_);
                        if (i == 2) i = 0;
                    }
                }
                return false;
            }
            /*查看单个祝福*/
            else if(Message0.equals("looknumber"))
            {
                String Message1 = strings[1];
                int i = Integer.parseInt( Message1 );
                String messagedi = plugin.getConfig().getString("message-di");
                String messageis = plugin.getConfig().getString("message-is");
                commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.GREEN + messagedi+Message1+messageis);
                List<String> blessing = plugin.getConfig().getStringList("Blessing");
                commandSender.sendMessage(blessing.get(i*2-2));
            }
            else
            {
                String messageerror = plugin.getConfig().getString("message-error");
                commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.RED+messageerror);
            }
        }
        else if(strings.length == 1)
        {
            /*查看全部祝福*/
            String Message0 = strings[0];
            if (Message0.equals("list"))
            {
                String messagelist = plugin.getConfig().getString("message-list");
                commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.GREEN+messagelist);
                List<String> blessing = plugin.getConfig().getStringList("Blessing");
                int i=0;
                for (String command_ : blessing)
                {

                    i++;
                    if(i==1) commandSender.sendMessage(command_);
                    if(i==2) i=0;
                }
            }
            /*重载插件*/
            else if(Message0.equals("reload"))
            {
                if (commandSender.hasPermission("newyearblessing.commands.reload"))
                {
                    plugin.reloadConfig();
                    String messagereload = plugin.getConfig().getString("message-reload");
                    commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.GREEN + messagereload);
                }
                else {
                    String messageno = plugin.getConfig().getString("message-no");
                    commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messageno);
                }
            }
            /*创建hd*/
            else if(Message0.equals("createhd"))
            {
                if (commandSender.hasPermission("newyearblessing.commands.createhd")) {
                    if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                        commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messagenohd);
                        return false;
                    }
                    Plugin plugin2 = newyearblessing.newyearblessing.NewyearBlessing.getPlugin(newyearblessing.newyearblessing.NewyearBlessing.class);
                    Player player = (Player) commandSender;
                    Location where =  player.getLocation();// The location where the hologram will be placed
                    HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin2); // The API instance for your plugin
                    api.deleteHolograms();
                    Hologram hologram = api.createHologram(where);

                    List<String> blessing = plugin.getConfig().getStringList("Blessing");
                    TextHologramLine textLine;
                    int i=0;
                    for (String command_ : blessing)
                    {
                        i++;
                        if(i==1)  textLine = hologram.getLines().appendText(command_);
                        if(i==2) i=0;
                    }

                    plugin.getConfig().set("local",where);
                    plugin.saveConfig();
                }
            }
            /*删除hd*/
            else if(Message0.equals("deletehd"))
            {
                if (commandSender.hasPermission("newyearblessing.commands.createhd")) {
                    if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
                        commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messagenohd);
                        return false;
                    }
                    Plugin plugin2 = newyearblessing.newyearblessing.NewyearBlessing.getPlugin(newyearblessing.newyearblessing.NewyearBlessing.class);
                    HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin2); // The API instance for your plugin
                    api.deleteHolograms();
                }
            }
            else if(Message0.equals("lottery"))
            {
                if (!commandSender.hasPermission("newyearblessing.command.lottery")) //权限检查
                {
                    String messageno = plugin.getConfig().getString("message-no");
                    commandSender.sendMessage(ChatColor.YELLOW + messagefront + ChatColor.RED + messageno);
                }
                List<String> blessing = plugin.getConfig().getStringList("Blessing"); //获取许愿列表
                int join = blessing.size(); //获取列表长度
                new BukkitRunnable() { //异步计数器
                    int i = 0; //存储循环次数
                    int j = 1; //用于遍历许愿玩家
                    String get; //用于存储中奖玩家
                    String temp;
                    @Override
                    public void run() {
                        if(i == 1 || i == 2 || i == 3 ) //准备抽的提示
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§a§l准备进行抽奖", "§b正在寻找参与抽奖的玩家");
                            }
                        }
                        if( i == 4 ) //计算参与人数
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§a§l有"+join/2+"人参与了抽奖", "");
                            }
                        }
                        if(i > 4 && i < join / 2 +5) //显示参与抽奖的玩家
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle(blessing.get(j), "§b参与抽奖的玩家是");
                            }
                            j+=2;
                        }
                        if(i == join + 5)  //开始抽奖提示
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l开始抽奖", "");
                            }
                        }
                        if( i > join + 5 && i < join + 25) //开始抽奖
                        {
                            /*随机数生成*/
                            Random r = new Random();
                            r.setSeed(System.currentTimeMillis());
                            int ran = r.nextInt(join/2+1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle(blessing.get(ran*2+1), "§4§l正在抽取玩家"); //title显示
                            }
                            get = blessing.get(ran*2+1); //记录当前随机到的人
                            temp = blessing.get(ran*2);
                        }
                        if(i == join +25) //停止抽取
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle(get, "§4§l正在抽取玩家");
                            }
                        }
                        if(i == join +26 || i == join +27) //显示中奖的玩家
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l中奖的是：", get);
                            }
                        }
                        if(i == join + 28) //q群通知
                        {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l中奖的是：", get+"："+ temp);
                            }
                            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                            Date date = new Date(System.currentTimeMillis());
                            System.out.println(formatter.format(date));
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"qq say 恭喜"+get+"中奖！他的愿望是："+temp+"。请凭此消息截图领取奖品！"+formatter.format(date));
                            cancel();
                            return;
                        }
                        i++;
                    }
                }.runTaskTimer(plugin, 20L, 20L);
            }
            else
            {
                String messageerror = plugin.getConfig().getString("message-error");
                commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.RED+messageerror);
            }
        }
        else
        {
            String messageerror = plugin.getConfig().getString("message-error");
            commandSender.sendMessage(ChatColor.YELLOW+messagefront+ChatColor.RED+messageerror);
        }
        return false;
    }
}

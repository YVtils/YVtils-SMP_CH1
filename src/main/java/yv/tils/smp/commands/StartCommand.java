package yv.tils.smp.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import yv.tils.smp.YVtils;
import yv.tils.smp.placeholder.Prefix;
import yv.tils.smp.internalapi.StringReplacer;
import yv.tils.smp.utils.configs.language.LanguageFile;
import yv.tils.smp.utils.configs.language.LanguageMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.6
 * @version 4.6.6
 * @deprecated
 */
public class StartCommand implements CommandExecutor {

    File file = new File(YVtils.getInstance().getDataFolder(), "DoNotEdit.yml");
    YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (modifyFile.getBoolean("Started")) {
            String args1 = LanguageFile.getMessage(LanguageMessage.SMP_ALREADY_STARTED);
            String s = ChatColor.translateAlternateColorCodes('&', args1);
            sender.sendMessage(s);
        }else {
            modifyFile.set("Started", true);
            try {
                modifyFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("yvtils.smp.play")) {
                    World world = Bukkit.getWorld("world");
                    WorldBorder worldBorder = world.getWorldBorder();
                    Bukkit.setWhitelist(YVtils.getInstance().getConfig().getBoolean("WhitelistafterStart"));
                    player.resetPlayerTime();
                    player.resetPlayerWeather();
                    player.getInventory().clear();
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setLevel(0);
                    player.setExp(0);
                    worldBorder.setWarningDistance(0);
                    worldBorder.setDamageAmount(2);
                    worldBorder.setDamageBuffer(5);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendTitle(YVtils.getInstance().getConfig().getString("StartTitle.Tob"), YVtils.getInstance().getConfig().getString("StartTitle.Bottom"), 20, 50, 20);
                    worldBorder.setSize(YVtils.getInstance().getConfig().getInt("worldborderafterstart"), TimeUnit.SECONDS , YVtils.getInstance().getConfig().getInt("worldbordergrowtime"));

                    List<String> list1 = new ArrayList();
                    List<String> list2 = new ArrayList();
                    list1.add("PREFIXSTART");
                    list2.add(Prefix.PREFIXSTART);

                    player.sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.SMP_START_MESSAGE), list1, list2));
                    Bukkit.getConsoleSender().sendMessage(new StringReplacer().ListReplacer(LanguageFile.getMessage(LanguageMessage.SMP_START_MESSAGE), list1, list2));
                }else {
                    player.kickPlayer("");
                }
            }
        }
        return true;
    }
}
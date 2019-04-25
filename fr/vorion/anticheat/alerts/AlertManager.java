package fr.vorion.anticheat.alerts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.vorion.anticheat.AntiCheat;

public class AlertManager
{

	public AntiCheat plugin;

	public AlertManager(AntiCheat pl)
	{
		plugin = pl;
	}

	public void startAlertChecker()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            public void run()
            {
            	for(Player p : Bukkit.getOnlinePlayers())
            	{
            		if(plugin.sql.getAlerts(p) >= 50 && plugin.sql.getKicks(p) < 3)
            		{
            			p.kickPlayer(plugin.defaultPrefix + plugin.defaultMessageColor + "Veuillez désactiver votre cheat pour jouer sur les serveurs ZK.");
            			int kicks = plugin.sql.getKicks(p) + 1;
            			plugin.sql.setAlerts(p, 0, kicks);
            			plugin.sql.setKicks(p, 0, kicks);
            		}
            		if(plugin.sql.getAlerts(p) >= 50 || plugin.sql.getKicks(p) >= 3)
            		{
            			p.kickPlayer(plugin.defaultPrefix + plugin.defaultMessageColor + "Vous avez été banni pour triche.");
            			plugin.sql.setBanned(p, 0, 0, true);
            			Bukkit.broadcastMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + p.getName() + " a été banni pour triche.");
            			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + p.getName() + " 14day" + " §cCheat");
            		}
            	}
            }

        }, 0L, 100L);
	}

}

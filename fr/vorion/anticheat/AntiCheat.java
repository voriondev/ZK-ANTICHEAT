package fr.vorion.anticheat;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.vorion.anticheat.alerts.AlertManager;
import fr.vorion.anticheat.cheatdetector.CheatManager;
import fr.vorion.anticheat.events.EventManager;
import fr.vorion.anticheat.inventory.InventoryManager;
import fr.vorion.anticheat.sqlite.SQLite;
import fr.vorion.anticheat.utils.Lag;

public class AntiCheat extends JavaPlugin
{

	public AntiCheat plugin;

	public HashMap<Player, Long> onlinePlayer = new HashMap<>();

	public ArrayList<Player> freezedPlayers = new ArrayList<>();

	public ArrayList<Player> onlineMods = new ArrayList<>();

	public InventoryManager invManager = new InventoryManager(this);
	public EventManager eventManager = new EventManager(this);
	public CheatManager cheatManager = new CheatManager(this);
	public AlertManager alertManager = new AlertManager(this);

	public SQLite sql = new SQLite(this);

	public String defaultPrefix = "§c[AntiCheat] ";
	public String defaultMessageColor = "§6";
	public String bypassPermission = "anticheat.bypass";
	public int defaultCpsLimit = 15;

	public void onEnable()
	{
		plugin = this;
		setupWithConfig();
		sql.load();
		eventManager.registerEvents();
		cheatManager.registerCheats();
		alertManager.startAlertChecker();

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);

		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.hasPermission(bypassPermission))
			{
				onlineMods.add(p);
			}
			onlinePlayer.put(p, System.currentTimeMillis());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = (Player)sender;
		/**
		 * VERIF
		 */
		if (cmd.getName().equalsIgnoreCase("verif") && p.hasPermission(bypassPermission))
		{
			if(args.length <= 0)
			{
				p.sendMessage(defaultPrefix + defaultMessageColor + "Utilisation: /verif pseudo");
			}

			else if(args.length >= 1)
			{
				Player target = Bukkit.getPlayer(args[0]);

				if(target != null)
					p.openInventory(invManager.getVerifInventory().getVerifInventory(target));
				else
					p.sendMessage(defaultPrefix + defaultMessageColor + "Ce joueur n'est pas en ligne / n'existe pas !");

			}

			return true;
		}

		/**
		 * ALERT
		 */
		if (cmd.getName().equalsIgnoreCase("alert") && p.hasPermission(bypassPermission))
		{
			if(args.length <= 0)
			{
				p.sendMessage(defaultPrefix + defaultMessageColor + "Utilisation: /alert pseudo");
			}

			else if(args.length >= 1)
			{
				Player target = Bukkit.getPlayer(args[0]);

				if(target != null)
				{
					int kicks = sql.getKicks(target);
					sql.setAlerts(target, sql.getAlerts(target) + 1, kicks);
					p.sendMessage(defaultPrefix + defaultMessageColor + "Une alerte a bien été ajoutée à: " + target.getName());
				}
				else
					p.sendMessage(defaultPrefix + defaultMessageColor + "Ce joueur n'est pas en ligne / n'existe pas !");
			}

			return true;
		}

		/**
		 * FREEZE
		 */
		if (cmd.getName().equalsIgnoreCase("freeze") && p.hasPermission(bypassPermission))
		{
			if(args.length <= 0)
			{
				p.sendMessage(defaultPrefix + defaultMessageColor + "Utilisation: /freeze pseudo");
			}

			else if(args.length >= 1)
			{
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null)
				{
					if(!freezedPlayers.contains(target))
					{
						freezedPlayers.add(target);
						p.sendMessage("§6Vous avez freeze §6" + target.getName());
					}
					else
					{
						freezedPlayers.remove(target);
						p.sendMessage("§6Vous avez unfreeze §6" + target.getName());
					}
				}
				else
					p.sendMessage(defaultPrefix + defaultMessageColor + "Ce joueur n'est pas en ligne / n'existe pas !");
			}

			return true;
		}

		return false;
	}

	/**
	 * CONFIG
	 */

	public void setupWithConfig()
	{
		loadConfiguration();
		defaultPrefix = (String) getConfig().get("prefix");
		defaultMessageColor = (String) getConfig().get("color");
		bypassPermission = (String) getConfig().get("bypassPermission");
		defaultCpsLimit = getConfig().getInt("cpsLimit");
	}

	public void loadConfiguration()
	{
		 createDefaultConfiguration();
	     getConfig().options().copyDefaults(true);
	     saveConfig();
	}

	public void createDefaultConfiguration()
	{
	     String prefixPath = "prefix";
	     getConfig().addDefault(prefixPath, defaultPrefix);

	     String messageColorPath = "color";
	     getConfig().addDefault(messageColorPath, defaultMessageColor);

	     String bypassPermPath = "bypassPermission";
	     getConfig().addDefault(bypassPermPath, bypassPermission);

	     String cpsLimitPath = "cpsLimit";
	     getConfig().addDefault(cpsLimitPath, defaultCpsLimit);
	}

}

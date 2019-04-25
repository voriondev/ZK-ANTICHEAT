package fr.vorion.anticheat.cheatdetector;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import fr.vorion.anticheat.AntiCheat;

public class FastBowDetector implements Listener {

	public AntiCheat plugin;

	public ArrayList<Player> cooldownedPlayers = new ArrayList<>();

	public FastBowDetector(AntiCheat pl)
	{
		plugin = pl;
	}

	@EventHandler
	public void onArrowShoot(EntityShootBowEvent event)
	{
		if ((event.getEntity() instanceof Player))
		{
			final Player player = (Player) event.getEntity();
			if (event.getForce() == 1.0D) {
				if (this.cooldownedPlayers.contains(player))
				{
					event.setCancelled(true);

					if (!plugin.onlineMods.isEmpty())
					{
						for (Player mod : plugin.onlineMods)
						{
							mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + player.getName()
									+ " §eest suspecté d'utiliser un FastBow.");
						}
					}

					int alerts = plugin.sql.getAlerts(player);
					int kicks = plugin.sql.getKicks(player);
					plugin.sql.setAlerts(player, alerts + 5, kicks);

					return;
				}

				if (!this.cooldownedPlayers.contains(player))
				{
					this.cooldownedPlayers.add(player);
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run() {
						cooldownedPlayers.remove(player);
					}
				}, 20L);
			}
		}
	}

}

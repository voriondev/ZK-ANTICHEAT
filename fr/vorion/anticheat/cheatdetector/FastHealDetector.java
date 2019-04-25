package fr.vorion.anticheat.cheatdetector;

import java.util.HashMap;

import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import fr.vorion.anticheat.AntiCheat;

public class FastHealDetector implements Listener
{

	public AntiCheat plugin;

	public HashMap<Player, Long> lastRegenTime = new HashMap<>();

	public FastHealDetector(AntiCheat pl)
	{
		plugin = pl;
	}

	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent e)
	{
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if ((p.hasPermission(plugin.bypassPermission)) || (e.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED))
		{
			return;
		}
		if(lastRegenTime.get(p) == null)
			return;
		if (lastRegenTime.get(p) == 0L)
		{
			lastRegenTime.put(p, System.currentTimeMillis());
			return;
		}
		if (p.getPlayer().getWorld().getDifficulty() == Difficulty.PEACEFUL) {
			return;
		}
		if (System.currentTimeMillis() - lastRegenTime.get(p) < 500L)
		{
			e.setCancelled(true);
			if (!plugin.onlineMods.isEmpty()) {
				for (Player mod : plugin.onlineMods) {
					mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + p.getName()
							+ " §eest suspecté d'utiliser un FastHeal.");
				}
			}

			int alerts = plugin.sql.getAlerts(p);
			int kicks = plugin.sql.getKicks(p);
			plugin.sql.setAlerts(p, alerts + 5, kicks);
		}
		lastRegenTime.put(p, System.currentTimeMillis());
	}
}

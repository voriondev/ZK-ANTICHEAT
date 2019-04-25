package fr.vorion.anticheat.cheatdetector;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

import fr.vorion.anticheat.AntiCheat;

public class AntiKnockbackDetector implements Listener {

	public AntiCheat plugin;

	public ArrayList<Player> cooldownedPlayers = new ArrayList<>();

	public AntiKnockbackDetector(AntiCheat pl)
	{
		plugin = pl;
	}

	@EventHandler
	public void onVelocityChange(final PlayerVelocityEvent e)
	{
		final Player p = e.getPlayer();

		if ((p.getLocation().getBlock().getRelative(-1, 0, 0).getType() != Material.AIR)
				|| (p.getLocation().getBlock().getRelative(1, 0, 0).getType() != Material.AIR)
				|| (p.getLocation().getBlock().getRelative(0, 0, -1).getType() != Material.AIR)
				|| (p.getLocation().getBlock().getRelative(0, 0, 1).getType() != Material.AIR)) {
			return;
		}
		final Location hit = p.getLocation();
		if (canBypass(p)) {
			return;
		}
		if (p.hasPermission(plugin.bypassPermission)) {
			return;
		}
		double force = Math.abs(e.getVelocity().getX() + e.getVelocity().getY() + e.getVelocity().getZ());
		if ((force >= 0.01D) || (e.getVelocity().getX() >= 0.01D) || (e.getVelocity().getZ() >= 0.01D)) {
			return;
		}
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				if ((p == null) || (!p.isValid()))
				{
					return;
				}
				if (p.getLocation().distanceSquared(hit) < 1.0E-4D) {
					e.setCancelled(true);
					if (!plugin.onlineMods.isEmpty())
					{
						for (Player mod : plugin.onlineMods)
						{
							mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + p.getName()
									+ " §eest suspecté d'utiliser un AntiKnockback. (Ping: " + ((CraftPlayer) p).getHandle().ping + " ms)");
						}
					}

					int alerts = plugin.sql.getAlerts(p);
					int kicks = plugin.sql.getKicks(p);
					plugin.sql.setAlerts(p, alerts + 5, kicks);
				}
			}
		}, 3L);
	}

	private boolean canBypass(Player p) {
		Boolean result = Boolean.valueOf(false);
		int ping = ((CraftPlayer) p).getHandle().ping;
		if (ping > 100) {
			result = Boolean.valueOf(true);
		}
		if (p.getVehicle() != null) {
			result = Boolean.valueOf(true);
		}
		return result.booleanValue();
	}
}

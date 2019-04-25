package fr.vorion.anticheat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.vorion.anticheat.AntiCheat;

public class JoinEvent implements Listener
{

	public AntiCheat plugin;

	public JoinEvent(AntiCheat pl)
	{
		plugin = pl;
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e)
	{

		plugin.onlinePlayer.put(e.getPlayer(), System.currentTimeMillis());
		if(e.getPlayer().hasPermission(plugin.bypassPermission))
		{
			plugin.onlineMods.add(e.getPlayer());
		}
	}

}

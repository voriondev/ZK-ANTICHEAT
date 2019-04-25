package fr.vorion.anticheat.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vorion.anticheat.AntiCheat;

public class MoveEvent implements Listener
{

	public AntiCheat plugin;

	public MoveEvent(AntiCheat pl)
	{
		plugin = pl;
	}


	@EventHandler
	public void move(PlayerMoveEvent e)
	{
		if(plugin.freezedPlayers.contains(e.getPlayer()))
		{
			Location from = e.getFrom();
	    	Location to = e.getTo();
	    	double x=Math.floor(from.getX());
	    	double z=Math.floor(from.getZ());

	    	if(Math.floor(to.getX())!=x||Math.floor(to.getZ())!=z)
	    	{
	    		x+=.5;
	    		z+=.5;
	    		e.getPlayer().teleport(new Location(from.getWorld(),x,from.getY(),z,from.getYaw(),from.getPitch()));
	    		e.getPlayer().sendMessage("§6Vous avez été freeze !");
	    		e.getPlayer().sendMessage("§6Veuillez venir d'urgence sur le Teamspeak: §7ts.groupezk.fr.");
	    		e.getPlayer().sendMessage("§6Vous pourrez ainsi prouver que vous ne trichez pas.");
	    		e.getPlayer().sendMessage("§cSi vous vous déconnectez, vous serez banni.");
	    	}
		}

	}

}

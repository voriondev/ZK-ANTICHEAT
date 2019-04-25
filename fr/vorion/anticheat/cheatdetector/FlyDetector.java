package fr.vorion.anticheat.cheatdetector;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vorion.anticheat.AntiCheat;
import fr.vorion.anticheat.utils.Lag;
import fr.vorion.anticheat.utils.PingUtils;

public class FlyDetector implements Listener
{

    public AntiCheat plugin;

    public PingUtils pingUtils;

    public FlyDetector(AntiCheat pl)
    {
    	plugin = pl;
    	pingUtils = new PingUtils(plugin);
    }

    @EventHandler
    public void onFly(PlayerMoveEvent e)
    {
    	Location from = e.getFrom();
    	Location to = e.getTo();
    	double distance = from.distance(to);
    	boolean isFalling = to.getY() - from.getY() < 0D;

    	boolean isJumping = e.getFrom().getY()<e.getTo().getY();

    	if(Double.compare(distance, .48) > 0 && !isFalling && !isJumping && e.getPlayer().getLocation().getBlock().getType() == Material.AIR && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !e.getPlayer().hasPermission(plugin.bypassPermission) && pingUtils.getPingFor(e.getPlayer()) < 500 && Lag.getTPS() > 16)
    	{
    		int alerts = plugin.sql.getAlerts(e.getPlayer());
    		int kicks = plugin.sql.getKicks(e.getPlayer());
    		plugin.sql.setAlerts(e.getPlayer(), alerts + 1, kicks);
            if(!plugin.onlineMods.isEmpty())
            {
            	for(Player mod : plugin.onlineMods)
            	{
            		mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + e.getPlayer().getName() + " §eest suspecté de fly / speedhack.");
            	}
            }
    	}

    }


}

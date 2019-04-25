package fr.vorion.anticheat.cheatdetector;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.vorion.anticheat.AntiCheat;
import fr.vorion.anticheat.utils.PingUtils;

public class AutoclickDetector implements Listener
{

    public AntiCheat plugin;

    public static HashMap<UUID, Integer> clickCount = new HashMap<UUID, Integer>();

    public static HashMap<UUID, Double> clickRate = new HashMap<UUID, Double>();

    public PingUtils pingUtils;

    public AutoclickDetector(AntiCheat pl)
    {
    	plugin = pl;
    	pingUtils = new PingUtils(plugin);
    }


    @EventHandler
    public void clickBlock(PlayerInteractEvent event)
    {

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR)
        {

            int newCount = 1;


            if(clickCount.get(event.getPlayer().getUniqueId()) != null)
            {

                newCount = newCount + clickCount.get(event.getPlayer().getUniqueId());
            }

            clickCount.put(event.getPlayer().getUniqueId(), newCount);

        }
    }

    @SuppressWarnings("deprecation")
	public void launchAutoclickChecker()
    {

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
        {
            public void run()
            {

                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(clickCount.containsKey(p.getUniqueId()))
                    {


                        double count = (double) clickCount.get(p.getUniqueId());


                        double rate = count/4;

                        if(rate > plugin.defaultCpsLimit && pingUtils.getPingFor(p) < 100)
                        {
                        	if(!plugin.onlineMods.isEmpty())
                        	{
                        		for(Player mod : plugin.onlineMods)
                        		{
                        			mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + p.getName() + " §ea fait §c" + Math.round(rate) + " §eCPS.");
                        		}
                        	}


                    		int alerts = plugin.sql.getAlerts(p);
                    		int kicks = plugin.sql.getKicks(p);
                    		plugin.sql.setAlerts(p, alerts + 10, kicks);
                        }


                        clickRate.put(p.getUniqueId(), rate);


                        clickCount.put(p.getUniqueId(), 0);

                    }

                }
            }

        }, 0L, 80L);
    }
}

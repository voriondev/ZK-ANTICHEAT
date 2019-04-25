package fr.vorion.anticheat.cheatdetector;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.vorion.anticheat.AntiCheat;

public class ReachModifierDetector implements Listener
{

    public AntiCheat plugin;

    public ReachModifierDetector(AntiCheat pl)
    {
    	plugin = pl;
    }

    public double getDistance3D(Location one, Location two)
    {
      double toReturn = 0.0D;
      double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
      double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
      double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
      double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
      toReturn = Math.abs(sqrt);
      return toReturn;
    }

    @EventHandler
    public void onReach(EntityDamageByEntityEvent event)
    {
      if ((!(event.getDamager() instanceof Player)) || (!(event.getEntity() instanceof LivingEntity))) {
        return;
      }
      Player player = (Player)event.getDamager();
      LivingEntity damaged = (LivingEntity)event.getEntity();
      Location entityLoc = damaged.getLocation().add(0.0D, damaged.getEyeHeight(), 0.0D);
      Location playerLoc = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);

      double distance = getDistance3D(entityLoc, playerLoc);
      if (distance > 4.05D)
      {
			if (!plugin.onlineMods.isEmpty())
			{
				for (Player mod : plugin.onlineMods)
				{
					mod.sendMessage(plugin.defaultPrefix + plugin.defaultMessageColor + "§c" + player.getName()
							+ " §eest suspecté de modifier sa reach. (" + distance + " blocs)");
				}
			}

			int alerts = plugin.sql.getAlerts(player);
			int kicks = plugin.sql.getKicks(player);
			plugin.sql.setAlerts(player, alerts + 5, kicks);
      }
    }

}

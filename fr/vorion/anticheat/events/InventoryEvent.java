package fr.vorion.anticheat.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.vorion.anticheat.AntiCheat;

public class InventoryEvent implements Listener 
{
	
	public AntiCheat plugin;
	
	public InventoryEvent(AntiCheat pl)
	{
		plugin = pl;
	}
	
	@EventHandler
	public void onInteractEvent(InventoryClickEvent e)
	{	
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getName().contains("Vérif"))
		{
				e.setCancelled(true);
				if(e.getCurrentItem() != null)
				{
					if(e.getCurrentItem().hasItemMeta())
					{
						if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM))
						{
							if(e.getClick() == ClickType.LEFT)
							{
								String playerName = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
								Player target = Bukkit.getPlayer(playerName);
								p.teleport(target);
								p.sendMessage("§6Téléportation vers " + target.getName() + "§6 !");
							}
							else if(e.getClick() == ClickType.RIGHT)
							{
								String playerName = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
								Player target = Bukkit.getPlayer(playerName);
								p.openInventory(plugin.invManager.getPlayerInventory().getPlayerInventory(target));
							}
						}
						else if(e.getCurrentItem().getType().equals(Material.BARRIER))
						{
							p.closeInventory();
						}
					}	
				}
			}
		if(e.getInventory().getName().contains("Inventaire:"))
		{
				e.setCancelled(true);
			}
		}

}
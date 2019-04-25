package fr.vorion.anticheat.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vorion.anticheat.AntiCheat;

public class PlayerInventory
{

	public AntiCheat plugin;

	public PlayerInventory(AntiCheat pl)
	{
		plugin = pl;
	}

	public Inventory getPlayerInventory(Player p)
	{
		Inventory verifInv = Bukkit.createInventory(null, 54, "§6Inventaire: " + p.getName());



		for(int i = 1; i < 8; i++)
		{
			ItemStack blackPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			ItemMeta blackPaneMeta = blackPane.getItemMeta();
			blackPaneMeta.setDisplayName(" ");
			blackPane.setItemMeta(blackPaneMeta);
			verifInv.setItem(i, blackPane);

			ItemStack orangePane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
			ItemMeta orangePaneMeta = orangePane.getItemMeta();
			orangePaneMeta.setDisplayName(" ");
			orangePane.setItemMeta(orangePaneMeta);
			verifInv.setItem(0, orangePane);
			verifInv.setItem(8, orangePane);
		}

		for(int i = 45; i < 54; i++)
		{
			ItemStack stainedPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			ItemMeta stainedPaneMeta = stainedPane.getItemMeta();
			stainedPaneMeta.setDisplayName(" ");
			stainedPane.setItemMeta(stainedPaneMeta);
			verifInv.setItem(i, stainedPane);

			ItemStack orangePane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
			ItemMeta orangePaneMeta = orangePane.getItemMeta();
			orangePaneMeta.setDisplayName(" ");
			orangePane.setItemMeta(orangePaneMeta);
			verifInv.setItem(45, orangePane);
			verifInv.setItem(53, orangePane);
		}

        int slot = 8;
        ItemStack[] arrayOfItemStack;
        int j = (arrayOfItemStack = p.getInventory().getContents()).length;
        for (int i = 0; i < j; i++)
        {
          ItemStack itemStack = arrayOfItemStack[i];

          slot++;
          verifInv.setItem(slot, itemStack);
        }

		return verifInv;
	}

}

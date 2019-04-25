package fr.vorion.anticheat.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.vorion.anticheat.AntiCheat;
import fr.vorion.anticheat.utils.PingUtils;

public class VerifInventory
{

	public AntiCheat plugin;

	public PingUtils pingUtils;

	public VerifInventory(AntiCheat pl)
	{
		plugin = pl;
		pingUtils = new PingUtils(pl);
	}

	public Inventory getVerifInventory(Player p)
	{
		Inventory verifInv = Bukkit.createInventory(null, 9, "§cVérif: " + p.getName());

		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
		headMeta.setOwner(p.getName());
		headMeta.setDisplayName("§6" + p.getName());
		headMeta.setLore(Arrays.asList("§cClic gauche: Se téléporter / Clic droit: voir l'inventaire", "§7Alertes: §e" + plugin.sql.getAlerts(p), "§7Kicks: §6" + plugin.sql.getKicks(p), "§7Reports: §c3", "§7Ping: " + pingUtils.getPing(p), "§7Temps de connexion: §a" + pingUtils.getPlayerUptime(p)));
		playerHead.setItemMeta(headMeta);

		verifInv.setItem(0, playerHead);

		for(int i = 1; i < 8; i++)
		{
			ItemStack stainedPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			ItemMeta stainedPaneMeta = stainedPane.getItemMeta();
			stainedPaneMeta.setDisplayName(" ");
			stainedPane.setItemMeta(stainedPaneMeta);
			verifInv.setItem(i, stainedPane);
		}

		ItemStack barrier = new ItemStack(Material.BARRIER);
		ItemMeta barrierMeta = barrier.getItemMeta();
		barrierMeta.setDisplayName("§cQuitter");
		barrier.setItemMeta(barrierMeta);

		verifInv.setItem(8, barrier);

		return verifInv;
	}

}

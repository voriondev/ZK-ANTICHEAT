package fr.vorion.anticheat.inventory;

import fr.vorion.anticheat.AntiCheat;

public class InventoryManager
{

	public AntiCheat plugin;

	public VerifInventory verifInv;
	public PlayerInventory playerInv;

	public InventoryManager(AntiCheat pl)
	{
		plugin = pl;
		verifInv = new VerifInventory(pl);
		playerInv = new PlayerInventory(pl);
	}

	public VerifInventory getVerifInventory()
	{
		return verifInv;
	}

	public PlayerInventory getPlayerInventory()
	{
		return playerInv;
	}

}

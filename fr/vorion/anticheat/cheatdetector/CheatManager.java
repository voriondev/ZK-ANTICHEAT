package fr.vorion.anticheat.cheatdetector;

import org.bukkit.Bukkit;

import fr.vorion.anticheat.AntiCheat;

public class CheatManager
{

	public AntiCheat plugin;

	public AutoclickDetector autoclickDetector;

	public FlyDetector flyDetector;

	public CheatManager(AntiCheat pl)
	{
		plugin = pl;
		autoclickDetector = new AutoclickDetector(pl);
		flyDetector = new FlyDetector(pl);
	}

	public void registerCheats()
	{
		autoclickDetector.launchAutoclickChecker();
		Bukkit.getPluginManager().registerEvents(new FlyDetector(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new ReachModifierDetector(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new FastBowDetector(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new FastHealDetector(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new AntiKnockbackDetector(plugin), plugin);
	}

	public AutoclickDetector getAutoclickDetector()
	{
		return autoclickDetector;
	}

	public FlyDetector getFlyDetector()
	{
		return flyDetector;
	}

}

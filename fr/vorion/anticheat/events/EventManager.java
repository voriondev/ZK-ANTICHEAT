package fr.vorion.anticheat.events;

import org.bukkit.Bukkit;

import fr.vorion.anticheat.AntiCheat;
import fr.vorion.anticheat.cheatdetector.AutoclickDetector;

public class EventManager
{

	public AntiCheat plugin;

	public EventManager(AntiCheat pl)
	{
		plugin = pl;
	}

	public void registerEvents()
	{
		Bukkit.getPluginManager().registerEvents(new JoinEvent(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new InventoryEvent(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new MoveEvent(plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new AutoclickDetector(plugin), plugin);
	}

}

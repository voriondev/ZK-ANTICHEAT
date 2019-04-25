package fr.vorion.anticheat.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.vorion.anticheat.AntiCheat;

public class PingUtils
{

	public static Class<?> CPClass;

	public String serverName  = Bukkit.getServer().getClass().getPackage().getName();

	public String serverVersion = serverName.substring(serverName.lastIndexOf(".") + 1, serverName.length());

	public AntiCheat plugin;

	public PingUtils(AntiCheat pl)
	{
		plugin = pl;
	}

	public String getPing(Player player)
	{
		try
		{
			CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
			Object CraftPlayer = CPClass.cast(player);

			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);

			Field ping = EntityPlayer.getClass().getDeclaredField("ping");

			int playerPing = ping.getInt(EntityPlayer);

			if(playerPing < 80)
				return "§a" + playerPing + "ms";
			else if(playerPing > 80 && playerPing < 200)
				return "§e" + playerPing + "ms";
			else if(playerPing > 200)
				return "§c" + playerPing + "ms";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "0ms";
	}

	public int getPingFor(Player player)
	{
		try
		{
			CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
			Object CraftPlayer = CPClass.cast(player);

			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);

			Field ping = EntityPlayer.getClass().getDeclaredField("ping");

			int playerPing = ping.getInt(EntityPlayer);

			return playerPing;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public String getPlayerUptime(Player player)
	{
		Long joinDate = plugin.onlinePlayer.get(player);
	    Long date = System.currentTimeMillis() - joinDate;

	    long seconds = date / 1000 % 60;
	    long minutes = date / (60 * 1000) % 60;
	    long hours = date / (60 * 60 * 1000) % 24;

	    return hours + " heures " + minutes + " minutes " + seconds + " secondes";
	}

}

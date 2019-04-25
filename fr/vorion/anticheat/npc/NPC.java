package fr.vorion.anticheat.npc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

public class NPC
{
  String name;
  World world;
  public int id;
  public static JavaPlugin plugin;
  Location l;
  int itemInHand;
  private UUID uuid;
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static ArrayList<Location> locations = new ArrayList();
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static ArrayList<NPC> humans = new ArrayList();

  @SuppressWarnings("rawtypes")
  private void setPrivateField(Class type, Object object, String name, Object value)
  {
    try
    {
      Field f = type.getDeclaredField(name);
      f.setAccessible(true);
      f.set(object, value);
      f.setAccessible(false);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void setPitch(float pitch)
  {
    walk(0.0D, 0.0D, 0.0D, this.l.getYaw(), pitch);
  }

  public void setYaw(float yaw)
  {
    walk(0.0D, 0.0D, 0.0D, yaw, this.l.getPitch());
  }


public NPC(World w, String name, int id, Location l, int itemInHand)
  {
    this.name = name;
    this.world = w;
    this.id = id;
    this.l = l;
    this.itemInHand = itemInHand;
    this.uuid = UUID.randomUUID();
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", Integer.valueOf(id));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b", uuid);
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c",
      Integer.valueOf((int)l.getX() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d",
      Integer.valueOf((int)l.getY() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e",
      Integer.valueOf((int)l.getZ() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f",
      Byte.valueOf(getCompressedAngle(l.getYaw())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g",
      Byte.valueOf(getCompressedAngle(l.getPitch())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h",
      Integer.valueOf(itemInHand));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);

    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b",
      Integer.valueOf((int)l.getX() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c",
      Integer.valueOf((int)l.getY() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d",
      Integer.valueOf((int)l.getZ() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e",
      Byte.valueOf(getCompressedAngle(l.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f",
      Byte.valueOf(getCompressedAngle(l.getPitch())));

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(spawn);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
    }
    locations.add(l);
    humans.add(this);
  }

  public void teleport(Location loc)
  {
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b",
      Integer.valueOf((int)(loc.getX() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c",
      Integer.valueOf((int)(loc.getY() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d",
      Integer.valueOf((int)(loc.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e",
      Byte.valueOf(getCompressedAngle(loc.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f",
      Byte.valueOf(getCompressedAngle(loc.getPitch())));
    locations.remove(this.l);
    humans.remove(this);
    this.l = loc;
    locations.add(this.l);
    humans.add(this);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
    }
  }

  private byte getCompressedAngle(float value)
  {
    return (byte)(int)(value * 256.0F / 360.0F);
  }

  private byte getCompressedAngle2(float value)
  {
    return (byte)(int)(value * 256.0F / 360.0F);
  }

  public void remove()
  {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.id });

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
  }

  public void updateItems(ItemStack hand, ItemStack boots, ItemStack legs, ItemStack chest, ItemStack helmet)
  {
    PacketPlayOutEntityEquipment[] ps = {
      new PacketPlayOutEntityEquipment(this.id, 1,
      CraftItemStack.asNMSCopy(boots)),
      new PacketPlayOutEntityEquipment(this.id, 2,
      CraftItemStack.asNMSCopy(legs)),
      new PacketPlayOutEntityEquipment(this.id, 3,
      CraftItemStack.asNMSCopy(chest)),
      new PacketPlayOutEntityEquipment(this.id, 4,
      CraftItemStack.asNMSCopy(helmet)),
      new PacketPlayOutEntityEquipment(this.id, 0,
      CraftItemStack.asNMSCopy(hand)) };
    PacketPlayOutEntityEquipment[] arrayOfPacketPlayOutEntityEquipment1;
    int j = (arrayOfPacketPlayOutEntityEquipment1 = ps).length;
    for (int i = 0; i < j; i++)
    {
      PacketPlayOutEntityEquipment pack = arrayOfPacketPlayOutEntityEquipment1[i];
      for (Player p : Bukkit.getOnlinePlayers())
      {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(pack);
      }
    }
  }

  @Deprecated
  public void setName(String s)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    d.a(10, s);

    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void hideForPlayer(Player p)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)32));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
  }

  public void showForPlayer(Player p)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
  }

  public void addPotionColor(Color r)
  {
    int color = r.asBGR();
    DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void addPotionColor(int color)
  {
    DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void walk(double a, double b, double c)
  {
    walk(a, b, c, this.l.getYaw(), this.l.getPitch());
  }

  public void walk(double a, double b, double c, float yaw, float pitch)
  {
    byte x = (byte)(int)a;
    byte y = (byte)(int)b;
    byte z = (byte)(int)c;
    PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook();
    setPrivateField(PacketPlayOutEntity.class, packet, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntity.class, packet, "b", Byte.valueOf(x));
    setPrivateField(PacketPlayOutEntity.class, packet, "c", Byte.valueOf(y));
    setPrivateField(PacketPlayOutEntity.class, packet, "d", Byte.valueOf(z));
    setPrivateField(PacketPlayOutEntity.class, packet, "e",
      Byte.valueOf(getCompressedAngle(yaw)));
    setPrivateField(PacketPlayOutEntity.class, packet, "f",
      Byte.valueOf(getCompressedAngle2(pitch)));

    PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "b",
      Byte.valueOf(getCompressedAngle(yaw)));
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(p2);
    }
    locations.remove(this.l);
    humans.remove(this);
    this.l.setPitch(pitch);
    this.l.setYaw(yaw);
    this.l.add(a, b, c);
    locations.add(this.l);
    humans.add(this);
  }

  public void sendToPlayer(Player who)
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b",
      new GameProfile(this.uuid, this.name));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c",
      Integer.valueOf((int)(this.l.getX() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d",
      Integer.valueOf((int)(this.l.getY() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e",
      Integer.valueOf((int)(this.l.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f",
      Byte.valueOf(getCompressedAngle(this.l.getYaw())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g",
      Byte.valueOf(getCompressedAngle(this.l.getPitch())));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h",
      Integer.valueOf(this.itemInHand));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);

    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b",
      Integer.valueOf((int)(this.l.getX() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c",
      Integer.valueOf((int)(this.l.getY() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d",
      Integer.valueOf((int)(this.l.getZ() * 32.0D)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e",
      Byte.valueOf(getCompressedAngle(this.l.getYaw())));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f",
      Byte.valueOf(getCompressedAngle(this.l.getPitch())));

    ((CraftPlayer)who).getHandle().playerConnection.sendPacket(spawn);
    ((CraftPlayer)who).getHandle().playerConnection.sendPacket(tp);
    locations.remove(this.l);
    humans.remove(this);
    this.l = who.getLocation();
    locations.add(this.l);
    humans.add(this);
  }

  public void setInvisible()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)32));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void setCrouched()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)2));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void reset()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)0));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void sprint()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)8));
    d.a(1, Short.valueOf((short)0));
    d.a(8, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);
    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  @Deprecated
  public void block()
  {
    DataWatcher d = new DataWatcher(null);
    d.a(0, Byte.valueOf((byte)16));
    d.a(1, Short.valueOf((short)0));
    d.a(6, Byte.valueOf((byte)0));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(
      this.id, d, true);

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet40);
    }
  }

  public void damage()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(2));

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }

  public void swingArm()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(0));

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }

  @Deprecated
  public void eatInHand()
  {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", Integer.valueOf(5));

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet18);
    }
  }

  public void sleep(Location loc)
  {
    PacketPlayOutBed packet17 = new PacketPlayOutBed();
    setPrivateField(PacketPlayOutBed.class, packet17, "a", Integer.valueOf(this.id));
    setPrivateField(PacketPlayOutBed.class, packet17, "b", Integer.valueOf((int)loc.getX()));
    setPrivateField(PacketPlayOutBed.class, packet17, "c", Integer.valueOf((int)loc.getY()));
    setPrivateField(PacketPlayOutBed.class, packet17, "d", Integer.valueOf((int)loc.getZ()));

    for (Player p : Bukkit.getOnlinePlayers())
    {
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet17);
    }
    locations.remove(this.l);
    this.l = loc;
    locations.add(this.l);
  }

  public double getX()
  {
    return this.l.getX();
  }

  public double getY()
  {
    return this.l.getY();
  }

  public double getZ()
  {
    return this.l.getZ();
  }

  public Location getLocation()
  {
    return this.l;
  }

  public static void serialize()
  {
    File file = new File(plugin.getDataFolder(), "npcs.yml");
    FileConfiguration npcs = YamlConfiguration.loadConfiguration(file);
    int current = 1;
    for (NPC human : humans)
    {
      npcs.set("npcs.npc " + current + ".name", human.name);
      npcs.set("npcs.npc " + current + ".world", human.world.getName());
      npcs.set("npcs.npc " + current + ".id", Integer.valueOf(human.id));
      npcs.set("npcs.npc " + current + ".location",
        locationToString(human.l));
      npcs.set("npcs.npc " + current + ".item", Integer.valueOf(human.itemInHand));
      current++;
    }
    try
    {
      npcs.save(file);
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static ArrayList<NPC> deserialize()
  {
    ArrayList<NPC> humans = new ArrayList();
    File file = new File(plugin.getDataFolder() + "/npcs.yml");
    FileConfiguration npcs = YamlConfiguration.loadConfiguration(file);
    int current = 1;
    while (npcs.isConfigurationSection("npcs.npc " + current))
    {
      ConfigurationSection sec = npcs.getConfigurationSection("npcs.npc " +
        current);
      NPC human = new NPC(Bukkit.getWorld(sec.getString("world")),
        sec.getString("name"), sec.getInt("id"),
        locationFromString(sec.getString("location")),
        sec.getInt("item"));
      humans.add(human);
      locations.add(human.l);
      current++;
    }
    try
    {
      npcs.set("npcs", null);
      npcs.save(file);
    }
    catch (IOException e)
    {
      System.out.println("ERROR: " + e.getMessage());
    }
    return humans;
  }

  public static Location locationFromString(String string)
  {
    String[] location = string.split(",");
    return new Location(Bukkit.getWorld(location[0]),
      Double.parseDouble(location[1]),
      Double.parseDouble(location[2]),
      Double.parseDouble(location[3]));
  }

  public static String locationToString(Location loc)
  {
    return
      loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
  }
}

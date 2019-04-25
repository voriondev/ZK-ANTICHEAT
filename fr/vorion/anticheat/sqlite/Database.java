package fr.vorion.anticheat.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import fr.vorion.anticheat.AntiCheat;


public abstract class Database
{
    public AntiCheat plugin;
    Connection connection;

    public String table = "anticheat";

    public int alerts = 0;
    public int kicks = 0;

    public Database(AntiCheat pl)
    {
        plugin = pl;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize()
    {
        connection = getSQLConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
        }
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    public Integer getAlerts(Player p)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+ p.getUniqueId() +"';");

            rs = ps.executeQuery();
            while(rs.next())
            {
                if(rs.getString("player").equalsIgnoreCase(p.getUniqueId().toString()))
                {
                    return rs.getInt("alerts");
                }
            }
        }
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0;
    }

    public Integer getKicks(Player p)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+p.getUniqueId()+"';");

            rs = ps.executeQuery();
            while(rs.next())
            {
                if(rs.getString("player").equalsIgnoreCase(p.getUniqueId().toString()))
                {
                    return rs.getInt("kicks");
                }
            }
        }
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0;
    }

    public boolean isBanned(Player p)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '"+p.getUniqueId()+"';");

            rs = ps.executeQuery();
            while(rs.next())
            {
                if(rs.getString("player").equalsIgnoreCase(p.getUniqueId().toString()))
                {
                    return rs.getString("banned").equals("yes");
                }
            }
        }
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
            return false;
        }
        return false;
    }


    public void setAlerts(Player player, Integer alerts, Integer kicks)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (player,alerts,kicks,banned) VALUES(?,?,?,?)");

            ps.setString(1, player.getUniqueId().toString());

            ps.setInt(2, alerts);

            ps.setInt(3, kicks);

            ps.setString(4, "no");

            ps.executeUpdate();
            return;
        }

        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException ex)
            {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void setKicks(Player player, Integer alerts, Integer kicks)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (player,alerts,kicks, banned) VALUES(?,?,?,?)");

            ps.setString(1, player.getUniqueId().toString());

            ps.setInt(2, alerts);

            ps.setInt(3, kicks);

            ps.setString(4, "no");

            ps.executeUpdate();
            return;
        }

        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException ex)
            {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void setBanned(Player player, Integer alerts, Integer kicks, boolean banned)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (player,alerts,kicks, banned) VALUES(?,?,?,?)");

            ps.setString(1, player.getUniqueId().toString());

            ps.setInt(2, alerts);

            ps.setInt(3, kicks);


            if(banned == true)
            	ps.setString(4, "yes");
            else
            	ps.setString(4, "no");

            ps.executeUpdate();
            return;
        }

        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        }
        finally
        {
            try
            {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException ex)
            {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void close(PreparedStatement ps,ResultSet rs)
    {
        try
        {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
        catch (SQLException ex)
        {

        }
    }
}

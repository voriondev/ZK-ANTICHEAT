package fr.vorion.anticheat.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import fr.vorion.anticheat.AntiCheat;

public class SQLite extends Database
{
    String dbname;

    public SQLite(AntiCheat pl)
    {
        super(pl);
        dbname = "anticheat";
    }

    public String sqlCreate = "CREATE TABLE IF NOT EXISTS anticheat (" +
            "`player` varchar(32) NOT NULL," +
            "`alerts` int(11) NOT NULL," +
            "`kicks` int(11) NOT NULL," +
            "`banned` varchar(32) NOT NULL," +
            "PRIMARY KEY (`player`)" +
            ");";



    public Connection getSQLConnection()
    {
        File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists())
        {
            try
            {
                dataFolder.createNewFile();
            }
            catch (IOException e)
            {
                plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try
        {
            if(connection!=null&&!connection.isClosed())
            {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        }
        catch (SQLException ex)
        {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        }
        catch (ClassNotFoundException ex)
        {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load()
    {
        connection = getSQLConnection();
        try
        {
            Statement s = connection.createStatement();
            s.executeUpdate(sqlCreate);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}

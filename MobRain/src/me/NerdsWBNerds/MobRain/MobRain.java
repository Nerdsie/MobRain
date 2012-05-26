package me.NerdsWBNerds.MobRain;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class MobRain extends JavaPlugin {
	public MRListener Listener = new MRListener(this);
	public Server server;
	public Logger log;
	
	public void onEnable(){
		server = this.getServer();
		log = this.getLogger();

		server.getPluginManager().registerEvents(Listener, this);
	}
	
	public void onDisable(){
		
	}
}

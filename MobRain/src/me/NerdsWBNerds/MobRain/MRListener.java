package me.NerdsWBNerds.MobRain;

import static org.bukkit.ChatColor.*;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MRListener implements Listener{
	public MobRain plugin;

	Player player;
	String[] split;
	Spawner spawner;
	int radius = 25;
	
	public MRListener(MobRain p){
		plugin = p;
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		player = e.getPlayer();
		split = e.getMessage().split(" ");
		
		if(match(0, "/mr", "/mobrain")){			
			if(split.length>=3 && split.length <= 4 && !match(1, "r", "radius")){
				e.setCancelled(true);
				
				String name;
				final int amount;
				if(isNumber(split[1])){
					name = split[2];
					amount = Integer.valueOf(split[1]);
				}else if(isNumber(split[2])){
					name = split[1];
					amount = Integer.valueOf(split[2]);
				}else{
					tell(player, RED + "[MobRain] You didn't set an amount for us to spawn, try again.");
					return;
				}
				
				int time = -1;
				
				final EntityType spawn = Mobs.getMob(name.toLowerCase());
				if(spawn==null){
					tell(player, RED + "[MobRain] Sorry, we didn't find any mobs with the name " + name + ", try again.");
					return;
				}
				
				if(player.isOp()){
					tell(player, GOLD + "[MobRain] " + GREEN + "You want to spawn " + AQUA + amount + GREEN + " of " + AQUA + spawn.getName());
					
					if(spawner != null) spawner.running = false;

					spawner = new Spawner(player, amount, radius, spawn, time);
					spawner.start();
				}else{
					tell(player, RED + "[MobRain] Sorry, you do not have permission to do this.");
					return;
				}
			}
			if(split.length==3){
				if(!player.isOp()){
					tell(player, RED + "[MobRain] Sorry, you do not have permission to do this.");
					return;
				}
				
				e.setCancelled(true);
				
				if(match(1, "r", "radius")){
					if(isNumber(split[2])){
						int r = Integer.valueOf(split[2]);
						if(r >= 2){
							radius = r;
							
							tell(player,  GOLD + "[MobRain] " + GREEN + "Mobs will now be able to rain " + AQUA + r + GREEN + " block away in any direction.");
						}
					}
				}
			}
			
			if(split.length==2){
				if(!player.isOp()){
					tell(player, RED + "[MobRain] Sorry, you do not have permission to do this.");
					return;
				}
				if(match(1, "stop")){
					spawner.running = false;
					e.setCancelled(true);
					tell(player, GOLD + "[MobRain] " + GREEN + "Current raining of mobs now stopped.");
				}
			}
		}
	}
	
	public boolean match(int x, String y){
		if(split[x].equalsIgnoreCase(y)){
			return true;
		}
		
		return false;
	}
	public boolean match(int x, String y, String z){
		if(split[x].equalsIgnoreCase(y) || split[x].equalsIgnoreCase(z)){
			return true;
		}
		
		return false;
	}
	public boolean match(String x, String y){
		if(x.equalsIgnoreCase(y)){
			return true;
		}
		
		return false;
	}
	
	private boolean isNumber(String toCheck) {
		try {
			Integer.parseInt(toCheck);
			return true;
		} catch (NumberFormatException numForEx) {
			return false;
		} 
	}
	
	public static String correctCaps(String name) {
	    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
	
	public void tell(Player player, String m){
		player.sendMessage(m);
	}
}

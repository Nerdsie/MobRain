package me.NerdsWBNerds.MobRain;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

public class MobRain extends JavaPlugin implements CommandExecutor, Listener{
	public static ArrayList<LivingEntity> spawned = new ArrayList<LivingEntity>();
	public static boolean surviveFall = true, useOP = true;
	public static Server server;
	public static Logger log;
	
	public void onEnable(){
		server = this.getServer();
		log = this.getLogger();

		if(getConfig().contains("surviveFall")){
			try{
				surviveFall = getConfig().getBoolean("surviveFall");
				
				if(getConfig().get("surviveFall") == null){
					getConfig().set("surviveFall", true);
					this.saveConfig();
				}
			}catch(Exception e){
				getConfig().set("surviveFall", true);
				this.saveConfig();
			}
		}else{
			getConfig().set("surviveFall", true);
			this.saveConfig();
		}
		
		if(getConfig().contains("opUse")){
			try{
				useOP = getConfig().getBoolean("opUse");
				
				if(getConfig().get("opUse") == null){
					getConfig().set("opUse", true);
					this.saveConfig();
				}
			}catch(Exception e){
				getConfig().set("opUse", true);
				this.saveConfig();
			}
		}else{
			getConfig().set("opUse", true);
			this.saveConfig();
		}
		
		server.getPluginManager().registerEvents(this, this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;

		if(!player.isOp())
			return false;
		
		if(cmd.getName().equalsIgnoreCase("drops")){
			if(!hasPerm(player, "mobrain.drops")){
				warn(player, "You don't have permission to do this.");
				return true;
			}
			
			try{
				int rad = Integer.parseInt(args[0]);
				int removed = 0;
				
				for(Entity e: player.getWorld().getEntities()){
					if(e.getType() == EntityType.DROPPED_ITEM && player.getLocation().distance(e.getLocation()) <= rad){
						e.remove();
						removed++;
					}
				}
				
				tell(player, ChatColor.AQUA + Integer.toString(removed) + ChatColor.GREEN + " drops removed.");
				return true;
			}catch(Exception e){
				warn(player, "Error clearing drops, use /drops <radius>");
				return true;
			}
		}

		if(cmd.getName().equalsIgnoreCase("butcher") || cmd.getName().equalsIgnoreCase("killmobs")){
			if(!hasPerm(player, "mobrain.butcher")){
				warn(player, "You don't have permission to do this.");
				return true;
			}
			
			try{
				int rad = Integer.parseInt(args[0]);
				int removed = 0;
				
				for(Entity e: player.getWorld().getEntities()){
					if((!e.getType().equals(EntityType.COMPLEX_PART) && !e.getType().equals(EntityType.PRIMED_TNT) && !e.getType().equals(EntityType.THROWN_EXP_BOTTLE) && !e.getType().equals(EntityType.WEATHER) && !e.getType().equals(EntityType.SPLASH_POTION) && !e.getType().equals(EntityType.FALLING_BLOCK) && !e.getType().equals(EntityType.FISHING_HOOK) && !e.getType().equals(EntityType.PAINTING) && !e.getType().equals(EntityType.PLAYER)) && player.getLocation().distance(e.getLocation()) <= rad){
						e.remove();
						removed++;
					}
				}
				
				tell(player, ChatColor.AQUA + Integer.toString(removed) + ChatColor.GREEN + " mobs removed.");
				return true;
			}catch(Exception e){
				warn(player, "Error clearing mobs, use /butcher <radius>");
				return true;
			}
		}

		if(cmd.getName().equalsIgnoreCase("mr") || cmd.getName().equalsIgnoreCase("mrain") && args.length==1){
			if(!hasPerm(player, "mobrain.stop")){
				warn(player, "You don't have permission to do this.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("stop")){
				stopRain();
				tell(player, ChatColor.GREEN + "All raining mobs now stopped.");
				return true;
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("mrain") || cmd.getName().equalsIgnoreCase("mr")){
			if(!hasPerm(player, "mobrain.rain")){
				warn(player, "You don't have permission to do this.");
				return true;
			}
			
			EntityType ent = Mobs.getMob(args[1]);
			int amount = Integer.parseInt(args[0]);
			int radius = 25;
			Player target = player;
			if((args.length==3)){
				try{
					radius = Integer.parseInt(args[2]);
				}catch(Exception e){
					try{
						target = server.getPlayer(args[2]);
					}catch(Exception ee){
						warn(player, "Error raining mobs, either your radius was not a number, or player was not found. /mrain <amount> <mob> (radius) (player)");
						return true;
					}
				}
			}
			
			if(args.length==4){
				try{
					radius = Integer.parseInt(args[2]);
					target = server.getPlayer(args[3]);
				}catch(Exception e){
					try{
						radius = Integer.parseInt(args[3]);
						target = server.getPlayer(args[2]);
					}catch(Exception ee){
						warn(player, "Error raining mobs, either your radius was not a number, or player was not found. /mrain <amount> <mob> (radius) (player)");
						return true;
					}
				}
			}
			
			tell(player, ChatColor.GREEN + "Now raining " + ChatColor.AQUA + amount + " " + ent.getName() + "'s");
			startRain(ent, amount, radius, target);
			return true;
		}
		return false;
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void onFallDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.FALL && surviveFall){
			if(spawned.contains(e.getEntity())){
				e.setCancelled(true);
				e.setDamage(0);
				spawned.remove(e.getEntity());
			}
		}
	}
	
	public void startRain(EntityType e, int a, int r, Player t){
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Rain(e, a, r, t), 20L, 20L);
	}
	
	public void stopRain(){
		this.getServer().getScheduler().cancelAllTasks();
	}
	
	public void tell(Player p, String m){
		p.sendMessage(ChatColor.GOLD + "[MobRain] " + m);
	}
	
	public void warn(Player p, String m){
		p.sendMessage(ChatColor.RED + "[MobRain] " + m);
	}
	
	public boolean hasPerm(Player p, String pe){
		if(p.hasPermission(pe)){
			return true;
		}
		
		if(useOP && p.isOp()){
			return true;
		}
		
		return false;
	}
}

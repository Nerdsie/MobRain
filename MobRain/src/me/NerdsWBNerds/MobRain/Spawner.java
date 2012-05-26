package me.NerdsWBNerds.MobRain;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Spawner extends Thread{
	Logger log = Logger.getLogger("Minecraft");
	int ticks = 0;
	Player player;
	int amount;
	int radius;
	EntityType spawn;
	int time;
	int sleep = 300;
	boolean running = true;
	
	public Spawner(Player p, int a, int r, EntityType s, int t){
		player = p;
		amount = a;
		radius = r;
		spawn = s;
		time = t;
		
		if(time != -1){
			time *= 1000;
			sleep = time / amount;
			
			System.out.println(time + " " + amount + " " + sleep);
		}
	}
	
	public void run(){
		if(ticks >= amount || !running) return;
		
		if(Bukkit.getServer().getOnlinePlayers().length==0){
			return;
		}
		
		if(!player.isOnline()){
			player = Bukkit.getServer().getOnlinePlayers()[0];
		}
			
		int xx = player.getLocation().getBlockX();
		int yy = player.getLocation().getBlockY();
		int zz = player.getLocation().getBlockZ();
		
		Random r = new Random();
		
		int x = xx - radius + r.nextInt(radius) * 2;
		
		int y = r.nextInt(50 + 70);
		
		if(yy >= 50){
			y = r.nextInt(yy + 70);
		}
		
		int z = zz - radius + r.nextInt(radius) * 2;
		
		Location toSpawn = new Location(player.getWorld(), x, y, z);
		
		player.getWorld().spawnCreature(toSpawn, spawn);
		
		ticks++;
		
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(ticks >= amount || !running) return;
		
		if(Bukkit.getServer().getOnlinePlayers().length==0){
			return;
		}
		
		try{
			run();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}

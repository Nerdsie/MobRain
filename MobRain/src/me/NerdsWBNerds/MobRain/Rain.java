package me.NerdsWBNerds.MobRain;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Rain implements Runnable {
	Player target = null;
	EntityType type = null;
	int amount = 100, radius = 25;
	
	public Rain(EntityType t, int a, int r, Player ta){
		type = t;
		amount = a;
		radius = r;
		target = ta;
	}
	
	@Override
	public void run() {
		int rain = 25;
		
		if(!target.isOnline())
			target = MobRain.server.getOnlinePlayers()[0];
		
		if(amount <= 0)
			MobRain.server.getScheduler().cancelAllTasks();
		
		if(rain > amount)
			rain = amount;
		
		for(int i = 0; i < rain; i++){		
			int xx = target.getLocation().getBlockX();
			int zz = target.getLocation().getBlockZ();

			Random r = new Random();

			int x = xx - radius + r.nextInt(radius) * 2;
			int y = 150 + r.nextInt(20);
			int z = zz - radius + r.nextInt(radius) * 2;

			Location dropFrom = new Location(target.getWorld(), x, y, z);
			MobRain.spawned.add(target.getWorld().spawnCreature(dropFrom, type));
			amount--;
		}
	}
}

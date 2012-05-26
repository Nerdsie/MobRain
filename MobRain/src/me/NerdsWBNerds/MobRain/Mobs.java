package me.NerdsWBNerds.MobRain;
import org.bukkit.entity.EntityType;;

public class Mobs {
	public static EntityType getMob(String name){
		if(name.startsWith("b"))
			return EntityType.BLAZE;
		if(name.startsWith("ca"))
			return EntityType.CAVE_SPIDER;
		if(name.startsWith("ch"))
			return EntityType.CHICKEN;
		if(name.startsWith("co"))
			return EntityType.COW;
		if(name.startsWith("cr"))
			return EntityType.CREEPER;
		if(name.startsWith("d") || name.startsWith("enderd") || name.startsWith("ender_") || name.startsWith("ender-"))
			return EntityType.ENDER_DRAGON;
		if(name.startsWith("e"))
			return EntityType.ENDERMAN;
		if(name.startsWith("gh"))
			return EntityType.GHAST;
		if(name.startsWith("gi"))
			return EntityType.GIANT;
		if(name.startsWith("i"))
			return EntityType.IRON_GOLEM;
		if(name.startsWith("ma"))
			return EntityType.MAGMA_CUBE;
		if(name.startsWith("mu"))
			return EntityType.MUSHROOM_COW;
		if(name.startsWith("pi"))
			return EntityType.PIG;
		if(name.startsWith("o"))
			return EntityType.OCELOT;
		if(name.startsWith("pigz") || name.startsWith("pig_") || name.startsWith("pig-") || name.startsWith("zombiep") || name.startsWith("zombie-") || name.startsWith("zombie_") || name.startsWith("pigm"))
			return EntityType.PIG_ZOMBIE;
		if(name.startsWith("sh"))
			return EntityType.SHEEP;
		if(name.startsWith("si"))
			return EntityType.SILVERFISH;
		if(name.startsWith("sk"))
			return EntityType.SKELETON;
		if(name.startsWith("sl"))
			return EntityType.SLIME;
		if(name.startsWith("sn"))
			return EntityType.SNOWMAN;
		if(name.startsWith("sp"))
			return EntityType.SPIDER;
		if(name.startsWith("sq"))
			return EntityType.SQUID;
		if(name.startsWith("v"))
			return EntityType.VILLAGER;
		if(name.startsWith("w"))
			return EntityType.WOLF;
		if(name.startsWith("z"))
			return EntityType.ZOMBIE;

		return null;
	}
}

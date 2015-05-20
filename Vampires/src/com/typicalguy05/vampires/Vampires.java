package com.typicalguy05.vampires;

import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Vampires extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {

		getServer().getPluginManager().registerEvents(this, this);

		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof Zombie) {

					Zombie vampire = (Zombie) entity;
					Location vampireLocation = vampire.getLocation();
					Block block = vampireLocation.getWorld().getBlockAt(new Location(vampire.getWorld(), vampireLocation.getX(), (vampireLocation.getY() + 1), vampireLocation.getZ()));

					Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			        	public void run() {
			                if (!block.getType().equals(Material.AIR) || !block.getType().equals(Material.LEAVES) || !block.getType().equals(Material.LEAVES_2) || !block.getType().equals(null)) {
			                	if (vampire.getWorld().getTime() > 0 && vampire.getWorld().getTime() < 13100) {
			                		vampire.setFireTicks(20);
			                	} else {
			                		vampire.setFireTicks(0);
			                	}
			               	}
			            }
			        }, 5, 5);

				}	
			}
		}

	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vampireSpawn(CreatureSpawnEvent event) {
		
		Entity mobSpawned = event.getEntity();

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setDisplayName(ChatColor.WHITE + "Vampire Leather Jacket");
        chestplateMeta.setColor(Color.BLACK);
        chestplate.setItemMeta(chestplateMeta);
		
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setDisplayName(ChatColor.WHITE + "Vampire Leather Pants");
        leggingsMeta.setColor(Color.BLACK);
        leggings.setItemMeta(leggingsMeta);
		
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setDisplayName(ChatColor.WHITE + "Vampire Leather Boots");
        bootsMeta.setColor(Color.BLACK);
        boots.setItemMeta(bootsMeta);
        
        ItemStack fangs = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta fangsMeta = fangs.getItemMeta();
        fangsMeta.setDisplayName(ChatColor.WHITE + "Vampire Fang");
        fangs.setItemMeta(fangsMeta);
		
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(ChatColor.WHITE + "Vampire Head");
        head.setDurability((short) 1);
        head.setItemMeta(headMeta);
		
        if (mobSpawned instanceof Zombie) {
            ((LivingEntity) mobSpawned).setCustomNameVisible(false);
            ((LivingEntity) mobSpawned).setCustomName("Vampire");
            ((LivingEntity) mobSpawned).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) Double.MAX_VALUE, 256, true));
            ((LivingEntity) mobSpawned).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) Double.MAX_VALUE, 2, true));
            ((LivingEntity) mobSpawned).setMaxHealth(40);
            ((LivingEntity) mobSpawned).getEquipment().setChestplate(chestplate);
            ((LivingEntity) mobSpawned).getEquipment().setChestplateDropChance((float) 0.3);
            ((LivingEntity) mobSpawned).getEquipment().setLeggings(leggings);
            ((LivingEntity) mobSpawned).getEquipment().setLeggingsDropChance((float) 0.3);
            ((LivingEntity) mobSpawned).getEquipment().setItemInHand(fangs);
            ((LivingEntity) mobSpawned).getEquipment().setItemInHandDropChance((float) 0.2);
            ((LivingEntity) mobSpawned).getEquipment().setHelmet(head);
            ((LivingEntity) mobSpawned).getEquipment().setHelmetDropChance((float) 0.1);
            ((LivingEntity) mobSpawned).getEquipment().setBoots(boots);
            ((LivingEntity) mobSpawned).getEquipment().setBootsDropChance((float) 0.3);
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vampireBloodSuck(EntityDamageByEntityEvent event) {
		
		Entity damager = event.getDamager();
		Entity damagee = event.getEntity();
		
		if (damager instanceof Zombie) {
			((Zombie) damager).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 3, false));
			damager.getWorld().playSound(damager.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
			((LivingEntity) damagee).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 3, false));
		}
		if (!(damager instanceof Zombie)) {
			damagee.getWorld().playSound(damagee.getLocation(), Sound.HURT_FLESH, 1, 1);
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vampireDeath(EntityDeathEvent event) {
		
		Entity mobDied = event.getEntity();
        ListIterator<ItemStack> lIt = event.getDrops().listIterator();

        if (mobDied instanceof Zombie) {
        	mobDied.getWorld().playSound(mobDied.getLocation(), Sound.GHAST_SCREAM2, 1, 1);
        	
        	while (lIt.hasNext()) {

        		ItemStack stack = lIt.next();

	        	if (stack.equals(new ItemStack(Material.ROTTEN_FLESH)) || stack.equals(new ItemStack(Material.POTATO_ITEM)) || stack.equals(new ItemStack(Material.CARROT_ITEM)) || stack.equals(new ItemStack(Material.IRON_INGOT))) {
	        		lIt.remove();
	        	}
        	}
        }
	        
	}
	
}

package com.applenick.Wait;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

/************************************************
			 Created By AppleNick
Copyright © 2016 , AppleNick, All rights reserved.
			http://applenick.com
 *************************************************/
public class Wait extends JavaPlugin implements Listener {
	
	private String PREFIX = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Wait" + ChatColor.GRAY + "] ";
	
	private boolean canLogin = false; // If players can login
	private boolean waitForAll; // If the server should wait for all plugins to load
	private String waitForPlugin; //The name of a specific plugin to wait for
	private String kickMessage;// The message sent when a player tries to login
	
	@Override
	public void onEnable(){
		//Save Config
		this.saveDefaultConfig();
		
		//Check if blocking All
		waitForAll = this.getConfig().getString("wait-for").equalsIgnoreCase("ALL");
		
		//Get name of single plugin if in single mode
		if(!(waitForAll)){
			waitForPlugin = this.getConfig().getString("wait-for");
		}	
		
		
		//Get the kick message
		kickMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message"));
				
		//Register Listener
		this.getServer().getPluginManager().registerEvents(this, this);
		
		this.getServer().getConsoleSender().sendMessage(PREFIX + ChatColor.GOLD + "Waiting for " + (waitForAll ? ChatColor.GREEN + "ALL" : ChatColor.GREEN + waitForPlugin));
		
		if(waitForAll){
			toggleLogin(false);
		}
	}
	
	private void toggleLogin(boolean instant){
		if(!(instant)){
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
				public void run() {
					canLogin = true;
					console(ChatColor.GREEN + "Players can now login");
				}
			});
		}else{
			canLogin = true;
			console(ChatColor.GREEN + "Players can now login");
		}
	}
	
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event){
		//Enable logins after the specific plugin is enabled.
		if(!(waitForAll)){
			if(event.getPlugin().getName().equalsIgnoreCase(waitForPlugin)){
				toggleLogin(true);
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		if(!(canLogin)){
			event.getPlayer().kickPlayer(kickMessage);
		}else{
			return;
		}
	}
	
	private void console(String message){
		getServer().getConsoleSender().sendMessage(PREFIX + message);
	}
}

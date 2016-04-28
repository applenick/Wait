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
	
	private boolean canLogin = false; // If players can login
	private boolean waitForAll = false; // If the server should wait for all plugins to load
	private String waitForPlugin = "none"; //The name of a specfic plugin to wait for
	private String kickMessage;// The message sent when a player tries to login
	
	@Override
	public void onEnable(){
		//Save Config
		this.saveDefaultConfig();
		
		//Check if blocking All
		waitForAll = this.getConfig().getString("wait-for").equalsIgnoreCase("waitForAll");
		
		//Get name of single plugin if in single mode
		if(!(waitForAll)){
			waitForPlugin = this.getConfig().getString("wait-for");
		}	
		
		//Get the kick message
		kickMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message"));
				
		//Register Listener
		this.getServer().getPluginManager().registerEvents(this, this);
		
		if(waitForAll){
			//Schedule the server to allow login after everything is loaded.
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
				public void run() {
					canLogin = true;
				}
			}, 0L);
		}
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event){
		//Enable logins after the specfic plugin is enabled.
		if(!(waitForAll)){
			if(event.getPlugin().getName().equalsIgnoreCase(waitForPlugin)){
				canLogin = true;
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
	
}

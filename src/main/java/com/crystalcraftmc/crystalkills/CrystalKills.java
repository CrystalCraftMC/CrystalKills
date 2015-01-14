/*
 * Copyright 2015 CrystalCraftMC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.crystalcraftmc.crystalkills;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CrystalKills extends JavaPlugin{

	public void onEnable() {
		getLogger().info(ChatColor.GRAY + "CrystalKills has been initialized!");
		try {
			File database = new File(getDataFolder(), "config.yml");
			if (!database.exists()) saveDefaultConfig();
		} catch (Exception e1) {
			getLogger().info(ChatColor.DARK_RED + "CrystalKills _failed_ to initialize.");
			e1.printStackTrace();
		}
	}

	public void onDisable() {
		getLogger().info(ChatColor.GRAY + "CrystalKills has been stopped by the server.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("crystalkills") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (isPlayer(sender) && sender.isOp()) reloadMethod(sender);
			else if (!isPlayer(sender)) reloadMethod(sender);
			return true;
		}
		else if (isPlayer(sender) && sender.hasPermission("crystalkills.count") && args.length == 0) {
			Player me = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("crystalkills")) {
				int kills = me.getStatistic(Statistic.PLAYER_KILLS);
				if(kills == 0){
					me.sendMessage(ChatColor.GREEN + "The peace of the universe flows in your veins. You have not killed.");
				} else if(kills == 1){
					me.sendMessage(ChatColor.GRAY + "How does feel... to have killed a player? ");
				} else if(kills > 1){
					me.sendMessage(ChatColor.GRAY + "Your hands are covered with the blood of " + ChatColor.DARK_RED + kills + ChatColor.GRAY + " players.");
				} else if(kills < 0){
					me.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "You have achieved Ferwinn's power - the definition of minecraft peace.");
				}
				return true;
			}
			return false;
		}
		else if (cmd.getName().equalsIgnoreCase("crystalkills") && args.length == 1) {
			if (isPlayer(sender) && sender.isOp()){
				Player findPlayer = getServer().getPlayer(args[0]);
				if(findPlayer.isOnline() && findPlayer.getName() != null){
					sender.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.RED + findPlayer.getName() + ChatColor.GOLD + " has " + ChatColor.RED + findPlayer.getStatistic(org.bukkit.Statistic.PLAYER_KILLS) + ChatColor.GOLD + " kills.");
					return true;
				}
				else if (sender.isOp()) {
					sender.sendMessage(ChatColor.GRAY + "Requested player is offline - otherwise username was mispelled. ");
					sender.sendMessage(ChatColor.GRAY + "Usage: /CrystalKills [Player Name]");
				}
			}
			return false;
		}
		else if (cmd.getName().equalsIgnoreCase("crystalkills") && args.length == 3 && args[0].equalsIgnoreCase("editKills") && isInt(sender, args[2]) ) {
			if (isPlayer(sender) && sender.isOp()){
				Player findPlayer = null;
				try{
				findPlayer = getServer().getPlayer(args[1]);
				} catch (Exception e) {
					return false;
				}
				int x = toInt(sender, args[2]);
				if(findPlayer.isOnline() && findPlayer.getName() != null){
					findPlayer.setStatistic(Statistic.PLAYER_KILLS, x);
					sender.sendMessage(ChatColor.GRAY + findPlayer.getName() + " now has " + findPlayer.getStatistic(Statistic.PLAYER_KILLS));
					return true;
				}
				else{
					sender.sendMessage(ChatColor.GRAY + "Requested player is offline - otherwise username was mispelled. ");
					sender.sendMessage(ChatColor.GRAY + "Usage: /CrystalKills editKills [Player] [integer number of kills]");
				}
			}
			return false;
		}
		
		sender.sendMessage(ChatColor.GOLD + "Usage: /CrystalKills");
		return false;
	}

	private static int toInt(CommandSender sender, String temp) {
		int x;
		try {
			x = Integer.parseInt(temp);
		} catch (NumberFormatException nFE) {
			return 0;
		}
		return x;
	}

	private boolean isPlayer(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
			return false;
		} else {
			return true;
		}
	}

	private boolean reloadMethod(CommandSender sender){
		this.reloadConfig();
		sender.sendMessage(ChatColor.GRAY + "Configuration reloaded!");
		return true;
	}

	private static boolean isInt(CommandSender sender, String temp) {
		try {
			@SuppressWarnings("unused")
			int x = Integer.parseInt(temp);
		} catch (NumberFormatException nFE) {
			sender.sendMessage(ChatColor.RED + "There was a problem with the value you entered. Try again.");
			return false;
		}
		return true;
	}
}

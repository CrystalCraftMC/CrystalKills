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

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public class CrystalKills extends JavaPlugin implements Listener{
	
	public static Scoreboard ckBoard;
	public static Score scores;

	public void onEnable() {
		getLogger().info(ChatColor.GRAY + "CrystalKills has been initialized!");
		getServer().getPluginManager().registerEvents(this, this);
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

	public void sBoard () {
		ScoreboardManager manager = Bukkit.getServer().getScoreboardManager();
		Scoreboard ckboard = manager.getNewScoreboard();
		Objective obj = ckboard.registerNewObjective("Kills", "playerKills");
		obj.setDisplayName("CrystalKill");
		obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ChatColor au = ChatColor.GOLD; 
		ChatColor g = ChatColor.GRAY;
		ChatColor i = ChatColor.ITALIC;

		if(cmd.getName().equalsIgnoreCase("crystalkills")) 
		{
			//LENGTH 3
			if (args.length == 3) 
			{
				if (args[0].equalsIgnoreCase("setKills") && isInt(sender, args[2]) && sender.isOp()) {
					int num = toInt(sender, args[2]);
					String playerName = args[1];
					editKill(sender, playerName, num);
				} else if (sender.isOp()) {
					sender.sendMessage(g + "" + i + "Usage: /CrystalKills setKills <player> <Integer>");
				}
			}

			//LENGTH 2
			else if (args.length == 2) 
			{
				if (args[0].equalsIgnoreCase("check")) 
				{
					return checkPlayer(sender, args[1]);
				} else 
				{
					sender.sendMessage(g + "" + i + "Usage: /CrystalKills check <player>");
				}
				if (args[0].equalsIgnoreCase("statSync") && sender.isOp()) {
					return updatePlayerScoreToStat(args[1]);
				} else {
					sender.sendMessage(g + "" + i + "Usage: /CrystalKills sync <player>");
				}
			}

			//LENGTH 1 -- 
			else if (args.length == 1) 
			{
				if (args[0].equalsIgnoreCase("reload") && sender.isOp())
					return reloadMethod(sender);
			}

			//LENGTH 0
			else if (args.length == 0 && isPlayer(sender)) 
			{
				return ckCMD(sender);
			}
		}
		sender.sendMessage(au + "Usages: /CrystalKills [statSync,check,reload] <>");
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean editKill(CommandSender sender, String playerName, int num) {
		ChatColor au = ChatColor.GOLD; ChatColor g = ChatColor.GRAY; ChatColor r = ChatColor.RED;
		Player findPlayer = getServer().getPlayer(playerName);
		if(findPlayer.getName() != null){
			ckBoard.getObjective("Kill").getScore(findPlayer).setScore(num);
			sender.sendMessage(au + "Player: " + r + findPlayer.getName() + au + " has " + r + findPlayer.getStatistic(Statistic.PLAYER_KILLS) + au + " kills.");
			return true;
		} else {
			sender.sendMessage(g + "Requested player is offline - otherwise username was mispelled. ");
			sender.sendMessage(g + "" + ChatColor.ITALIC + "Usage: /CrystalKills <player> <positive integer>");
			return true;
		}
	}

	private boolean checkPlayer(CommandSender sender, String arg0) {
		ChatColor au = ChatColor.GOLD; 
		ChatColor g = ChatColor.GRAY; 
		ChatColor r = ChatColor.RED;

		Player findPlayer = getServer().getPlayer(arg0);
		if(findPlayer.getName() != null && findPlayer.isOnline()){
			sender.sendMessage(au + "Player: " + r + findPlayer.getName() + au + " has " + r + findPlayer.getStatistic(Statistic.PLAYER_KILLS) + au + " kills.");
			return true;
		} else {
			sender.sendMessage(g + "Requested player is offline - otherwise username was mispelled. ");
			sender.sendMessage(g + "Usage: /CrystalKills [player]");
			return true;
		}
	}

	private boolean ckCMD(CommandSender sender) {
		ChatColor g = ChatColor.GRAY;
		Player me = (Player) sender;
		int kills = me.getStatistic(Statistic.PLAYER_KILLS);
		if(kills < 0){
			me.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "You have unlocked Ferwinn's power - the definition of minecraft peace. Once lost it cannot be regained.");
			return true;
		} else if(kills == 0){
			me.sendMessage(ChatColor.GREEN + "Zero kills - The path is before you, war or peace and the choice never ending... choose well. ");
			return true;
		} else if(kills == 1){
			me.sendMessage(g + "How does feel... to have killed a player? ");
			return true;
		} else if(kills > 1){
			me.sendMessage(g + "Your hands run red with the blood of " + ChatColor.DARK_RED + kills + g + " deaths.");
			return true;
		} else if(kills > 1000){
			me.sendMessage(ChatColor.DARK_RED + "You have killed over one thousand times: " + ChatColor.GOLD + kills + ChatColor.DARK_RED + " flows in only one direction - life to death.");
			return true;
		} else if(kills > 1020){
			me.sendMessage(ChatColor.RED + "You have grown accustom to killing, blood no long weighs on your mind: " + ChatColor.GOLD + kills + ChatColor.RED + " kills");
			return true;
		} else if(kills > 2000){
			me.sendMessage(ChatColor.GRAY + "Killing is second nature for you now - lets just get the number, get this over with this: " + ChatColor.RED + kills + ChatColor.GRAY + " kills");
			return true;
		} else if(kills > 5000){
			me.sendMessage(ChatColor.DARK_PURPLE + "You are a constant companion to the grim reaper: " + ChatColor.GRAY + kills + ChatColor.DARK_PURPLE + " kills");
			return true;
		} else if(kills > 5020){
			me.sendMessage(ChatColor.DARK_GRAY + "The bodies just keep piling up: " + ChatColor.BLUE + kills + ChatColor.DARK_GRAY + " kills");
			return true;
		} else if(kills > 10000){
			me.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Your name is Ares, God of War... the number of dead is an absolute vector: " + ChatColor.MAGIC + kills + ChatColor.DARK_RED + " kills");
			return true;
		}
		sender.sendMessage("Usage: /CrystalKills");
		return true;
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
		@SuppressWarnings("unused")
		int x = 0;
		try {
			x = Integer.parseInt(temp);
		} catch (NumberFormatException nFE) {
			sender.sendMessage(ChatColor.RED + "There was a problem with "+ temp +" you entered. Try again.");
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean updatePlayerScoreToStat(String temp) {
		Player findPlayer = getServer().getPlayer(temp);
		if(findPlayer.getName() != null && findPlayer.isOnline()){
			findPlayer.getStatistic(Statistic.PLAYER_KILLS);
			findPlayer.setScoreboard(ckBoard);
			ckBoard.getObjective("Kill").getScore(findPlayer).setScore(findPlayer.getStatistic(Statistic.PLAYER_KILLS));
			return true;
		} else {
			getLogger().info(ChatColor.GRAY + "Requested player is offline - otherwise username was mispelled.");
			return true;
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		updatePlayerScoreToStat(event.getPlayer().getName());
	}
}

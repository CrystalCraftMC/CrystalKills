/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 CrystalCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.crystalcraftmc.crystalkills;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
		if (isPlayer(sender) && sender.hasPermission("crystalkills.count")) {
			Player me = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("crystalkills")) {
				int kills = me.getStatistic(org.bukkit.Statistic.PLAYER_KILLS);
				if(kills == 0){
					me.sendMessage(ChatColor.GREEN + "The peace of the universe flows in your veins. You have not killed.");
				} else if(kills == 1){
					me.sendMessage(ChatColor.GRAY + "How does feel... to have killed a player? ");
				} else if(kills > 1){
					me.sendMessage(ChatColor.DARK_GRAY + "Your hands are covered with the blood of "  + kills + " players.");
				} else if(kills < 0){
					me.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "You have achieved Ferwinn's level - the definition of minecraft peace.");
				}
				return true;
			}
		}
		return false;
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
}

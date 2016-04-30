package com.ricky30.xpdeposit.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.ricky30.xpdeposit.xpdeposit;

import ninja.leaping.configurate.ConfigurationNode;

public class commandXpBalance implements CommandExecutor
{
	private ConfigurationNode config = null;

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final Player player = (Player) src;
		this.config = xpdeposit.plugin.getConfig();
		final ConfigurationNode node = this.config.getNode(player.getUniqueId().toString());
		if (node.getValue() == null)
		{
			return CommandResult.empty();
		}
		final int Amount = this.config.getNode(player.getUniqueId().toString(), "amount").getInt();
		player.sendMessage(Text.of("Xp balance: " + Amount));
		return CommandResult.success();
	}

}

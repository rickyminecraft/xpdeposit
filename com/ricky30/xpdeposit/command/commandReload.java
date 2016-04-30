package com.ricky30.xpdeposit.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.xpdeposit.xpdeposit;

public class commandReload implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		xpdeposit.plugin.reload();
		src.sendMessage(Text.of("Done reloading"));
		return CommandResult.success();
	}

}

package com.ricky30.xpdeposit.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class commandXpDeposit implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		src.sendMessage(Text.of("XpDeposit plugin"));
		src.sendMessage(Text.of("Usage."));
		src.sendMessage(Text.of("Put a sign with these lines:"));
		src.sendMessage(Text.of("1: [xpdepot]"));
		src.sendMessage(Text.of("2: -price for picking up xp-"));
		src.sendMessage(Text.of("3: -price for putting down xp-"));
		src.sendMessage(Text.of("4: -amount of xp level to take or put-"));
		src.sendMessage(Text.of("--------------------"));
		src.sendMessage(Text.of("If you left click you put down Xp"));
		src.sendMessage(Text.of("If you right click you pick up Xp"));
		src.sendMessage(Text.of("--------------------"));
		src.sendMessage(Text.of("Commands :"));
		src.sendMessage(Text.of(""));
		src.sendMessage(Text.of("xpdeposit"));
		src.sendMessage(Text.of("xpdeposit reload"));
		src.sendMessage(Text.of("xpdeposit balance"));

		return CommandResult.success();
	}

}

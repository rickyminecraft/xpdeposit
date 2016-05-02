package com.ricky30.xpdeposit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;
import com.ricky30.xpdeposit.command.commandReload;
import com.ricky30.xpdeposit.command.commandXpBalance;
import com.ricky30.xpdeposit.command.commandXpDeposit;
import com.ricky30.xpdeposit.event.createevent;
import com.ricky30.xpdeposit.event.useevent;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "com.ricky30.xpdeposit", name = "xpdeposit", version = "1.0.1")
public class xpdeposit
{
	private EconomyService service = null;

	@Inject
	private Logger logger;
	private ConfigurationNode config = null;
	public static xpdeposit plugin;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	public ConfigurationNode getConfig()
	{
		return this.config;
	}

	public Path getDefaultConfig() 
	{
		return this.defaultConfig;
	}

	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() 
	{
		return this.configManager;
	}

	public Logger getLogger()
	{
		return this.logger;
	}

	public EconomyService getEconomy() 
	{
		return this.service;
	}

	@Listener
	public void onServerStart(GameInitializationEvent event)
	{
		getLogger().info("XpDeposit start.");
		plugin = this;
		if (Sponge.getServiceManager().provide(EconomyService.class).isPresent())
		{
			service = Sponge.getServiceManager().provide(EconomyService.class).get();
		}
		else
		{
			getLogger().error("Economy plugin missing");
		}
		InitConfigFile();

		final HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();
		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
				.description(Text.of("Reload config file"))
				.permission("xpdeposit.reload")
				.executor(new commandReload())
				.build());
		subcommands.put(Arrays.asList("balance"), CommandSpec.builder()
				.description(Text.of("get your Xp balance"))
				.permission("xpdeposit.balance")
				.executor(new commandXpBalance())
				.build());

		final CommandSpec movingobjectcommand = CommandSpec.builder()
				.description(Text.of("How to use Xpdeposit"))
				.executor(new commandXpDeposit())
				.children(subcommands)
				.build();

		Sponge.getCommandManager().register(this, movingobjectcommand, "xpdeposit");
		Sponge.getEventManager().registerListeners(this, new createevent());
		Sponge.getEventManager().registerListeners(this, new useevent());
		getLogger().info("XpDeposit started.");
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		getLogger().info("XpDeposit stop.");
	}

	private void InitConfigFile()
	{
		try
		{
			reload();
			if (!Files.exists(getDefaultConfig())) 
			{

				Files.createFile(getDefaultConfig());
				setupconfig();
			}
		}
		catch (final IOException e)
		{
			getLogger().error("Couldn't create default configuration file!");
		}
	}

	private void setupconfig()
	{
		this.config.getNode("ConfigVersion").setValue(1);
		save();
	}

	public void save()
	{
		try
		{
			getConfigManager().save(this.config);
		} catch (final IOException e) 
		{
			getLogger().error("Failed to save config file!", e);
		}
	}

	public void reload()
	{
		try
		{
			this.config = getConfigManager().load();
		} catch (final IOException e)
		{
			getLogger().error("Failed to load config file!", e);
		}
	}
}

package com.ricky30.xpdeposit.event;

import java.math.BigDecimal;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.xpdeposit.xpdeposit;

import ninja.leaping.configurate.ConfigurationNode;

public class useevent
{
	private EconomyService service = null;
	private ConfigurationNode config = null;

	@Listener
	public void onHitLeftEvent(InteractBlockEvent.Primary event, @First Player player)
	{
		this.service = xpdeposit.plugin.getEconomy();
		this.config = xpdeposit.plugin.getConfig();
		if (service != null)
		{
			final Vector3i position = event.getTargetBlock().getPosition();
			final String World = player.getWorld().getName();
			final String NodeName = World.concat(String.valueOf(position.getX()).concat(String.valueOf(position.getY()).concat(String.valueOf(position.getZ()))));
			if (this.config.getNode("XPdeposit", "signs").getChildrenMap().containsKey(NodeName))
			{
				final int signX = this.config.getNode("XPdeposit", "signs", NodeName, "X").getInt();
				final int signY = this.config.getNode("XPdeposit", "signs", NodeName, "Y").getInt();
				final int signZ = this.config.getNode("XPdeposit", "signs", NodeName, "Z").getInt();

				final Vector3i SignPosition = new Vector3i(signX, signY, signZ);
				if (position.equals(SignPosition))
				{
					final String SignWorld = this.config.getNode("XPdeposit", "signs", NodeName, "world").getString();
					if (World.equals(SignWorld))
					{
						if (service.hasAccount(player.getUniqueId()))
						{
							final Account PlayerAccount = service.getOrCreateAccount(player.getUniqueId()).get();
							final Double WithdrawAmount = this.config.getNode("XPdeposit", "signs", NodeName, "getprice").getDouble();
							final BigDecimal BDAmount = BigDecimal.valueOf(WithdrawAmount);
							int level = player.get(Keys.EXPERIENCE_LEVEL).get();
							final ConfigurationNode node = this.config.getNode(player.getUniqueId().toString());
							if (node.getValue() == null)
							{
								this.config.getNode(player.getUniqueId().toString(), "amount").setValue(0);
								xpdeposit.plugin.save();
								player.sendMessage(Text.of("Nothing left"));
							}
							else
							{
								int Amount = this.config.getNode(player.getUniqueId().toString(), "amount").getInt();
								final int GetLevel = this.config.getNode("XPdeposit", "signs", NodeName, "xplevel").getInt();
								if (Amount >= GetLevel)
								{
									PlayerAccount.withdraw(service.getDefaultCurrency(),BDAmount, Cause.of(NamedCause.owner(player)));
									Amount -= GetLevel;
									level += GetLevel;
									this.config.getNode(player.getUniqueId().toString(), "amount").setValue(Amount);
									player.offer(Keys.EXPERIENCE_LEVEL, level);
									player.sendMessage(Text.of("Money balance: " + PlayerAccount.getBalance(service.getDefaultCurrency()).toPlainString()));
									player.sendMessage(Text.of("Xp balance: " + Amount));
									xpdeposit.plugin.save();
								}
								else
								{
									player.sendMessage(Text.of("Nothing left"));
								}
							}
						}
						else
						{
							player.sendMessage(Text.of("No economy account"));
						}
					}
				}
			}
		}
		else
		{
			player.sendMessage(Text.of("Economy plugin missing"));
		}
	}

	@Listener
	public void onHitRightEvent(InteractBlockEvent.Secondary event, @First Player player)
	{
		this.service = xpdeposit.plugin.getEconomy();
		this.config = xpdeposit.plugin.getConfig();
		if (service != null)
		{
			final Vector3i position = event.getTargetBlock().getPosition();
			final String World = event.getTargetBlock().getLocation().get().getExtent().getName();
			final String NodeName = World.concat(String.valueOf(position.getX()).concat(String.valueOf(position.getY()).concat(String.valueOf(position.getZ()))));
			if (this.config.getNode("XPdeposit", "signs").getChildrenMap().containsKey(NodeName))
			{
				final int signX = this.config.getNode("XPdeposit", "signs", NodeName, "X").getInt();
				final int signY = this.config.getNode("XPdeposit", "signs", NodeName, "Y").getInt();
				final int signZ = this.config.getNode("XPdeposit", "signs", NodeName, "Z").getInt();

				final Vector3i SignPosition = new Vector3i(signX, signY, signZ);
				if (position.equals(SignPosition))
				{
					final String SignWorld = this.config.getNode("XPdeposit", "signs", NodeName, "world").getString();
					if (World.equals(SignWorld))
					{
						if (service.hasAccount(player.getUniqueId()))
						{
							final Account PlayerAccount = service.getOrCreateAccount(player.getUniqueId()).get();
							final Double WithdrawAmount = this.config.getNode("XPdeposit", "signs", NodeName, "putprice").getDouble();
							final BigDecimal BDAmount = BigDecimal.valueOf(WithdrawAmount);
							int level = player.get(Keys.EXPERIENCE_LEVEL).get();
							final ConfigurationNode node = this.config.getNode(player.getUniqueId().toString());
							int Amount = 0;
							if (node.getValue() == null)
							{
								this.config.getNode(player.getUniqueId().toString(), "amount").setValue(0);
							}
							else
							{
								Amount = this.config.getNode(player.getUniqueId().toString(), "amount").getInt();
							}
							final int GetLevel = this.config.getNode("XPdeposit", "signs", NodeName, "xplevel").getInt();
							if (level >= GetLevel)
							{
								PlayerAccount.withdraw(service.getDefaultCurrency(),BDAmount, Cause.of(NamedCause.owner(player)));
								Amount += GetLevel;
								level -= GetLevel;
								this.config.getNode(player.getUniqueId().toString(), "amount").setValue(Amount);
								player.offer(Keys.EXPERIENCE_LEVEL, level);
								player.sendMessage(Text.of("Money balance: " + PlayerAccount.getBalance(service.getDefaultCurrency()).toPlainString()));
								player.sendMessage(Text.of("Xp balance: " + Amount));
								xpdeposit.plugin.save();
							}
							else
							{
								player.sendMessage(Text.of("Nothing left"));
							}
						}
						else
						{
							player.sendMessage(Text.of("No economy account"));
						}
					}
				}
			}
		}
		else
		{
			player.sendMessage(Text.of("Economy plugin missing"));
		}
	}

	@Listener
	public void oninteractblockPrimary(ChangeBlockEvent.Break Event, @First Player player)
	{
		for (final Transaction<BlockSnapshot> transaction : Event.getTransactions())
		{
			this.config = xpdeposit.plugin.getConfig();
			final Vector3i position = transaction.getDefault().getPosition();
			final String World = transaction.getDefault().getLocation().get().getExtent().getName();
			final String NodeName = World.concat(String.valueOf(position.getX()).concat(String.valueOf(position.getY()).concat(String.valueOf(position.getZ()))));
			if (this.config.getNode("XPdeposit", "signs").getChildrenMap().containsKey(NodeName))
			{
				if (!player.hasPermission("xpdeposit.break"))
				{
					Event.setCancelled(true);
				}
				else
				{
					this.config.getNode("XPdeposit", "signs").removeChild(NodeName);
					xpdeposit.plugin.save();
				}
			}
		}
	}
}

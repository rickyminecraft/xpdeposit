package com.ricky30.xpdeposit.event;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.xpdeposit.xpdeposit;

import ninja.leaping.configurate.ConfigurationNode;

public class createevent
{
	private ConfigurationNode config = null;

	@Listener
	public void onsignevent(ChangeSignEvent event, @First Player player)
	{
		this.config = xpdeposit.plugin.getConfig();
		final Sign sign = event.getTargetTile();
		final Vector3i position = sign.getLocation().getBlockPosition();
		final World world = player.getWorld();
		final SignData text = event.getText();

		if (text.get(0).get().toPlain().equals("[xpdepot]"))
		{
			int BuyPrice = 0;
			int SellPrice = 0;
			int Xplevelnumber = 1;
			if (text.get(1).isPresent())
			{
				BuyPrice = Integer.parseInt(text.get(1).get().toPlain());
			}
			if (text.get(2).isPresent())
			{
				SellPrice = Integer.parseInt(text.get(2).get().toPlain());
			}
			if (text.get(3).isPresent())
			{
				Xplevelnumber = Integer.parseInt(text.get(3).get().toPlain());
			}
			final String NodeName = world.getName().concat(String.valueOf(position.getX()).concat(String.valueOf(position.getY()).concat(String.valueOf(position.getZ()))));
			this.config.getNode("XPdeposit", "signs", NodeName, "X").setValue(position.getX());
			this.config.getNode("XPdeposit", "signs", NodeName, "Y").setValue(position.getY());
			this.config.getNode("XPdeposit", "signs", NodeName, "Z").setValue(position.getZ());
			this.config.getNode("XPdeposit", "signs", NodeName, "world").setValue(world.getName());
			this.config.getNode("XPdeposit", "signs", NodeName, "getprice").setValue(BuyPrice);
			this.config.getNode("XPdeposit", "signs", NodeName, "putprice").setValue(SellPrice);
			this.config.getNode("XPdeposit", "signs", NodeName, "xplevel").setValue(Xplevelnumber);
			xpdeposit.plugin.save();
			text.set(sign.lines().set(0, Text.of(TextColors.RED, "XP DEPOT")));
			sign.offer(text);
			text.set(sign.lines().set(1, Text.of("Take: " + BuyPrice)));
			sign.offer(text);
			text.set(sign.lines().set(2, Text.of("Put: " + SellPrice)));
			sign.offer(text);
			text.set(sign.lines().set(3, Text.of("Xp level: " + Xplevelnumber)));
			sign.offer(text);
			xpdeposit.plugin.getLogger().info("Sign XpDeposit set");
		}
	}
}

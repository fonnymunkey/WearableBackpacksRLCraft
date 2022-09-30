package net.mcft.copy.backpacks.network;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import net.mcft.copy.backpacks.WearableBackpacks;

/** Main network class. Handles registering messages
 *  and sending them to clients as well as the server. */
public class BackpacksChannel extends SimpleNetworkWrapper {
	
	public BackpacksChannel() {
		super(WearableBackpacks.MOD_ID);

		registerMessage(MessageBackpackUpdate.Handler.class, MessageBackpackUpdate.class, 1, Side.CLIENT);
		registerMessage(MessageOpenGui.Handler.class,        MessageOpenGui.class,        2, Side.CLIENT);
		registerMessage(MessageOpenBackpack.Handler.class,   MessageOpenBackpack.class,   3, Side.SERVER);
	}
	
	/** Sends a message to a player. */
	public void sendTo(IMessage message, EntityPlayer player)
		{ sendTo(message, (EntityPlayerMP)player); }
	
	/** Sends a message to everyone on the server, except to the specified player. */
	public void sendToAll(IMessage message, EntityPlayer except)
		{ sendToAll(message, player -> (player != except)); }
	/** Sends a message to everyone on the server, except to players not matching the specified filter. */
	public void sendToAll(IMessage message, Predicate<EntityPlayer> filter) {
		for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
			if (filter.test(player)) sendTo(message, player);
	}
	
	/** Sends a message to everyone around a point. */
	public void sendToAllAround(IMessage message, World world, double x, double y, double z, double distance)
		{ sendToAllAround(message, new TargetPoint(world.provider.getDimension(), x, y, z, distance)); }
	/** Sends a message to everyone around a point, except to the specific player. */
	public void sendToAllAround(IMessage message, World world, double x, double y, double z,
	                            double distance, EntityPlayer except)
		{ sendToAllAround(message, world, x, y, z, distance, player -> (player != except)); }
	/** Sends a message to everyone around a point, except to players not matching the specified filter. */
	public void sendToAllAround(IMessage message, World world, double x, double y, double z,
	                            double distance, Predicate<EntityPlayer> filter) {
		for (EntityPlayer player : (List<EntityPlayer>)world.playerEntities) {
			if (!filter.test(player)) continue;
			double dx = x - player.posX;
			double dy = y - player.posY;
			double dz = z - player.posZ;
			if ((dx * dx + dy * dy + dz * dz) < (distance * distance))
				sendTo(message, player);
		}
	}
	
	/** Sends a message to a everyone tracking an entity. If sendToEntity is
	 *  true and the entity is a player, also sends the message to them. */
	public void sendToAllTracking(IMessage message, Entity entity, boolean sendToEntity) {
		((WorldServer)entity.world).getEntityTracker()
			.sendToTracking(entity, getPacketFrom(message));
		if (sendToEntity && (entity instanceof EntityPlayer))
			sendTo(message, (EntityPlayer)entity);
	}
	
}

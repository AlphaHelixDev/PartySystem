package de.popokaka.alphalibary.nms.npc;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.mojang.authlib.GameProfile;

import de.popokaka.alphalibary.UUID.UUIDFetcher;
import de.popokaka.alphalibary.nms.REnumPlayerInfoAction;
import de.popokaka.alphalibary.reflection.PacketUtil;
import de.popokaka.alphalibary.reflection.ReflectionUtil;

public class SimpleNPC<P extends JavaPlugin> {

	private final P plugin;
	private Location spawn;
	private OfflinePlayer skin;
	private String name;
	private GameProfile profile;
	private Object npc;
	private int id;
	private BukkitTask moveTask;

	public SimpleNPC(P pl, Location where, OfflinePlayer skin, String name) {
		this.spawn = where;
		this.skin = skin;
		this.name = name;
		this.plugin = pl;
	}

	public SimpleNPC(P pl, Location where, OfflinePlayer skin) {
		this.spawn = where;
		this.skin = skin;
		this.name = skin.getName();
		this.plugin = pl;
	}

	// Spawning
	public void spawnToPlayer(Player p) {
		try {
			Class<?> nmsServerClass = ReflectionUtil.getNmsClass("MinecraftServer");
			Class<?> nmsWorldClass = ReflectionUtil.getNmsClass("World");
			Class<?> nmsWorldServerClass = ReflectionUtil.getNmsClass("WorldServer");
			Class<?> nmsPlayerInteractManagerClass = ReflectionUtil.getNmsClass("PlayerInteractManager");

			Field id = ReflectionUtil.getNmsClass("Entity").getDeclaredField("id");
			Field name = ReflectionUtil.getNmsClass("EntityPlayer").getField("listName");
			Field gPName = GameProfile.class.getDeclaredField("name");
			Field yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw");
			Field pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
			Field lYaw = ReflectionUtil.getNmsClass("Entity").getField("lastYaw");
			Field lPitch = ReflectionUtil.getNmsClass("Entity").getField("lastPitch");
			Field aP = ReflectionUtil.getNmsClass("EntityLiving").getField("aP");
			Field aQ = ReflectionUtil.getNmsClass("EntityLiving").getField("aQ");
			Field aO = ReflectionUtil.getNmsClass("EntityLiving").getField("aO");

			this.profile = new GameProfile(UUIDFetcher.getUUID(skin.getName()), skin.getName());

			Object nmsServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());
			Object nmsWorld = spawn.getWorld().getClass().getMethod("getHandle").invoke(spawn.getWorld());
			Object nmsPIM = nmsPlayerInteractManagerClass.getConstructor(nmsWorldClass).newInstance(nmsWorld);
			Object npc = ReflectionUtil.getNmsClass("EntityPlayer").getConstructor(nmsServerClass, nmsWorldServerClass,
					GameProfile.class, nmsPlayerInteractManagerClass)
					.newInstance(nmsServer, nmsWorld, this.profile, nmsPIM);

			id.setAccessible(true);
			name.setAccessible(true);
			gPName.setAccessible(true);
			yaw.setAccessible(true);
			pitch.setAccessible(true);
			lYaw.setAccessible(true);
			lPitch.setAccessible(true);
			aP.setAccessible(true);
			aQ.setAccessible(true);
			aO.setAccessible(true);

			ReflectionUtil.getNmsClass("Entity")
					.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
					.invoke(npc, this.spawn.getX(), this.spawn.getY(), this.spawn.getZ(), this.spawn.getYaw(),
							this.spawn.getPitch());

			name.set(npc, ReflectionUtil.serializeString(this.name));
			gPName.set(this.profile, this.name);

			Object ppoPlayerInfo = PacketUtil.createPlayerInfoPacket(
					REnumPlayerInfoAction.ADD_PLAYER.getPlayerInfoAction(), this.profile, 0,
					ReflectionUtil.getEnumGamemode(this.skin), this.name);

			Object ppoNamedEntitySpawn = ReflectionUtil.getNmsClass("PacketPlayOutNamedEntitySpawn")
					.getConstructor(ReflectionUtil.getNmsClass("EntityHuman")).newInstance(npc);

			Object ppoEntityHeadRotation = ReflectionUtil.getNmsClass("PacketPlayOutEntityHeadRotation")
					.getConstructor(ReflectionUtil.getNmsClass("Entity"), byte.class)
					.newInstance(npc, toAngle(this.spawn.getYaw()));

			Object ppoEntityLook = ReflectionUtil.getNmsClass("PacketPlayOutEntity$PacketPlayOutEntityLook")
					.getConstructor(int.class, byte.class, byte.class, boolean.class)
					.newInstance(id.get(npc), toAngle(this.spawn.getYaw()), toAngle(this.spawn.getPitch()), true);

			yaw.set(npc, this.spawn.getYaw());
			pitch.set(npc, this.spawn.getPitch());
			aP.set(npc, (this.spawn.getYaw() - 90));
			aQ.set(npc, this.spawn.getYaw());
			aO.set(npc, this.spawn.getYaw());

			ReflectionUtil.sendPacket(p, ppoPlayerInfo);
			ReflectionUtil.sendPacket(p, ppoNamedEntitySpawn);
			ReflectionUtil.sendPacket(p, ppoEntityHeadRotation);
			ReflectionUtil.sendPacket(p, ppoEntityLook);
			this.setNpc(npc);
			this.setId(id.getInt(npc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void despawn() {
		Object ppoPlayerInfo = PacketUtil.createPlayerInfoPacket(REnumPlayerInfoAction.REMOVE_PLAYER, profile, 0,
				ReflectionUtil.getEnumGamemode(this.skin), this.name);

		ReflectionUtil.sendPackets(ppoPlayerInfo);

		if (this.moveTask != null)
			this.moveTask.cancel();
		this.moveTask = null;
	}

	// Utils

	private byte toAngle(float v) {
		return (byte) ((int) (v * 256.0F / 360.0F));
	}

	public Object getNpc() {
		return npc;
	}

	public void setNpc(Object npc) {
		this.npc = npc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public P getPlugin() {
		return plugin;
	}
}

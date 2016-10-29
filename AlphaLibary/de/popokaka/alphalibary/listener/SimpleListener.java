package de.popokaka.alphalibary.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import de.popokaka.alphalibary.AlphaPlugin;
import de.popokaka.alphalibary.utils.Util;

public class SimpleListener<P extends AlphaPlugin, R> extends Util<P, R> implements Listener {
	
	public SimpleListener(P plugin, R register) {
		super(plugin, register);
		Bukkit.getPluginManager().registerEvents(this, getPluginInstance());
	}
}

package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends SimpleListener<PartySystem, Register> {

    public JoinListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().getInventory().setItem(getRegister().getInventoryFile().getItem("Items.join.open GUI").getSlot(), getRegister().getInventoryFile().getItem("Items.join.open GUI").getItemStack());
    }
}

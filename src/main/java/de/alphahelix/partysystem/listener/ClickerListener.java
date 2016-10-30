package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickerListener extends SimpleListener<PartySystem, Register> {

    public ClickerListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (p.getInventory().getItemInMainHand() == null) return;
        if (p.getInventory().getItemInMainHand().getType() != getRegister().getInventoryFile().getItem("Items.join.open GUI").getItemStack().getType())
            return;
        if (e.getAction() == Action.PHYSICAL) return;

        e.setCancelled(true);
        getRegister().getSelectInventory().openInventory(p);
    }
}

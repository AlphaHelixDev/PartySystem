package de.alphahelix.partysystem.listener;

import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.listener.SimpleListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends SimpleListener<PartySystem, Register> {

    public ChatListener(PartySystem plugin, Register register) {
        super(plugin, register);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if(getRegister().getInventoryClickListener().isAboutToCreateParty(p)) {
            getRegister().getInventoryClickListener().removePlayerFromList(p);
            String msg = e.getMessage();
            e.setCancelled(true);

            if(Bukkit.getPlayer(msg) == null) {
                p.sendMessage(getPluginInstance().getPrefix() + getRegister().getMessageFile().getColorString("Player.not online").replace("[player]", msg));
            } else {
                //TODO: send invitation
            }
        }
    }
}

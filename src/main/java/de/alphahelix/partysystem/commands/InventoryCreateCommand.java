package de.alphahelix.partysystem.commands;

import de.alphahelix.partysystem.LegalInventories;
import de.alphahelix.partysystem.PartySystem;
import de.alphahelix.partysystem.Register;
import de.popokaka.alphalibary.command.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InventoryCreateCommand extends SimpleCommand<PartySystem, Register> {

    public InventoryCreateCommand(PartySystem plugin, Register register) {
        super(plugin, register, "createGUI", "Create/Customize a inventory", "cGUI", "cInv", "createInventory");
    }

    @Override
    public boolean execute(CommandSender cs, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player p = (Player) cs;

        if (args.length == 3) {
            if (p.hasPermission("alphaparties.createGUI")) {
                if (LegalInventories.getStringNameList().contains(args[0].toUpperCase())) {
                    getRegister().getInventoryCreateListener().addCreator(p);
                    getRegister().getInventoryCreateListener().setWhich(p, LegalInventories.valueOf(args[0].toUpperCase()));
                    p.openInventory(Bukkit.createInventory(p, Integer.parseInt(args[2]), args[1]));
                } else {
                    p.sendMessage(getPlugin().getPrefix() + "§7You can't create this inventory!" +
                            "\n Only ["+LegalInventories.values()+"] are allowed");
                }
            } else {
                p.sendMessage(getPlugin().getPrefix() + getRegister().getMessageFile().getColorString("Player.not enough permissions"));
            }
        } else {
            if (p.hasPermission("alphaparties.createGUI")) {
                p.sendMessage(getPlugin().getPrefix() + "§7The syntax is as the following: " +
                        "\n /createGUI <whichInventory> <newName> <slots>");
            } else {
                p.sendMessage(getPlugin().getPrefix() + getRegister().getMessageFile().getColorString("Player.not enough permissions"));
            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender cs, String label, String[] args) {
        return null;
    }
}

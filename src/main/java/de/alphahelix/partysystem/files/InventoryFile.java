package de.alphahelix.partysystem.files;

import de.alphahelix.partysystem.PartySystem;
import de.popokaka.alphalibary.file.SimpleFile;
import org.bukkit.Material;

public class InventoryFile extends SimpleFile<PartySystem> {

    public InventoryFile(PartySystem pl) {
        super("plugins/PartySystem", "inventories.pss", pl);
    }

    public void addValues() {
        setInventoryInformations("Inventories.main", "Alpha Parties", 9);

        setItem("Items.join.open GUI item", Material.CHEST, 5, "&aParties", " ");

        setItem("Items.select.invite item", Material.NAME_TAG, 4, "&eInvite a player", "&7Invites a player to join", "&7you in a new party.");

        setItem("Items.select.view invites", Material.BOOK, 6, "&eView Invites", "&7Manage invites to parties.");
    }
}

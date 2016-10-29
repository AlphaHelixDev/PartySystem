package de.popokaka.alphalibary.inventorys;


import de.popokaka.alphalibary.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class InventoryItem {

    private ItemStack is;
    private int slot;
    private String name;
    private String[] lore;

    public InventoryItem(ItemStack is, int slot, String name, String... lore) {
        this.is = is;
        this.slot = slot;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack getItemStack() {
        return new ItemBuilder(this.is.getType()).setAmount(this.is.getAmount()).setLore(this.lore).setDamage(this.is.getDurability()).setName(this.name).build();
    }

    public int getSlot() {
        return slot;
    }

    public InventoryItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public String getName() {
        return name;
    }

    public InventoryItem setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryItem setIs(ItemStack is) {
        this.is = is;
        return this;
    }
}

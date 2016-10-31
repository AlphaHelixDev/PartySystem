package de.popokaka.alphalibary.inventorys;


import de.popokaka.alphalibary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryItem {

    private ItemStack is;
    private int slot, damage;
    private String name;
    private String[] lore;

    public InventoryItem(ItemStack is, int slot, String name, String... lore) {
        this.is = is;
        this.slot = slot;
        this.name = name;
        this.lore = lore;
    }

    public InventoryItem(Material m, int slot, int dmg, String name, String... lore) {
        this.is = new ItemStack(m);
        this.slot = slot;
        this.damage = dmg;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack getItemStack() {
        return new ItemBuilder(this.is.getType()).setAmount(this.is.getAmount()).setLore(this.lore).setDamage(this.is.getDurability()).setName(this.name.replace("&", "§")).build();
    }

    public int getSlot() {
        return slot;
    }

    public InventoryItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public String getName() {
        return name.replace("&" , "§");
    }

    public InventoryItem setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryItem setItemstack(ItemStack is) {
        this.is = is;
        return this;
    }

    public String[] getLore() {
        return lore;
    }

    public InventoryItem setLore(String[] lore) {
        this.lore = lore;
        return this;
    }

    public int getDamage() {
        return damage;
    }

    public InventoryItem setDamage(int damage) {
        this.damage = damage;
        return this;
    }
}

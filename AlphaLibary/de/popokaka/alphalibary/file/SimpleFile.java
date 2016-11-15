package de.popokaka.alphalibary.file;

import de.alphahelix.partysystem.ItemFunction;
import de.alphahelix.partysystem.ItemFunctionEnum;
import de.alphahelix.partysystem.Prompts;
import de.popokaka.alphalibary.AlphaPlugin;
import de.popokaka.alphalibary.inventorys.InventoryItem;
import de.popokaka.alphalibary.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleFile<P extends AlphaPlugin> extends YamlConfiguration {

    private P pl;
    private File source = null;

    /**
     * Create a new SimpleFile inside the given path with the name 'name'
     *
     * @param path the path where the file should be created in
     * @param name the name which the file should have
     */
    public SimpleFile(String path, String name, P pl) {
        setPluginInstance(pl);
        source = new File(path, name);
        createIfNotExist();
    }

    public SimpleFile(P plugin, String name) {
        if (plugin == null) {
            return;
        }
        setPluginInstance(plugin);
        source = new File(plugin.getDataFolder().getPath(), name);
        createIfNotExist();
    }

    /**
     * Convert a normal File into a SimpleFile
     *
     * @param f the old File which you want to convert
     */
    public SimpleFile(File f, P pl) {
        setPluginInstance(pl);
        source = f;
        createIfNotExist();
    }

    /**
     * Finish the setup of the SimpleFile
     */
    private void finishSetup() {
        try {
            load(source);
        } catch (Exception ignored) {

        }
    }

    public void addValues() {

    }

    /**
     * Create a new SimpleFile if it's not existing
     */
    private void createIfNotExist() {

        options().copyDefaults(true);
        if (source == null || !source.exists()) {
            try {
                source.createNewFile();
            } catch (IOException e) {
                new BukkitRunnable() {
                    public void run() {
                        try {
                            source.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskLaterAsynchronously(getPluginInstance(), 20);
            }
        }
        finishSetup();
    }

    /**
     * Get a colored String out of e.g.(&aHey)
     *
     * @param path the path inside your file
     * @return the String with Colors
     */
    public String getColorString(String path) {
        if (!contains(path))
            return "";

        try {
            String toReturn = getString(path);
            return ChatColor.translateAlternateColorCodes('&', toReturn);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Save a ItemStackArray inside an file
     *
     * @param path  The path inside the file where the ItemStackArray should be
     *              serialized to
     * @param array The ItemStackArray you want to serialize
     */
    public void setItemStackArray(String path, ItemStack... array) {
        ArrayList<String> gInfo = new ArrayList<>();
        ArrayList<String> mInfo = new ArrayList<>();
        for (ItemStack s : array) {
            if (s == null)
                continue;
            // Material:Amout:Damage
            gInfo.add(s.getType().name().toLowerCase() + ":" + s.getAmount() + ":" + s.getDurability() + ":"
                    + s.getEnchantments());

            // Name:ItemFlags:Lore
            ItemMeta m = s.getItemMeta();
            mInfo.add(m.getDisplayName() + ":" + m.getItemFlags());
        }
        set(path, gInfo);
        set(path + ".meta", mInfo);
        save();
    }

    public void setInventoryInformations(String path, String title, int lines) {
        setDefault(path + ".title", title);
        setDefault(path + ".lines", lines);
    }

    public void updateInventoryInformations(String path, String title, int lines) {
        set(path + ".title", title);
        set(path + ".lines", lines);
    }

    public String serializeItem(Material is, int slot, String name, int dmg, ItemFunction function, String... lore) {
        String addLore = "";
        for (String l : lore) {
            addLore += "~" + l;
        }
        return transformMaterial(is) + ":" + slot + ":" + name + ":" + dmg + ":" + transformFunction(function) + ":" + addLore;
    }

    public InventoryItem deserializeItem(String str) {
        String[] a = str.split(":");

        Material m = transformMaterialString(a[0]);
        int slot = Integer.parseInt(a[1]);
        String name = a[2];
        short dmg = Short.parseShort(a[3]);
        ItemFunction function = transformFunctionString(a[4]);
        String[] lore = a[5].split("~");

        return new InventoryItem(new ItemStack(m, 1, dmg), slot, name, function, lore);
    }

    public ArrayList<InventoryItem> getItemsFromInventory(String path) {
        ArrayList<InventoryItem> tR = new ArrayList<>();

        tR.addAll(getStringList(path).stream().map(this::deserializeItem).collect(Collectors.toList()));

        return tR;
    }

    public void setItem(String path, Material is, int slot, String name, int dmg, ItemFunction function, String... lore) {
        setDefault(path + ".name", name);
        setDefault(path + ".slot", slot);
        setDefault(path + ".material", transformMaterial(is));
        setDefault(path + ".function", transformFunction(function));
        setDefault(path + ".damage", dmg);
        setDefault(path + ".lore", Arrays.asList(lore));
    }

    public InventoryItem getItem(String path) {
        String name = getColorString(path + ".name");
        List<String> loreList = getColoredStringList(path + ".lore");
        int slot = getSlot(path + ".slot");
        short dmg = (short) getInt(path + ".damage");
        Material m = transformMaterialString(getString(path + ".material"));
        ItemFunction function = transformFunctionString(getString(path + ".function"));
        return new InventoryItem(new ItemStack(m, 1, dmg), slot, name, function, loreList.toArray(new String[loreList.size()]));
    }

    public ArrayList<String> getColoredStringList(String path) {
        return getStringList(path).stream().map(v -> v.replace("&", "§")).collect(Collectors.toCollection(ArrayList::new));
    }

    private int getSlot(String path) {
        return getInt(path);
    }

    public void setInventory(String path, Inventory i) {
        updateInventoryInformations(path + ".information", i.getTitle(), i.getSize() / 9);
        ConfigurationSection cfgs = getConfigurationSection(path + ".content");
        for (ItemStack is : i.getContents()) {
            if (is == null) continue;
            if (!is.hasItemMeta()) {
                setItem(path + ".content." + Integer.toString(cfgs.getKeys(false).size()),
                        is.getType(),
                        i.first(is),
                        is.getType().name().toLowerCase(),
                        is.getDurability(),
                        new ItemFunction(ItemFunctionEnum.NONE),
                        "");
            } else if (!(is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore())) {
                setItem(path + ".content." + Integer.toString(cfgs.getKeys(false).size()),
                        is.getType(),
                        i.first(is),
                        is.getType().name().toLowerCase(),
                        is.getDurability(),
                        new ItemFunction(ItemFunctionEnum.NONE),
                        "");
            } else if (is.getItemMeta().hasDisplayName() && !is.getItemMeta().hasLore()) {
                setItem(path + ".content." + Integer.toString(cfgs.getKeys(false).size()),
                        is.getType(),
                        i.first(is),
                        is.getItemMeta().getDisplayName(),
                        is.getDurability(),
                        new ItemFunction(ItemFunctionEnum.NONE),
                        "");
            } else if (!is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore()) {
                setItem(path + ".content." + Integer.toString(cfgs.getKeys(false).size()),
                        is.getType(),
                        i.first(is),
                        is.getType().name().toLowerCase(),
                        is.getDurability(),
                        new ItemFunction(ItemFunctionEnum.NONE),
                        is.getItemMeta().getLore().toArray(new String[is.getItemMeta().getLore().size()]));
            } else {
                setItem(path + ".content." + Integer.toString(cfgs.getKeys(false).size()),
                        is.getType(),
                        i.first(is),
                        is.getItemMeta().getDisplayName(),
                        is.getDurability(),
                        new ItemFunction(ItemFunctionEnum.NONE),
                        is.getItemMeta().getLore().toArray(new String[is.getItemMeta().getLore().size()]));
            }
            i.clear(i.first(is));
        }
        save();
    }

    public void setInventory(String path, String title, int lines, InventoryItem... ii) {
        setInventoryInformations(path + ".information", title, lines);
        for (InventoryItem is : ii) {
            if (is == null) continue;
            if (getConfigurationSection(path + ".content") == null) {
                setItem(path + ".content." + Integer.toString(0),
                        is.getItemStack().getType(), is.getSlot(), is.getName(), is.getItemStack().getDurability(),
                        is.getFunction(), is.getLore());
            } else
                setItem(path + ".content." + Integer.toString(getConfigurationSection(path + ".content").getKeys(false).size()),
                        is.getItemStack().getType(), is.getSlot(), is.getName(), is.getItemStack().getDurability(),
                        is.getFunction(), is.getLore());
        }
    }

    public Inventory getInventory(String path) {
        String title = getColorString(path + ".information.title").replace("_", " ");
        int size = getInt(path + ".information.lines") * 9;
        ArrayList<InventoryItem> stacks = getConfigurationSection(path + ".content").getKeys(false).stream().map(index -> getItem(path + ".content." + index)).collect(Collectors.toCollection(ArrayList::new));

        Inventory inv = Bukkit.createInventory(null, size, title);

        for (InventoryItem ii : stacks) {
            if (ii.getSlot() > inv.getSize()) return inv;
            inv.setItem(ii.getSlot(), ii.getItemStack());
        }

        return inv;
    }

    public InventoryItem scanInventoryForItemname(String path, String name) {
        for (String index : getConfigurationSection(path + ".content").getKeys(false))
            if (getItem(path + ".content." + index).getName().equals(name))
                return getItem(path + ".content." + index);
        return null;
    }

    /**
     * Get a ItemStackArray out of an file
     *
     * @param path The path where the ItemStackArray should be serialized in
     * @return The ItemStackArray at the given path
     */
    public ItemStack[] getItemStackArray(String path) {
        List<String> gInfo = getStringList(path);
        List<String> mInfo = getStringList(path + ".meta");
        ArrayList<ItemStack> tr = new ArrayList<>();

        for (String infos : gInfo) {
            String[] g = infos.split(":");

            Material mat = Material.getMaterial(g[0]);
            int amount = Integer.parseInt(g[1]);
            short dura = Short.parseShort(g[2]);

            for (String infosB : mInfo) {
                String[] m = infosB.split(":");

                String name = m[0];
                ItemFlag ifg = ItemFlag.valueOf(m[1].replace(" ", "_").toUpperCase());

                tr.add(new ItemBuilder(mat).setAmount(amount).setDamage(dura).setName(name).addItemFlags(ifg).build());
            }
        }

        return tr.toArray(new ItemStack[tr.size()]);
    }

    public void setMaterialStringList(String path, String... array) {
        ArrayList<String> stacks = new ArrayList<>();
        Collections.addAll(stacks, array);
        set(path, stacks);
        save();
    }

    public List<String> getMaterialStringList(String path) {
        return getStringList(path);
    }

    public void setItemStackList(String path, ItemStack... array) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        Collections.addAll(stacks, array);
        set(path, stacks);
    }

    @SuppressWarnings("unchecked")
    public List<ItemStack> getItemStackList(String path) {
        return (List<ItemStack>) getList(path);
    }

    public void setLocation(String path, Location loc, boolean deserialized) {
        if (deserialized) {
            String location = loc.getX() + "," + loc.getY() + "," + loc.getZ() + ","
                    + String.valueOf(loc.getWorld().getName()) + "," + loc.getYaw() + "," + loc.getPitch();
            set(path, location);
        } else {
            set(path + ".x", loc.getX());
            set(path + ".y", loc.getY());
            set(path + ".z", loc.getZ());
            set(path + ".world", loc.getWorld().getName());
            set(path + ".yaw", loc.getYaw());
            set(path + ".pitch", loc.getPitch());
        }
        save();
    }

    public NotInitLocation getLocation(String path, boolean serialized) {

        if (serialized) {
            try {

                String s = getString(path);
                String[] array = s.split(",");
                double x = Double.parseDouble(array[0]);
                double y = Double.valueOf(array[1]);
                double z = Double.valueOf(array[2]);
                String world = array[3];
                float yaw = Float.valueOf(array[4]);
                float pitch = Float.valueOf(array[5]);

                return new NotInitLocation(x, y, z, world, yaw, pitch);
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("Die Location war nicht deserialized");
            }
            return null;
        } else {

            double x;
            double y;
            double z;
            String world = "";
            float yaw = 0F;
            float pitch = 0F;

            try {
                x = getDouble(path + ".x");
                y = getDouble(path + ".y");
                z = getDouble(path + ".z");
            } catch (Exception e) {
                System.out.println("Location " + path + ": " + ChatColor.DARK_RED + "Konnte nicht gelesen werden!");
                return null;
            }

            try {
                world = String.valueOf(get(path + ".world"));
            } catch (Exception e) {
                System.out.println("Location " + path + ": Weltname nicht vorhanden!");
            }
            try {
                yaw = getLong(path + ".yaw");
                pitch = getLong(path + ".pitch");
            } catch (Exception e) {
                System.out.println("Location " + path + ": Weltname nicht vorhanden!");
            }

            return new NotInitLocation(x, y, z, world, yaw, pitch);
        }
    }

    /**
     * Save & load the file
     */
    public void save() {
        try {
            save(source);
        } catch (IOException ignored) {
        }
    }

    /**
     * Add a new value to your file
     *
     * @param path  The path where the value should be saved at
     * @param value The value which you want to save inside your file
     */
    public void setDefault(String path, Object value) {
        if (value instanceof String)
            value = ((String) value).replaceAll("§", "&");

        addDefault(path, value);
        save();
    }

    /**
     * @return the pl
     */
    public P getPluginInstance() {
        return this.pl;
    }

    /**
     * @param pl the pl to set
     */
    public void setPluginInstance(P pl) {
        this.pl = pl;
    }

    public void setMaterial(String path, Material material) {
        this.setDefault(path, material.name().replace("_", " ").toLowerCase());
    }

    public String transformMaterial(Material m) {
        return m.name().replace("_", " ").toLowerCase();
    }

    public Material transformMaterialString(String str) {
        return Material.getMaterial(str.replace(" ", "_").toUpperCase());
    }

    /**
     * Serilize an {@code ItemFunction} to:
     * <p>
     * itemFunctionEnum-promt-inventoryInsideFile-isPromptItem
     *
     * @param i the ItemFunction to serialize
     * @return the serialized ItemFunction
     */
    public String transformFunction(ItemFunction i) {
        return i.getItemFunctionEnum() + "-" + i.getPrompt() + "-" + i.getInventory() + "-" + i.isPromptingItem();
    }

    public ItemFunction transformFunctionString(String str) {
        String[] vars = str.split("-");

        ItemFunctionEnum itemFunctionEnum = ItemFunctionEnum.valueOf(vars[0]);
        Prompts prompts = Prompts.valueOf(vars[1]);
        String inventory = vars[2];
        boolean isPromtItem = Boolean.getBoolean(vars[3]);

        return new ItemFunction(itemFunctionEnum).setPrompt(prompts).setInventory(inventory).setPromptingItem(isPromtItem);
    }

    public boolean configContains(String arg) {
        boolean boo = false;
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(this.getKeys(false));
        for (String key : keys)
            if (key.equalsIgnoreCase(arg))
                boo = true;

        return boo;

    }
}

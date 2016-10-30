package de.popokaka.alphalibary.file;

import de.popokaka.alphalibary.AlphaPlugin;
import de.popokaka.alphalibary.inventorys.InventoryItem;
import de.popokaka.alphalibary.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

    public void setItem(String path, Material is, int slot, String name, int dmg, String... lore) {
        setDefault(path + ".name", name);
        setDefault(path + ".slot", slot);
        setDefault(path + ".material", is.name().toLowerCase().replace("_", " "));
        setDefault(path + ".damage", dmg);
        setDefault(path + ".lore", Arrays.asList(lore));
    }

    public String serializeItem(Material is, int slot, String name, int dmg, String... lore) {
        String addLore = "";
        for (String l : lore) {
            addLore += "~" + l;
        }
        return transformMaterial(is) + ":" + slot + ":" + name + ":" + dmg + ":" + addLore;
    }

    public InventoryItem deserializeItem(String str) {
        String[] a = str.split(":");

        Material m = transformString(a[0]);
        int slot = Integer.parseInt(a[1]);
        String name = a[2];
        short dmg = Short.parseShort(a[3]);
        String[] lore = a[4].split("~");

        return new InventoryItem(new ItemStack(m, 1, dmg), slot, name, lore);
    }

    public InventoryItem getItem(String path) {
        String name = getColorString(path + ".name");
        List<String> loreList = getColoredStringList(path + ".lore");
        int slot = getSlot(path + ".slot");
        short dmg = (short) getInt(path + ".damage");
        Material m = getMaterial(path + ".material");
        return new InventoryItem(new ItemStack(m, 1, dmg), slot, name, loreList.toArray(new String[loreList.size()]));
    }

    public ArrayList<String> getColoredStringList(String path) {
        return getStringList(path).stream().map(v -> v.replace("&", "§")).collect(Collectors.toCollection(ArrayList::new));
    }

    private int getSlot(String path) {
        return getInt(path) - 1;
    }

    public void setInventory(String path, Inventory i) {
        setInventoryInformations(path + ".information", i.getTitle(), i.getSize() / 9);
        ArrayList<String> items = new ArrayList<>();
        for (ItemStack is : i.getContents()) {
            if (is == null) continue;
            items.add(serializeItem(is.getType(), i.first(is), is.getItemMeta().getDisplayName(), is.getDurability(), is.getItemMeta().getLore().toArray(new String[is.getItemMeta().getLore().size()])));
            i.remove(is);
        }
        setDefault(path + ".content", items);
    }

    public void setInventory(String path, String title, int lines, InventoryItem... ii) {
        setInventoryInformations(path + ".information", title, lines / 9);
        ArrayList<String> items = new ArrayList<>();
        for (InventoryItem is : ii) {
            if (is == null) continue;
            items.add(serializeItem(is.getItemStack().getType(), is.getSlot(), is.getName(), is.getItemStack().getDurability(), is.getItemStack().getItemMeta().getLore().toArray(new String[is.getItemStack().getItemMeta().getLore().size()])));
        }
        setDefault(path + ".content", items);
    }

    public Inventory getInventory(String path) {
        String title = getColorString(path + ".information.title");
        int size = getInt(path + ".information.lines") * 9;
        ArrayList<InventoryItem> stacks = getStringList(path + ".content").stream().map(this::deserializeItem).collect(Collectors.toCollection(ArrayList::new));

        Inventory inv = Bukkit.createInventory(null, size, title);

        for(InventoryItem ii : stacks) {
            inv.setItem(ii.getSlot(), ii.getItemStack());
        }

        return inv;
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

    public Material transformString(String str) {
        return Material.getMaterial(str.replace(" ", "_").toUpperCase());
    }

    public Material getMaterial(String path) {
        return Material.getMaterial(getString(path).replace(" ", "_").toUpperCase());
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

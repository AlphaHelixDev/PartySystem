package de.popokaka.alphalibary.mysql;

import de.popokaka.alphalibary.AlphaPlugin;
import de.popokaka.alphalibary.file.SimpleFile;

class MySQLFileManager {
	
	private static AlphaPlugin plugin;
	
	public MySQLFileManager(AlphaPlugin plugin) {
		MySQLFileManager.plugin = plugin;
	}

	private SimpleFile<AlphaPlugin> getMySQLFile() {
		return new SimpleFile<AlphaPlugin>(MySQLAPI.getMySQLFilePath(), "mysql.uhc", plugin);	
	}
	
	public void setStandardMySQL() {		
		SimpleFile<AlphaPlugin> cfg = getMySQLFile();
		
		cfg.setDefault("username", "root");
		cfg.setDefault("password", "password");	
		cfg.setDefault("database", "localhost");		
		cfg.setDefault("host", "localhost");		
		cfg.setDefault("port", "3306");
		
		cfg.save();	
	}
	
	public void readMySQL() {
		SimpleFile<AlphaPlugin> cfg = getMySQLFile();

		MySQLAPI.setMySQLConnection(cfg.getString("username"), cfg.getString("password"), cfg.getString("database"), cfg.getString("host"), cfg.getString("port"));		
	}
}
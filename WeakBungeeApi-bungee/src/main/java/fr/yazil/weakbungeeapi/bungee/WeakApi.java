package fr.yazil.weakbungeeapi.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class WeakApi extends Plugin{

	private String key;
	private int port;
	
	@Override
	public void onEnable() {
		
		if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

   
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			key = configuration.getString("key");
			port = configuration.getInt("port");
			
			@SuppressWarnings("unused")
			RespWeb web = new RespWeb(key, port);
			
        } catch (IOException e) {
			e.printStackTrace();
		}
		
		super.onEnable();
	}

}

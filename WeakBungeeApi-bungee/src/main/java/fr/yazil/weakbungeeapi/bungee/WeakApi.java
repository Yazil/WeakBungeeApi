package fr.yazil.weakbungeeapi.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class WeakApi extends Plugin implements Listener{

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
        
        getProxy().registerChannel( "weakbungee:tp" );
        getProxy().getPluginManager().registerListener(this, this);
        
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
	
	@EventHandler
	public void on(PluginMessageEvent event) {

        if ( !event.getTag().equalsIgnoreCase( "my:channel" ) )
        {
            return;
        }
        
        String action = null;

        ArrayList<String> received = new ArrayList<>();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            action = in.readUTF();

            while (in.available() > 0) {
                received.add(in.readUTF());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (action == null) return;

        if (action.equalsIgnoreCase("teleport")) {
            ProxiedPlayer from = getProxy().getPlayer(received.get(0));
            ProxiedPlayer to = getProxy().getPlayer(received.get(1));
            assert to != null;
            assert from != null;
            teleport(from, to);
        }
	}
	
	public void teleportt(ProxiedPlayer from, ProxiedPlayer to)
    {

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);
        try {

            out.writeUTF("Teleport");
            out.writeUTF(from.getName());
            out.writeUTF(to.getName());

            from.getServer().getInfo()
                    .sendData("weakbungee:tp", byteArrayOut.toByteArray());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	
	public void teleport(ProxiedPlayer from, ProxiedPlayer to)
    {
		
		
        if (from.getServer().getInfo() != to.getServer().getInfo())
            from.connect(to.getServer().getInfo());

        @SuppressWarnings("unused")
		ScheduledTask schedule = ProxyServer.getInstance().getScheduler()
                .schedule(this, () -> teleportt(from, to), (long) 1, TimeUnit.MILLISECONDS);
    }

}

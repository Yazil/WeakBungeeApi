package fr.yazil.weakbungeeapi.spigot;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class WeakBungeeApi extends JavaPlugin implements PluginMessageListener {

	private int port;
	private String key;
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	public int getTotalPlayerCount() {

		HttpGet http = new HttpGet("http://localhost:" + port + "/api/" + key + "/global/playercount");

		try {
			CloseableHttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				int playerCount = 0;
				try {
					playerCount = Integer.parseInt(result);

				} catch (NumberFormatException e) {
					System.out.println("API ERROR: " + result);
				}

				return playerCount;
			}
		} catch (IOException e) {
			System.out.println("Failed to contact api");
			e.printStackTrace();
		}

		return 0;
	}

	public String[] getGlobalPlayerList() {

		HttpGet http = new HttpGet("http://localhost:" + port + "/api/" + key + "/global/playerlist");

		try {
			CloseableHttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);

				String[] players = result.split(",");

				return players;
			}
		} catch (IOException e) {
			System.out.println("Failed to contact api");
			e.printStackTrace();
		}

		return null;
	}

	public String[] getServerPlayerList(String serverName) {

		HttpGet http = new HttpGet(
				"http://localhost:" + port + "/api/" + key + "/server/" + serverName + "/playerlist");

		try {
			CloseableHttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);

				String[] players = result.split(",");

				return players;
			}
		} catch (IOException e) {
			System.out.println("Failed to contact api");
			e.printStackTrace();
		}

		return null;
	}

	public int getServerPlayerCount(String server) {

		HttpGet http = new HttpGet("http://localhost:" + port + "/api/" + key + "/server/" + server + "/playercount");

		try {
			CloseableHttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				int playerCount = 0;
				try {
					playerCount = Integer.parseInt(result);

				} catch (NumberFormatException e) {
					System.out.println("API ERROR: " + result);
				}

				return playerCount;
			}
		} catch (IOException e) {
			System.out.println("Failed to contact api");
			e.printStackTrace();
		}

		return 0;
	}
	
	public void teleportPlayerToPlayer(Player from, Player to) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("TpRequest");
		out.writeUTF(from.getName());
		out.writeUTF(to.getName());
		
		from.sendPluginMessage(this, "weakbungee:tp", out.toByteArray());
	}

	public boolean getServerStatus(String server) {

		HttpGet http = new HttpGet("http://localhost:" + port + "/api/" + key + "/server/" + server + "/status");

		try {
			CloseableHttpResponse response = httpClient.execute(http);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				boolean status = true;

				status = Boolean.parseBoolean(result);

				return status;
			}
		} catch (IOException e) {
			System.out.println("Failed to contact api");
			e.printStackTrace();
		}

		return false;
	}
	

	@Override
	public void onEnable() {

		this.saveDefaultConfig();

		port = this.getConfig().getInt("port");
		key = this.getConfig().getString("key");
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "weakbungee:tp");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "weakbungee:tp", this);
		
		super.onEnable();
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		 if (!channel.equals("weakbungee:tp")) return;

	        String action = null;

	        ArrayList<String> received = new ArrayList<>();

	        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

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
	            Player from = Bukkit.getServer().getPlayer(received.get(0));
	            Player to = Bukkit.getServer().getPlayer(received.get(1));
	            assert to != null;
	            assert from != null;
	            from.teleport(to);
	        }
		
	}

}

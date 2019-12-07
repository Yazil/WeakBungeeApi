package fr.yazil.weakbungeeapi.spigot;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class WeakBungeeApi extends JavaPlugin {

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
	
	public String getServerMotd(String host,int port) {
		
		PingServer p = new PingServer(host, port);

		return p.getMotd();
	}

	@Override
	public void onEnable() {

		this.saveDefaultConfig();

		port = this.getConfig().getInt("port");
		key = this.getConfig().getString("key");

		super.onEnable();
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

}

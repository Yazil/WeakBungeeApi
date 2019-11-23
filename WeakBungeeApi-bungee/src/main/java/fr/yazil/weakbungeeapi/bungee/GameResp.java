package fr.yazil.weakbungeeapi.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GameResp {

	private static ProxyServer p = ProxyServer.getInstance();
	
	public static int getPlayerCount() {
		
		
		return p.getOnlineCount();	
	}
	
	public static boolean serverExist(String name) {
		
		return p.getServers().containsKey(name);
	}
	
	public static int getServerPlayerCount(String serverName) {
		return p.getServerInfo(serverName).getPlayers().size();
	}
	
	public static String getFormatedGlobalPlayerList() {
		StringBuilder sb = new StringBuilder();
		for(ProxiedPlayer player : p.getPlayers()) {
			sb.append(player.getName() + ",");
		}
		
		return sb.toString();
	}
	
	public static String getFormatedServerPlayerList(String serverName) {
		StringBuilder sb = new StringBuilder();
		for(ProxiedPlayer player : p.getServerInfo(serverName).getPlayers()) {
			sb.append(player.getName() + ",");
		}
		
		return sb.toString();
	}
	
}

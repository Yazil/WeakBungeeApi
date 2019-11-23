package fr.yazil.weakbungeeapi.bungee;

import net.md_5.bungee.api.ProxyServer;

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
	
}

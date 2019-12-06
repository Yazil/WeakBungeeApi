package fr.yazil.weakbungeeapi.bungee;

import static spark.Spark.*;

public class RespWeb {

	
	public RespWeb(String key, int port) {
		
		port(port);
		path("/api/" + key, () -> {
			path("/global", () -> {
				get("/playercount", (request, response) -> {
					return GameResp.getPlayerCount();
				});
				get("/playerlist", (request, response) -> {
					return GameResp.getFormatedGlobalPlayerList();
				});
			});
			path("/server/:server", () -> {
				get("/playercount", (request, reponse) -> {
					if(GameResp.serverExist(request.params(":server"))) {
						return GameResp.getServerPlayerCount(request.params(":server"));
					}
					return "Server doesn't exist : " + request.params(":server");
				});
				get("/status", (request, reponse) -> {
					if(GameResp.serverExist(request.params(":server"))) {
						return GameResp.serverExist(request.params(":server"));
					}
					return "Server doesn't exist : " + request.params(":server");
				});
				get("/playerlist", (request, reponse) -> {
					if(GameResp.serverExist(request.params(":server"))) {
						return GameResp.getFormatedServerPlayerList(request.params(":server"));
					}
					return "Server doesn't exist : " + request.params(":server");
				});
				get("/motd", (request, reponse) -> {
					if(GameResp.serverExist(request.params(":server"))) {
						return GameResp.getServerMotd(request.params(":server"));
					}
					return "Server doesn't exist : " + request.params(":server");
				});
			});
		});
	}
	
}

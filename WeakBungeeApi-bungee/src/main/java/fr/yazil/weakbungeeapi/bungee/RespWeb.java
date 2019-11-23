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
			});
			path("/server/:server", () -> {
				get("/playercount", (request, reponse) -> {
					if(GameResp.serverExist(request.params(":server"))) {
						return GameResp.getServerPlayerCount(request.params(":server"));
					}
					return "Server doesn't exist : " + request.params(":server");
				});
			});
		});
	}
	
}

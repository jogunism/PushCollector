import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import Handler.CustomLogHandler;
import Handler.MySqlConnector;

public class PushCollector extends Verticle
{
	private static final String HOST = "localhost";
	private static final int PORT = 8080;

	public void start()
	{
		EventBus eventbus = vertx.eventBus();
		HttpServer server = vertx.createHttpServer();
		RouteMatcher matcher = (RouteMatcher) registerMatcher(eventbus);

		// register server
		server.requestHandler(matcher).listen(PORT, HOST);

		//register handler
		eventbus.registerHandler("mysql.handler", new MySqlConnector(container));
		eventbus.registerHandler("customlogger.handler", new CustomLogHandler());		

		container.logger().info("-------------------------------------------");
		container.logger().info(" vert.x collector server started ");
		container.logger().info("-------------------------------------------");
	}

	
	/**
	 * Restful APIs
	 **/
	private RouteMatcher registerMatcher(final EventBus eventbus)
	{
		RouteMatcher matcher = new RouteMatcher();

		//root
		matcher.get("/", new Handler<HttpServerRequest>(){

			@Override
			public void handle(HttpServerRequest req) {

				JsonObject wrapper = new JsonObject();
				wrapper.putString("type", "sender");
				wrapper.putNumber("campaign", 15222);
				wrapper.putString("token", "a");

//				eventbus.send("mysql.handler", jsonobject);
//				eventbus.send("customlogger.handler", wrapper);
			}
		});

		//send
		matcher.get("/send/:campaign/:token/:mNo/:isSuccess", new Handler<HttpServerRequest>(){
			@Override
			public void handle(HttpServerRequest req) {

				String type = req.params().get("type");
				String campaign = req.params().get("campaign");
				String token = req.params().get("token");
				String mNo = req.params().get("mNo");
				String isSuccess = req.params().get("isSuccess");				

				System.out.println(type +" / "+ campaign +" / "+ token +" / "+ mNo +" / "+ isSuccess);

			 	if(type == null || campaign == null || token == null)
			 	{
			 		System.out.println("식별할수없는 요청.");
			 		return;
			 	}

			 	//logfile
			 	//insert to db
			}
		});

		//receive, read
		matcher.get("/:type/:campaign/:token/:mNo", new Handler<HttpServerRequest>(){
			@Override
			public void handle(HttpServerRequest req) {

				String type = req.params().get("type");
				String campaign = req.params().get("campaign");
				String token = req.params().get("token");
				String mNo = req.params().get("mNo");
//				System.out.println(type +" / "+ campaign +" / "+ token +" / "+ mNo);

//			 	if(type == null || campaign == null || token == null)
//			 	{
//			 		System.out.println("식별할수없는 요청.");
//			 		return;
//			 	}

				//logfile
			 	JsonObject wrapper = new JsonObject();
				wrapper.putString("type", type);
				wrapper.putNumber("campaign", Long.parseLong(campaign));
				wrapper.putString("token", token);
				wrapper.putString("mNo", mNo);				
				eventbus.send("customlogger.handler", wrapper);

			 	//insert to db

			 	String jsonstring = new JsonObject().putString("status", "ok").toString();
			 	req.response().putHeader("Content-Type", "application/json");
			 	req.response().end(jsonstring);
			}
		});

		return matcher;
	}
	
}

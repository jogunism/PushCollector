package Handler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

public class MySqlConnector implements Handler<Message<JsonObject>>
{
	private final String EX_MYSQL_MODULE = "io.vertx~mod-mysql-postgresql~0.3.0";
	private Container container = null;	

	public MySqlConnector(Container container){
		this.container = container;
	}

	@Override
	public void handle(Message<JsonObject> reply)
	{
		//todo
		JsonObject config = new JsonObject();
		config.putString("address", "mysql-asyncdb");
		config.putString("connnection", "MySql");
		config.putString("host", "dbsafer.tmonc.net");
		config.putNumber("port", 4005);
		config.putString("username", "jogun");
		config.putString("password", "whgusdn!@43");
		config.putString("database", "tmon_mobile");

		this.container.deployModule(EX_MYSQL_MODULE, config);

		
		
		
		System.out.println(reply.body().getString("collection"));
		
	}

}

package Handler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import Utility.Property;

public class MySqlHandler implements Handler<Message<JsonObject>>
{
	private final String EX_MYSQL_MODULE = "io.vertx~mod-mysql-postgresql~0.3.0";
	private Container container = null;	

	public MySqlHandler(Container container){
		this.container = container;
	}

	@Override
	public void handle(Message<JsonObject> reply)
	{
		this.container.deployModule(EX_MYSQL_MODULE, getDBConInfo());

//		System.out.println(reply.body().getString("collection"));

	}

	private static JsonObject getDBConInfo()
	{
		return new JsonObject()
			.putString("address", Property.getProperty("mysql.address"))
			.putString("connnection", Property.getProperty("mysql.connection"))
			.putString("host", Property.getProperty("mysql.host"))
			.putNumber("port", Integer.parseInt(Property.getProperty("mysql.port")))
			.putString("username", Property.getProperty("mysql.username"))
			.putString("password", Property.getProperty("mysql.password"))
			.putString("database", Property.getProperty("mysql.database"));
	}

}

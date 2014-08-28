package Handler;

import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

public class CustomLogHandler implements Handler<Message<JsonObject>>
{
	@Override
	public void handle(Message<JsonObject> wrapper) {

		LoggerContext loggercontext = (LoggerContext)LoggerFactory.getILoggerFactory();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggercontext);
		encoder.setPattern("%d{HH:mm:ss.SSS} %-5level%msg%n");
		encoder.start();

		FileAppender<ILoggingEvent> fileappender = new FileAppender<ILoggingEvent>();
		fileappender.setContext(loggercontext);
		fileappender.setName("timestamp");
		fileappender.setFile(".logs/"+ wrapper.body().getString("type") +"_"+ wrapper.body().getNumber("campaign") +".log");
		fileappender.setEncoder(encoder);
		fileappender.start();

		Logger logger = loggercontext.getLogger("Main");
		logger.addAppender(fileappender);

		//add raw
		String logstring = wrapper.body().getString("mNo")+"|"+wrapper.body().getString("token");
		System.out.println(logstring);
		logger.info(logstring);


		fileappender.stop();

		loggercontext = null;
		encoder = null;
		fileappender = null;
		logger = null;
		
		
	}
}

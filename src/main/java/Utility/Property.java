package Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Property
{
	private static final String PROPERTY_FILE = "config.properties";

	public static String getProperty(String key)
	{
		String value = "";
		try
		{
			String path = getPropertyFile();

			Properties properties = new Properties();
			properties.load(new FileInputStream(path));

			value = properties.getProperty(key);
		}
		catch(FileNotFoundException fe)
		{
			fe.printStackTrace();
			value = null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			value = null;
		}

		return value;
	}

	private static String getPropertyFile()
	{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		if(classloader == null)
			classloader = ClassLoader.getSystemClassLoader();
		return classloader.getResource(PROPERTY_FILE).getPath();
	}
}

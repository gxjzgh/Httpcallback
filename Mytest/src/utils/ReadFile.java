package utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ReadFile {
	private static final Logger logger = LogManager.getLogger(ReadFile.class);
	public static Properties getProperties(){
		Properties prop = new Properties();
		//读取配置文件params.properties
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream("../config/params.properties"));
			prop.load(in);//加载属性列表
			logger.info("properties get success!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return prop;
	}
	
}

package utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import work.UrlEntity;

public class GsonUtils {
	private static final Logger logger = LogManager.getLogger(GsonUtils.class);

	public String serialize(UrlEntity urlEntity) {
		Gson gson = new Gson();
		String message = null;
		try {
			message = gson.toJson(urlEntity);
		} catch (Exception e) {
			logger.error(e);
		}

		return message;
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(String json, Class<T> clazz) {
		Gson gson = new Gson();
		Object object = null;
		try {
			object = gson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			logger.error(e);
		}

		return (T) object;
	}
}

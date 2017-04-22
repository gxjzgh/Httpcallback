package work;

import java.io.Serializable;
import java.util.Map;

public class  UrlEntity implements Serializable{
	private static final long serialVersionUID = 8760715659582130397L;
	private String url;
	private Map<String,String> map;
	public final String getUrl() {
		return url;
	}
	public final void setUrl(String url) {
		this.url = url;
	}
	public final Map<String, String> getMap() {
		return map;
	}
	public final void setMap(Map<String, String> map) {
		this.map = map;
	}


	
}

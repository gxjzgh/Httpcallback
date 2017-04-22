package work;

import java.util.HashMap;
import java.util.Map;

public class InterfaceTest {

	public static void main(String[] args){
		for(int i=0;i<1000;i++){
			
			UrlEntity urlEntity = new UrlEntity();
		
			Map<String,String> map = new HashMap<String, String>();
			map.put("time","第"+i+"次");
			map.put("username", "123"+i);
			map.put("password", "guixuejianshiwo"+i);
			urlEntity.setMap(map);
			urlEntity.setUrl("http://www.cnblogs.com/mengheng/p/3490693.html");
			Sender sender = new Sender();
			sender.sendToJMS(urlEntity);
		}
	}
}

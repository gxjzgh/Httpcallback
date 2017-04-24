package work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import utils.ReadFile;
import utils.UrlValidate;

public class HttpCallBack {

	private static final Logger logger = LogManager.getLogger(HttpCallBack.class);
	private static HttpURLConnection conn;

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param map
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static String sendPost(String path, Map<String, String> params) {
		// StringBuilder是用来组拼请求参数

		PropertyConfigurator.configure("../config/log4j.properties");
		Properties prop = ReadFile.getProperties();

		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		String result = "";
		boolean re = false;

		if (params != null & params.size() != 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e);
				}
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		// entity为请求体部分内容
		// 如果有中文则以utf-8编码为username=%E4%B8%AD%E5%9B%BD&password=123 
		byte[] entity = sb.toString().getBytes();
		// 验证请求地址
		if (UrlValidate.IsUrl(path)) // 请求地址合法
		{
			try {

				URL url = new URL(path);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(Integer.parseInt(prop.getProperty("conTimeout")));
				System.out.println(params.get("readTimeout")==null);

				if(params.get("readTimeout")==null){
					conn.setReadTimeout(Integer.parseInt(prop.getProperty("readTimeout")));

				}else{
					conn.setReadTimeout(Integer.parseInt(params.get("readTimeout")));
				}
				System.out.println(conn.getReadTimeout());
				conn.setRequestMethod(prop.getProperty("reqMode"));
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length", entity.length + "");
				try {
					conn.connect();
				} catch (SocketTimeoutException e) {
					logger.error(e);
				}

				// 要想外输出的数据，要设置这个

				OutputStream os = conn.getOutputStream();
				os.write(entity);
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
					System.out.println(result);
					
				}
				logger.info("Post Request Result测试");
				// 以Post方式发送请求体

				if (conn.getResponseCode() == 200) {
					logger.info("Post URL Success");
				} else {
					logger.info("Post URL false");
				}
			} catch (IOException e) {
				logger.error(e);
			}
		} else {// 请求URL地址有误
			logger.info("Req URL is irregular!");
		}
		return result;// sendPost方法返回值
	}
}
package work;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import exception.JmsException;
import utils.GsonUtils;
import utils.ReadFile;

public class Receiver {
		private static final Logger logger = LogManager.getLogger(Receiver.class);
		private static int threadMaxnum;
		private static AtomicInteger currentThreadnum = new AtomicInteger();
		static String result;
		
	public static void getConnection() {
		// 获取配置信息
		PropertyConfigurator.configure("../config/log4j.properties");
		
		Properties prop = ReadFile.getProperties();
		ConnectionFactory connectionFactory;

		Connection connection = null;

		Session session = null;

		Destination destination;

		// 消费者，消息接收者
		MessageConsumer consumer;

		System.out.println("ip:" + prop.getProperty("ip") + "port:" + prop.getProperty("port"));

		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,
					"tcp://" + prop.getProperty("ip") + ":" + prop.getProperty("port"));

			connection = connectionFactory.createConnection();
			connection.start();
			logger.info("与服务器连接成功！");
			connection.setExceptionListener(new ExceptionListener() {
				public void onException(JMSException e) {
					logger.error(e);
					JmsException.dealEOFException();

				}
			});
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(prop.getProperty("queue"));
			System.out.println(prop.getProperty("queue"));
			consumer = session.createConsumer(destination);
			threadMaxnum = Integer.parseInt(prop.getProperty("threadmaxnum"));
			System.out.println(threadMaxnum);
			while (true){
			System.out.println("当前线程数："+currentThreadnum);
			if(currentThreadnum.intValue()<threadMaxnum){
				System.out.println("可建立新线程！");
				TextMessage message = (TextMessage) consumer.receive(Integer.parseInt(prop.getProperty("jmsTimeout")));
				if (null != message) {
					String text = message.getText();
					Date date = new Date();
					System.out.println(new Date() + "收到消息:" + text);
				    UrlEntity urlEntity = new UrlEntity();
					// 序列化
					GsonUtils gs = new GsonUtils();
					try{
						urlEntity = gs.deserialize(text, urlEntity.getClass());
					}catch(Exception e){
						logger.error("反序列化失败！"+e);
						
					}
					
					logger.info("Send Post Request!! ");
					
					Map<String,String> map = urlEntity.getMap();
					System.out.println("map"+map);
					String url = urlEntity.getUrl();
					System.out.println("url"+url);
					map.put("readTimeout", "300000");
					Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								Date datebegin = new Date();
								System.out.println("thread："+currentThreadnum);
								currentThreadnum.incrementAndGet();
								try{
									result = HttpCallBack.sendPost(url, map);
									System.out.println(result);
								}catch(Exception e){
									logger.error(e);
								}finally{
									currentThreadnum.decrementAndGet();
								}
								
								
								/*try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								
								
								logger.info("POST请求结果为："+result);

								System.out.println(new Date()+"--------一次请求结束！--------------");
								Date dateend = new Date();
								System.out.println("******************************************");
								System.out.println("线程开始时间："+datebegin);
								System.out.println("线程结束时间："+dateend);
							}
						});
					thread.start();
					}else{
						System.out.println("未收到消息！" + new Date());
					}		
			}else{
				System.out.println("线程数达到上限！");
			}
			session.commit();
		}} catch (JMSException e) {
			JmsException.dealJmsException();
			logger.error(e);
		}
	}
}
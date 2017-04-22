package work;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import utils.GsonUtils;


public class Sender {
	private static final Logger logger = LogManager.getLogger(Sender.class);
	public void sendToJMS(UrlEntity urlEntity){
		//ConnectionFactory :连接工厂，JMS用它创建连接
		ConnectionFactory connectionFactory;
		//provider的连接
		Connection connection = null;
		//Session:一个发送或接收消息的线程
		Session session;
		//Destination :消息的目的地；消息发给谁
		Destination destination;
		//MessageProducer:消息发送者
		MessageProducer producer;
		
		TextMessage message;
		
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,"tcp://192.168.1.40:61616?wireFormat.maxInactivityDuration=0");
		
		try {
			//从构造工厂得到连接对象
			connection = connectionFactory.createConnection();
			//启动
			connection.start();
			//获取操作连接
			session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
			//获取session注意参数数值xingbo.xu-queue是一个服务器的queue，需要在ActiveMq的console配置
			
			
			destination = session.createQueue("myQueue");
			//得到消息生成者（发送者）
			producer = session.createProducer(destination);
			//设置不持久化,此处根据实际项目决定
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			//构造消息,此处写死，项目就是参数，或者方法获取
			message=session.createTextMessage();
			
			/*
			 * 对UrlEntity对象进行序列化
			 */
			GsonUtils gs = new GsonUtils();
			String text = gs.serialize(urlEntity);
			System.out.println("Sender中的序列化:"+text.toString());
			
			message.setText(text);
			producer.send(message);
			session.commit();
			session.close();
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
	}
}

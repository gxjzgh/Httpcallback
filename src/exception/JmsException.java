package exception;

import work.Receiver;

public class JmsException {

	public static void dealJmsException() {
		System.out.println("连接失败！");
	}

	public static void dealEOFException() {
		System.out.println("和服务器连接断开");
		while (true) {
			Receiver.getConnection();
		}
	}
}

package ChatWithUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SendMessage {
	private DatagramSocket datagramSocket = null;
	private DatagramPacket datagramPacket = null;
	private String message;
	private String destIP;
	private int destPort;
	
	public SendMessage(DatagramSocket datagramSocket,String destIP, int destPort, String message) {
		this.datagramSocket = datagramSocket;
		this.destIP = destIP;
		this.destPort = destPort;
		this.message = message;
	}
	
	public void sendMessage() {
		try {
			datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length,
					InetAddress.getByName(destIP), this.destPort);
			datagramSocket.send(datagramPacket);
			// 添加到消息列表
			Chat.ClientList.add("向" + InetAddress.getByName(null).toString() + 
					":" + this.destPort + "发送了消息：" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

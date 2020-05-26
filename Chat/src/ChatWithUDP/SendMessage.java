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
			// ��ӵ���Ϣ�б�
			Chat.ClientList.add("��" + InetAddress.getByName(null).toString() + 
					":" + this.destPort + "��������Ϣ��" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

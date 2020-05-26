package ChatWithUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveMessage extends Thread{
	private byte receive[] = new byte[1024];
	private DatagramPacket datagramPacket = new DatagramPacket(receive,  receive.length);
	private DatagramSocket datagramSocket;
	
	public ReceiveMessage(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	@Override
	public void run(){
		while(true) {
			try {
				datagramSocket.receive(datagramPacket);
				String name = datagramPacket.getAddress().toString();
				int sourcePort = datagramPacket.getPort();
				String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				Chat.ClientList.add(name + ":" + sourcePort + "发来消息：" + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

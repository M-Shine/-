package ChatWithUDP;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.event.WindowEvent;

public class Chat extends JFrame {
	
	private DatagramSocket datagramSocket = null;
	public static Vector<String> ClientList = new <String>Vector();

	private JPanel contentPane;
	private JTextField sourcePortTextField;
	private JTextField messageTextField;
	private JTextField destPortTextField;
	private JTextField destIPTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chat() {
		setTitle("\u9E21\u808B\u7684\u5C40\u57DF\u7F51\u804A\u5929\u5668");
		// 设置默认焦点位置
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				sourcePortTextField.requestFocus();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel sourcePortLabel = new JLabel("\u672C\u5730\u7AEF\u53E3\uFF1A");
		sourcePortLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		sourcePortLabel.setBounds(10, 10, 80, 20);
		contentPane.add(sourcePortLabel);
		
		sourcePortTextField = new JTextField();
		sourcePortTextField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		sourcePortTextField.setBounds(85, 10, 128, 20);
		contentPane.add(sourcePortTextField);
		sourcePortTextField.setColumns(10);
		
		messageTextField = new JTextField();
		messageTextField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		messageTextField.setBounds(56, 70, 370, 20);
		contentPane.add(messageTextField);
		messageTextField.setColumns(10);
		
		JList messageList = new JList();
		messageList.setBounds(10, 130, 416, 150);
		contentPane.add(messageList);
		
		JLabel destPortLabel = new JLabel("\u76EE\u6807\u7AEF\u53E3\uFF1A");
		destPortLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		destPortLabel.setBounds(223, 40, 80, 20);
		contentPane.add(destPortLabel);
		
		destPortTextField = new JTextField();
		destPortTextField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		destPortTextField.setBounds(298, 40, 128, 20);
		contentPane.add(destPortTextField);
		destPortTextField.setColumns(10);
		
		JLabel destIPLabel = new JLabel("\u76EE\u6807 IP \uFF1A");
		destIPLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		destIPLabel.setBounds(10, 40, 80, 20);
		contentPane.add(destIPLabel);
		
		destIPTextField = new JTextField();
		destIPTextField.setText("127.0.0.1");
		destIPTextField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		destIPTextField.setBounds(85, 40, 128, 20);
		contentPane.add(destIPTextField);
		destIPTextField.setColumns(10);
		
		JLabel messageLabel = new JLabel("\u6D88\u606F\uFF1A");
		messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		messageLabel.setBounds(10, 70, 46, 20);
		contentPane.add(messageLabel);
		
		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 检查目标IP
				if(destIPTextField.getText().equals("")) {
					ClientList.add("Error：目标IP不能为空！！！");
					return;
				}
				// 检查目标端口
				if(destPortTextField.getText().equals("")) {
					ClientList.add("Error：目标端口不能为空！！！");
					return;
				}
				// 检查消息
				if(messageTextField.getText().equals("")) {
					ClientList.add("Error：消息不能为空！！！");
					return;
				}
				// 发送消息
				new SendMessage(datagramSocket,destIPTextField.getText(),
						Integer.parseInt(destPortTextField.getText()), messageTextField.getText())
				.sendMessage();
				// 清空消息框
				messageTextField.setText("");
			}
		});
		sendButton.setEnabled(false);
		sendButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		sendButton.setBounds(10, 100, 416, 20);
		contentPane.add(sendButton);
		
		JButton startButton = new JButton("\u542F\u52A8");
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if(sourcePortTextField.getText().equals("")) {
						ClientList.add("Error：本地端口不能为空！！！");
						return;
					}
					// 开启端口
					datagramSocket = new DatagramSocket(Integer.parseInt(sourcePortTextField.getText()));
					// 创建线程用于接收消息
					new ReceiveMessage(datagramSocket).start();
					// 启用与禁用按钮
					startButton.setEnabled(false);
					sendButton.setEnabled(true);
					sourcePortTextField.setEnabled(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		startButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		startButton.setBounds(223, 10, 203, 20);
		contentPane.add(startButton);
		
		// 定时刷新消息列表以获得最新消息
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				messageList.setListData(ClientList);
			}
		}, 0, 250);
		
	}
}

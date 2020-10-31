package snake_prac;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SnakeSetting extends JFrame implements ActionListener {

	private SnakeClient sFrame;

	// ip입력
	JLabel label_IP;
	JTextField input_IP;
	// 포트입력
	JLabel label_Port;
	JTextField input_Port;
	// 닉네임 입력
	JLabel label_Nickname;
	JTextField input_Nickname;
	// 버튼
	JButton btn;

	public SnakeSetting(SnakeClient sFrame) {
		this.sFrame = sFrame;
		
		setAlwaysOnTop(true);
		setBounds(700, 400, 300, 200);
		setResizable(false);
		setLayout(new GridLayout(4, 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridLayout(2, 1));
		label_IP = new JLabel("IP address");
		input_IP = new JTextField("서버 안됨");
		panel.add(label_IP);
		panel.add(input_IP);

		JPanel panel2 = new JPanel(new GridLayout(2, 1));
		label_Port = new JLabel("Port");
		input_Port = new JTextField("9500");
		panel2.add(label_Port);
		panel2.add(input_Port);

		JPanel panel3 = new JPanel(new GridLayout(2, 1));
		label_Nickname = new JLabel("Nickname");
		input_Nickname = new JTextField("플레이어1");
		panel3.add(label_Nickname);
		panel3.add(input_Nickname);

		JPanel panel4 = new JPanel();

		btn = new JButton("Connect");
		panel4.add(btn);

		add(panel);
		add(panel2);
		add(panel3);
		add(panel4);

		setVisible(true);
		
		//이벤트
		btn.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("SnakeSetting: System.exit(0)");
				System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn) {
			
			System.out.println("시작 버튼눌림");
			sFrame.setIp_Port_Name(input_IP.getText(), input_Port.getText(), input_Nickname.getText());
//			sFrame.init();
			dispose();
		}
		
	}

}

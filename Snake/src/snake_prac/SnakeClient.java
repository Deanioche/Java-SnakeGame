package snake_prac;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class SnakeClient extends JFrame implements ActionListener { // Runnable

	// 필드
	private SnakeCanvas sCanvas;
	private String nickname;

	// 채팅창
	private JTextArea textArea;
	private JTextField textField;
	private JButton btn_send;
	
/*
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
*/
	
	private JTable scoreTable; // 차트식으로 윗줄 닉네임 아랫줄 점수 표시

	public DefaultTableModel model;
	public JButton btn_Start;

	public boolean isOnline = false;

	// 생성자
	public SnakeClient() {

		setBounds(50, 50, 1200, 900);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle("왕꿈틀이");

		// 컨테이너 생성
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		// 우측 패널 (점수창, 채팅창, 시작버튼)
		JPanel scorePanel = new JPanel(new BorderLayout());
		scorePanel.setBackground(Color.DARK_GRAY);

		// 채팅창
		JPanel textPanel = new JPanel(new BorderLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		JScrollPane jscChat = new JScrollPane(textArea);
		jscChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textPanel.add("Center", jscChat);

		// 채팅창 하단 패널
		JPanel southTextPanel = new JPanel(new BorderLayout());
		textField = new JTextField();
		btn_send = new JButton("Send");
		southTextPanel.add("Center", textField);
		southTextPanel.add("East", btn_send);
		textPanel.add("South", southTextPanel);
		scorePanel.add("Center", textPanel);

		// 센터 패널 (캔버스)
		JPanel canvasPanel = new JPanel();
		canvasPanel.setBackground(Color.LIGHT_GRAY);
		sCanvas = new SnakeCanvas(SnakeClient.this);
		System.out.println("캔버스 생성 완료");

		// 버튼 생성 (우측 패널 하단)
		btn_Start = new JButton("게임 시작");
		scorePanel.add("South", btn_Start);

		// 점수표 생성
		Vector<String> vector = new Vector<String>();
		vector.add("닉네임");
		vector.add("점수");
		vector.add("몸길이");
		model = new DefaultTableModel(vector, 0);
		scoreTable = new JTable(model);
		scoreTable.setEnabled(false);
		JScrollPane jsc = new JScrollPane(scoreTable);

		scorePanel.add("North", jsc);
		jsc.setPreferredSize(new Dimension(200, 200));

		// 컨테이너에 추가
		c.add("East", scorePanel);
		c.add("Center", sCanvas); // 객체 생성한 다른 클래스 =캔버스를 직접 컨테이너에 추가

		// 창 종료 이벤트
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				System.exit(0);
/*
			try {
				// EXIT 프로토콜을 서버로 보낸다.
				SnakeDTO dto = new SnakeDTO();
				dto.setCommand(Notice.EXIT);
					oos.writeObject(dto);
					oos.flush();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
*/
			}
		});
		setVisible(true);
		new SnakeSetting(this);

		// 버튼 이벤트
		textField.addActionListener(SnakeClient.this);
		btn_send.addActionListener(SnakeClient.this);
		btn_Start.addActionListener(SnakeClient.this);
		
		btn_send.setFocusable(false);

	} // SnakeClient()

	/*
	 * // 서버로 접속 public void init() {
	 * 
	 * try {
	 * 
	 * // 아이피 초기값이 수정되면 접속
	 * 
	 * // ip, port 입력해서 소켓 생성 socket = new Socket(ip,
	 * Integer.parseInt(port)); System.out.println("소켓 생성 완료");
	 * 
	 * // 클라이언트 닉네임 저장 nickname = settingInfo[2];
	 * 
	 * System.out.println("SnakeClient : 접속 시도"); oos = new
	 * ObjectOutputStream(socket.getOutputStream()); // OutputStream을 먼저 생성해야한다.
	 * System.out.println("SnakeClient : 클라이언트 oos 생성됨"); ois = new
	 * ObjectInputStream(socket.getInputStream());
	 * System.out.println("SnakeClient : 클라이언트 ois 생성됨");
	 * 
	 * Thread t = new Thread(this); t.start();
	 * 
	 * isOnline = true; System.out.println("isOnline : " + isOnline);
	 * 
	 * // 닉네임, 점수 입력 SnakeDTO dto = new SnakeDTO(); dto.setCommand(Notice.JOIN);
	 * dto.setNickname(nickname); dto.setScore(0); oos.writeObject(dto);
	 * oos.flush();
	 * 
	 * System.out.println("닉네임, 점수 입력 oos.writeObject(dto) : 완료");
	 * 
	 * } catch (NumberFormatException | IOException e) { e.printStackTrace();
	 * System.out.println(settingInfo[0] + ":" + settingInfo[1] + "에 접속 불가"); }
	 * 
	 * } // init()
	 */

	/*
	 * // 서버로부터 정보를 받는다.
	 * 
	 * @Override public void run() {
	 * 
	 * SnakeDTO dto = null;
	 * 
	 * while (true) {
	 * 
	 * try { // 수신 dto = (SnakeDTO) ois.readObject();
	 * 
	 * System.out.println("SnakeClient.ois.readObject() : 객체 정보 수신 : " +
	 * dto.getCommand());
	 * 
	 * if (dto.getCommand() == Notice.MOVE) {
	 * 
	 * } else if (dto.getCommand() == Notice.SEND) { // 메세지 수신
	 * 
	 * System.out.println("SnakeClient.메세지 수신 : " + dto.getMessage());
	 * 
	 * textArea.append(dto.getMessage() + "\n");
	 * 
	 * textArea.setCaretPosition(textArea.getText().length()); // 마지막 라인에 맞춰 스크롤 이동
	 * 
	 * } else if (dto.getCommand() == Notice.KILLED) {
	 * 
	 * } else if (dto.getCommand() == Notice.KILL) {
	 * 
	 * } else if (dto.getCommand() == Notice.EXIT) { // 종료 프로토콜 수신
	 * 
	 * System.out.println("dto.getCommand() == Notice.EXIT 수신"); ois.close();
	 * oos.close(); socket.close();
	 * 
	 * System.exit(0);
	 * 
	 * } else if (dto.getCommand() == Notice.START) {
	 * 
	 * // 상대 플레이어 색상과 좌표 받아서 필드상에 이미지 출력 this.model = dto.getModel();
	 * this.list_playerInfo = dto.getList_playerInfo();
	 * 
	 * }
	 * 
	 * } catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public void actionPerformed(ActionEvent e) {

		// 게임 시작 버튼 눌림
		if (e.getSource() == btn_Start) {
			System.out.println("actionPerformed : e.getSource() == btn_Start");
			// 캔버스의 startGame 메소드 실행
			sCanvas.startGame();
			
			textArea.append(nickname + "님이 시작하셨습니다.\n");

			// 버튼 안보이게
			btn_Start.setVisible(false);

		}

		// 메세지 보내기 버튼 눌림
		else if (e.getSource() == btn_send || e.getSource() == textField) {

			sCanvas.dto.setMessage(textField.getText());
			textArea.append("[ " + nickname +" ] " + textField.getText() + "\n");
			textField.setText("");
			
			SnakeThread.balloonTimer = 50;
			SnakeThread.showBalloon = true;
			
			sCanvas.requestFocus();
			//메세지를 전송하고 즉시 캔버스에 포커스를 옮겨 뱀 컨트롤이 가능하게 한다.
			
//				oos.writeObject(dto);
//				oos.flush();

		}

	}

	// 세팅값을 한번에 받는 Setter
	public void setIp_Port_Name(String ip, String port, String name) {

//		ip = ip;
//		port = port;
		nickname = name;
		System.out.println("setIp_Port_Name : 메소드 동작");

	}


	// Setter Getter
	public String getNickname() {
		return nickname;
	}

	/*	# 캔버스에서도 oos 쓸수 있도록
	 * 	public ObjectOutputStream getOos() { return oos; }
	 * 
	 *  # 접속 여부 확인 
	 *   public boolean getIsOnline() { return this.isOnline; }
	 */

	// main
	public static void main(String[] args) {
		new SnakeClient();
	}
}

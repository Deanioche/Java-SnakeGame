package snake_prac;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

// 캔버스
public class SnakeCanvas extends Canvas implements KeyListener {

	private SnakeClient sClient;
	private Image bufferImage;
	private Graphics bufferGraphic;
	private SnakeThread sThread;

	public Color headColor;
	private int direction = 0;

	// 게임필드 범위를 x방향 85칸, y방향 70칸으로 설정
	// 처음엔 움직이지 않는 상태로 시작

	public int moveSpeed = 100; // 뱀 이동 속도, 1초에 2칸, 난이도에 따라 조절

	boolean gameRunning = false; // 게임 진행 유무

	Coordinate headCoordinate;			// 좌표클래스

	private ArrayList<Coordinate> list_Body; // 캔버스에서 new 생성해 값을 넣어 준 후, dto에 넣어준다
	private ArrayList<Coordinate> list_Food;
	private ArrayList<Coordinate> list_Bomb;

	private int startCount = -1;	// 재시작 횟수
	String chatBalloon;				// 말풍선에 들어갈 텍스트
	SnakeDTO dto; 					// 서버가 없으므로 dto를 여기에 생성

	// 생성자
	public SnakeCanvas(SnakeClient sClient) {
		this.sClient = sClient;
		setBackground(new Color(200, 250, 250));

		dto = new SnakeDTO();
		list_Body = new ArrayList<Coordinate>();
		list_Food = new ArrayList<Coordinate>();
		list_Bomb = new ArrayList<Coordinate>();
		sThread = new SnakeThread(SnakeCanvas.this);

		// 키 입력 이벤트
		addKeyListener(this);

		System.out.println("캔버스 실행 완료");

	} // SnakeCanvas()

	@Override
	public void update(Graphics g) {

		bufferImage = createImage(this.getWidth(), this.getHeight());
		bufferGraphic = bufferImage.getGraphics();

		if (gameRunning) {

			// 게임 필드 줄긋기
			bufferGraphic.drawRect(50, 50, 900, 750); // 게임 필드 범위 x 850 y 700
			bufferGraphic.drawRect(45, 45, 910, 760);

			bufferGraphic.drawString("주말도 자바와 함께", 500, 500);
			bufferGraphic.drawString("테두리를 넘으면 반대편으로 나온다", 770, 43);
			
			bufferGraphic.drawString("화살표 키로 이동", 300, 280);
			bufferGraphic.drawString("게임중 Tab키를 2번 누르면 채팅창에 커서가 생긴다.", 300, 300);
			bufferGraphic.drawString("채팅창에서 엔터를 치면 다시 뱀 컨트롤을 할 수 있다.", 300, 320);
			bufferGraphic.drawString("폭탄(빨강) 먹으면 몸길이 -3, 점수 -30, 몸길이가 2개 이하이면 게임 오버", 300, 340);

			// 몸 그리기

			if (list_Body != null) {
				if (list_Body.size() >= 1) {
					for (int i = 0; i < list_Body.size(); i++) {
						bufferGraphic.setColor(headColor);
						bufferGraphic.fillOval(list_Body.get(i).getX(), list_Body.get(i).getY(), 20, 20);
						bufferGraphic.setColor(Color.BLACK);
						bufferGraphic.drawOval(list_Body.get(i).getX(), list_Body.get(i).getY(), 20, 20);
					}
				}
			}

			int headX = dto.getCoordinate().getX();
			int headY = dto.getCoordinate().getY();

			// 머리 색칠
			bufferGraphic.setColor(dto.getHeadColor());
			bufferGraphic.fillOval(headX, headY, 20, 20);
			// 머리 테두리
			bufferGraphic.setColor(Color.BLACK);
			bufferGraphic.drawOval(headX, headY, 20, 20);
			// 뱀눈 그리기
			bufferGraphic.setColor(Color.BLACK);
			bufferGraphic.fillOval(headX + 8, headY + 9, 3, 5);
			bufferGraphic.fillOval(headX + 12, headY + 9, 3, 5);
			// 머리 옆에 닉네임 표시
			bufferGraphic.drawString(dto.getNickname(), headX + 8, headY - 2);

			// 먹이 생성
			for (int i = 0; i < list_Food.size(); i++) {

				bufferGraphic.setColor(Color.GREEN);
				bufferGraphic.fillOval(list_Food.get(i).getX(), list_Food.get(i).getY(), 10, 10);
				bufferGraphic.setColor(Color.BLACK);
				bufferGraphic.drawOval(list_Food.get(i).getX(), list_Food.get(i).getY(), 10, 10);

			}
			
			// 폭탄 생성
			for (int i = 0; i < list_Bomb.size(); i++) {
				
//				System.out.println("폭탄 좌표 : " + list_Bomb.get(i).getX() +", "+list_Bomb.get(i).getY());
				bufferGraphic.setColor(Color.RED);
				bufferGraphic.fillOval(list_Bomb.get(i).getX(), list_Bomb.get(i).getY(), 10, 10);
				bufferGraphic.setColor(Color.BLACK);
				bufferGraphic.drawOval(list_Bomb.get(i).getX(), list_Bomb.get(i).getY(), 10, 10);
				
			}
			
			
			
			sendMessage(bufferGraphic);

		}
		paint(g);

	} // update();

	@Override
	public void paint(java.awt.Graphics g) { // import 대신 이렇게 할수도 있다

		g.drawImage(bufferImage, 0, 0, this);
		if (gameRunning) {

			getFood();
			getBomb();
			bodyMove();
			gameOver();
			moveTo(direction);

			dto.setList_Food(list_Food);
			dto.setList_Body(list_Body);
			dto.setList_Body(list_Bomb);

			
			// 음식이 생성될때마다 폭탄최대개수 +2, 폭탄이 최대개수보다 적을경우 폭탄 생성
			if (dto.getList_Food().size() <= 10) {
				createFood();
				
				dto.setBombNum(dto.getBombNum() + 2);
				
				if (list_Bomb.size() < dto.getBombNum()) {
					createBomb();
				}
			}
			
			
		}

	} // paint();

	// 게임 시작
	public void startGame() {

		// 게임 시작시,

//		dto.setCommand(Notice.START); // 시작 프로토콜
		dto.setNickname(sClient.getNickname()); // 닉네임
		System.out.println("dto.setNickname(sClient.getNickname() : " + sClient.getNickname());
		moveSpeed = 100;
		dto.setScore(0); // 점수 초기화
		dto.setBodyNum(0); // 몸 길이
		dto.setBombNum(3); // 폭탄 개수
		createPlayerHead(); // 머리좌표,색상 생성
		dto.setFoodNum(20); // 시작 먹이 10개
		createFood();
		createBomb();

		/*
		 * sClient.getOos().writeObject(dto); sClient.getOos().flush();
		 * System.out.println(" startGame() : oos객체 전달 완료");
		 * 
		 * # 클라이언트의 oos를 getter로 받아 사용
		 */

		// 점수판 갱신
		Vector<String> vector = new Vector<String>();
		vector.add(dto.getNickname());
		vector.add(dto.getScore() + "");
		vector.add(dto.getBodyNum() + "");
		startCount++;
		sClient.model.addRow(vector);

		// 게임 진행여부
		gameRunning = true;
		if (!sThread.isAlive()) {
			sThread.start();
			System.out.println(" startGame() : 객체 전달 완료");
		}

		repaint();
		SnakeCanvas.this.requestFocus();

	} // startGame();

	
	
	
	public void sendMessage(Graphics bufferGraphic) {

		if (gameRunning && dto.getMessage() != null && SnakeThread.showBalloon) {
			
			chatBalloon = dto.getMessage();
			
			// 말풍선 기준점 잡기

			int xT = dto.getCoordinate().getX() - 40;
			int yT = dto.getCoordinate().getY() - 55;

			int x2[] = { xT, xT + 90, xT + 90, xT + 70, xT + 60, xT + 55, xT };
			int y2[] = { yT, yT, yT + 40, yT + 40, yT + 50, yT + 40, yT + 40 };

			bufferGraphic.setColor(Color.WHITE);
			bufferGraphic.fillPolygon(x2, y2, 7); // 말풍선 색

			bufferGraphic.setColor(Color.BLACK);
			bufferGraphic.drawPolygon(x2, y2, 7); // 말풍선 테두리

			// 채팅 내용
			System.out.println("chatBalloon : " +chatBalloon +" 말풍선 표시 남은 시간 ( " + SnakeThread.balloonTimer + " / 50 )");
			if (chatBalloon.length() > 0) {

				int row = chatBalloon.length() / 8;

				if (row >= 1) {

					for (int i = 0; i < row; i++) {

						if (chatBalloon.length() > 8) {
							bufferGraphic.drawString(chatBalloon.substring(0, 7), xT + 5, yT + 15 + (i * 12));
							chatBalloon = chatBalloon.substring(8);
						}
					}
					bufferGraphic.drawString(chatBalloon, xT + 5, yT + 15 + (row * 12));
				} else {
					bufferGraphic.drawString(chatBalloon, xT + 5, yT + 15);
				}
			}

		}
	}

	// 머리 좌표 랜덤 돌려서 게임필드상으로 이동
	private void createPlayerHead() {

		// 온라인일때는 list 객체로 플레이어 수만큼 for문 돌려야 한다.

		headColor = new Color((int) (Math.random() * 105) + 150, (int) (Math.random() * 105) + 150,
				(int) (Math.random() * 105) + 150);

		int headX = ((int) (Math.random() * 66) + 10) * 10; // 생성 범위 10 ~ 75
		int headY = ((int) (Math.random() * 51) + 10) * 10; // 생성 범위 10 ~ 60
		System.out.println("머리 좌표 생성 완료");

		dto.setHeadColor(headColor);
		dto.setCoordinate(new Coordinate(headX, headY));

		System.out.println("headColor : " + headColor);
		System.out.println("dto.getCoordinate() : " + dto.getCoordinate());

	}

	// 먹이 생성
	private void createFood() {

		// 먹이 개수 설정
		// 기본값 : foodNum = 10;
		// for문
		// 1. 먹이 좌표 랜덤 생성
		// 2. 순서대로 먹이 좌표 지정
		// 3. 먹이 좌표가 겹칠경우 좌표 재지정
		// 4. 안겹칠 경우, 먹이 ArrayList에 add

		// foodList.size()가 5 이하일경우 메소드 설정된 개수 (dto.getFoodNum() = 10개) 생성

		for (int i = 0; i < dto.getFoodNum(); i++) {

			int foodX = ((int) (Math.random() * 80) + 6) * 10; // 생성 범위 6 ~ 85
			int foodY = ((int) (Math.random() * 65) + 6) * 10; // 생성 범위 6 ~ 70

			list_Food.add(new Coordinate(foodX, foodY));

		}

	}

	// 폭탄 생성
	private void createBomb() {

		for (int i = 0; i < dto.getBombNum(); i++) {

			int bombX = ((int) (Math.random() * 80) + 6) * 10; // 생성 범위 6 ~ 85
			int bombY = ((int) (Math.random() * 65) + 6) * 10; // 생성 범위 6 ~ 70

			list_Bomb.add(new Coordinate(bombX, bombY));
			
		}

	}

	// 먹이 먹기
	private void getFood() {

		// if 머리 좌표와 먹이 좌표가 같으면
		// 먹이는 사라지고
		// 해당 뱀의 몸길이 bodyNum++;
		// 해당 플레이어의 점수 +10;

		int[] midC_Head = { dto.getCoordinate().getX() + 10, dto.getCoordinate().getY() + 10 };

		for (int i = 0; i < list_Food.size(); i++) {

			int[] midC_food = { list_Food.get(i).getX() + 5, list_Food.get(i).getY() + 5 };

//			System.out.println(dto.getCoordinate().getX() + ", " + dto.getCoordinate().getY());
//			System.out.println(list_Food.get(i).getX() + ", " + list_Food.get(i).getY());

			// 도형과 도형 사이의 거리
			if (Math.sqrt(
					Math.pow((midC_Head[0] - midC_food[0]), 2) + Math.pow((midC_Head[1] - midC_food[1]), 2)) <= 15) {
				// 루트( x - x2의 제곱 + y - y2의 제곱 )

				list_Food.remove(i);

				// 몸 추가
				dto.setBodyNum(dto.getBodyNum() + 1);
				list_Body.add(new Coordinate(midC_Head[0], midC_Head[1]));

				// 속도업
				if (moveSpeed >= 30)
					moveSpeed -= 2;

				// 점수 추가
				dto.setScore(dto.getScore() + 10);
				sClient.model.setValueAt(dto.getScore(), startCount, 1);
				sClient.model.setValueAt(dto.getBodyNum(), startCount, 2);
				System.out.println("몸 길이, 점수 추가");
			}
		}

	} // getFood()
	
	// 폭탄 먹기
	private void getBomb() {
		
		int[] midC_Head = { dto.getCoordinate().getX() + 10, dto.getCoordinate().getY() + 10 };
		
		for (int i = 0; i < list_Bomb.size(); i++) {
			
			int[] midC_bomb = { list_Bomb.get(i).getX() + 5, list_Bomb.get(i).getY() + 5 };
			
//			System.out.println(dto.getCoordinate().getX() + ", " + dto.getCoordinate().getY());
//			System.out.println(list_Food.get(i).getX() + ", " + list_Food.get(i).getY());
			
			// 도형과 도형 사이의 거리
		
			try {
				if (Math.sqrt(
						Math.pow((midC_Head[0] - midC_bomb[0]), 2) + Math.pow((midC_Head[1] - midC_bomb[1]), 2)) <= 15) {
					// 루트( x - x2의 제곱 + y - y2의 제곱 )
					
					list_Bomb.remove(i);
					
					// 몸 줄어들기 - 3개
					dto.setBodyNum(dto.getBodyNum() - 3);
					list_Body.remove(list_Body.size() - 1);
					list_Body.remove(list_Body.size() - 1);
					list_Body.remove(list_Body.size() - 1);
					
					// 속도다운
					if (moveSpeed >= 10)
						moveSpeed += 2;
					
					// 점수 빼기
					dto.setScore(dto.getScore() - 30);
					sClient.model.setValueAt(dto.getScore(), startCount, 1);
					sClient.model.setValueAt(dto.getBodyNum(), startCount, 2);
					System.out.println("몸 길이, 점수 감소");
				}
			} catch (Exception e) { // 몸길이가 짧은데 폭탄을 먹었을 경우 OutOfBounds예외가 발생하므로
//				e.printStackTrace();
				System.out.println("OutOfBounds 발생");
				
				System.out.println("게임오버");
				gameRunning = false;
				dto.getList_Body().clear();
				dto.getList_Food().clear();
				list_Bomb.clear();
				JOptionPane.showMessageDialog(this, "Game over!");
				sClient.btn_Start.setVisible(true);
				
			}
		}
		
	} // getFood()

	// 뱀 몸 움직이기
	private void bodyMove() {

		// 리스트 제일 마지막 인덱스의 몸 객체 제거
		// 머리 좌표에 리스트 인덱스 0인 몸 객체 하나 생성
		if (list_Body.size() != 0) {
			list_Body.remove(list_Body.size() - 1);
			list_Body.add(0, new Coordinate(dto.getCoordinate().getX(), dto.getCoordinate().getY()));
		}
	}

	// 게임 오버
	private void gameOver() {

		// 조건1 x
		// if 머리 좌표가 게임 필드를 벗어났을 경우 (X)
		// 테두리를 벗어나면 반대편으로 이동하도록 구현했음

		// 조건2
		// if 머리 좌표가 필드 위의 몸의 좌표와 같을경우
		// 해당 플레이어는 gameRunning = false;

		if (gameRunning && list_Body.size() > 1) {
			for (int i = 1; i < list_Body.size(); i++) {
				if (dto.getCoordinate().getX() == list_Body.get(i).getX()
						&& dto.getCoordinate().getY() == list_Body.get(i).getY()) {
					System.out.println("게임오버");
					gameRunning = false;
					dto.getList_Body().clear();
					dto.getList_Food().clear();
					list_Bomb.clear();
					JOptionPane.showMessageDialog(this, "Game over!");
					sClient.btn_Start.setVisible(true);
				}
			}
		}

		// 싱글 - 클라이언트에서 돌아감

	}

	public void moveTo(int direction) {

		if (gameRunning) {

			if (direction == 0) {

			} else if (direction == 1) { // 상

				dto.getCoordinate().moveX(0);
				dto.getCoordinate().moveY(-10);

				if (dto.getCoordinate().getY() <= 40) {
					dto.getCoordinate().setY(780);
				}

			} else if (direction == 2) { // 하

				dto.getCoordinate().moveX(0);
				dto.getCoordinate().moveY(10);
				if (dto.getCoordinate().getY() >= 790) {
					dto.getCoordinate().setY(50);
				}

			} else if (direction == 3) { // 좌

				dto.getCoordinate().moveX(-10);
				dto.getCoordinate().moveY(0);
				if (dto.getCoordinate().getX() <= 40) {
					dto.getCoordinate().setX(930);
				}

			} else if (direction == 4) { // 우

				dto.getCoordinate().moveX(10);
				dto.getCoordinate().moveY(0);
				if (dto.getCoordinate().getX() >= 940) {
					dto.getCoordinate().setX(50);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (gameRunning) {

			if (e.getKeyCode() == KeyEvent.VK_UP) {

				this.direction = 1;

			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

				this.direction = 2;

			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

				this.direction = 3;

			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

				this.direction = 4;

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}

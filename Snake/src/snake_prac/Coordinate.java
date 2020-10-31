package snake_prac;

import java.io.Serializable;

//좌표 저장용 클래스
class Coordinate implements Serializable {

	private int x, y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// 좌표 이동 메소드
	public void moveX(int x) {
		this.x += x;
	}

	public void moveY(int y) {
		this.y += y;
	}

}
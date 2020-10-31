package snake_prac;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.swing.table.DefaultTableModel;

/*
enum Notice {
	JOIN, EXIT, MOVE, KILL, KILLED, SEND, EAT, SPREADFOOD, START
}
*/

public class SnakeDTO implements Serializable {


	// 플레이어 정보

	private String nickname;
	private int score;

	// 뱀 정보
	private Color headColor;
	private Coordinate coordinate;

	private int moveX;
	private int moveY;

	private int bodyNum;
	private int foodNum;
	private int bombNum;

	private List<Coordinate> list_Body;
	private List<Coordinate> list_Food;
	private List<Coordinate> list_Bomb;

	private String message;
//	private Notice command;

	// // // // // // // // // // // // // // // // // //



	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public Color getHeadColor() {
		return headColor;
	}


	public void setHeadColor(Color headColor) {
		this.headColor = headColor;
	}


	public Coordinate getCoordinate() {
		return coordinate;
	}


	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}


	public int getMoveX() {
		return moveX;
	}


	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}


	public int getMoveY() {
		return moveY;
	}


	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}


	public int getBodyNum() {
		return bodyNum;
	}


	public void setBodyNum(int bodyNum) {
		this.bodyNum = bodyNum;
	}


	public int getFoodNum() {
		return foodNum;
	}


	public void setFoodNum(int foodNum) {
		this.foodNum = foodNum;
	}


	public int getBombNum() {
		return bombNum;
	}


	public void setBombNum(int bombNum) {
		this.bombNum = bombNum;
	}


	public List<Coordinate> getList_Body() {
		return list_Body;
	}


	public void setList_Body(List<Coordinate> list_Body) {
		this.list_Body = list_Body;
	}


	public List<Coordinate> getList_Food() {
		return list_Food;
	}


	public void setList_Food(List<Coordinate> list_Food) {
		this.list_Food = list_Food;
	}


	public List<Coordinate> getList_Bomb() {
		return list_Bomb;
	}


	public void setList_Bomb(List<Coordinate> list_Bomb) {
		this.list_Bomb = list_Bomb;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	


	/*
	 * public Notice getCommand() { return command; }
	 * 
	 * public void setCommand(Notice command) { this.command = command; }
	 */

}

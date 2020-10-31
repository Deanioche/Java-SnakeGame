package snake_prac;

public class SnakeThread extends Thread {

	private SnakeCanvas sCanvas;

	public static int balloonTimer = 50;
	public static boolean showBalloon = false;

	public SnakeThread(SnakeCanvas sCanvas) {

		this.sCanvas = sCanvas;

	}

	@Override
	public void run() {

		try {
			while (true) {
				sCanvas.repaint();

				// 말풍선 타이머
				if (showBalloon) {
					balloonTimer--;
					if (balloonTimer <= 0) {
						showBalloon = false;
					}
				}

				sleep(sCanvas.moveSpeed);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

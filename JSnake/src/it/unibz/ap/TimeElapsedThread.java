//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)

package it.unibz.ap;

import javax.swing.JLabel;

public class TimeElapsedThread extends Thread
{

	JLabel labelGameTime;
	Thread increaseTime;

	int playingTime;

	boolean keepRunning = true;

	public TimeElapsedThread(JLabel gameTime) {
		labelGameTime = gameTime;
		increaseTime = new Thread(this, "Time thread");

	}

	public void run() {
		// used to keep increasing the game time
		while (true) {
			try {
				labelGameTime.setText("Game Time: " + playingTime);
				Thread.sleep(1000);

				if (keepRunning == true) {
					playingTime++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

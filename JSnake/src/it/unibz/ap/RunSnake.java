//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)

package it.unibz.ap;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;




public class RunSnake 
{
	
	public static final int height = 600; //pixels
	public static final int width = 600; //pixels
	
	public static void main(String[] args)
	{
		//the root of all evils
		RootPanel rootPanel = new RootPanel();
          JFrame frame = new JFrame("JSnake - Have fun!");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          //center the application upon starting
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          double width = screenSize.getWidth();
          double height = screenSize.getHeight();
          frame.setLocation(((int)(width - 600) / 2), (int)((height - 600) / 2));
          //little top menu with some buttons
          TopMenu topMenu = new TopMenu(frame);
          //pack it up and get it ready to go
          frame.add(rootPanel);
          frame.getContentPane();
          frame.pack();
          frame.setVisible(true);
          //if not set, the game is not gonna work correctly
          frame.setResizable(false);
	}

}

//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;




@SuppressWarnings("serial")
public class MainMenu extends JPanel 
{
	public static int buttonDimensionX = 100;
	
	public static int buttonDimensionY = 63;
	
	//this is gonna contain all the panels from the main menu
	public JPanel mainMenuPanel;
	
	//this is gonna contain the buttons in the very top
	public JPanel mainMenuTopPanel;
	
	//this is gonna contain the buttons in the center
	public JPanel mainMenuCenterPanel;
	
	JLabel mainTitle;
	JLabel subTitle;
	
	JButton buttonPlay;
	JButton buttonExit;
	JButton buttonLeaderboard;
	JButton buttonCredits;
	
	Font curFont;
		
	//Populate this panel with several different buttons upon initialization 
	//to show some main menu options. The listeners are located in RootPanel
	public MainMenu()
	{ 
		//initialize menus here
		mainMenuPanel = new JPanel(new BorderLayout());
		this.add(mainMenuPanel);
		this.setPreferredSize(new Dimension(RunSnake.height, RunSnake.width));
		
		//this contains the mainTitle and subTitle
		mainMenuTopPanel = new JPanel(new BorderLayout());		
		mainMenuPanel.add(mainMenuTopPanel, BorderLayout.NORTH);
		
		
		//this contains the buttons to play
		mainMenuCenterPanel = new JPanel();
		mainMenuPanel.add(mainMenuCenterPanel, BorderLayout.CENTER);
		mainMenuCenterPanel.setLayout(new GridLayout(7, 1));
		
		mainTitle = new JLabel("JSnake", JLabel.CENTER);
		curFont = mainTitle.getFont();
		mainTitle.setFont(new Font(curFont.getName(), Font.BOLD, 30));
		mainMenuTopPanel.add(mainTitle, BorderLayout.NORTH);
		
		
		subTitle = new JLabel("Snake now featuring Java", JLabel.CENTER);
		curFont = subTitle.getFont();
		subTitle.setFont(new Font(curFont.getName(), Font.ITALIC, 25));
		mainMenuTopPanel.add(subTitle, BorderLayout.CENTER);
		
		JButton buttonInvisible = new JButton("Invisible");
		buttonInvisible.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		mainMenuCenterPanel.add(buttonInvisible);
		buttonInvisible.setVisible(false);
		
		JButton buttonInvisible1 = new JButton("Invisible");
		buttonInvisible1.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		mainMenuCenterPanel.add(buttonInvisible1);
		buttonInvisible1.setVisible(false);
		
		buttonPlay = new JButton("Play Now!");
		buttonPlay.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		buttonPlay.setFont(new Font(curFont.getName(), Font.BOLD, 20));
		mainMenuCenterPanel.add(buttonPlay);
		
		JButton buttonInvisible2 = new JButton("Invisible");
		buttonInvisible2.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		buttonInvisible2.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		mainMenuCenterPanel.add(buttonInvisible2);
		buttonInvisible2.setVisible(false);
		
		buttonLeaderboard = new JButton("Leaderboard");
		buttonLeaderboard.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		buttonLeaderboard.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		mainMenuCenterPanel.add(buttonLeaderboard);
		
		JButton buttonInvisible3 = new JButton("Invisible");
		buttonInvisible3.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		buttonInvisible3.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		mainMenuCenterPanel.add(buttonInvisible3);
		buttonInvisible3.setVisible(false);
		
		
		//buttonCredits = new JButton("Credits");
		//buttonCredits.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		//buttonCredits.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		//mainMenuCenterPanel.add(buttonCredits);
		
		//JButton buttonInvisible4 = new JButton("Invisible");
		//buttonInvisible4.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		//buttonInvisible4.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		//mainMenuCenterPanel.add(buttonInvisible4);
		//buttonInvisible4.setVisible(false);
		
		
		buttonExit = new JButton("Exit");
		buttonExit.setPreferredSize(new Dimension(buttonDimensionX, buttonDimensionY));
		buttonExit.setFont(new Font(curFont.getName(), Font.PLAIN, 20));
		mainMenuCenterPanel.add(buttonExit);
		
	}
	 
	

}

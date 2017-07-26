//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)

package it.unibz.ap;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RootPanel extends JPanel 
{
	JPanel rootMenu;
	MainMenu mainMenu;
	LeaderboardPanel leaderboardPanel;
	
	boolean gameAlreadyStarted = false;

	
	//this panel is the root of everything. All menus are attached to it
	public RootPanel()
	{
		//first of all, set the overall size of the window.
        this.setPreferredSize(new Dimension(RunSnake.width, RunSnake.height));        	
        
        //generate the leaderboard as well, but hide it suddenly
        leaderboardPanel = new LeaderboardPanel();
    	this.add(leaderboardPanel);
    	leaderboardPanel.setVisible(false);   
    	
    	leaderboardPanel.backButton.addActionListener(new MainMenuListener());
 		
		//this panel contains the stuff displayed to start playing
		mainMenu = new MainMenu();		
		this.add(mainMenu);
		
		mainMenu.buttonLeaderboard.addActionListener(new MainMenuListener());
		mainMenu.buttonPlay.addActionListener(new MainMenuListener(this));
		//mainMenu.buttonCredits.addActionListener(new MainMenuListener());
		mainMenu.buttonExit.addActionListener(new MainMenuListener());
	
	}
	
	
	//redirects the user to a different menu
	private class MainMenuListener implements ActionListener
	 {
		RootPanel rootPanelToAdd;
		
		public MainMenuListener(RootPanel newRootPanelToAdd)
		{
			rootPanelToAdd = newRootPanelToAdd;
		}
		
		public MainMenuListener()
		{
			
		}
		
	        @Override
	        public void actionPerformed(ActionEvent evt)
	        {
	        	JButton source = (JButton) evt.getSource();
	            String text = source.getText();
	          

	            
	            //if clicking on the exit button
	            if (text.equals("Exit")) 
	            {
	                System.exit(0);
	            }
	            //if clicking on the Play now button
	            if (text.equals("Play Now!")) 
	            {               	            	
	            	 //already generate the game panel, but hide it suddenly
	            	GamePanel gamePanel = new GamePanel();
	                rootPanelToAdd.add(gamePanel);
	                
	            	mainMenu.setVisible(false);
	            	//when generating the GamePanel, the game is actually launched
	            	gamePanel.setVisible(true);
					gamePanel.startGame(gameAlreadyStarted);
					gamePanel.setHighScore((int)leaderboardPanel.topPlayers[0].getHighScore(), leaderboardPanel.topPlayers[0].getPlayerName(), leaderboardPanel.topPlayers, leaderboardPanel);
	                
	            	gameAlreadyStarted = true;

	            }    
	            else if (text.equals("Leaderboard"))
	            {
	            	mainMenu.setVisible(false);
	            	leaderboardPanel.setVisible(true);
	            }
	            else if (text.equals("Back"))
	            {
	            	leaderboardPanel.setVisible(false);
	            	mainMenu.setVisible(true);
	            }
	        }
	    }

}

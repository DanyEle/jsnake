//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LeaderboardPanel extends JPanel
{
	//this is gonna contain all the panels from the main menu
		public JPanel mainMenuPanel;
		
		//this is gonna contain the buttons in the very top
		public JPanel mainMenuTopPanel;
		
		//this is gonna contain the top players in the center
		public JPanel mainMenuCenterPanel;
		
		//this is just gonna contain the button to get back to the main menu
		public JPanel mainMenuBottomPanel;
		
		JLabel mainTitle;
		JLabel subTitle;	
		
		JButton backButton;
		
		Font curFont;
		
		TopPlayer[] topPlayers = new TopPlayer[10];
		
		//to re-create the labels upon refreshing
		ArrayList<JLabel> allLabels = new ArrayList<JLabel>();
	
	public LeaderboardPanel()
	{
		
		mainMenuPanel = new JPanel(new BorderLayout());
		this.add(mainMenuPanel);
		this.setPreferredSize(new Dimension(RunSnake.height, RunSnake.width));
		
		//this contains the mainTitle and subTitle
		mainMenuTopPanel = new JPanel(new BorderLayout());		
		mainMenuPanel.add(mainMenuTopPanel, BorderLayout.NORTH);
		
		mainTitle = new JLabel("Leaderboard", JLabel.CENTER);
		curFont = mainTitle.getFont();
		mainTitle.setFont(new Font(curFont.getName(), Font.BOLD, 30));
		mainMenuTopPanel.add(mainTitle, BorderLayout.NORTH);
		
		
		subTitle = new JLabel("Top 10 players ever",  JLabel.CENTER);
		curFont = subTitle.getFont();
		subTitle.setFont(new Font(curFont.getName(), Font.ITALIC, 25));
		mainMenuTopPanel.add(subTitle, BorderLayout.CENTER);
		
		//this contains the labels for the top players
		mainMenuCenterPanel = new JPanel();
		mainMenuPanel.add(mainMenuCenterPanel, BorderLayout.CENTER);
		mainMenuCenterPanel.setLayout(new GridLayout(12, 3));
		
		
		//this contains the labels for the top players
		mainMenuBottomPanel = new JPanel();
		mainMenuPanel.add(mainMenuBottomPanel, BorderLayout.SOUTH);
		mainMenuBottomPanel.setLayout(new GridLayout(1, 3));
		
		//initialize a class for each player by reading the file containing the leaderboard information
		populateTopPlayersArrayFromFile();
		
		//generate one invisible more to have some additional space between the title and the table
		JLabel labelInvisible1 = new JLabel("Invisible", JLabel.CENTER);
		mainMenuCenterPanel.add(labelInvisible1);
		
		JLabel labelInvisible2 = new JLabel("Invisible", JLabel.CENTER);
		mainMenuCenterPanel.add(labelInvisible2);
		
		JLabel labelInvisible3 = new JLabel("Invisible", JLabel.CENTER);
		mainMenuCenterPanel.add(labelInvisible3);
		
		labelInvisible1.setVisible(false);
		labelInvisible2.setVisible(false);
		labelInvisible3.setVisible(false);
		
		//instantiate the actual labels with the elements
		instantiateLabelsFromArray();
		
	
		//for layout reasons generate these invisible buttons too
		JButton invButton = new JButton("Invisible");
		mainMenuBottomPanel.add(invButton);
		invButton.setPreferredSize(new Dimension(80, 40));
		invButton.setVisible(false);
		
		backButton = new JButton("Back");
		mainMenuBottomPanel.add(backButton);
		backButton.setPreferredSize(new Dimension(80, 40));
		
		JButton invButton2 = new JButton("Invisible");
		mainMenuBottomPanel.add(invButton2);
		invButton2.setPreferredSize(new Dimension(80, 40));
		invButton2.setVisible(false);
	
		
	}
	
	//Input: None
	//Output: None
	//Effect:  populates the array topPlayers with all the players'
	//declarations from the leaderboard.txt file, containing rank, name and score.
	public void populateTopPlayersArrayFromFile()
	{
	    Scanner fileFromPath = null;
	    
		try 
		{
			fileFromPath = new Scanner(new File("leaderboard.txt"));

		} catch (FileNotFoundException e1) 
		{
			System.out.println("File 'leaderboard.txt' not found. ");
		}
       
        fileFromPath.useDelimiter(" ");

        //populate the array topPlayers with all the players' declarations from the leaderboard.txt file
        int index = 0;
        while(fileFromPath.hasNextLine() == true)
        {
        	int topRank = fileFromPath.nextInt();
        	int topScore = 0;
        	String topPlayerName = null;
        	        	
        	if (fileFromPath.hasNext())
        	{
        		topPlayerName = fileFromPath.next();
        		
        		if (fileFromPath.hasNext())
        		{
        			topScore = fileFromPath.nextInt();
        		}
        	}
        	
        	//instantiate the class featuring all these elements
        	TopPlayer topPlayer = new TopPlayer(topRank, topPlayerName, topScore);
        	topPlayers[index] = topPlayer;
        	index++;
        }
	}
	
	//Input: None
	//Output: None
	//Effect: instantiates JLabels corresponding to the data in the topPlayers array
	public void instantiateLabelsFromArray()
	{		
		
		for(int i = 0; i < topPlayers.length; i++)
		{
			JLabel labelRank = new JLabel("" + topPlayers[i].getRank(), JLabel.CENTER);
			labelRank.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			mainMenuCenterPanel.add(labelRank);
			labelRank.setFont(new Font(curFont.getName(), Font.BOLD, 20));
			labelRank.setPreferredSize(new Dimension(150, 40));
			allLabels.add(labelRank);
			
			JLabel labelName = new JLabel("" + topPlayers[i].getPlayerName(), JLabel.CENTER);
			labelName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			mainMenuCenterPanel.add(labelName);
			labelName.setFont(new Font(curFont.getName(), Font.BOLD, 20));
			labelName.setPreferredSize(new Dimension(150, 40));
			allLabels.add(labelName);

			
			JLabel labelScore = new JLabel("" + topPlayers[i].getHighScore(), JLabel.CENTER);
			labelScore.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			mainMenuCenterPanel.add(labelScore);
			labelScore.setFont(new Font(curFont.getName(), Font.BOLD, 20));
			labelScore.setPreferredSize(new Dimension(150, 40));
			allLabels.add(labelScore);

			
			//make the first one red
			if (i == 0)
			{
				labelRank.setForeground(Color.RED);
				labelName.setForeground(Color.RED);
				labelScore.setForeground(Color.RED);
			}

		}
	}
	
	//Input: None
	//Output: None
	//Effect: deletes all labels and generates new ones
	public void reloadLabelsFromArray()
	{
		deleteAllLabelsInPanel();
		
		instantiateLabelsFromArray();
	}
	
	//Input: None
	//Output: None
	//Effect: unwraps the current array of top players and writes them into the file 
	public void writeNewArrayToFile()
	{
		String newArrayContent = "";
		for(int i = 0; i < topPlayers.length; i++)
		{
			//if last, no space at the end
			if (i == topPlayers.length - 1)
			{
				newArrayContent += topPlayers[i].toString();
			}
			else
			{
				newArrayContent += topPlayers[i].toString() + " ";
			}
		}
		
		try {
			print(newArrayContent, "leaderboard.txt");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		//System.out.println("Shit to write in file: \n" + newArrayContent );
	}
	
	public void print(String content, String filePath) throws FileNotFoundException, UnsupportedEncodingException
	{
		byte[] bytesString = content.getBytes("UTF-8");
		File filePrintWithDir = new File(filePath);
		PrintWriter printToFile = new PrintWriter(filePrintWithDir);
		String utfContent = new String(bytesString, "UTF-8");
		printToFile.write(utfContent);
		printToFile.close();
	}
	
	public void deleteAllLabelsInPanel()
	{
		//delete all the labels in the current array
		for(int i = 0; i < allLabels.size(); i++)
		{
			JLabel labelToDelete = allLabels.get(i);
			mainMenuCenterPanel.remove(labelToDelete);
		}
	}
	//megusta ucho esta tastiera
	
	

}

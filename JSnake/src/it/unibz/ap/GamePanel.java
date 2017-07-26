//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
//import stuff to play .mp3 sounds



@SuppressWarnings("serial")
public class GamePanel extends JPanel
{
	//these can be just changed to have the snake spawned at a different position at the beginning
	public static final int snakeSpawnPointX = 290;
	public static final int snakeSpawnPointY = 250;
	
	public static final int snakeBodySizeX = 10;
	public static final int snakeBodySizeY = 10;

	
	//used to check if new food needs to be spawned
	public static final int foodFrequencyCheck = 50;
	//used to check if the food has been eaten
	public static final int foodEatenFrequencyCheck = 25;
	//time of the soundtrack. was 3:18. now 3:00 
	public static final int soundtrackOverdue = 180000;
	//milliseconds to wait before pressing another key
	public static final int keyRepetitionTime = 100;
	
	//the upper screen does not actually finish at 0, but about at 40 due to the score stuff
	public static final int upperScreenBound = 40;
	
	//2 seconds: time that needs to pass to show the user either the menu to insert their name
	// in case they achieve a high score, or the leaderboard
	public static final int timeToMakeNextMenuAppear = 2000;
	
	//decrease it to make the snake faster. increase it to make the snake slower
	public static int snakeMovementSpeed = 80;
		
	//1 = up, 2 = right, 3 = down, 4 = left
	public static int snakeMovement = 1;
	
	//if set to false, then there's no need to spawn food
	public static boolean needToSpawnFood = true;
	
	//used to check whether the game is currently paused or not
	public static boolean gameIsPaused = false;
		
	//to avoid the snake from eating food twice (it may become 2 fat :P )
	public static boolean canEatFood = true;
			
	//Your Score:
	public static int currentScore = 0;
	
	//High Score:
	public static int highScore = 0; 
	
	public static boolean canPressKey = true;
	
	//all the variables BELOW hold a valid value AFTER the script startGame has been called
	BufferedImage snakeBody = null;
	BufferedImage snakeDeadBody = null;
	BufferedImage snakeHead = null;
	BufferedImage snakeDeadHead = null;
	
	BufferedImage horizontalRule = null;
	BufferedImage snakeFood = null;
	
	JLabel foodToMove;
	JLabel labelScore;
	JLabel labelPause;
	JLabel labelPauseSubTitle;
	
	JLabel highestScore;
	JLabel gameTime;
	
	//contains all the pieces of snake generated so far. at the beginning, they're just 4
	ArrayList<JLabel> snakePieces = new ArrayList<JLabel>();
		
	Timer soundtrackTimer;
	Timer moveTimer;
	Timer foodSpawnTimer;
	Timer foodEatenTimer;
	Timer showNextMenuTimer;
	Timer pressKeyTimer;
	
	File eatFoodzSound;
	File snakeSoundtrack;
	File snakeGameOver;
	
	Clip snakeSoundtrackClip;
	
	TopPlayer[] topPlayers = new TopPlayer[10];
	
	LeaderboardPanel leaderboardPanel;
	
	JTextField textField;
	JLabel invalidCharMessage;
	JLabel invalidCharRetry;
	
	TimeElapsedThread timeElapsed;
	
	
	public GamePanel()
	{

	}
	
	
	public void startGame(boolean gameAlreadyStarted)
	{
		//to allow pictures to be set at a custom position
		this.setLayout(null); 
		
		this.setPreferredSize(new Dimension(RunSnake.width, RunSnake.height));

		//first of all, initialize resources
		try 
		{                
			snakeBody = ImageIO.read(new File("img/snake_piece.png"));
			snakeDeadBody = ImageIO.read(new File("img/snake_dead_piece.png"));
			snakeHead = ImageIO.read(new File("img/snake_head.png"));
			snakeDeadHead = ImageIO.read(new File("img/snake_dead_head.png"));
			horizontalRule = ImageIO.read(new File("img/line_above.png"));
			snakeFood = ImageIO.read(new File("img/snake_food.png"));
		}
		catch (IOException ex) 
		{
			System.out.println("The resources from the folders img/ or sounds/ cannot be found. Please, verify the game's integrity, Then, retry");
	    }
		finally
		{	
		}
		
		//is resource is actually found..
		if (snakeBody != null && horizontalRule != null && snakeFood != null)
		{		
			//this is needed to allow keys to be pressed and be considered
			this.requestFocus();
			//generate a nice listener to allow the snake to be moved
			this.addKeyListener(new snakeDirectionListener());
			this.setPreferredSize(new Dimension(RunSnake.height, RunSnake.width));

			spawnSnake();	
			
			foodToMove = new JLabel(new ImageIcon(snakeFood));
			
			//dummy location, it's gonna be disabled anyway
			foodToMove.setBounds(0, 0, snakeBodySizeX, snakeBodySizeY);
			foodToMove.setVisible(false);
			
			this.add(foodToMove);
			
			//now spawn the current game score
			labelScore = new JLabel("Your Score: " + currentScore);
			labelScore.setFont(new Font(labelScore.getFont().getName(), Font.BOLD, 15));
	    	this.add(labelScore);
			labelScore.setBounds(10, 10, 200, 20);
			
			//and the highest score thus far
			highestScore = new JLabel("High Score: 0 - Test");
			highestScore.setFont(new Font(highestScore.getFont().getName(), Font.BOLD, 15));
	    	this.add(highestScore);
	    	highestScore.setBounds(350, 10, 200, 20);
	    	
	    	//and the seconds elapsed
			gameTime = new JLabel("Game Time: " + 0);
			gameTime.setFont(new Font(gameTime.getFont().getName(), Font.BOLD, 15));
	    	this.add(gameTime);
	    	gameTime.setBounds(200, 10, 200, 20);
	    	
	    	//start actually increasing dat time
	    	timeElapsed = new TimeElapsedThread(gameTime);
	    	timeElapsed.start();
	    	
			//also include a horizontal break to divide the score area from the game board
			JLabel hrBreak = new JLabel(new ImageIcon(horizontalRule));
			hrBreak.setBounds(0, 35, 600, 3);
			this.add(hrBreak);
			
			//now create the pause label with a subtitle too and then hide them
			labelPause = new JLabel("- Game Paused - ");
			labelPause.setFont(new Font(labelPause.getFont().getName(), Font.BOLD, 30));
			labelPause.setForeground(Color.red);
			this.add(labelPause);
			labelPause.setBounds(185, 200, 350, 50);
			
			//eventual pause title
			labelPauseSubTitle = new JLabel("Press 'Escape' to resume the game");
			labelPauseSubTitle.setFont(new Font(labelPauseSubTitle.getFont().getName(), Font.BOLD, 15));
			this.add(labelPauseSubTitle);
			labelPauseSubTitle.setBounds(178, 250, 350, 50);
			
			//just make them visible when actually pausing (pressing escape)
			labelPause.setVisible(false);
			labelPauseSubTitle.setVisible(false);
			
			//initialize sounds
			eatFoodzSound = new File("sounds/eat_food.wav");			
			snakeSoundtrack = new File("sounds/snake_soundtrack.wav");		
			snakeGameOver = new File("sounds/game_over.wav");		
			
			//if we already started the game once, then reset stuff first
            if (gameAlreadyStarted == true)
            {
            	resetStuffNewGame();
            }
			
			//start the psychedelic music!
			playSound(snakeSoundtrack);
			
			//make the soundtrack be played whenever it stops being played
			soundtrackTimer = new Timer(soundtrackOverdue, new soundtrackListener());
			soundtrackTimer.start();
			
			//start moving that snake!
			moveTimer = new Timer(snakeMovementSpeed, new moveSnakeListener());
		    moveTimer.start();
		    
		    //start spawning that food!
			foodSpawnTimer = new Timer(foodFrequencyCheck, new foodSpawnListener());
		    foodSpawnTimer.start();
		    
		    //start checking if that food has been eaten!
		    foodEatenTimer = new Timer(foodEatenFrequencyCheck, new foodEatenListener());
		    foodEatenTimer.start();
		    
		    pressKeyTimer = new Timer(keyRepetitionTime, new pressNextKeyListener());
		    pressKeyTimer.start();
			
		}
	 
	}
	
	//Input: None
	//Output: None
	//Effect: makes the food visible upon starting a new game
	public void resetStuffNewGame()
	{
		this.foodToMove.setVisible(true);
		
		//put the head back into the spawning position so it doesn't eat itself upon respawning
		snakePieces.get(0).getLocation().setLocation(snakeBodySizeX, snakeBodySizeY);
		
		//reset current score
		currentScore = 0;	
		labelScore.setText("Your Score: " + currentScore);
		
		//make it move up
		snakeMovement = 1;
	}
	
	//Input: a file in .wav format 
	//Output: None
	//Effect: plays the input file
	public void playSound(File fileSound)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(fileSound));
			
			clip.start();	
			
			//used to stop the soundtrack when pausing
			if (fileSound.equals(snakeSoundtrack))
			{
				snakeSoundtrackClip = clip;
				
			}
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	//moves all pieces of the snake
	public void moveSnake()
	{		
		//going up at the beginning
		if (GamePanel.snakeMovement == 1)
		{		
			//we just need to set the position of the head. everything else is gonna follow it 
			int newSnakeHeadX = (int)snakePieces.get(0).getLocation().getX();
			int newSnakeHeadY = (int)snakePieces.get(0).getLocation().getY() - 10;
			
			moveSnakePiecesFollowHead(newSnakeHeadX, newSnakeHeadY);
			
		}
		//go right
		else if (GamePanel.snakeMovement == 2)
		{			
			//we just need to set the position of the head. everything else is gonna follow it 
			int newSnakeHeadX = (int)snakePieces.get(0).getLocation().getX() + 10;
			int newSnakeHeadY = (int)snakePieces.get(0).getLocation().getY();
			
			moveSnakePiecesFollowHead(newSnakeHeadX, newSnakeHeadY);
			
		}
		//go down
		else if (GamePanel.snakeMovement == 3)
		{
			//we just need to set the position of the head. everything else is gonna follow it 
			int newSnakeHeadX = (int)snakePieces.get(0).getLocation().getX();
			int newSnakeHeadY = (int)snakePieces.get(0).getLocation().getY() + 10;
			
			moveSnakePiecesFollowHead(newSnakeHeadX, newSnakeHeadY);
			
		}
		//go left
		else if (GamePanel.snakeMovement == 4)
		{
			//we just need to set the position of the head. everything else is gonna follow it 
			int newSnakeHeadX = (int)snakePieces.get(0).getLocation().getX() - 10;
			int newSnakeHeadY = (int)snakePieces.get(0).getLocation().getY();
			
			moveSnakePiecesFollowHead(newSnakeHeadX, newSnakeHeadY);
			
		}
	}
	
	
	//Input: new head X and Y coordinates
	//Output: None
	//Effect: Makes the remaining body pieces follow the head's position
	public void moveSnakePiecesFollowHead(int newSnakeHeadX, int newSnakeHeadY)
	{
		//check if eating your own tail , hence gameover
		boolean gameOver = checkGameOver(newSnakeHeadX, newSnakeHeadY);
				
		if (gameOver == false)
		{
			//check if going out of screen bounds above or below, right or left
			boolean canGoHere = checkCanGoThisDirection(newSnakeHeadX, newSnakeHeadY);
			
			//no problem, go ahead!
			if (canGoHere == true)
			{
				//this temporary array contains the position of the snake pieces 
				//from 1 to the snake's length - 1
				ArrayList<Point> tempPositions = new ArrayList<Point>();
				
				for(int i = 0; i < snakePieces.size() - 1; i++)
				{
					Point locationPiece = snakePieces.get(i).getLocation();
					tempPositions.add(locationPiece);
				}
				
				snakePieces.get(0).setLocation(newSnakeHeadX, newSnakeHeadY);
		
				int p = 0;
				//now move all pieces to the position of the element before them
				for(int i = 1; i < snakePieces.size(); i++)
				{
					snakePieces.get(i).setLocation(tempPositions.get(p));
					p++;			
				}	
			}
			//cannot go here, now check where we are going out of bounds and relocate the snake
			else
			{
				checkWhereToRelocateSnakeOutBounds(newSnakeHeadX, newSnakeHeadY);
			}
		}
		//Houston, we got a problem!
		else if(gameOver == true)
		{
			triggerGameOver();
			
		}
	}
	
	//Input: the new snake's head position on the X and Y axes
	//Output: The new gameOver value.
	//		  false if the head is not colliding with the snake's body
	//		  true if the head is colliding with the snake's body
	public boolean checkGameOver(int newSnakeHeadX, int newSnakeHeadY)
	{
		//if the new head's position matches the position of any piece of
		//the snake's body, then the game is over
		boolean gameOver = false;
		
		//no need to start from 0 as 0 is the head. how can the head hit itself?
		for(int i = 1; i < snakePieces.size(); i++)
		{
			if ((int)snakePieces.get(i).getLocation().getX() == newSnakeHeadX && ((int)snakePieces.get(i).getLocation().getY() == newSnakeHeadY))
			{
				return true;
			}
		}		
		return gameOver;
	}
	
	public void triggerGameOver()
	{
		//stop all timers, play gameOver sound, make the snake become red, wait 2 seconds. Eventually ask the user to enter name, show leaderboard
		soundtrackTimer.stop();
		moveTimer.stop();
		foodSpawnTimer.stop();
		foodEatenTimer.stop();		
		snakeSoundtrackClip.stop();
		
		timeElapsed.keepRunning = false;
		
		needToSpawnFood = true;
		
		playSound(snakeGameOver);
		
		//turn the snake's head red
		snakePieces.get(0).setIcon(new ImageIcon(snakeDeadHead));
		
		//turn the whole body red
		for(int i = 1; i < snakePieces.size(); i++)
		{
			snakePieces.get(i).setIcon(new ImageIcon(snakeDeadBody));
		}
		
		//wait 2 secs more before showing next menu
		showNextMenuTimer = new Timer(timeToMakeNextMenuAppear, new showNextMenuListener(this, leaderboardPanel));
		showNextMenuTimer.start();		
				
	}
	
	//Input: new snake head position X and new snake head position Y
	//Output: None
	//Effect: relocates the snake depending on where it is going out of bounds
	public void checkWhereToRelocateSnakeOutBounds(int newSnakeHeadX, int newSnakeHeadY)
	{
		//going too much right
		if(newSnakeHeadX > RunSnake.width - 20)
		{
			//put it on the left border 
			int newSnakeSpawnX = 0;
			int newSnakeSpawnY = (int)snakePieces.get(0).getLocation().getY();
						
			moveSnakePiecesFollowHead(newSnakeSpawnX, newSnakeSpawnY);
		}
		//going too much left
		else if (newSnakeHeadX < 0)
		{
			//put it on the right border 
			int newSnakeSpawnX = RunSnake.width - 10;
			int newSnakeSpawnY = (int)snakePieces.get(0).getLocation().getY();
						
			moveSnakePiecesFollowHead(newSnakeSpawnX, newSnakeSpawnY);
		}
		//going too low
		else if (newSnakeHeadY > RunSnake.height - 10)
		{
			//put it back at the top 
			int newSnakeSpawnX = (int)snakePieces.get(0).getLocation().getX();
			int newSnakeSpawnY = upperScreenBound + 10;
						
			moveSnakePiecesFollowHead(newSnakeSpawnX, newSnakeSpawnY);	
		}
		//going too high
		else if (newSnakeHeadY < upperScreenBound)
		{
			//put it back at the bottom 
			int newSnakeSpawnX = (int)snakePieces.get(0).getLocation().getX();
			int newSnakeSpawnY = RunSnake.height - 10;
									
			moveSnakePiecesFollowHead(newSnakeSpawnX, newSnakeSpawnY);			
		}		
	}		
	
	
	//Input: new snake head position X and new snake head position Y
	//Output: true if the snake is not going outside the window
	// 		  false is the snake is trying to leave the window
	public boolean checkCanGoThisDirection(int newSnakeHeadX, int newSnakeHeadY)
	{
		//check if the snake is roaming outside the screen                //up                                //up
		if(newSnakeHeadX > RunSnake.width - 10|| newSnakeHeadX < 0 || newSnakeHeadY > RunSnake.height - 10|| newSnakeHeadY < upperScreenBound)
		{
			return false;
		}
		else
		{
			//everything else is acceptable
			return true;
		}
	}
	
	//script called upon initialization to spawn the snake on the table
	public void spawnSnake()
	{
		JLabel snakePiece1 = new JLabel(new ImageIcon(snakeHead));
		JLabel snakePiece2 = new JLabel(new ImageIcon(snakeBody));
		JLabel snakePiece3 = new JLabel(new ImageIcon(snakeBody));
		JLabel snakePiece4 = new JLabel(new ImageIcon(snakeBody));
		

		snakePiece1.setBounds(snakeSpawnPointX, snakeSpawnPointY, snakeBodySizeX, snakeBodySizeY);
		snakePiece2.setBounds(snakeSpawnPointX, snakeSpawnPointY + 10, snakeBodySizeX, snakeBodySizeY);
		snakePiece3.setBounds(snakeSpawnPointX, snakeSpawnPointY + 20, snakeBodySizeX, snakeBodySizeY);
		snakePiece4.setBounds(snakeSpawnPointX, snakeSpawnPointY + 30, snakeBodySizeX, snakeBodySizeY);
		
		
		snakePieces.add(snakePiece1);
		snakePieces.add(snakePiece2);
		snakePieces.add(snakePiece3);
		snakePieces.add(snakePiece4);
		
		this.add(snakePiece1);
		this.add(snakePiece2);
		this.add(snakePiece3);
		this.add(snakePiece4);
		
	}
	
	public void spawnFood()
	{
		if (needToSpawnFood == true)
		{
			//get random coordinate that's a multiple of 10. from 50 to 590
			Random rand = new Random();
			
			//(5 to 59) * 10 
			int randXCoordinate = (rand.nextInt(54) + 5) * 10;
			int randYCoordinate = (rand.nextInt(54) + 5) * 10;
			
			
			//if not visible at the beginning, then make it visible
			if (foodToMove.isVisible() == false)
			{
				foodToMove.setVisible(true);
			}
			
			foodToMove.setLocation(randXCoordinate, randYCoordinate);
			
			//do not spawn anything else
			needToSpawnFood = false;
			//allow the snake to eat again!
			canEatFood = true;
		}
	}
	
	//Input: None
	//Output: None
	//Effect: checks that the head is colliding with the food
	public void checkFoodEaten()
	{
		
		if (canEatFood == true)
		{
			//if the head's position matches the food's position
			if (snakePieces.get(0).getLocation().getX() == foodToMove.getLocation().getX() &&
				snakePieces.get(0).getLocation().getY() == foodToMove.getLocation().getY())
			{
				
				enlargeSnake();		
				
				//increase score
				currentScore++;
				labelScore.setText("Your Score: " + currentScore);
				
				//play a nice sound
				playSound(eatFoodzSound);
				
				//if overtaking the current high score, make the current score become red
				if (currentScore > highScore)
				{
					labelScore.setForeground(Color.RED);
				}
				
				//whenever the score is increased up to a multiple of 5, also increase the game's speed
				if (currentScore % 5 == 0)
				{
					snakeMovementSpeed -= 5;
					moveTimer.setDelay(snakeMovementSpeed);
				
				}
			}
		}		
	}
	
	public void setHighScore(int newHighScore, String newPlayer, TopPlayer[] newTopPlayers, LeaderboardPanel newLeaderboardPanel)
	{
		highestScore.setText("High Score: " + newHighScore + " - " + newPlayer);
		highScore = newHighScore;
		
		topPlayers = newTopPlayers.clone();
		
		leaderboardPanel = newLeaderboardPanel;
		
	}
	
	//make the snake gain one more piece after the tail
	//Input: None
	//Output: None
	public void enlargeSnake()
	{
		//get the tail's position
		JLabel snakePieceToSpawn = new JLabel(new ImageIcon(snakeBody));
		this.add(snakePieceToSpawn);
		Point tailPosition = snakePieces.get(snakePieces.size() - 1).getLocation();
		snakePieces.add(snakePieceToSpawn);
		
		
		snakePieceToSpawn.setBounds((int)tailPosition.getX(), (int)tailPosition.getY(), snakeBodySizeX, snakeBodySizeY);
		
		canEatFood = false;
		needToSpawnFood = true;
		
	}
	
	public void managePause()
	{
		//not paused, then pause it!
		if (gameIsPaused == false)
		{
			timeElapsed.keepRunning = false;
			moveTimer.stop();
			foodSpawnTimer.stop();
			foodEatenTimer.stop();
			soundtrackTimer.stop();
			
			
			
			labelPause.setVisible(true);
			labelPauseSubTitle.setVisible(true);
			
			//hide the snake as well
			for(int i = 0; i < snakePieces.size(); i++)
			{
				snakePieces.get(i).setVisible(false);
			}
			
			//and the foodz too
			foodToMove.setVisible(false);
			//stop the soundtrack
			snakeSoundtrackClip.stop();
			gameIsPaused = true;
		}
		//paused, then unpause it!
		else if (gameIsPaused == true)
		{
			timeElapsed.keepRunning = true;
			moveTimer.start();
			foodSpawnTimer.start();
			foodEatenTimer.start();
			soundtrackTimer.start();
			
			labelPause.setVisible(false);
			labelPauseSubTitle.setVisible(false);
			
			//hide the snake as well
			for(int i = 0; i < snakePieces.size(); i++)
			{
				snakePieces.get(i).setVisible(true);
			}
			
			//and the foodz too
			foodToMove.setVisible(true);
			//play the soudntrack
			snakeSoundtrackClip.start();
			gameIsPaused = false;
			
		}
	}
	
	public void resetCanPressKey()
	{
		canPressKey = true;
	}

	
	
	//controls the snake's movement
	private class moveSnakeListener implements ActionListener 
	{	
		public void actionPerformed(ActionEvent e) 
		{
			moveSnake();
		}
	}
	
	//controls the food's spawning
	private class foodSpawnListener implements ActionListener 
	{	
        public void actionPerformed(ActionEvent e) 
        {
			spawnFood();
        }
	}
	
	//checks if food has been eaten
	private class foodEatenListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			checkFoodEaten();
		}
	}
	
	private class pressNextKeyListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//used to let the user press another key again.
			//implemented to prevent weird game overs
			resetCanPressKey();
		}
	}
	
	
	//restarts the soundtrack once it has finished playing
	private class soundtrackListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			playSound(snakeSoundtrack);
		}
	}
	
	
	//restarts the soundtrack once it has finished playing
	private class showNextMenuListener implements ActionListener
	{
		GamePanel gamePanel;
		LeaderboardPanel leaderboardPanel;
		
		
		public showNextMenuListener(GamePanel gamePanel, LeaderboardPanel newLeaderBoardPanel)
		{
			this.gamePanel = (GamePanel) gamePanel;
			this.leaderboardPanel = (LeaderboardPanel)newLeaderBoardPanel;
		}
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e)
		{			
			showNextMenuTimer.stop();
			
			int positionToAdd = -1;
			//now decide whether to show the leaderboard or a textField to enter the name
			//loop from the bottom up
			for(int i = topPlayers.length - 1; i >= 0; i--)
			{
				if ((int)topPlayers[i].getHighScore() <= currentScore)
				{
					positionToAdd = i;
				}
			}
			
			//if the current Score is indeed greater than one in the array 
			if (positionToAdd >= 0)
			{
				//now need to prompt the user for their name	
				
				//if 2nd, 3rd, until 10tj
				if (positionToAdd > 0)
				{
					positionToAdd++;

				}
				
				
				//hide the snake
				for(int i = 0; i < snakePieces.size(); i++)
				{
					snakePieces.get(i).setVisible(false);
				}
				
				foodToMove.setVisible(false);
				
				//Labels, textfield and button prompting the user to enter their nickname and being registered as top player
				JLabel scoreLabel = new JLabel("New High Score: " + currentScore, JLabel.CENTER);
				scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 25));
				gamePanel.add(scoreLabel);
				scoreLabel.setBounds(150, 150, 300, 50 );
				
				JLabel newPositionLabel = null;
				
				if (positionToAdd == 0)
				{
					newPositionLabel = new JLabel(" You now occupy position [" + (positionToAdd + 1) + "] among the best players", JLabel.CENTER);

				}
				else
				{
					newPositionLabel = new JLabel(" You now occupy position [" + (positionToAdd) + "] among the best players", JLabel.CENTER);
				}
				
				newPositionLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.ITALIC, 20));
				gamePanel.add(newPositionLabel);
				newPositionLabel.setBounds(65, 180, 500, 50 );
				
				JLabel nickNameLabel = new JLabel("Please enter your nickname:", JLabel.CENTER);
				nickNameLabel.setFont(new Font(nickNameLabel.getFont().getName(), Font.PLAIN, 20));
				gamePanel.add(nickNameLabel);
				nickNameLabel.setBounds(150, 210, 300, 50 );
				
				
				textField = new JTextField(20);
				textField.setText("");
				gamePanel.add(textField);
				textField.setLocation(180, 250);
				textField.setSize(new Dimension(250, 30));
				
				//used to tell the user they entered an invalid name. for now not used
				invalidCharMessage = new JLabel("Your name cannot contain any special characters.");
				invalidCharMessage.setVisible(false);
				gamePanel.add(invalidCharMessage);
				invalidCharMessage.setBounds(100, 370, 500, 50);
				invalidCharMessage.setForeground(Color.red);
				invalidCharMessage.setFont(new Font(invalidCharMessage.getFont().getName(), Font.PLAIN, 20));				
				
				JButton buttonConfirm =  new JButton("Confirm");
				gamePanel.add(buttonConfirm);
				buttonConfirm.setLocation(260, 300);
				buttonConfirm.setSize(new Dimension(80, 50));
				
				buttonConfirm.addActionListener(new confirmButtonListener(this.leaderboardPanel, this.gamePanel, positionToAdd,  gamePanel.currentScore));
				
				
			}
			//no problem, just show leaderboard
			else
			{
				leaderboardPanel.setVisible(true);
				this.gamePanel.setVisible(false);
			}
			
		}
		
		private class confirmButtonListener implements ActionListener
		 {
			LeaderboardPanel leaderboardPanel;
			GamePanel gamePanel;
			int positionToModify = 0;
			
			String playerName;
			int highScore;
			
			
			public confirmButtonListener(LeaderboardPanel newLeaderboardPanel, GamePanel newGamePanel, int newPositionToModify,  int newHighScore)
			{
				this.leaderboardPanel = newLeaderboardPanel;				
				this.gamePanel = newGamePanel;
				this.positionToModify = newPositionToModify;
				this.highScore = newHighScore;				
			}

			@SuppressWarnings("unchecked")
			@Override
			//clicking confirm
			public void actionPerformed(ActionEvent e) 
			{
				String playerNameEntered = gamePanel.textField.getText();
				
				this.playerName = playerNameEntered;
				
				//only accept letters and numbers
				Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]*");
				Matcher matcher = pattern.matcher(this.playerName);
				boolean isFound = matcher.matches();
								
				if (isFound == false)
				{
					//tell the user he cannot use that special character
					invalidCharMessage.setVisible(true);
				}
				//else it's fine...
				else
				{
					//just disable it for the future
					invalidCharMessage.setVisible(false);
					
					//adjust index for player becoming the top high scorer
					if (positionToModify == 0)
					{
						positionToModify = 1;
					}
					
					this.playerName = gamePanel.textField.getText();
					
					gamePanel.setVisible(false);
					leaderboardPanel.topPlayers[positionToModify - 1].setHighScore(this.highScore);
					leaderboardPanel.topPlayers[positionToModify - 1].setPlayerName(this.playerName);
					leaderboardPanel.topPlayers[positionToModify - 1].setRank(this.positionToModify);
					
					leaderboardPanel.reloadLabelsFromArray();
					leaderboardPanel.writeNewArrayToFile();
					leaderboardPanel.setVisible(true);
				}
			}
		 }
	}
	
	
	private class snakeDirectionListener implements KeyListener
	{
		 public void keyTyped(KeyEvent e) 
		 {
		 }

	    public void keyPressed(KeyEvent e) 
	    {
	    	int keyCode = e.getKeyCode();
	    	
	    		//further check for position in the screen to avoid the snake from getting lost
	    		if (canPressKey == true)
	    		{
		    			
			    	//up arrow
			    	if (keyCode == KeyEvent.VK_UP)
			    	{
			    		//if not already going up nor down
			    		if (snakeMovement != 1 && snakeMovement != 3)
			    		{
			    			//then go up
			    			snakeMovement = 1;  	
			    			canPressKey = false;
			    		}
			    	}
			    	
			    	//right arrow
			    	if (keyCode == KeyEvent.VK_RIGHT)
			    	{
			    		//if not already going right nor left
			    		if (snakeMovement != 2 && snakeMovement != 4)
			    		{
			    			//then go right
			    			snakeMovement = 2;  	
			    			canPressKey = false;
			    		}
			    	}
			    	
			    	//left arrow
			    	else if (keyCode == KeyEvent.VK_LEFT)
			    	{
			    		//if not already going left nor right
			    		if (snakeMovement != 4 && snakeMovement != 2)
			    		{
			    			//then go left
			    			snakeMovement = 4;
			    			canPressKey = false;
			    		}
			    	}
			    	
			    	//down arrow
			    	else if (keyCode == KeyEvent.VK_DOWN)
			    	{
			    		//if not already going down nor up
			    		if (snakeMovement != 3 && snakeMovement != 1)
			    		{
			    			//then go down
			    			snakeMovement = 3;
			    			canPressKey = false;
			    		}
			    	}
			    	else if (keyCode == KeyEvent.VK_ESCAPE)
			    	{
			    		managePause();
			    	}
	    		}
	    	
	    }

	    public void keyReleased(KeyEvent e) 
	    {
	    	
	    }	    
	}
}

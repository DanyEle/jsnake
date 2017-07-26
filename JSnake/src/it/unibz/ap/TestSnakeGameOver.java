//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class TestSnakeGameOver extends TestCase
{
	GamePanel gamePanel;
	
	public TestSnakeGameOver()
	{
		super();
		gamePanel = new GamePanel();
	}
	
	@Before
	public void setUp() throws Exception 
	{
		super.setUp();
	}

	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	
	//check if the head is not colliding with the tail and we can keep playing
	public final void testNotGameOver()
	{
		//first we need to pretend we have instantiated the snake
		gamePanel.startGame(false);
		
		
		//get head's position
		int headPosX = (int)(gamePanel.snakePieces.get(0).getLocation().getX());
		int headPosY = (int)(gamePanel.snakePieces.get(0).getLocation().getY());
		
		//checking if we can continue playing and no gameover needs to be thrown
		assertFalse(gamePanel.checkGameOver(headPosX, headPosY));
		
	}
	
	//check if the head is not colliding with the tail and we can keep playing
	public final void testGameOver()
	{
		//first we need to pretend we have instantiated the snake
		gamePanel.startGame(false);
		
		//put the head on the same position of the tail, meaning we are eating our own tail
		gamePanel.snakePieces.get(0).setLocation(gamePanel.snakePieces.get(gamePanel.snakePieces.size() - 1).getLocation());	
		
		//get head's position
		int headPosX = (int)(gamePanel.snakePieces.get(0).getLocation().getX());
		int headPosY = (int)(gamePanel.snakePieces.get(0).getLocation().getY());
		
		//checking if we cannot continue playing and we have encountered a gameover
		assertTrue(gamePanel.checkGameOver(headPosX, headPosY));
	}
	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(TestSnakeGameOver.suite());
	}
	
	
	public static TestSuite suite()
	{
		TestSuite suite = new TestSuite("Test for TestSnakeGameOver.java");
		suite.addTestSuite(TestSnakeGameOver.class);
		return suite;
	}
	
	

	

}

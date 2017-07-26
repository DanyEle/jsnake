//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

public class TestSnakeBounds extends TestCase
{
	GamePanel gamePanel;
	
	public TestSnakeBounds()
	{
		super();
		gamePanel = new GamePanel();
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	//checks if the snake is actually going out of bounds on the right side of the screen
	public final void testSnakeBoundRight()
	{
		//test if within the board normally
		assertTrue(gamePanel.checkCanGoThisDirection(200, 500));
		
		//check if actually going out of bounds
		assertFalse(gamePanel.checkCanGoThisDirection(605, 100));
		
	}
	
	//checks if the snake is actually going out of bounds on the left side of the screen
	public final void testSnakeBoundLeft()
	{
		//test if within the board normally
		assertTrue(gamePanel.checkCanGoThisDirection(350, 350));
		
		//check if actually going out of bounds
		assertFalse(gamePanel.checkCanGoThisDirection(-20, 100));
		
	}
	
	//checks if the snake is actually going out of bounds on the down side of the screen
	public final void testSnakeBoundDown()
	{
		//test if within the board normally
		assertTrue(gamePanel.checkCanGoThisDirection(100, 100));
		
		//check if actually going out of bounds
		assertFalse(gamePanel.checkCanGoThisDirection(20, 620));
		
	}
	
	//checks if the snake is actually going out of bounds on the upper side of the screen
	public final void testSnakeBoundUp()
	{
		//test if within the board normally
		assertTrue(gamePanel.checkCanGoThisDirection(220, 430));
		
		//check if actually going out of bounds
		assertFalse(gamePanel.checkCanGoThisDirection(300, 20));			
	}
	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(TestSnakeBounds.suite());
	}
	
	
	public static TestSuite suite()
	{
		TestSuite suite = new TestSuite("Test for TestSnakeBounds.java");
		suite.addTestSuite(TestSnakeBounds.class);
		return suite;
	}
	

}

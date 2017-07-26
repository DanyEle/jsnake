//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

import junit.framework.*;


public class RunTestsSnake 
{
	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite("All Junit Tests");
		suite.addTest(it.unibz.ap.TestSnakeBounds.suite());		
		suite.addTest(it.unibz.ap.TestSnakeGameOver.suite());		
		return suite;
	}

}

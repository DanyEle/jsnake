//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)


package it.unibz.ap;

public class TopPlayer <T>
{
	
	 private T t;
	 
	 int rank;
	 String playerName;
	 int highScore;
	
	public TopPlayer(int topRank, String topPlayerName, int topHighScore)
	{
		rank = topRank;
		playerName = topPlayerName;
		highScore = topHighScore;
		
	}
	
	//Input: None
	//Output: a String containing the "summary" of the current TopPlayer class' status
	public String toString()
	{
		return rank + " " + playerName + " " + highScore;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public int getHighScore()
	{
		return highScore;
	}
	
	public void setHighScore(T t)
	{
		this.highScore = (int)t;
	}
	
	public void setPlayerName(String newPlayerName)
	{
		this.playerName = newPlayerName;
	}
	
	public void setRank(T t)
	{
		this.rank = (int)t;
	}

}

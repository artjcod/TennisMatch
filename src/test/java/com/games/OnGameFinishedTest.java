package com.games;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class OnGameFinishedTest {

    private TennisSet currentSet;
    private String player1Name;
    private String player2Name;

    @Before
    public void before() {
        player1Name = "Serena Williams";
        player2Name = "Ones Jabeur";
        ScoreBoard scoreBoard = new ScoreBoard(player1Name, player2Name);
        currentSet = scoreBoard.getCurrentSet();
    }

    @Test
    public void playerTwo_Ones_Jabeur_WinsTheCurrentGame() {
        Game currentGame = currentSet.getCurrentGame();
        currentGame.player2Scores();//15
        currentGame.player2Scores(); //30
        currentGame.player1Scores();//15
        currentGame.player2Scores();//40
        currentGame.player2Scores(); //WIN
        assertEquals(currentSet.getPlayer1WonGames(), 0);
        assertEquals(currentSet.getPlayer2WonGames(), 1);
        assertEquals(currentGame.getGameWinner(), player2Name);
    }

    @Test
    public void testUpdatePlayer1SetScore_TWO_Player2SetScoreOne() {
        Game currentGame = currentSet.getCurrentGame();
        currentGame.player2Scores();//15
        currentGame.player2Scores(); //30
        currentGame.player1Scores();//15
        currentGame.player2Scores();//40
        currentGame.player2Scores(); //WIN

        assertEquals(currentSet.getPlayer1WonGames(), 0);
        assertEquals(currentSet.getPlayer2WonGames(), 1);
        assertEquals(currentGame.getGameWinner(), player2Name);

        // 2nd game
        currentSet.startNewGame();
        currentGame = currentSet.getCurrentGame();
        currentGame.player1Scores(); //15
        currentGame.player1Scores(); //30
        currentGame.player1Scores(); //40
        currentGame.player1Scores(); //WIN

        //3rd game
        // next game
        currentSet.startNewGame();
        currentGame = currentSet.getCurrentGame();

        currentGame.player2Scores();//15
        currentGame.player2Scores(); //30
        currentGame.player1Scores();//15
        currentGame.player2Scores();//40
        currentGame.player2Scores(); //WIN

        assertEquals(currentSet.getPlayer1WonGames(), 1);
        assertEquals(currentSet.getPlayer2WonGames(), 2);
    }

	@Test(expected = IllegalArgumentException.class)
	public void throwIllegalArgumentExceptionWhenInvalidObject() {
		OnGameFinished listener = new OnGameFinished();
		listener.update(currentSet, null);
	}
}

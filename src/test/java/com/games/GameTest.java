package com.games;





import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {



    private Game game;
    private String player1Name = "Serena Williams";
    private String player2Name = "Ones Jabeur";


    @BeforeEach
    public void before() {
        ScoreBoard scoreBoard = new ScoreBoard(player1Name, player2Name);
        TennisSet currentSet = scoreBoard.getCurrentSet();
        game = currentSet.getCurrentGame();
    }

    @Test
    public void hasAdvantageFalse() {
        game.player1Scores(); // 15
        game.player1Scores(); //30
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player1Scores(); // 40
        game.player2Scores(); // DEUCE
        game.player1Scores(); // ADV
        game.player2Scores(); // DEUCE
        assertFalse(game.hasAdvantage());
    }

    @Test
    public void player1ScoredTwiceGameScoreUpdate() {
        game.player1Scores();
        game.player1Scores();
        assertThat(game.getPlayer1Points(), equalTo(2));
    }

    @Test
    public void player2ScoredTwiceGameScoreUpdated() {
        game.player2Scores();
        game.player2Scores();
        assertThat(game.getPlayer2Points(), equalTo(2));
    }

    @Test
    public void player1WinTheFirstGameWithDEUCE() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player1Scores(); //30
        game.player1Scores(); //DEUCE
        game.player1Scores(); // ADV
        game.player1Scores(); // WIN
        assertEquals(game.getGameWinner(), player1Name);
    }

    @Test
    public void player2WinTheFirstGameWithoutDEUCE() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player2Scores(); //WIN
        assertEquals(game.getGameWinner(), player2Name);
    }

    @Test
    public void player1WinTheFirstGameWithoutDEUCE() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player1Scores(); //30
        game.player1Scores(); // 40
        game.player1Scores(); //WIN
        assertEquals(game.getGameWinner(), player1Name);
    }


    @Test
    public void player2WinTheFirstGameWithDEUCE() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player1Scores(); //30
        game.player1Scores(); //DEUCE
        game.player2Scores(); // ADV
        game.player2Scores(); // WIN
        assertEquals(game.getGameWinner(), player2Name);
    }

    @Test
    public void isDEUCESCore() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player1Scores(); //30
        game.player1Scores(); //DEUCE
        assertTrue(game.isDeuce());
    }

    @Test
    public void player1HasAdvantage() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player1Scores(); //30
        game.player1Scores(); //DEUCE
        game.player1Scores(); // ADV
        assertTrue(game.hasAdvantage());
        assertEquals(game.playerHaveHighestScore(), player1Name);
    }

    @Test
    public void player2HasAdvantage() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40
        game.player1Scores(); //30
        game.player1Scores(); //DEUCE
        game.player2Scores(); // ADV
        assertTrue(game.hasAdvantage());
        assertEquals(game.playerHaveHighestScore(), player2Name);
    }

    @Test
    public void gameIsNotFinished() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); // 30
        assertFalse(game.isFinishedGame());
        assertEquals(game.getGameWinner(), "No Winner");
    }


    @Test
    public void gameIsFinishedAndWinnerPlayer1() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); // 30
        game.player2Scores(); // 40
        game.player1Scores(); //30
        game.player1Scores(); // DEUCE
        game.player1Scores(); //ADV
        game.player1Scores(); // WIN
        assertTrue(game.isFinishedGame());
        assertEquals(game.getGameWinner(), player1Name);
    }


    @Test
    public void gameIsFinishedAndWinnerPlayer2() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); // 30
        game.player2Scores(); // 40
        game.player1Scores(); //30
        game.player1Scores(); // DEUCE
        game.player1Scores(); //ADV
        game.player2Scores(); // DEUCE
        game.player2Scores(); //ADV
        game.player2Scores(); // WIN
        assertTrue(game.isFinishedGame());
        assertEquals(game.getGameWinner(), player2Name);
    }

    @Test
    public void player1HaveHighestScore() {

        game.player1Scores();
        game.player2Scores();
        game.player1Scores();
        assertEquals(game.playerHaveHighestScore(), player1Name);
    }

    @Test
    public void player2HaveHighestScore() {

        game.player1Scores();
        game.player2Scores();
        game.player1Scores();
        game.player2Scores();
        game.player2Scores();
        assertEquals(game.playerHaveHighestScore(), player2Name);
    }

    @Test
    public void announcedGameScoreTHIRTY_ALL() {
        game.player1Scores();
        game.player2Scores();
        game.player1Scores();
        game.player2Scores();
        assertEquals(game.getGameScore(), "THIRTY ALL");
    }

    @Test
    public void announcedGameScoreDEUCE() {
        game.player1Scores();
        game.player1Scores();
        game.player1Scores();
        game.player2Scores();
        game.player2Scores();
        game.player2Scores();
        assertEquals(game.getGameScore(), "DEUCE");
    }

    @Test
    public void announcedGameScoreFIFTEEN_ALL() {
        game.player1Scores();
        game.player2Scores();
        assertEquals(game.getGameScore(), "FIFTEEN ALL");
    }

    @Test
    public void announcedGameScoreFORTY_THIRTY() {
        game.player1Scores(); // 15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player1Scores(); //30
        game.player1Scores(); //40
        assertEquals(game.getGameScore(), "FORTY-THIRTY");
    }

    @Test
    public void announcedGameScoreLOVE_THIRTY() {
        game.player2Scores(); //15
        game.player2Scores(); //30
        assertEquals(game.getGameScore(), "LOVE-THIRTY");
    }

    @Test
    public void announcedGameScoreLOVE_ALL() {
        assertEquals(game.getGameScore(), "LOVE ALL");
    }

    @Test
    public void throwNumberFormatExceptionWhenScoreIsInvalid() {
      assertThrows(NumberFormatException.class, ()->{
        game.toAnnouncedScore(6);
      });



       
    }




    @Test
    public void thirtyAsAnnouncedScore() {
        assertEquals(game.toAnnouncedScore(2), "THIRTY");
    }


    @Test
    public void announcedGameScore_ADV_ForPlayer1() {
        game.player1Scores(); //15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player1Scores(); //30
        game.player1Scores(); //40
        game.player2Scores(); //40
        game.player1Scores(); //ADV

        assertEquals(game.getGameScore(), "ADV for " + player1Name);
    }

    @Test
    public void announcedGameScore_ADV_ForPlayer2() {
        game.player1Scores(); //15
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player1Scores(); //30
        game.player1Scores(); //40
        game.player2Scores(); //40
        game.player2Scores(); //ADV

        assertEquals(game.getGameScore(), "ADV for " + player2Name);
    }

    @Test
    public void finishTheGamePlayer1Wins() {
        game.player1Scores(); // 15
        game.player1Scores(); // 30
        game.player1Scores(); // 40
        game.player1Scores(); // WIN

        assertEquals(game.getGameWinner(), player1Name);
        assertEquals(game.getGameScore(), player1Name + " is the winner !");
        assertEquals(game.playerHaveHighestScore(), player1Name);
        assertTrue(game.isFinishedGame());
    }

    @Test
    public void finishTheGamePlayer2Wins() {
        game.player1Scores(); // 15
        game.player1Scores(); // 30
        game.player1Scores(); // 40
        game.player2Scores(); //15
        game.player2Scores(); //30
        game.player2Scores(); //40 DEUCE
        assertTrue(game.isDeuce());
        game.player2Scores(); //ADV
        assertTrue(game.hasAdvantage());
        game.player2Scores(); //WIN
        assertTrue(game.isFinishedGame());
        assertEquals(game.getGameWinner(), player2Name);
        assertEquals(game.getGameScore(), player2Name + " is the winner !");
        assertEquals(game.playerHaveHighestScore(), player2Name);
    }
}

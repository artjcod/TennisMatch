package com.games;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.junit.LoggerContextRule;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class GameTest {
    private static ListAppender appender;
    @ClassRule
    public static LoggerContextRule init = new LoggerContextRule("log4j2.xml");

    private Game game;
    private String player1Name = "Serena Williams";
    private String player2Name = "Ones Jabeur";

    @BeforeClass
    public static void setupLogging() {
        appender = init.getListAppender("List");
    }

    @Before
    public void before() {
        appender.clear();
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
        assertThat(game.getPlayer1Score(), equalTo(2));
    }

    @Test
    public void player2ScoredTwiceGameScoreUpdated() {
        game.player2Scores();
        game.player2Scores();
        assertThat(game.getPlayer2Score(), equalTo(2));
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

    @Test(expected = NumberFormatException.class)
    public void throwNumberFormatExceptionWhenScoreIsInvalid() {
        game.toAnnouncedScore(6);
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

        List<LogEvent> logEvents = appender.getEvents();
        List<String> infos = logEvents.stream()
                .filter(event -> event.getLevel().equals(Level.INFO))
                .map(event -> event.getMessage().getFormattedMessage())
                .collect(Collectors.toList());
        assertThat(infos, everyItem(containsString(player1Name + " wins the game!")));
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
        List<LogEvent> logEvents = appender.getEvents();
        List<String> infos = logEvents.stream()
                .filter(event -> event.getLevel().equals(Level.INFO))
                .map(event -> event.getMessage().getFormattedMessage())
                .collect(Collectors.toList());
        assertThat(infos, everyItem(containsString(player2Name + " wins the game!")));
    }
}

package com.games;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.IntStream;


@RunWith(MockitoJUnitRunner.class)
public class TennisSetTest {

    private ScoreBoard scoreBoard;
    private String player1Name, player2Name;

    @Before
    public void before() {
        player1Name = "Serena Williams";
        player2Name = "Ones Jabeur";
        scoreBoard = new ScoreBoard(player1Name, player2Name);
    }

    @Test
    public void testInitialSetScore0_0() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet.getPlayer1Score()).isEqualTo(0);
        assertThat(tennisSet.getPlayer2Score()).isEqualTo(0);
        assertThat(tennisSet.getPlayer1TieBreakScore()).isEqualTo(0);
        assertThat(tennisSet.getPlayer2TieBreakScore()).isEqualTo(0);
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.isTieBreakActive()).isFalse();
        assertThat(tennisSet.getSetScoreByLeader()).isEqualTo("0-0");
        assertThat(tennisSet.getTieBreakScore()).isEqualTo("Tie-Break is not active !");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGameWith1PlayerIllegalArgumentException() {
        new TennisSet(scoreBoard, "Player1");
    }

    @Test
    public void testTennisSetCreation() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet.getCurrentGame()).isNotNull();
        assertThat(tennisSet.getRelatedGames()).hasOnlyElementsOfType(Game.class);
        assertThat(tennisSet.getRelatedGames()).contains(tennisSet.getCurrentGame());
        assertThat(tennisSet.getCurrentGame().getPlayer1Name()).isEqualTo(player1Name);
        assertThat(tennisSet.getCurrentGame().getPlayer2Name()).isEqualTo(player2Name);
        assertThat(tennisSet.countObservers()).isEqualTo(1);
    }

    @Test
    public void testStartNewGame() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        Game currentGame = tennisSet.getCurrentGame();
        tennisSet.startNewGame();
        Game nextGame = tennisSet.getCurrentGame();
        assertThat(currentGame).isNotSameAs(nextGame);
        assertThat(nextGame.getPlayer1Score()).isEqualTo(nextGame.getPlayer2Score()).isEqualTo(0);
        assertThat(tennisSet.getRelatedGames()).hasSize(2);
        assertThat(tennisSet.getRelatedGames()).contains(nextGame);
        assertThat(tennisSet.getRelatedGames()).contains(currentGame);
    }

    @Test
    public void testGetGameById() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        Game currentGame = tennisSet.getCurrentGame();
        Game gameById = tennisSet.getGameById(0);
        assertThat(currentGame)
                .isEqualTo(gameById);
        tennisSet.startNewGame();
        tennisSet.startNewGame();
        Game game3 = tennisSet.getCurrentGame();
        tennisSet.startNewGame();
        assertThat(game3).isEqualTo(tennisSet.getGameById(2));
    }

    @Test
    public void player1Scores_4_2SetScore_4_2() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1Scores(); // 1-0
        tennisSet.player1Scores(); // 2-0
        tennisSet.player2Scores(); // 2-1
        tennisSet.player2Scores(); // 2-2
        tennisSet.player1Scores(); // 3-2
        tennisSet.player1Scores(); // 4-2
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.getPlayer1Score()).isEqualTo(4);
        assertThat(tennisSet.getPlayer2Score()).isEqualTo(2);
        assertThat(tennisSet.getSetScoreByLeader()).isEqualTo("4-2");
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
    }

    @Test
    public void player1ScoresAndWinsTheSet_SetScore_6_4() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1Scores(); // 1-0
        tennisSet.player1Scores(); // 2-0
        tennisSet.player2Scores(); // 2-1
        tennisSet.player2Scores(); // 2-2
        tennisSet.player1Scores(); // 3-2
        tennisSet.player1Scores(); // 4-2
        tennisSet.player2Scores(); // 4-3
        tennisSet.player2Scores(); // 4-4
        tennisSet.player1Scores(); // 5-4
        tennisSet.player1Scores(); // 6-4
        assertThat(tennisSet.isFinishedSet()).isTrue();
        assertThat(tennisSet.getPlayer1Score()).isEqualTo(6);
        assertThat(tennisSet.getPlayer2Score()).isEqualTo(4);
        assertThat(tennisSet.getSetScoreByLeader()).isEqualTo("6-4");
        assertThat(tennisSet.getSetWinner()).isEqualTo(player1Name);
    }

    @Test
    public void isTieBreak_Score_8_6() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet).isNotNull();
        //the player1 wins the 1st set
        tennisSet.player1Scores(); // 1-0
        tennisSet.player2Scores(); // 1-1
        tennisSet.player2Scores(); // 1-2
        tennisSet.player1Scores(); // 2-2
        tennisSet.player1Scores(); // 3-2
        tennisSet.player1Scores(); // 4-2
        tennisSet.player1Scores(); // 5-2
        tennisSet.player1Scores(); // 6-2

        assertThat(tennisSet.isFinishedSet()).isTrue();
        assertThat(tennisSet.getSetScoreByLeader()).isEqualTo("6-2");
        assertThat(scoreBoard.getPreviousSets()).contains(tennisSet);
        assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
        assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);

        //start the next set (started via the listener OnFinishSet)

        TennisSet nextSet = scoreBoard.getCurrentSet();
        assertThat(nextSet).isNotNull();
        nextSet.player1Scores(); // 1-0
        nextSet.player2Scores(); // 1-1
        nextSet.player2Scores(); // 1-2
        nextSet.player1Scores(); // 2-2
        nextSet.player1Scores(); // 3-2
        nextSet.player1Scores(); // 4-2
        nextSet.player1Scores(); // 5-2
        nextSet.player2Scores(); // 5-3
        nextSet.player2Scores(); // 5-4
        nextSet.player2Scores(); // 5-5
        nextSet.player1Scores(); //6-5
        //Tie Break activated
        nextSet.player2Scores(); //6-6
        assertThat(nextSet.getPlayer1Score()).isEqualTo(nextSet.getPlayer2Score()).
                isEqualTo(6);
        assertThat(nextSet.isFinishedSet()).isFalse();
        //check whether the time break is active
        assertThat(nextSet.isTieBreakActive()).isTrue();
        assertThat(nextSet.getPlayer1TieBreakScore()).isEqualTo(nextSet.getPlayer2TieBreakScore()).
                isEqualTo(0);
        nextSet.player1Scores(); // 1-0
        nextSet.player1Scores(); // 2-0
        nextSet.player1Scores(); // 3-0
        nextSet.player2Scores(); // 3-1
        nextSet.player2Scores(); // 3-2
        nextSet.player2Scores(); // 3-3
        nextSet.player2Scores(); // 3-4
        nextSet.player2Scores(); // 3-5
        nextSet.player1Scores(); // 4-5
        nextSet.player1Scores(); // 5-5
        nextSet.player1Scores(); // 6-5
        nextSet.player2Scores(); // 6-6
        nextSet.player1Scores(); // 7-6
        nextSet.player1Scores(); // 8-6

        assertThat(nextSet.getPlayer1TieBreakScore()).isEqualTo(8);
        assertThat(nextSet.getPlayer2TieBreakScore()).isEqualTo(6);
        assertThat(nextSet.getPlayer1Score()).isEqualTo(nextSet.getPlayer2Score()).isEqualTo(6);
        assertThat(nextSet.isFinishedSet()).isTrue();
        assertThat(nextSet.getSetWinner()).isEqualTo(player1Name);
        assertThat(nextSet.getSetScoreByLeader()).isEqualTo("6-6(8-6)");
        //the next Set will be started
        assertThat(scoreBoard.getCurrentSet()).isNotNull();
        assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
        assertThat(scoreBoard.getPreviousSets()).hasSize(2);

        assertThat(scoreBoard.getPreviousSets()).contains(tennisSet);
        assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
        assertThat(nextSet).isNotEqualTo(tennisSet);
        assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
        assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
    }

    @Test
    public void player1ScoresWhenNotTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        currentSet.player1Scores();
        currentSet.player1Scores();
        currentSet.player1Scores();
        assertThat(currentSet.getPlayer1Score()).isEqualTo(3);
        assertThat(currentSet.isTieBreakActive()).isFalse();
        assertThat(currentSet.getPlayer1TieBreakScore()).isEqualTo(0);
    }

    @Test
    public void player2ScoresWhenNotTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        currentSet.player2Scores();
        currentSet.player2Scores();
        currentSet.player2Scores();
        assertThat(currentSet.getPlayer2Score()).isEqualTo(3);
        assertThat(currentSet.isTieBreakActive()).isFalse();
        assertThat(currentSet.getPlayer2TieBreakScore()).isEqualTo(0);
    }

    @Test
    public void player1And2ScoreWhenTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        currentSet.player2Scores(); // 0-1
        currentSet.player2Scores(); // 0-2
        currentSet.player2Scores(); // 0-3
        currentSet.player1Scores(); // 1-3
        currentSet.player1Scores(); // 2-3
        currentSet.player1Scores(); // 3-3
        currentSet.player2Scores(); //3-4
        currentSet.player1Scores(); // 4-4
        currentSet.player1Scores(); // 5-4
        currentSet.player2Scores(); // 5-5
        currentSet.player2Scores(); // 5-6
        currentSet.player1Scores(); // 6-6 Tie-break activated
        assertThat(currentSet.getPlayer2Score()).isEqualTo(currentSet.getPlayer1Score()).isEqualTo(6);
        assertThat(currentSet.isTieBreakActive()).isTrue();
        currentSet.player1Scores(); // 1-0 Tie-break
        currentSet.player2Scores(); // 1-1 Tie-break
        currentSet.player1Scores(); // 2-1 Tie-break
        currentSet.player1Scores(); // 3-1 Tie-break
        currentSet.player2Scores(); // 3-2 Tie-break
        currentSet.player1Scores(); // 4-2 Tie-break
        currentSet.player2Scores(); // 4-3 Tie-break
        currentSet.player2Scores(); // 4-4 Tie-break

        assertThat(currentSet.getPlayer1TieBreakScore()).isEqualTo(currentSet.getPlayer2TieBreakScore()).isEqualTo(4);
        assertThat(currentSet.getTieBreakScore()).isEqualTo("6-6(4-4)");
        assertThat(currentSet.isFinishedSet()).isFalse();

        currentSet.player1Scores(); //5-4 Tie-break
        currentSet.player2Scores(); //5-5 Tie-break
        currentSet.player2Scores(); //5-6 Tie-break
        currentSet.player2Scores(); //5-7 Tie-break

        assertThat(currentSet.getPlayer1TieBreakScore()).isEqualTo(5);
        assertThat(currentSet.getPlayer2TieBreakScore()).isEqualTo(7);
        assertThat(currentSet.getTieBreakScore()).isEqualTo("6-6(5-7)");
        assertThat(currentSet.isFinishedSet()).isTrue();

        assertThat(currentSet.getSetWinner()).isEqualTo(player2Name);
        assertThat(currentSet.getSetScoreByLeader()).isEqualTo(currentSet.getTieBreakScore()).isEqualTo("6-6(5-7)");
    }

    @Test
    public void testCloseSet() {
        TennisSet tennisSet = spy(scoreBoard.getCurrentSet());
        //the player1 wins the 1st set
        tennisSet.player1Scores(); // 1-0
        tennisSet.player2Scores(); // 1-1
        tennisSet.player2Scores(); // 1-2
        tennisSet.player1Scores(); // 2-2
        tennisSet.player1Scores(); // 3-2
        tennisSet.player1Scores(); // 4-2
        tennisSet.player1Scores(); // 5-2
        tennisSet.player1Scores(); // 6-2
        //Board score updated
        verify(tennisSet, times(1)).closeSet();
    }

    @Test
    public void player1IsTheWinner() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1Scores(); // 1-0
        tennisSet.player2Scores(); // 1-1
        tennisSet.player2Scores(); // 1-2
        tennisSet.player1Scores(); // 2-2
        tennisSet.player1Scores(); // 3-2
        tennisSet.player1Scores(); // 4-2
        tennisSet.player1Scores(); // 5-2
        tennisSet.player1Scores(); // 6-2
        assertThat(tennisSet.getSetWinner()).isEqualTo(player1Name);
    }

    @Test
    public void player2IsTheWinner() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1Scores(); // 1-0
        tennisSet.player2Scores(); // 1-1
        tennisSet.player2Scores(); // 1-2
        tennisSet.player2Scores(); // 1-3
        tennisSet.player2Scores(); // 1-4
        tennisSet.player2Scores(); // 1-5
        tennisSet.player2Scores(); // 1-6
        assertThat(tennisSet.getSetWinner()).isEqualTo(player2Name);
    }
    
    @Test
    public void player2IsTheWinnerTieBreak() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w->{
        	 tennisSet.player1Scores();
             tennisSet.player2Scores();
        }); //gives 6-6
        
        assertThat(tennisSet.isTieBreakActive()).isTrue();
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.getPlayer1Score()).isEqualTo(tennisSet.getPlayer2Score()).isEqualTo(6);
        
        IntStream.rangeClosed(1, 5).forEach(w->{
       	    tennisSet.player1Scores();
            tennisSet.player2Scores();
       }); //gives 6-6(5-5)
        
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
        assertThat(tennisSet.getSetScoreByLeader()).isEqualTo("6-6(5-5)");

        IntStream.rangeClosed(1, 2).forEach(w->tennisSet.player2Scores());
        assertThat(tennisSet.getSetWinner()).isEqualTo(player2Name);
    }

    @Test
    public void noWinnerSetNotFinished() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1Scores(); // 1-0
        tennisSet.player2Scores(); // 1-1
        tennisSet.player2Scores(); // 1-2
        tennisSet.player2Scores(); // 1-3
        tennisSet.player2Scores(); // 1-4
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
        assertThat(tennisSet.isFinishedSet()).isFalse();
    }

}

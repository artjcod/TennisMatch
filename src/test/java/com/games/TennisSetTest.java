package com.games;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.IntStream;


@ExtendWith(MockitoExtension.class)
public class TennisSetTest {

    private ScoreBoard scoreBoard;
    private String player1Name, player2Name;

    @BeforeEach
    public void before() {
        player1Name = "Serena Williams";
        player2Name = "Ones Jabeur";
        scoreBoard = new ScoreBoard(player1Name, player2Name);
    }

    @Test
    public void testInitialSetScore0_0() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet.getPlayer1WonGames()).isEqualTo(0);
        assertThat(tennisSet.getPlayer2WonGames()).isEqualTo(0);
        assertThat(tennisSet.getPlayer1TieBreakPoints()).isEqualTo(0);
        assertThat(tennisSet.getPlayer2TieBreakPoints()).isEqualTo(0);
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.isTieBreakActive()).isFalse();
        assertThat(tennisSet.getCurrentSetScoreByLeader()).isEqualTo("0-0");
        assertThat(tennisSet.getTieBreakScore()).isEqualTo("Tie-Break is not active !");
    }

 
      
    @Test
    public void testGameWith1PlayerIllegalArgumentException() {
            Assertions.assertThrows(IllegalArgumentException.class, ()->{
            new TennisSet(scoreBoard, "Player1");
        });
       
    }

    @Test
    public void tennisSetCreatedWithDefaultValues() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet.getCurrentGame()).isNotNull();
        assertThat(tennisSet.getRelatedGames()).hasOnlyElementsOfType(Game.class);
        assertThat(tennisSet.getRelatedGames()).contains(tennisSet.getCurrentGame());
        assertThat(tennisSet.getCurrentGame().getPlayer1Name()).isEqualTo(player1Name);
        assertThat(tennisSet.getCurrentGame().getPlayer2Name()).isEqualTo(player2Name);
    }

    @Test
    public void startNewGameWithDefaultValues() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        Game currentGame = tennisSet.getCurrentGame();
        tennisSet.startNewGame();
        Game nextGame = tennisSet.getCurrentGame();
        assertThat(currentGame).isNotSameAs(nextGame);
        assertThat(nextGame.getPlayer1Points()).isEqualTo(nextGame.getPlayer2Points()).isEqualTo(0);
        assertThat(tennisSet.getRelatedGames()).hasSize(2);
        assertThat(tennisSet.getRelatedGames()).contains(nextGame);
        assertThat(tennisSet.getRelatedGames()).contains(currentGame);
    }

    @Test
    public void getGameByIdEqualsToCurrentGame() {
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
    public void player1WonGamesEqualsTo4Against2ForPlayer2() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player1WinsCurrentGame());
        IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player2WinsCurrentGame());
        IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player1WinsCurrentGame());
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.getPlayer1WonGames()).isEqualTo(4);
        assertThat(tennisSet.getPlayer2WonGames()).isEqualTo(2);
        assertThat(tennisSet.getCurrentSetScoreByLeader()).isEqualTo("4-2");
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
    }

    @Test
    public void player1ScoresAndWinsTheSet_SetScore_6_4() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1,2).forEach(w->{
            IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player1WinsCurrentGame());
            IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player2WinsCurrentGame());
        });
        //6-4
        IntStream.rangeClosed(1, 2).forEach(x -> tennisSet.player1WinsCurrentGame());
        assertThat(tennisSet.isFinishedSet()).isTrue();
        assertThat(tennisSet.getPlayer1WonGames()).isEqualTo(6);
        assertThat(tennisSet.getPlayer2WonGames()).isEqualTo(4);
        assertThat(tennisSet.getCurrentSetScoreByLeader()).isEqualTo("6-4");
        assertThat(tennisSet.getSetWinner()).isEqualTo(player1Name);
    }

    @Test
    public void isTieBreak_Score_8_6() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        assertThat(tennisSet).isNotNull();
        //the player1 wins the 1st set
        tennisSet.player1WinsCurrentGame(); // 1-0
        // Next line gives 1-2
        IntStream.rangeClosed(1, 2).forEach(w -> tennisSet.player2WinsCurrentGame());
        //Next line gives 6-2
        IntStream.rangeClosed(1, 5).forEach(w -> tennisSet.player1WinsCurrentGame());
        assertThat(tennisSet.isFinishedSet()).isTrue();
        assertThat(tennisSet.getCurrentSetScoreByLeader()).isEqualTo("6-2");
        assertThat(scoreBoard.getPreviousSets()).contains(tennisSet);
        assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(1);
        assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);

        //start the next set (started via the listener OnFinishSet)
        TennisSet nextSet = scoreBoard.getCurrentSet();
        assertThat(nextSet).isNotNull();
        nextSet.player1WinsCurrentGame(); // 1-0
        //Next line gives
        IntStream.rangeClosed(1, 2).forEach(w -> nextSet.player2WinsCurrentGame());
        //Next gives 5-2
        IntStream.rangeClosed(1, 4).forEach(w -> nextSet.player1WinsCurrentGame());
        // Next line 5-5
        IntStream.rangeClosed(1, 3).forEach(w -> nextSet.player2WinsCurrentGame());
        nextSet.player1WinsCurrentGame(); //6-5
        //Tie Break activated
        nextSet.player2WinsCurrentGame(); //6-6
        assertThat(nextSet.getPlayer1WonGames()).isEqualTo(nextSet.getPlayer2WonGames()).
                isEqualTo(6);
        assertThat(nextSet.isFinishedSet()).isFalse();
        //check whether the time break is active
        assertThat(nextSet.isTieBreakActive()).isTrue();
        assertThat(nextSet.getPlayer1TieBreakPoints()).isEqualTo(nextSet.getPlayer2TieBreakPoints()).
                isEqualTo(0);
        //Next line gives 3-0
        IntStream.rangeClosed(1,3).forEach(w->nextSet.player1WinsOnePoint());
        //Next line gives 3-5
        IntStream.rangeClosed(1,5).forEach(w->nextSet.player2WinsOnePoint());
        //Next line gives 6-5
        IntStream.rangeClosed(1,3).forEach(w->nextSet.player1WinsOnePoint());
        nextSet.player2WinsCurrentGame(); // 6-6
        //Next line gives 8-6
        IntStream.rangeClosed(1,2).forEach(w->nextSet.player1WinsOnePoint());
        assertThat(nextSet.getPlayer1TieBreakPoints()).isEqualTo(8);
        assertThat(nextSet.getPlayer2TieBreakPoints()).isEqualTo(6);
        assertThat(nextSet.getPlayer1WonGames()).isEqualTo(nextSet.getPlayer2WonGames()).isEqualTo(6);
        assertThat(nextSet.isFinishedSet()).isTrue();
        assertThat(nextSet.getSetWinner()).isEqualTo(player1Name);
        assertThat(nextSet.getCurrentSetScoreByLeader()).isEqualTo("6-6(8-6)");
        //the next Set will be started
        assertThat(scoreBoard.getCurrentSet()).isNotNull();
        assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
        assertThat(scoreBoard.getPreviousSets()).hasSize(2);

        assertThat(scoreBoard.getPreviousSets()).contains(tennisSet);
        assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
        assertThat(nextSet).isNotEqualTo(tennisSet);
        assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(2);
        assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);
    }

    @Test
    public void player1ScoresWhenNotTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 3).forEach(w -> currentSet.player1WinsCurrentGame());
        assertThat(currentSet.getPlayer1WonGames()).isEqualTo(3);
        assertThat(currentSet.isTieBreakActive()).isFalse();
        assertThat(currentSet.getPlayer1TieBreakPoints()).isEqualTo(0);
    }

    @Test
    public void player2ScoresWhenNotTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 3).forEach(w -> currentSet.player2WinsCurrentGame());
        assertThat(currentSet.getPlayer2WonGames()).isEqualTo(3);
        assertThat(currentSet.isTieBreakActive()).isFalse();
        assertThat(currentSet.getPlayer2TieBreakPoints()).isEqualTo(0);
    }

    @Test
    public void player1And2ScoreWhenTieBreak() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1,3).forEach(w->currentSet.player2WinsCurrentGame());
        IntStream.rangeClosed(1,3).forEach(w->currentSet.player1WinsCurrentGame());
        currentSet.player2WinsCurrentGame(); //3-4
        IntStream.rangeClosed(1,2).forEach(w->currentSet.player1WinsCurrentGame());
        IntStream.rangeClosed(1,2).forEach(w->currentSet.player2WinsCurrentGame());
        currentSet.player1WinsCurrentGame(); // 6-6 Tie-break activated
        assertThat(currentSet.getPlayer2WonGames()).isEqualTo(currentSet.getPlayer1WonGames()).isEqualTo(6);
        assertThat(currentSet.isTieBreakActive()).isTrue();
        IntStream.rangeClosed(1,2).forEach(w->{
            currentSet.player1WinsOnePoint();
            currentSet.player2WinsOnePoint();
        });
        IntStream.rangeClosed(1,2).forEach(w->currentSet.player1WinsOnePoint());
        IntStream.rangeClosed(1,2).forEach(w->currentSet.player2WinsOnePoint());
        assertThat(currentSet.getPlayer1TieBreakPoints()).isEqualTo(currentSet.getPlayer2TieBreakPoints()).isEqualTo(4);
        assertThat(currentSet.getTieBreakScore()).isEqualTo("6-6(4-4)");
        assertThat(currentSet.isFinishedSet()).isFalse();
        currentSet.player1WinsOnePoint(); //5-4 Tie-break
        IntStream.rangeClosed(1,3).forEach(w->currentSet.player2WinsOnePoint()); //5-7 Tie-break
        assertThat(currentSet.getPlayer1TieBreakPoints()).isEqualTo(5);
        assertThat(currentSet.getPlayer2TieBreakPoints()).isEqualTo(7);
        assertThat(currentSet.getTieBreakScore()).isEqualTo("6-6(7-5)");
        assertThat(currentSet.isFinishedSet()).isTrue();
        assertThat(currentSet.getSetWinner()).isEqualTo(player2Name);
        assertThat(currentSet.getCurrentSetScoreByLeader()).isEqualTo(currentSet.getTieBreakScore()).isEqualTo("6-6(7-5)");
    }

    @Test
    public void closeCurrentSet() {
        TennisSet tennisSet = spy(scoreBoard.getCurrentSet());
        //the player1 wins the 1st set
        tennisSet.player1WinsCurrentGame(); // 1-0
        //1-2
        IntStream.rangeClosed(1,2).forEach(w->tennisSet.player2WinsCurrentGame());
        // 6-2
        IntStream.rangeClosed(1,5).forEach(w->tennisSet.player1WinsCurrentGame());
        //Board score updated
        verify(tennisSet, times(1)).closeSet();
    }

    @Test
    public void currentSetNotYetFinishedCloseCurrentEventNotCalled() {
        TennisSet tennisSet = spy(scoreBoard.getCurrentSet());
        //the player1 wins the 1st set
        tennisSet.player1WinsCurrentGame(); // 1-0
        //1-2
        IntStream.rangeClosed(1,2).forEach(w->tennisSet.player2WinsCurrentGame());
        // 6-2
        IntStream.rangeClosed(1,4).forEach(w->tennisSet.player1WinsCurrentGame());
        //Board score updated
        verify(tennisSet, times(0)).closeSet();
    }

    @Test
    public void player1IsTheWinner() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1WinsCurrentGame(); // 1-0
        //Next line gives 1-2
        IntStream.rangeClosed(1,2).forEach(w->tennisSet.player2WinsCurrentGame());
        //Next line gives 6-2
        IntStream.rangeClosed(1,5).forEach(w->tennisSet.player1WinsCurrentGame());
        assertThat(tennisSet.getSetWinner()).isEqualTo(player1Name);
    }

    @Test
    public void player2IsTheWinnerOneToSix() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1WinsCurrentGame(); // 1-0
        //Next line gives 1-6
        IntStream.rangeClosed(1, 6).forEach(w -> tennisSet.player2WinsCurrentGame());
        assertThat(tennisSet.getSetWinner()).isEqualTo(player2Name);
    }
    
    @Test
    public void player2IsTheWinnerTieBreakActive() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w->{
        	 tennisSet.player1WinsCurrentGame();
             tennisSet.player2WinsCurrentGame();
        }); //gives 6-6
        
        assertThat(tennisSet.isTieBreakActive()).isTrue();
        assertThat(tennisSet.isFinishedSet()).isFalse();
        assertThat(tennisSet.getPlayer1WonGames()).isEqualTo(tennisSet.getPlayer2WonGames()).isEqualTo(6);
        
        IntStream.rangeClosed(1, 5).forEach(w->{
       	    tennisSet.player1WinsOnePoint();
            tennisSet.player2WinsOnePoint();
       }); //gives 6-6(5-5)
        
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
        assertThat(tennisSet.getCurrentSetScoreByLeader()).isEqualTo("6-6(5-5)");
        IntStream.rangeClosed(1, 2).forEach(w->tennisSet.player2WinsOnePoint());
        assertThat(tennisSet.getSetWinner()).isEqualTo(player2Name);
    }

    @Test
    public void noWinnerSetNotFinished() {
        TennisSet tennisSet = scoreBoard.getCurrentSet();
        tennisSet.player1WinsCurrentGame(); // 1-0
        IntStream.rangeClosed(1,4).forEach(w->tennisSet.player2WinsCurrentGame());
        assertThat(tennisSet.getSetWinner()).isEqualTo("No Winner!");
        assertThat(tennisSet.isFinishedSet()).isFalse();
    }

}

package com.games;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


public class TennisMatchTest {

    private TennisMatch match;
    private String player1Name, player2Name;

    @Before
    public void before() {
        player1Name = "Serena Williams";
        player2Name = "Ones Jabeur";
        match = new TennisMatch(player1Name, player2Name);
    }

    @Test
    public void checkMatchCreationIsOK() {
        assertThat(match).isNotNull();
        assertThat(match.getScoreBoard()).isNotNull();
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getFinalScore()).isEqualTo("Serena Williams:0 Sets,Ones Jabeur:0 Sets");
        assertThat(match.getLeader()).isEqualTo("No Leader!");
        assertThat(match.getPlayer1Name()).isEqualTo(player1Name);
        assertThat(match.getPlayer2Name()).isEqualTo(player2Name);

    }

    @Test
    public void leaderIsPlayer1() {
        match.getScoreBoard().player1WinsCurrentSet();
        match.getScoreBoard().player2WinsCurrentSet();
        match.getScoreBoard().player1WinsCurrentSet();
        assertThat(match.getLeader()).isEqualTo(player1Name);
    }

    @Test
    public void leaderIsPlayer2() {
        match.getScoreBoard().player2WinsCurrentSet();
        assertThat(match.getLeader()).isEqualTo(player2Name);
    }

    @Test
    public void noLeader() {
        assertThat(match.getLeader()).isEqualTo("No Leader!");
    }

    @Test
    public void matchISFinishedPlayer1Wins_3Sets_Love() {
        ScoreBoard scoreBoard = match.getScoreBoard();
        IntStream.rangeClosed(1, 3).forEach(w -> {
            TennisSet tennisSet = new TennisSet(scoreBoard, player1Name, player2Name);
            tennisSet.setPlayer1WonGames(6);
            tennisSet.setPlayer2WonGames(3);
            scoreBoard.getPreviousSets().add(tennisSet);
        });
        IntStream.rangeClosed(1, 3).forEach(i -> scoreBoard.player1WinsCurrentSet());
        assertThat(match.isFinished()).isTrue();
        assertThat(match.getLeader()).isEqualTo(player1Name);
    }

    @Test
    public void mathIsFinishedPlayer2Wins_3Sets_Love() {
        ScoreBoard scoreBoard = match.getScoreBoard();
        IntStream.rangeClosed(1, 3).forEach(w -> {
            TennisSet tennisSet = new TennisSet(scoreBoard, player1Name, player2Name);
            tennisSet.setPlayer1WonGames(5);
            tennisSet.setPlayer2WonGames(7);
            scoreBoard.getPreviousSets().add(tennisSet);
        });
        IntStream.rangeClosed(1, 3).forEach(i -> scoreBoard.player2WinsCurrentSet());
        assertThat(match.isFinished()).isTrue();
        assertThat(match.getLeader()).isEqualTo(player2Name);
    }

    @Test
    public void mathNotFinishedScore2SetsAll() {
        ScoreBoard scoreBoard = match.getScoreBoard();
        IntStream.rangeClosed(1, 2).forEach(i -> scoreBoard.player1WinsCurrentSet());
        IntStream.rangeClosed(1, 2).forEach(i -> scoreBoard.player2WinsCurrentSet());
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:2 Sets");
        assertThat(match.getLeader()).isEqualTo("No Leader!");
    }

    @Test
    public void liveScoreAnnouncedAsTwoSets_To_One() {
        match.getScoreBoard().player1WinsCurrentSet();
        match.getScoreBoard().player1WinsCurrentSet();
        match.getScoreBoard().player2WinsCurrentSet();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
    }

    @Test
    public void mathFinishedScore3Sets_1Sets() {
        ScoreBoard scoreBoard = match.getScoreBoard();
        IntStream.range(1, 3).forEach(val -> {
            IntStream.range(1, 6).forEach(value -> {
                scoreBoard.getCurrentSet().player1WinsCurrentGame(); // 5-0
            });
            IntStream.range(1, 5).forEach(value -> {
                scoreBoard.getCurrentSet().player2WinsCurrentGame(); // 5-4
            });
            scoreBoard.getCurrentSet().player1WinsCurrentGame(); // 6-4

            // player 1 wins the 1stSet & 2nd Set
        });

        IntStream.range(1, 6).forEach(value -> {
            scoreBoard.getCurrentSet().player2WinsCurrentGame(); // 0-5
        });
        IntStream.range(1, 5).forEach(value -> {
            scoreBoard.getCurrentSet().player1WinsCurrentGame(); // 4-5
        });
        scoreBoard.getCurrentSet().player2WinsCurrentGame(); // 6-4

        // player 2 wins the set

        IntStream.range(1, 6).forEach(value -> {
            scoreBoard.getCurrentSet().player1WinsCurrentGame(); // 5-0
        });
        IntStream.range(1, 5).forEach(value -> {
            scoreBoard.getCurrentSet().player2WinsCurrentGame(); // 5-4
        });
        scoreBoard.getCurrentSet().player1WinsCurrentGame(); // 6-4

        // player 1 wins the set

        assertThat(match.isFinished()).isTrue();
        assertThat(match.getFinalScore()).isEqualTo("Serena Williams defeated Ones Jabeur 6-4,6-4,4-6,6-4");
        assertThat(match.getLeader()).isEqualTo(player1Name);
    }

    @Test
    public void mathFinishedScore3Sets_2Sets_TieBreakActive() {
        ScoreBoard scoreBoard = match.getScoreBoard();
        // Set 1 Winner Player 1 : Serena Williams | 7-5
        TennisSet set1 = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            set1.player1WinsCurrentGame();
            set1.player2WinsCurrentGame();
        }); // gives 5-5
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("5-5");

        IntStream.rangeClosed(1, 2).forEach(i -> set1.player1WinsCurrentGame());// gives 7-5
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("7-5");
        assertThat(set1.getSetWinner()).isEqualTo(player1Name);

        // Set 2 Winner Player 1 : Serena Williams | 6-6(7-5)
        TennisSet set2 = scoreBoard.getCurrentSet();

        IntStream.rangeClosed(1, 6).forEach(i -> {
            set2.player1WinsCurrentGame();
            set2.player2WinsCurrentGame();
        }); // gives 6-6 Tie-Break

        assertThat(set2.getCurrentSetScoreByLeader()).isEqualTo("6-6(0-0)");
        assertThat(set2.isTieBreakActive()).isTrue();

        IntStream.rangeClosed(1, 4).forEach(i -> {
            set2.player1WinsOnePoint(); // 1-0
            set2.player2WinsOnePoint(); // 1-1
        }); // gives 4-4

        assertThat(set2.getCurrentSetScoreByLeader()).isEqualTo("6-6(4-4)");

        IntStream.rangeClosed(1, 2).forEach(i -> set2.player1WinsOnePoint()); // gives 6-4

        assertThat(set2.getCurrentSetScoreByLeader()).isEqualTo("6-6(6-4)");
        assertThat(set2.getSetWinner()).isEqualTo("No Winner!");
        assertThat(set2.isFinishedSet()).isFalse();

        set2.player2WinsOnePoint(); // 6-5
        set2.player1WinsOnePoint(); // 7-5

        assertThat(set2.getCurrentSetScoreByLeader()).isEqualTo("6-6(7-5)");
        assertThat(set2.getSetWinner()).isEqualTo(player1Name);
        assertThat(set2.isFinishedSet()).isTrue();

        // Check the board score & match
        assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(2);
        assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);
        assertThat(match.getLeader()).isEqualTo(player1Name);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getFinalScore()).isEqualTo(match.getLiveScore())
                .isEqualTo("Serena Williams:2 Sets,Ones Jabeur:0 Sets");

        // Set 3 Winner Player 1 : Ones Jabeur | 6-6(8-10)
        TennisSet set3 = scoreBoard.getCurrentSet();

        IntStream.rangeClosed(1, 6).forEach(j -> {
            set3.player2WinsCurrentGame();
            set3.player1WinsCurrentGame();
        }); // gives 6-6
        assertThat(set3.getCurrentSetScoreByLeader()).isEqualTo("6-6(0-0)");
        assertThat(set3.isTieBreakActive()).isTrue();
        IntStream.rangeClosed(1, 6).forEach(j -> {
            set3.player1WinsOnePoint();
            set3.player2WinsOnePoint();
        }); // gives 6-6(6-6)
        assertThat(set3.getCurrentSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(6-6)");
        IntStream.rangeClosed(1, 2).forEach(j -> {
            set3.player2WinsOnePoint();
            set3.player1WinsOnePoint();
        }); // gives 6-6(8-8)
        assertThat(set3.getCurrentSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(8-8)");
        IntStream.rangeClosed(1, 2).forEach(x -> set3.player2WinsOnePoint());
        assertThat(set3.getCurrentSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(8-10)");
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getFinalScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);

        // Set 4 Winner Player 1 : Serena Williams 6-6 (15-13)
        TennisSet set4 = scoreBoard.getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(i -> {
            set4.player1WinsCurrentGame();
            set4.player2WinsCurrentGame();
        });
        assertThat(set4.getCurrentSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(0-0)");
        IntStream.rangeClosed(1, 13).forEach(i -> {
            set4.player1WinsOnePoint();
            set4.player2WinsOnePoint();
        });
        assertThat(set4.getCurrentSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(13-13)");
        IntStream.rangeClosed(1, 2).forEach(x -> set4.player1WinsOnePoint());
        assertThat(set4.getCurrentSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(15-13)");
        assertThat(match.isFinished()).isTrue();
        assertThat(match.getFinalScore())
                .isEqualTo("Serena Williams defeated Ones Jabeur 7-5,6-6(7-5),6-6(8-10),6-6(15-13)");
        assertThat(match.getLeader()).isEqualTo(player1Name);
    }

    @Test
    public void MatchFinishedPlayer2Wins() {
        //expected: the number of won sets per player.
        simulateTwoSets_Score_X_Set_Per_Player(1);
        simulateTwoSets_Score_X_Set_Per_Player(2);
        assertThat(match.isFinished()).isFalse();
        TennisSet set3 = match.getScoreBoard().getCurrentSet();

        IntStream.rangeClosed(1, 6).forEach(z -> IntStream.rangeClosed(1, 4).forEach(w -> match.player2Scores()));
        assertThat(set3.isFinishedSet()).isTrue();
        assertThat(match.isFinished()).isTrue();

        assertThat(match.getLeader()).isEqualTo(player2Name);
        assertThat(match.getFinalScore()).isEqualTo("Ones Jabeur defeated Serena Williams 6-0,6-6(7-9),6-0,6-6(7-9),6-0");

    }

    private void simulateTwoSets_Score_X_Set_Per_Player(int expected) {
        assertThat(expected).isGreaterThan(0);
        TennisSet set1 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player2Scores()));
        assertThat(set1.isFinishedSet()).isTrue();
        assertThat(set1.getPlayer2WonGames()).isEqualTo(6);
        assertThat(set1.getPlayer1WonGames()).isEqualTo(0);
        // There is always one game(love,love) created by the listener OnGameFinished
        // when the point 4 is reached.
        assertThat(set1.getRelatedGames()).hasSize(6);
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("6-0");

        TennisSet set2 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 5).forEach(x -> {
            match.player2Scores(); // 0-15
            match.player2Scores(); // 0-30
            match.player1Scores(); // 15-30
            match.player2Scores(); // 15-40
            match.player1Scores(); // 30-40
            match.player1Scores(); // DEUCE
            assertThat(set2.getCurrentGame().isDeuce()).isTrue();
            match.player1Scores(); // ADV
            assertThat(set2.getCurrentGame().hasAdvantage()).isTrue();
            assertThat(set2.getCurrentGame().getGameScore()).isEqualTo("ADV for " + player1Name);
            match.player1Scores(); // WIN
        });
        IntStream.rangeClosed(1, 5).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player2Scores()));
        assertThat(set2.isFinishedSet()).isFalse();
        assertThat(set2.isTieBreakActive()).isFalse();
        assertThat(set2.getPlayer2WonGames()).isEqualTo(5);
        assertThat(set2.getPlayer1WonGames()).isEqualTo(5);

        IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
        IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        assertThat(set2.isFinishedSet()).isFalse();
        assertThat(match.isFinished()).isFalse();
        assertThat(set2.isTieBreakActive()).isTrue();

        // play the Tie-Break
        IntStream.rangeClosed(1, 7).forEach(y -> {
            match.player1Scores();
            match.player2Scores();
        });

        assertThat(set2.isFinishedSet()).isFalse();
        assertThat(match.isFinished()).isFalse();
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(7-7)");

        match.player1Scores();
        match.player1Scores();
        assertThat(set2.isFinishedSet()).isTrue();
        assertThat(match.isFinished()).isFalse();
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(9-7)");
        //When the match is not finished ,
        // the system will always initiate a new set once the current is finished.
        assertThat(match.getScoreBoard().getPreviousSets()).hasSize(expected * 2);
        // End of the 2nd Set
        assertThat(match.getLiveScore())
                .isEqualTo("Serena Williams:" + expected + " Sets,Ones Jabeur:" + expected + " Sets");
    }

    @Test(expected = IllegalArgumentException.class)
    public void IllegalArgumentExceptionWhenInvalidObject() {
        match.update(match.getScoreBoard().getCurrentSet(), null);
    }

    @Test
    public void fullMatchPlayer1_3Sets_Player2_2Sets() {
        // Set1 Player1 : WonGames = 6 , Player2 WonGames = 3
        TennisSet set1 = match.getScoreBoard().getCurrentSet();
        // Player 1 -> Games = 2 , Player2 -> Games = 0
        IntStream.rangeClosed(1, 2).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player1Scores()));
        assertThat(set1.getPlayer1WonGames()).isEqualTo(2);
        assertThat(set1.getPlayer2WonGames()).isEqualTo(0);
        assertThat(set1.isFinishedSet()).isEqualTo(set1.isTieBreakActive()).isFalse();
        // Player 1 -> Games = 2 , Player2 -> Games = 3
        IntStream.rangeClosed(1, 3).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player2Scores()));
        assertThat(set1.getPlayer1WonGames()).isEqualTo(2);
        assertThat(set1.getPlayer2WonGames()).isEqualTo(3);
        assertThat(set1.isFinishedSet()).isEqualTo(set1.isTieBreakActive()).isFalse();
        assertThat(set1.getPlayer1TieBreakPoints()).isEqualTo(set1.getPlayer2TieBreakPoints()).isEqualTo(0);
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("2-3");
        assertThat(set1.getRelatedGames()).hasSize(6);
        // Player 1 -> Games = 6 , Player2 -> Games = 3
        IntStream.rangeClosed(1, 4).forEach(x -> {
            int previousWonGames = set1.getPlayer1WonGames();
            IntStream.rangeClosed(1, 2).forEach(y -> match.player1Scores());
            IntStream.rangeClosed(1, 2).forEach(y -> match.player2Scores());
            Game currentGame = set1.getCurrentGame();
            assertThat(currentGame.getGameWinner()).isEqualTo("No Winner");
            assertThat(currentGame.getGameScore()).isEqualTo("THIRTY ALL");
            assertThat(currentGame.isFinishedGame()).isFalse();
            assertThat(currentGame.isDeuce()).isFalse();
            assertThat(currentGame.getPlayer1Points()).isEqualTo(currentGame.getPlayer2Points());
            match.player1Scores();
            match.player2Scores(); //DEUCE
            assertThat(currentGame.isDeuce()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo("DEUCE");
            match.player1Scores(); // ADV for player1
            assertThat(currentGame.hasAdvantage()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo("ADV for " + player1Name);
            match.player1Scores(); // Player 1 wins
            assertThat(currentGame.isFinishedGame()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo(player1Name + " is the winner !");
            assertThat(set1.getPlayer1WonGames()).isEqualTo(previousWonGames + 1);
            assertThat(set1.getPlayer2WonGames()).isEqualTo(3);
        });
        assertThat(set1.isFinishedSet()).isTrue();
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("6-3");
        assertThat(set1.isTieBreakActive()).isFalse();
        assertThat(set1.getSetWinner()).isEqualTo(player1Name);
        //END SET 1


        // Set2 Player1 : WonGames = 6 , Player2 WonGames = 6 , Tie-Break 8-6
        TennisSet set2 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });

        assertThat(set2.isTieBreakActive()).isTrue();
        assertThat(set2.isFinishedSet()).isFalse();
        assertThat(set2.getPlayer1WonGames()).isEqualTo(set2.getPlayer2WonGames()).isEqualTo(6);
        assertThat(set2.getSetWinner()).isEqualTo("No Winner!");
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(0-0)");

        IntStream.rangeClosed(1, 6).forEach(x -> {
            match.player1Scores();
            match.player2Scores();
        });
        match.player1Scores();
        match.player1Scores();
        assertThat(set2.isFinishedSet()).isTrue();
        assertThat(set2.getSetWinner()).isEqualTo(player1Name);
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(8-6)");
        assertThat(set2.getPlayer1WonGames()).isEqualTo(6).isEqualTo(set2.getPlayer2WonGames());
        assertThat(set2.getPlayer1TieBreakPoints()).isEqualTo(8);
        assertThat(set2.getPlayer2TieBreakPoints()).isEqualTo(6);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:0 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);

        // Set3 Player1 : WonGames = 6 , Player2 WonGames = 6 , Tie-Break 6-8
        TennisSet set3 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });
        assertThat(set3.isTieBreakActive()).isTrue();
        assertThat(set3.isFinishedSet()).isFalse();
        assertThat(set3.getPlayer1WonGames()).isEqualTo(set3.getPlayer2WonGames()).isEqualTo(6);
        assertThat(set3.getSetWinner()).isEqualTo("No Winner!");
        assertThat(set3.getTieBreakScore()).isEqualTo("6-6(0-0)");
        IntStream.rangeClosed(1, 6).forEach(x -> {
            match.player1Scores();
            match.player2Scores();
        });
        match.player2Scores();
        match.player2Scores();
        assertThat(set3.isFinishedSet()).isTrue();
        assertThat(set3.getSetWinner()).isEqualTo(player2Name);
        assertThat(set3.getTieBreakScore()).isEqualTo("6-6(6-8)");
        assertThat(set3.getPlayer1WonGames()).isEqualTo(6).isEqualTo(set3.getPlayer2WonGames());
        assertThat(set3.getPlayer1TieBreakPoints()).isEqualTo(6);
        assertThat(set3.getPlayer2TieBreakPoints()).isEqualTo(8);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);

        // Set4 Player1 : WonGames = 5 , Player2 WonGames = 6 , 5-7
        TennisSet set4 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 5).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });
        IntStream.rangeClosed(1, 2).forEach(y -> IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores()));
        assertThat(set4.isFinishedSet()).isTrue();
        assertThat(set4.getSetWinner()).isEqualTo(player2Name);
        assertThat(set4.getPlayer1WonGames()).isEqualTo(5);
        assertThat(set4.getPlayer2WonGames()).isEqualTo(7);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:2 Sets");
        assertThat(match.getLeader()).isEqualTo("No Leader!");

        // Set5 Player1 : WonGames = 6 , Player2 WonGames = 0
        TennisSet set5 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(y -> IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores()));
        assertThat(set5.isFinishedSet()).isTrue();
        assertThat(set5.getSetWinner()).isEqualTo(player1Name);
        assertThat(set5.getPlayer1WonGames()).isEqualTo(6);
        assertThat(set5.getPlayer2WonGames()).isEqualTo(0);
        assertThat(match.isFinished()).isTrue();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:3 Sets,Ones Jabeur:2 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);
        assertThat(match.getFinalScore()).isEqualTo("Serena Williams defeated Ones Jabeur 6-3,6-6(8-6),6-6(6-8),5-7,6-0");

    }

    @Test
    public void fullMatchPlayer1_2Sets_Player2_3Sets() {
        // Set1 Player1 : WonGames = 6 , Player2 WonGames = 3
        TennisSet set1 = match.getScoreBoard().getCurrentSet();
        // Player 1 -> Games = 2 , Player2 -> Games = 0
        IntStream.rangeClosed(1, 2).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player1Scores()));
        assertThat(set1.getPlayer1WonGames()).isEqualTo(2);
        assertThat(set1.getPlayer2WonGames()).isEqualTo(0);
        assertThat(set1.isFinishedSet()).isEqualTo(set1.isTieBreakActive()).isFalse();
        // Player 1 -> Games = 2 , Player2 -> Games = 3
        IntStream.rangeClosed(1, 3).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player2Scores()));
        assertThat(set1.getPlayer1WonGames()).isEqualTo(2);
        assertThat(set1.getPlayer2WonGames()).isEqualTo(3);
        assertThat(set1.isFinishedSet()).isEqualTo(set1.isTieBreakActive()).isFalse();
        assertThat(set1.getPlayer1TieBreakPoints()).isEqualTo(set1.getPlayer2TieBreakPoints()).isEqualTo(0);
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("2-3");
        assertThat(set1.getRelatedGames()).hasSize(6);
        // Player 1 -> Games = 6 , Player2 -> Games = 3
        IntStream.rangeClosed(1, 4).forEach(x -> {
            int previousWonGames = set1.getPlayer1WonGames();
            IntStream.rangeClosed(1, 2).forEach(y -> match.player1Scores());
            IntStream.rangeClosed(1, 2).forEach(y -> match.player2Scores());
            Game currentGame = set1.getCurrentGame();
            assertThat(currentGame.getGameWinner()).isEqualTo("No Winner");
            assertThat(currentGame.getGameScore()).isEqualTo("THIRTY ALL");
            assertThat(currentGame.isFinishedGame()).isFalse();
            assertThat(currentGame.isDeuce()).isFalse();
            assertThat(currentGame.getPlayer1Points()).isEqualTo(currentGame.getPlayer2Points());
            match.player1Scores();
            match.player2Scores(); //DEUCE
            assertThat(currentGame.isDeuce()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo("DEUCE");
            match.player1Scores(); // ADV for player1
            assertThat(currentGame.hasAdvantage()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo("ADV for " + player1Name);
            match.player1Scores(); // Player 1 wins
            assertThat(currentGame.isFinishedGame()).isTrue();
            assertThat(currentGame.getGameScore()).isEqualTo(player1Name + " is the winner !");
            assertThat(set1.getPlayer1WonGames()).isEqualTo(previousWonGames + 1);
            assertThat(set1.getPlayer2WonGames()).isEqualTo(3);
        });
        assertThat(set1.isFinishedSet()).isTrue();
        assertThat(set1.getCurrentSetScoreByLeader()).isEqualTo("6-3");
        assertThat(set1.isTieBreakActive()).isFalse();
        assertThat(set1.getSetWinner()).isEqualTo(player1Name);
        //END SET 1


        // Set2 Player1 : WonGames = 6 , Player2 WonGames = 6 , Tie-Break 8-6
        TennisSet set2 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });

        assertThat(set2.isTieBreakActive()).isTrue();
        assertThat(set2.isFinishedSet()).isFalse();
        assertThat(set2.getPlayer1WonGames()).isEqualTo(set2.getPlayer2WonGames()).isEqualTo(6);
        assertThat(set2.getSetWinner()).isEqualTo("No Winner!");
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(0-0)");

        IntStream.rangeClosed(1, 6).forEach(x -> {
            match.player1Scores();
            match.player2Scores();
        });
        match.player1Scores();
        match.player1Scores();
        assertThat(set2.isFinishedSet()).isTrue();
        assertThat(set2.getSetWinner()).isEqualTo(player1Name);
        assertThat(set2.getTieBreakScore()).isEqualTo("6-6(8-6)");
        assertThat(set2.getPlayer1WonGames()).isEqualTo(6).isEqualTo(set2.getPlayer2WonGames());
        assertThat(set2.getPlayer1TieBreakPoints()).isEqualTo(8);
        assertThat(set2.getPlayer2TieBreakPoints()).isEqualTo(6);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:0 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);

        // Set3 Player1 : WonGames = 6 , Player2 WonGames = 6 , Tie-Break 6-8
        TennisSet set3 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });
        assertThat(set3.isTieBreakActive()).isTrue();
        assertThat(set3.isFinishedSet()).isFalse();
        assertThat(set3.getPlayer1WonGames()).isEqualTo(set3.getPlayer2WonGames()).isEqualTo(6);
        assertThat(set3.getSetWinner()).isEqualTo("No Winner!");
        assertThat(set3.getTieBreakScore()).isEqualTo("6-6(0-0)");
        IntStream.rangeClosed(1, 6).forEach(x -> {
            match.player1Scores();
            match.player2Scores();
        });
        match.player2Scores();
        match.player2Scores();
        assertThat(set3.isFinishedSet()).isTrue();
        assertThat(set3.getSetWinner()).isEqualTo(player2Name);
        assertThat(set3.getTieBreakScore()).isEqualTo("6-6(6-8)");
        assertThat(set3.getPlayer1WonGames()).isEqualTo(6).isEqualTo(set3.getPlayer2WonGames());
        assertThat(set3.getPlayer1TieBreakPoints()).isEqualTo(6);
        assertThat(set3.getPlayer2TieBreakPoints()).isEqualTo(8);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
        assertThat(match.getLeader()).isEqualTo(player1Name);

        // Set4 Player1 : WonGames = 5 , Player2 WonGames = 6 , 5-7
        TennisSet set4 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 5).forEach(w -> {
            IntStream.rangeClosed(1, 4).forEach(x -> match.player1Scores());
            IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores());
        });
        IntStream.rangeClosed(1, 2).forEach(y -> IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores()));
        assertThat(set4.isFinishedSet()).isTrue();
        assertThat(set4.getSetWinner()).isEqualTo(player2Name);
        assertThat(set4.getPlayer1WonGames()).isEqualTo(5);
        assertThat(set4.getPlayer2WonGames()).isEqualTo(7);
        assertThat(match.isFinished()).isFalse();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:2 Sets");
        assertThat(match.getLeader()).isEqualTo("No Leader!");

        // Set5 Player1 : WonGames = 0 , Player2 WonGames = 6
        TennisSet set5 = match.getScoreBoard().getCurrentSet();
        IntStream.rangeClosed(1, 6).forEach(y -> IntStream.rangeClosed(1, 4).forEach(x -> match.player2Scores()));
        assertThat(set5.isFinishedSet()).isTrue();
        assertThat(set5.getSetWinner()).isEqualTo(player2Name);
        assertThat(set5.getPlayer1WonGames()).isEqualTo(0);
        assertThat(set5.getPlayer2WonGames()).isEqualTo(6);
        assertThat(match.isFinished()).isTrue();
        assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:3 Sets");
        assertThat(match.getLeader()).isEqualTo(player2Name);
        assertThat(match.getFinalScore()).isEqualTo("Ones Jabeur defeated Serena Williams 3-6,6-6(6-8),6-6(8-6),7-5,6-0");

    }

    @Test(expected = IllegalStateException.class)
    public void scoresWhereasTheMatchIsFinishedPlayer1(){
        match.setFinished(true);
        match.player1Scores();
    }
    @Test(expected = IllegalStateException.class)
    public void scoresWhereasTheMatchIsFinishedPlayer2(){
        match.setFinished(true);
        match.player2Scores();
    }

}

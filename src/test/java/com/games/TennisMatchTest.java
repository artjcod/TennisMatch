package com.games;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;



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
		match.getScoreBoard().player1SetScores();
		match.getScoreBoard().player2SetScores();
		match.getScoreBoard().player1SetScores();
		assertThat(match.getLeader()).isEqualTo(player1Name);
	}

	@Test
	public void leaderIsPlayer2() {
		match.getScoreBoard().player2SetScores();
		assertThat(match.getLeader()).isEqualTo(player2Name);
	}

	@Test
	public void noLeader() {
		assertThat(match.getLeader()).isEqualTo("No Leader!");
	}

	@Test
	public void matchISFinishedPlayer1Wins_3Sets_Love() {
		ScoreBoard scoreBoard = match.getScoreBoard();
		IntStream.rangeClosed(1,3).forEach(w-> {
			TennisSet tennisSet = new TennisSet(scoreBoard, player1Name, player2Name);
			tennisSet.setPlayer1Score(6);
			tennisSet.setPlayer2Score(3);
			scoreBoard.getPreviousSets().add(tennisSet);
		});
		IntStream.rangeClosed(1, 3).forEach(i -> scoreBoard.player1SetScores());
		assertThat(match.isFinished()).isTrue();
		assertThat(match.getLeader()).isEqualTo(player1Name);
	}
	@Test
	public void mathIsFinishedPlayer2Wins_3Sets_Love() {
		ScoreBoard scoreBoard = match.getScoreBoard();
		IntStream.rangeClosed(1,3).forEach(w-> {
			TennisSet tennisSet = new TennisSet(scoreBoard, player1Name, player2Name);
			tennisSet.setPlayer1Score(5);
			tennisSet.setPlayer2Score(7);
			scoreBoard.getPreviousSets().add(tennisSet);
		});
		IntStream.rangeClosed(1, 3).forEach(i -> scoreBoard.player2SetScores());
		assertThat(match.isFinished()).isTrue();
		assertThat(match.getLeader()).isEqualTo(player2Name);
	}

	@Test
	public void mathNotFinishedScore2SetsAll() {
		ScoreBoard scoreBoard = match.getScoreBoard();
		IntStream.rangeClosed(1, 2).forEach(i -> scoreBoard.player1SetScores());
		IntStream.rangeClosed(1, 2).forEach(i -> scoreBoard.player2SetScores());
		assertThat(match.isFinished()).isFalse();
		assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:2 Sets");
		assertThat(match.getLeader()).isEqualTo("No Leader!");
	}

	@Test
	public void liveScoreAnnouncedAsTwoSets_To_One() {
		match.getScoreBoard().player1SetScores();
		match.getScoreBoard().player1SetScores();
		match.getScoreBoard().player2SetScores();
		assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
	}

	@Test
	public void mathFinishedScore3Sets_1Sets() {
		ScoreBoard scoreBoard = match.getScoreBoard();
		IntStream.range(1, 3).forEach(val -> {
			IntStream.range(1, 6).forEach(value -> {
				scoreBoard.getCurrentSet().player1Scores(); // 5-0
			});
			IntStream.range(1, 5).forEach(value -> {
				scoreBoard.getCurrentSet().player2Scores(); // 5-4
			});
			scoreBoard.getCurrentSet().player1Scores(); // 6-4

			// player 1 wins the 1stSet & 2nd Set
		});

		IntStream.range(1, 6).forEach(value -> {
			scoreBoard.getCurrentSet().player2Scores(); // 0-5
		});
		IntStream.range(1, 5).forEach(value -> {
			scoreBoard.getCurrentSet().player1Scores(); // 4-5
		});
		scoreBoard.getCurrentSet().player2Scores(); // 6-4

		// player 2 wins the set

		IntStream.range(1, 6).forEach(value -> {
			scoreBoard.getCurrentSet().player1Scores(); // 5-0
		});
		IntStream.range(1, 5).forEach(value -> {
			scoreBoard.getCurrentSet().player2Scores(); // 5-4
		});
		scoreBoard.getCurrentSet().player1Scores(); // 6-4

		// player 1 wins the set

		assertThat(match.isFinished()).isTrue();
		assertThat(match.getFinalScore()).isEqualTo("Serena Williams defeated Ones Jabeur 6-4,6-4,4-6,6-4");
		assertThat(match.getLeader()).isEqualTo(player1Name);
	}

	@Test
	public void mathFinishedScore3Sets_2Sets_TieBreak() {
		ScoreBoard scoreBoard = match.getScoreBoard();

		// Set 1 Winner Player 1 : Serena Williams | 7-5
		TennisSet set1;
		set1 = scoreBoard.getCurrentSet();
		IntStream.rangeClosed(1, 5).forEach(i -> {
			set1.player1Scores();
			set1.player2Scores();
		}); // gives 5-5
		assertThat(set1.getSetScoreByLeader()).isEqualTo("5-5");

		IntStream.rangeClosed(1, 2).forEach(i -> set1.player1Scores());// gives 7-5
		assertThat(set1.getSetScoreByLeader()).isEqualTo("7-5");
		assertThat(set1.getSetWinner()).isEqualTo(player1Name);

		// Set 2 Winner Player 1 : Serena Williams | 6-6(7-5)
		TennisSet set2 = scoreBoard.getCurrentSet();

		IntStream.rangeClosed(1, 6).forEach(i -> {
			set2.player1Scores();
			set2.player2Scores();
		}); // gives 6-6 Tie-Break

		assertThat(set2.getSetScoreByLeader()).isEqualTo("6-6(0-0)");
		assertThat(set2.isTieBreakActive()).isTrue();

		IntStream.rangeClosed(1, 4).forEach(i -> {
			set2.player1Scores(); // 1-0
			set2.player2Scores(); // 1-1
		}); // gives 4-4

		assertThat(set2.getSetScoreByLeader()).isEqualTo("6-6(4-4)");

		IntStream.rangeClosed(1, 2).forEach(i -> set2.player1Scores()); // gives 6-4

		assertThat(set2.getSetScoreByLeader()).isEqualTo("6-6(6-4)");
		assertThat(set2.getSetWinner()).isEqualTo("No Winner!");
		assertThat(set2.isFinishedSet()).isFalse();

		set2.player2Scores(); // 6-5
		set2.player1Scores(); // 7-5

		assertThat(set2.getSetScoreByLeader()).isEqualTo("6-6(7-5)");
		assertThat(set2.getSetWinner()).isEqualTo(player1Name);
		assertThat(set2.isFinishedSet()).isTrue();

		// Check the board score & match
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
		assertThat(match.getLeader()).isEqualTo(player1Name);
		assertThat(match.isFinished()).isFalse();
		assertThat(match.getFinalScore()).isEqualTo(match.getLiveScore())
				.isEqualTo("Serena Williams:2 Sets,Ones Jabeur:0 Sets");

		// Set 3 Winner Player 1 : Ones Jabeur | 6-6(8-10)
		TennisSet set3 = scoreBoard.getCurrentSet();

		IntStream.rangeClosed(1, 6).forEach(j -> {
			set3.player2Scores();
			set3.player1Scores();
		}); // gives 6-6
		assertThat(set3.getSetScoreByLeader()).isEqualTo("6-6(0-0)");
		assertThat(set3.isTieBreakActive()).isTrue();
		IntStream.rangeClosed(1, 6).forEach(j -> {
			set3.player1Scores();
			set3.player2Scores();
		}); // gives 6-6(6-6)
		assertThat(set3.getSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(6-6)");
		IntStream.rangeClosed(1, 2).forEach(j -> {
			set3.player2Scores();
			set3.player1Scores();
		}); // gives 6-6(8-8)
		assertThat(set3.getSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(8-8)");

		set3.player2Scores();
		set3.player2Scores();
		assertThat(set3.getSetScoreByLeader()).isEqualTo(set3.getTieBreakScore()).isEqualTo("6-6(8-10)");
		assertThat(match.isFinished()).isFalse();
		assertThat(match.getFinalScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:1 Sets");
		assertThat(match.getLeader()).isEqualTo(player1Name);

		// Set 4 Winner Player 1 : Serena Williams 6-6 (15-13)
		TennisSet set4 = scoreBoard.getCurrentSet();

		IntStream.rangeClosed(1, 6).forEach(i -> {
			set4.player1Scores();
			set4.player2Scores();
		});
		assertThat(set4.getSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(0-0)");

		IntStream.rangeClosed(1, 13).forEach(i -> {
			set4.player1Scores();
			set4.player2Scores();
		});
		assertThat(set4.getSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(13-13)");
		set4.player1Scores();
		set4.player1Scores();
		assertThat(set4.getSetScoreByLeader()).isEqualTo(set4.getTieBreakScore()).isEqualTo("6-6(15-13)");
		assertThat(match.isFinished()).isTrue();
		assertThat(match.getFinalScore())
				.isEqualTo("Serena Williams defeated Ones Jabeur 7-5,6-6(7-5),6-6(8-10),6-6(15-13)");
		assertThat(match.getLeader()).isEqualTo(player1Name);
	}

	@Test
	public void MatchFinishedPlayer2Wins() {
		simulateTwoSets_Score_One_Set_Per_Player(1);
		simulateTwoSets_Score_One_Set_Per_Player(2);
		assertThat(match.isFinished()).isFalse();
		TennisSet set3 = match.getScoreBoard().getCurrentSet();

		IntStream.rangeClosed(1, 6).forEach(z -> IntStream.rangeClosed(1, 4).forEach(w -> match.player2Scores()));
		assertThat(set3.isFinishedSet()).isTrue();
		assertThat(match.isFinished()).isTrue();
		
		assertThat(match.getLeader()).isEqualTo(player2Name);
		assertThat(match.getFinalScore()).isEqualTo("Ones Jabeur defeated Serena Williams 0-6,6-6(9-7),0-6,6-6(9-7),0-6");

	}

	private void simulateTwoSets_Score_One_Set_Per_Player(int expected) {
		assertThat(expected).isGreaterThan(0);
		TennisSet set1 = match.getScoreBoard().getCurrentSet();
		IntStream.rangeClosed(1, 6).forEach(x -> IntStream.rangeClosed(1, 4).forEach(y -> match.player2Scores()));
		assertThat(set1.isFinishedSet()).isTrue();
		assertThat(set1.getPlayer2Score()).isEqualTo(6);
		assertThat(set1.getPlayer1Score()).isEqualTo(0);
		// There is always one game(love,love) created by the listener OnGameFinished
		// when the Game4 is ended.
		assertThat(set1.getRelatedGames()).hasSize(6);
		assertThat(set1.getSetScoreByLeader()).isEqualTo("0-6");

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
		assertThat(set2.getPlayer2Score()).isEqualTo(5);
		assertThat(set2.getPlayer1Score()).isEqualTo(5);

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

		assertThat(match.getScoreBoard().getPreviousSets()).hasSize(expected * 2);
		// End of the 2nd Set
		assertThat(match.getLiveScore())
				.isEqualTo("Serena Williams:" + expected + " Sets,Ones Jabeur:" + expected + " Sets");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void IllegalArgumentExceptionWhenInvalidObject() {
		match.update(match.getScoreBoard().getCurrentSet(), null);
	}

}

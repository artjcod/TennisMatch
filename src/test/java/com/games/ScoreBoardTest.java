package com.games;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.IntStream;

public class ScoreBoardTest {

	private ScoreBoard scoreBoard;
	private TennisMatch match;
	private String player1Name, player2Name;

	@Before
	public void before() {
		player1Name = "Serena Williams";
		player2Name = "Ones Jabeur";
		match = new TennisMatch(player1Name, player2Name);
		scoreBoard = match.getScoreBoard();
	}

	@Test
	public void testInitBoard() {
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getPlayer1Name()).isEqualTo(player1Name);
		assertThat(scoreBoard.getPlayer2Name()).isEqualTo(player2Name);
	}

	@Test
	public void testCloseCurrentSet() {
		TennisSet currentSet = scoreBoard.getCurrentSet();
		currentSet.setPlayer1Score(6);
		currentSet.setPlayer2Score(4);
		scoreBoard.closeCurrentSet();
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet()).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getPreviousSets()).hasSize(1);
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
	}

	@Test
	public void MatchFinished() {

		// 1st Set Player 1 Wins 6-4
		TennisSet set1 = scoreBoard.getCurrentSet();
		IntStream.rangeClosed(1, 4).forEach(x -> {
			set1.player1Scores();
			set1.player2Scores();
		}); // gives 4-4

		IntStream.rangeClosed(1, 2).forEach(x -> set1.player1Scores()); // gives 6-4

		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);

		// Check whether a new set is started
		TennisSet set2 = scoreBoard.getCurrentSet();
		assertThat(set2).isNotEqualTo(set1);

		// 2nd Set Player 1 Wins 6-2

		IntStream.rangeClosed(1, 2).forEach(x -> {
			set2.player1Scores();
			set2.player2Scores();
		}); // gives 2-2

		IntStream.rangeClosed(1, 4).forEach(x -> set2.player1Scores()); // gives 6-2

		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
		assertThat(match.getLiveScore()).isEqualTo("Serena Williams:2 Sets,Ones Jabeur:0 Sets");

		// Check whether a new set is started
		TennisSet set3 = scoreBoard.getCurrentSet();
		assertThat(set3).isNotEqualTo(set1).isNotEqualTo(set2);

		// 3rd Set Player 2 Wins 5-7

		IntStream.rangeClosed(1, 5).forEach(x -> {
			set3.player1Scores();
			set3.player2Scores();
		}); // gives 5-5

		IntStream.rangeClosed(1, 2).forEach(x -> set3.player2Scores()); // gives 5-7

		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(1);
		assertThat(scoreBoard.isFinishedMatch()).isFalse();
		assertThat(scoreBoard.getPreviousSets()).hasSize(3);

		// 4th Set Player 1 Wins Tie Break 10-8
		TennisSet set4 = scoreBoard.getCurrentSet();

		assertThat(set4).isNotEqualTo(set1).isNotEqualTo(set2).isNotEqualTo(set3);

		IntStream.rangeClosed(1, 6).forEach(x -> {
			set4.player1Scores();
			set4.player2Scores();
		}); // gives 6-6

		assertThat(set4.isTieBreakActive()).isTrue();
		assertThat(set4.isFinishedSet()).isFalse();

		IntStream.rangeClosed(1, 8).forEach(x -> {
			set4.player1Scores();
			set4.player2Scores();
		}); // gives 6-6(8-8)

		IntStream.rangeClosed(1, 2).forEach(x -> set4.player1Scores()); // gives 6-6(10-8)

		assertThat(set4.isFinishedSet()).isTrue();
		assertThat(scoreBoard.isFinishedMatch()).isTrue();
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(3);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPreviousSets()).hasSize(4);

		assertThat(match.getLeader()).isEqualTo(player1Name);
		assertThat(match.isFinished()).isTrue();
		assertThat(match.getFinalScore()).isEqualTo("Serena Williams defeated Ones Jabeur 6-4,6-2,5-7,6-6(10-8)");
		assertThat(match.getLiveScore()).isEqualTo("Serena Williams:3 Sets,Ones Jabeur:1 Sets");
	}
	
	@Test
	public void testIsFinished_Player2Wins() {
		scoreBoard.setPlayer1SetScore(2);
		scoreBoard.setPlayer2SetScore(3);
		assertThat(scoreBoard.isFinishedMatch()).isTrue();
	}

	@Test
	public void testProperties_Setter() {
		String player3Name = "Rafael Nadal";
		String player4Name = "Roger Federer";
		scoreBoard.setPlayer1Name(player3Name);
		scoreBoard.setPlayer2Name(player4Name);
		assertThat(scoreBoard.getPlayer1Name()).isEqualTo(player3Name);
		assertThat(scoreBoard.getPlayer2Name()).isEqualTo(player4Name);
		scoreBoard.setPlayer1SetScore(3);
		scoreBoard.setPlayer2SetScore(2);
		assertThat(scoreBoard.getPlayer1SetScore()).isGreaterThan(scoreBoard.getPlayer2SetScore());
	}

}

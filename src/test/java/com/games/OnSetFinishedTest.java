package com.games;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class OnSetFinishedTest {

	private ScoreBoard scoreBoard;
	private TennisSet currentSet;
	private String player1Name, player2Name;

	@Before
	public void before() {
		player1Name = "Serena Williams";
		player2Name = "Ones Jabeur";
		this.scoreBoard = new ScoreBoard(player1Name, player2Name);
		this.currentSet = scoreBoard.getCurrentSet();
	}

	@Test
	public void checkTestInitIsOK() {
		assertThat(currentSet).isNotNull();
		assertThat(scoreBoard).isNotNull();
		assertThat(currentSet.getRelatedScoreBoard()).isEqualTo(scoreBoard);
		assertThat(scoreBoard.getPreviousSets()).hasSize(0);
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet()).isEqualTo(currentSet);
	}

	@Test
	public void setWonByPlayerOne_Six_TWO() {
		currentSet.player1Scores(); // 1-0
		currentSet.player2Scores(); // 1-1
		currentSet.player2Scores(); // 1-2
		currentSet.player1Scores(); // 2-2
		currentSet.player1Scores(); // 3-2
		currentSet.player1Scores(); // 4-2
		currentSet.player1Scores(); // 5-2
		currentSet.player1Scores(); // 6-2
		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);

		// The listener will start a new Set
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getCurrentSet()).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getCurrentSet().getPlayer1Score()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet().getPlayer2Score()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
	}

	@Test
	public void testUpdateSetScorePlayer1Scores2_0() {
		TennisSet currentSet = scoreBoard.getCurrentSet();
		assertThat(currentSet).isNotNull();
		currentSet.player1Scores(); // 1-0
		currentSet.player2Scores(); // 1-1
		currentSet.player2Scores(); // 1-2
		currentSet.player1Scores(); // 2-2
		currentSet.player1Scores(); // 3-2
		currentSet.player1Scores(); // 4-2
		currentSet.player1Scores(); // 5-2
		currentSet.player1Scores(); // 6-2
		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);

		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);

		// Next Set
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
		nextSet.player1Scores(); // 6-4
		assertThat(nextSet.isFinishedSet()).isTrue();
		assertThat(nextSet.getSetScoreByLeader()).isEqualTo("6-4");
		assertThat(scoreBoard.getPreviousSets()).hasSize(2);

		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
		assertThat(nextSet).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);
		assertThat(nextSet.isFinishedSet()).isTrue();
		// the next Set will be started
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
	}

	@Test
	public void testUpdateSetScorePlayer1WinsASet8_6_TieBreak() {
		TennisSet currentSet = scoreBoard.getCurrentSet();
		assertThat(currentSet).isNotNull();
		// the player1 wins the 1st set
		currentSet.player1Scores(); // 1-0
		currentSet.player2Scores(); // 1-1
		currentSet.player2Scores(); // 1-2
		currentSet.player1Scores(); // 2-2
		currentSet.player1Scores(); // 3-2
		currentSet.player1Scores(); // 4-2
		currentSet.player1Scores(); // 5-2
		currentSet.player1Scores(); // 6-2

		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);

		// start the next set (started via the listener OnFinishSet)

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
		nextSet.player1Scores(); // 6-5
		// Tie Break activated
		nextSet.player2Scores(); // 6-6
		assertThat(nextSet.getPlayer1Score()).isEqualTo(nextSet.getPlayer2Score()).isEqualTo(6);
		assertThat(nextSet.isFinishedSet()).isFalse();
		// check whether the time break is active
		assertThat(nextSet.isTieBreakActive()).isTrue();
		assertThat(nextSet.getPlayer1TieBreakScore()).isEqualTo(nextSet.getPlayer2TieBreakScore()).isEqualTo(0);
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
		// the next Set will be started
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
		assertThat(scoreBoard.getPreviousSets()).hasSize(2);

		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
		assertThat(nextSet).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getPlayer1SetScore()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2SetScore()).isEqualTo(0);

	}

	@Test(expected = IllegalArgumentException.class)
	public void throwIllegalArgumentExceptionWhenInvalidObject() {
		OnSetFinished listener = new OnSetFinished();
		listener.update(new Game(player1Name, player2Name,null), null);
	}

}

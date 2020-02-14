package com.games;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

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
		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(scoreBoard.getPlayer2WonSets()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet()).isEqualTo(currentSet);
	}

	@Test
	public void setWonByPlayerOne_Six_TWO() {
		currentSet.player1WinsCurrentGame(); // 1-0
		currentSet.player2WinsCurrentGame(); // 1-1
		currentSet.player2WinsCurrentGame(); // 1-2
		currentSet.player1WinsCurrentGame(); // 2-2
		currentSet.player1WinsCurrentGame(); // 3-2
		currentSet.player1WinsCurrentGame(); // 4-2
		currentSet.player1WinsCurrentGame(); // 5-2
		currentSet.player1WinsCurrentGame(); // 6-2
		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getCurrentSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);

		// The listener will start a new Set
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getCurrentSet()).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getCurrentSet().getPlayer1WonGames()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet().getPlayer2WonGames()).isEqualTo(0);
		assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);
	}

	@Test
	public void testUpdateSetScorePlayer1Scores2_0() {
		TennisSet currentSet = scoreBoard.getCurrentSet();
		assertThat(currentSet).isNotNull();
		currentSet.player1WinsCurrentGame(); // 1-0
		currentSet.player2WinsCurrentGame(); // 1-1
		currentSet.player2WinsCurrentGame(); // 1-2
		currentSet.player1WinsCurrentGame(); // 2-2
		currentSet.player1WinsCurrentGame(); // 3-2
		currentSet.player1WinsCurrentGame(); // 4-2
		currentSet.player1WinsCurrentGame(); // 5-2
		currentSet.player1WinsCurrentGame(); // 6-2
		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getCurrentSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);

		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);

		// Next Set
		TennisSet nextSet = scoreBoard.getCurrentSet();
		assertThat(nextSet).isNotNull();
		nextSet.player1WinsCurrentGame(); // 1-0
		nextSet.player2WinsCurrentGame(); // 1-1
		nextSet.player2WinsCurrentGame(); // 1-2
		nextSet.player1WinsCurrentGame(); // 2-2
		nextSet.player1WinsCurrentGame(); // 3-2
		nextSet.player1WinsCurrentGame(); // 4-2
		nextSet.player1WinsCurrentGame(); // 5-2
		nextSet.player2WinsCurrentGame(); // 5-3
		nextSet.player2WinsCurrentGame(); // 5-4
		nextSet.player1WinsCurrentGame(); // 6-4
		assertThat(nextSet.isFinishedSet()).isTrue();
		assertThat(nextSet.getCurrentSetScoreByLeader()).isEqualTo("6-4");
		assertThat(scoreBoard.getPreviousSets()).hasSize(2);

		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
		assertThat(nextSet).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);
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
		currentSet.player1WinsCurrentGame(); // 1-0
		currentSet.player2WinsCurrentGame(); // 1-1
		currentSet.player2WinsCurrentGame(); // 1-2
		currentSet.player1WinsCurrentGame(); // 2-2
		currentSet.player1WinsCurrentGame(); // 3-2
		currentSet.player1WinsCurrentGame(); // 4-2
		currentSet.player1WinsCurrentGame(); // 5-2
		currentSet.player1WinsCurrentGame(); // 6-2

		assertThat(currentSet.isFinishedSet()).isTrue();
		assertThat(currentSet.getCurrentSetScoreByLeader()).isEqualTo("6-2");
		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(1);
		assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);

		// start the next set (started via the listener OnFinishSet)

		TennisSet nextSet = scoreBoard.getCurrentSet();
		assertThat(nextSet).isNotNull();
		nextSet.player1WinsCurrentGame(); // 1-0
		nextSet.player2WinsCurrentGame(); // 1-1
		nextSet.player2WinsCurrentGame(); // 1-2
		nextSet.player1WinsCurrentGame(); // 2-2
		nextSet.player1WinsCurrentGame(); // 3-2
		nextSet.player1WinsCurrentGame(); // 4-2
		nextSet.player1WinsCurrentGame(); // 5-2
		nextSet.player2WinsCurrentGame(); // 5-3
		nextSet.player2WinsCurrentGame(); // 5-4
		nextSet.player2WinsCurrentGame(); // 5-5
		nextSet.player1WinsCurrentGame(); // 6-5
		// Tie Break activated
		nextSet.player2WinsCurrentGame(); // 6-6
		assertThat(nextSet.getPlayer1WonGames()).isEqualTo(nextSet.getPlayer2WonGames()).isEqualTo(6);
		assertThat(nextSet.isFinishedSet()).isFalse();
		// check whether the time break is active
		assertThat(nextSet.isTieBreakActive()).isTrue();
		assertThat(nextSet.getPlayer1TieBreakPoints()).isEqualTo(nextSet.getPlayer2TieBreakPoints()).isEqualTo(0);

		// gives 3-0
		IntStream.rangeClosed(1, 3).forEach(w -> nextSet.player1WinsOnePoint());
		//gives 3-5
		IntStream.rangeClosed(1, 5).forEach(w -> nextSet.player2WinsOnePoint());
        // gives 6-5
		IntStream.rangeClosed(1, 3).forEach(w -> nextSet.player1WinsOnePoint());

		nextSet.player2WinsOnePoint(); // 6-6
		nextSet.player1WinsOnePoint(); // 7-6
		nextSet.player1WinsOnePoint(); // 8-6

		assertThat(nextSet.getPlayer1TieBreakPoints()).isEqualTo(8);
		assertThat(nextSet.getPlayer2TieBreakPoints()).isEqualTo(6);
		assertThat(nextSet.getPlayer1WonGames()).isEqualTo(nextSet.getPlayer2WonGames()).isEqualTo(6);
		assertThat(nextSet.isFinishedSet()).isTrue();
		assertThat(nextSet.getSetWinner()).isEqualTo(player1Name);
		assertThat(nextSet.getCurrentSetScoreByLeader()).isEqualTo("6-6(8-6)");
		// the next Set will be started
		assertThat(scoreBoard.getCurrentSet()).isNotNull();
		assertThat(scoreBoard.getCurrentSet().isFinishedSet()).isFalse();
		assertThat(scoreBoard.getPreviousSets()).hasSize(2);

		assertThat(scoreBoard.getPreviousSets()).contains(currentSet);
		assertThat(scoreBoard.getPreviousSets()).contains(nextSet);
		assertThat(nextSet).isNotEqualTo(currentSet);
		assertThat(scoreBoard.getPlayer1WonSets()).isEqualTo(2);
		assertThat(scoreBoard.getPlayer2WonSets()).isEqualTo(0);

	}

	@Test(expected = IllegalArgumentException.class)
	public void throwIllegalArgumentExceptionWhenInvalidObject() {
		OnSetFinished listener = new OnSetFinished();
		listener.update(new Game(player1Name, player2Name,null), null);
	}

}

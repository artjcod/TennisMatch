package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is subject to Finished listener and considered
 * as an Observer to the score board.
 *
 * @author snaceur
 * @see com.games.FinishedListener
 * @see lombok.Lombok
 */

@EqualsAndHashCode(exclude = {"scoreBoard"}, callSuper = false)
@Data
public class TennisMatch implements FinishedListener {

    private ScoreBoard scoreBoard;
    private String player1Name;
    private String player2Name;
    private boolean finished;

    public TennisMatch(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.scoreBoard = new ScoreBoard(player1Name, player2Name);
        this.scoreBoard.addObserver(this);
    }

    public boolean isFinished() {
        return finished;
    }

    public String getLeader() {
        if (scoreBoard.getPlayer1SetScore() > scoreBoard.getPlayer2SetScore()) {
            return player1Name;
        } else if (scoreBoard.getPlayer2SetScore() > scoreBoard.getPlayer1SetScore()) {
            return player2Name;
        } else {
            return "No Leader!";
        }
    }

    public String getFinalScore() {
        if (isFinished()) {
            StringBuilder builder = new StringBuilder();
            if (getLeader().equals(player1Name)) {
                builder.append(player1Name).append(" defeated ");
                builder.append(player2Name);
                builder.append(" ");
                String score = scoreBoard.getPreviousSets().stream().map(TennisSet::getSetScoreByLeader)
                        .collect(Collectors.joining(","));
                builder.append(score);
            } else {
                builder.append(player2Name).append(" defeated ");
                builder.append(player1Name);
                builder.append(" ");
                String score = scoreBoard.getPreviousSets().stream().map(TennisSet::getSetScoreByLeader)
                        .collect(Collectors.joining(","));
                builder.append(score);
            }
            return builder.toString();
        }
        return getLiveScore();
    }

    public String getLiveScore() {
        StringBuilder builder = new StringBuilder(player1Name);
        builder.append(":").append(scoreBoard.getPlayer1SetScore());
        builder.append(" Sets").append(",").append(player2Name).append(":");
        builder.append(scoreBoard.getPlayer2SetScore()).append(" Sets");
        return builder.toString();
    }

    public void player1Scores() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        if (currentSet.isTieBreakActive()) {
            currentSet.player1Scores();
        } else {
            Game currentGame = currentSet.getCurrentGame();
            currentGame.player1Scores();
        }

    }

    public void player2Scores() {
        TennisSet currentSet = scoreBoard.getCurrentSet();
        if (currentSet.isTieBreakActive()) {
            currentSet.player2Scores();
        } else {
            Game currentGame = currentSet.getCurrentGame();
            currentGame.player2Scores();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ScoreBoard) {
            this.finished = true;
            createMatchReport(scoreBoard);
        } else {
            throw new IllegalArgumentException("Invalid object type received!");
        }
    }

    public void createMatchReport(ScoreBoard scoreBoard) {
        List<TennisSet> matchSets = scoreBoard.getPreviousSets();
        Map<Boolean, List<TennisSet>> isTieBreakSets = matchSets.stream().collect(Collectors.partitioningBy(set -> set.isTieBreakActive()));
		List<Object[]> noTieBreakSets = isTieBreakSets.get(false).stream().
				map(val -> Stream.of(val.getPlayer1Score(), val.getPlayer2Score()).toArray()).collect(Collectors.toList());
		List<Object[]> tieBreakSets = isTieBreakSets.get(true).stream().
				map(val -> Stream.of(val.getPlayer1Score() +
								"(" + val.getPlayer1TieBreakScore() + ")",
						val.getPlayer2Score() + "(" + val.getPlayer2TieBreakScore() + ")").toArray()).collect(Collectors.toList());
		Stream<Object[]> mergedSets = Stream.concat(noTieBreakSets.stream(), tieBreakSets.stream());
		mergedSets.forEach(score->{
			score.getClass();
		});




	}
}

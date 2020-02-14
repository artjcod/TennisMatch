package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
@ToString(exclude = {"scoreBoard"})
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
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Stream.of("Player Name").collect(Collectors.toList());
        IntStream.rangeClosed(1, matchSets.size()).forEach(i -> headers.add("Set " + i));
        rows.add(headers);
        List<String> player1Scores = matchSets.parallelStream().map(set -> set.isTieBreakActive() ? set.getPlayer1Score() +
                "(" + set.getPlayer1TieBreakScore() + ")" : String.valueOf(set.getPlayer1Score())).collect(Collectors.toList());
        List<String> player2Scores = matchSets.parallelStream().map(set ->
                set.isTieBreakActive() ? set.getPlayer2Score() + "(" + set.getPlayer2TieBreakScore() + ")" :
                        String.valueOf(set.getPlayer2Score())
        ).collect(Collectors.toList());
        player1Scores.add(0, player1Name);
        player2Scores.add(0, player2Name);
        rows.add(player1Scores);
        rows.add(player2Scores);
        int[] lineSizes = new int[rows.get(0).size()];
        rows.forEach(row -> IntStream.range(0, row.size()).forEach(i -> lineSizes[i] = Math.max(lineSizes[i], row.get(i).length())));
        String wordSpacing = Arrays.stream(lineSizes).mapToObj(lineSize -> "%-".concat(String.valueOf(lineSize + 2)).concat("s")
        ).collect(Collectors.joining());
        String lineFormat = rows.stream().map(row -> String.format(wordSpacing, row.toArray()) + "\n").collect(Collectors.joining());
         System.out.println(lineFormat);
    }
}

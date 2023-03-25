package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class TennisMatch  {
    private static final Logger logger = LogManager.getLogger(TennisMatch.class);

    private ScoreBoard scoreBoard;
    private String player1Name;
    private String player2Name;
    private boolean finished;

    public TennisMatch(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.scoreBoard = new ScoreBoard(player1Name, player2Name);
        this.scoreBoard.setTennisMatch(this);
    }

    public boolean isFinished() {
        return finished;
    }

    public String getLeader() {
      return scoreBoard.getLeader();
    }

    public String getFinalScore() {
        if (isFinished()) {
            StringBuilder builder = new StringBuilder();
            if (getLeader().equals(player1Name)) {
                builder.append(player1Name).append(" defeated ");
                builder.append(player2Name);
            } else {
                builder.append(player2Name).append(" defeated ");
                builder.append(player1Name);
            }
            builder.append(" ");
            String score = scoreBoard.getPreviousSets().stream().map(TennisSet::getCurrentSetScoreByLeader)
                    .collect(Collectors.joining(","));
            builder.append(score);
            return builder.toString();
        }
        return getLiveScore();
    }

    public String getLiveScore() {
        StringBuilder builder = new StringBuilder(player1Name);
        builder.append(":").append(scoreBoard.getPlayer1WonSets());
        builder.append(" Sets").append(",").append(player2Name).append(":");
        builder.append(scoreBoard.getPlayer2WonSets()).append(" Sets");
        return builder.toString();
    }

    public void player1Scores() {
        if (isFinished()) {
            throw new IllegalStateException("The match is finished!");
        } else {
            scoreBoard.player1WinsOnePoint();
        }
    }

    public void player2Scores() {
        if (isFinished()) {
            throw new IllegalStateException("The match is finished!");
        } else {
            scoreBoard.player2WinsOnePoint();
        }
    }


    public void generateMatchReport() {
        List<TennisSet> matchSets = scoreBoard.getPreviousSets();
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Stream.of("Player Name").collect(Collectors.toList());
        IntStream.rangeClosed(1, matchSets.size()).forEach(i -> headers.add("Set " + i));
        rows.add(headers);
        List<String> player1Scores = matchSets.parallelStream().map(set -> set.isTieBreakActive() ? set.getPlayer1WonGames() +
                "(" + set.getPlayer1TieBreakPoints() + ")" : String.valueOf(set.getPlayer1WonGames())).collect(Collectors.toList());
        List<String> player2Scores = matchSets.parallelStream().map(set ->
                set.isTieBreakActive() ? set.getPlayer2WonGames() + "(" + set.getPlayer2TieBreakPoints() + ")" :
                        String.valueOf(set.getPlayer2WonGames())
        ).collect(Collectors.toList());
        player1Scores.add(0, player1Name);
        player2Scores.add(0, player2Name);
        rows.add(player1Scores);
        rows.add(player2Scores);
        int[] lineSizes = new int[rows.get(0).size()];
        rows.forEach(row -> IntStream.range(0, row.size()).forEach(i -> lineSizes[i] = Math.max(lineSizes[i], row.get(i).length())));
        String wordSpacing = Arrays.stream(lineSizes).mapToObj(lineSize -> "%-".concat(String.valueOf(lineSize + 2)).concat("s")
        ).collect(Collectors.joining());
        String matchReport = rows.stream().map(row -> String.format(wordSpacing.concat("\n"), row.toArray()) ).collect(Collectors.joining());
        logger.info("\n{}" ,matchReport);
    }
}

package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * This class is observed by the listener OnSetFinished in order to notify the Score board for
 * set score update.
 * This class is subject to clone  x.clone()!=x is true whereas x.equals(x) is not.
 *
 * @author snaceur
 * @see java.util.Observable
 * @see java.lang.Cloneable
 * @see lombok.Lombok
 */

@EqualsAndHashCode(exclude = {"relatedGames", "currentGame", "relatedScoreBoard"}, callSuper = false)
@ToString(exclude = {"relatedGames", "currentGame", "relatedScoreBoard"})
@Data
public class TennisSet   {

    private List<Game> relatedGames = new LinkedList<>();

    SubmissionPublisher<TennisSet> publisher = new SubmissionPublisher<>(Runnable::run, Flow.defaultBufferSize());

    private Game currentGame;

    private ScoreBoard relatedScoreBoard;

    private int player1WonGames = 0;
    private int player2WonGames = 0;

    private boolean isTieBreakActive;

    private int player1TieBreakPoints = 0;
    private int player2TieBreakPoints = 0;

    public TennisSet(ScoreBoard relatedScoreBoard, String... players) {
        if (players.length < 2) {
            throw new IllegalArgumentException("2 players are required !");
        }
        this.currentGame = new Game(players[0], players[1], this);
        this.relatedGames.add(currentGame);
        this.relatedScoreBoard = relatedScoreBoard;
    }

    public List<Game> getRelatedGames() {
        return relatedGames;
    }

    public ScoreBoard getRelatedScoreBoard() {
        return relatedScoreBoard;
    }

    public void startNewGame() {
        Game newGame = new Game(currentGame.getPlayer1Name(),
                currentGame.getPlayer2Name(), this);
        relatedGames.add(newGame);
        currentGame = newGame;
    }

    public Game getGameById(Integer id) {
        return relatedGames.get(id);
    }

    public void player1WinsOnePoint() {
        if (isTieBreakActive) {
            player1TieBreakPoints++;
            if (isFinishedSet()) {
                closeSet();
            }
        } else {
            currentGame.player1Scores();
        }
    }

    public void player2WinsOnePoint() {
        if (isTieBreakActive) {
            player2TieBreakPoints++;
            if (isFinishedSet()) {
                closeSet();
            }
        } else {
            currentGame.player2Scores();
        }
    }

    public void player1WinsCurrentGame() {
        player1WonGames++;
        if (isTieBreak()) {
            isTieBreakActive = true;
        }
        if (isFinishedSet()) {
            closeSet();
        }
    }

    public String getTieBreakScore() {
        if (isTieBreakActive) {
            StringBuilder builder = new StringBuilder();
            String leader = relatedScoreBoard.getLeader();
            if (leader.equals(currentGame.getPlayer1Name()) || leader.equals("No Leader!")) {
                builder.append(player1WonGames).append("-").append(player2WonGames);
                builder.append("(");
                builder.append(player1TieBreakPoints).append("-").append(player2TieBreakPoints);
            } else {
                builder.append(player2WonGames).append("-").append(player1WonGames);
                builder.append("(");
                builder.append(player2TieBreakPoints).append("-").append(player1TieBreakPoints);
            }
            builder.append(")");
            return builder.toString();
        } else {
            return "Tie-Break is not active !";
        }
    }

    public String getSetWinner() {
        if ((isTieBreakActive && player2TieBreakPoints >= 7 && player2TieBreakPoints >= player1TieBreakPoints + 2)
                || (player2WonGames >= 6 && player2WonGames >= player1WonGames + 2)) {
            return this.currentGame.getPlayer2Name();
        } else if ((isTieBreakActive && player1TieBreakPoints >= 7 && player1TieBreakPoints >= player2TieBreakPoints + 2)
                || (player1WonGames >= 6 && player1WonGames >= player2WonGames + 2)) {
            return this.currentGame.getPlayer1Name();
        } else {
            return "No Winner!";
        }
    }

    public String getCurrentSetScoreByLeader() {
        if (isTieBreakActive) {
            return getTieBreakScore();
        } else {
            StringBuilder builder = new StringBuilder();
            String leader = relatedScoreBoard.getLeader();
            if (leader.equals(currentGame.getPlayer1Name()) || leader.equals("No Leader!")) {
                builder.append(player1WonGames).append("-").append(player2WonGames);
            } else {
                builder.append(player2WonGames).append("-").append(player1WonGames);
            }
            return builder.toString();
        }
    }

    public void player2WinsCurrentGame() {
        if (isTieBreakActive) {
            player2TieBreakPoints++;
        } else {
            player2WonGames++;
            if (isTieBreak()) {
                isTieBreakActive = true;
            }
        }
        if (isFinishedSet()) {
            closeSet();
        }
    }

    public boolean isFinishedSet() {
        if (isTieBreakActive) {
            return ((player1TieBreakPoints >= 7 && player1TieBreakPoints >= player2TieBreakPoints + 2)
                    || (player2TieBreakPoints >= 7 && player2TieBreakPoints >= player1TieBreakPoints + 2));
        } else {
            return ((player1WonGames >= 6 && player1WonGames >= player2WonGames + 2)
                    || (player2WonGames >= 6 && player2WonGames >= player1WonGames + 2));
        }
    }

    private boolean isTieBreak() {
        return (player1WonGames == 6 && player2WonGames == 6);
    }

    public boolean isTieBreakActive() {
        return isTieBreakActive;
    }

    public void closeSet() {
        Subscriber<TennisSet> listener=new OnSetFinished();
        publisher.subscribe(listener);
        publisher.submit(this);
    }
}

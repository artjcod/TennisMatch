package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

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
public class TennisSet extends Observable{

    private List<Game> relatedGames = new LinkedList<>();

    private Game currentGame;

    private ScoreBoard relatedScoreBoard;

    private int player1Score = 0;
    private int player2Score = 0;

    private boolean isTieBreakActive;

    private int player1TieBreakScore = 0;
    private int player2TieBreakScore = 0;

    public TennisSet(ScoreBoard relatedScoreBoard, String... players) {
        if (players.length < 2) {
            throw new IllegalArgumentException("2 players are required !");
        }
        this.currentGame = new Game(players[0], players[1], this);
        this.relatedGames.add(currentGame);
        this.relatedScoreBoard = relatedScoreBoard;
        this.addObserver(new OnSetFinished());
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

    public void player1Scores() {
        if (isTieBreakActive) {
            player1TieBreakScore++;
        } else {
            player1Score++;
            if (isTieBreak()) {
                isTieBreakActive = true;
            }
        }
        if (isFinishedSet()) {
            closeSet();
        }
    }

    public String getTieBreakScore() {
        if (isTieBreakActive) {
            StringBuilder builder = new StringBuilder();
            builder.append(player1Score).append("-").append(player1Score);
            builder.append("(");
            builder.append(player1TieBreakScore).append("-").append(player2TieBreakScore);
            builder.append(")");
            return builder.toString();
        } else {
            return "Tie-Break is not active !";
        }
    }

    public String getSetWinner() {
        if ((isTieBreakActive && player1TieBreakScore >= 7 && player1TieBreakScore >= player2TieBreakScore + 2)
                || (player1Score >= 6 && player1Score >= player2Score + 2)) {
            return this.currentGame.getPlayer1Name();
        } else if ((isTieBreakActive && player2TieBreakScore >= 7 && player2TieBreakScore >= player1TieBreakScore + 2)
                || (player2Score >= 6 && player2Score >= player1Score + 2)) {
            return this.currentGame.getPlayer2Name();
        } else {
            return "No Winner!";
        }
    }

    public String getSetScoreByLeader() {
        if (isTieBreakActive) {
            return getTieBreakScore();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(player1Score).append("-").append(player2Score);
            return builder.toString();
        }
    }

    public void player2Scores() {
        if (isTieBreakActive) {
            player2TieBreakScore++;
        } else {
            player2Score++;
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
            return ((player1TieBreakScore >= 7 && player1TieBreakScore >= player2TieBreakScore + 2)
                    || (player2TieBreakScore >= 7 && player2TieBreakScore >= player1TieBreakScore + 2));
        } else {
            return ((player1Score >= 6 && player1Score >= player2Score + 2)
                    || (player2Score >= 6 && player2Score >= player1Score + 2));
        }
    }

    private boolean isTieBreak() {
        return (player1Score == 6 && player2Score == 6);
    }

    public boolean isTieBreakActive() {
        return isTieBreakActive;
    }

    public void closeSet() {
        setChanged();
        notifyObservers();
    }
}

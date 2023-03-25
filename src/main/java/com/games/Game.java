package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * This class represents a tennis game (points) .
 * @author snaceur
 * @see java.util.Observable
 * @see lombok.Lombok
 *
 */


@EqualsAndHashCode(exclude = {"relatedSet"}, callSuper = false)
@Data
public class Game {

    private TennisSet relatedSet;
    private String player1Name;
    private String player2Name;

    private int player1Points = 0;
    private int player2Points = 0;

    SubmissionPublisher<Game> publisher = new SubmissionPublisher<>(Runnable::run, Flow.defaultBufferSize());

    public Game(String player1Name, String player2Name, TennisSet relatedSet) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.relatedSet = relatedSet;
    }

    public void player1Scores() {
        player1Points++;
        if (isFinishedGame()) {
            finishTheGame();
        }
    }

    public void player2Scores() {
        player2Points++;
        if (isFinishedGame()) {
            finishTheGame();
        }
    }

    public boolean isFinishedGame() {
        return (player1Points >= 4 && (player1Points >= player2Points + 2))
                || (player2Points >= 4 && (player2Points >= player1Points + 2));
    }

    public boolean isDeuce() {
        return player1Points >= 3 && (player1Points == player2Points);
    }

    public boolean hasAdvantage() {
        if (player1Points >= 4 || player2Points >= 4) {
            int diff = player1Points - player2Points;
            return Math.abs(diff) == 1;
        } else {
            return false;
        }
    }

    public String getGameWinner() {

        if (!isFinishedGame()) {
            return "No Winner";
        } else if (player1Points > player2Points) {
            return player1Name;
        } else {
            return player2Name;
        }
    }

    public String getGameScore() {
        if (isFinishedGame()) {
            return playerHaveHighestScore() + " is the winner !";
        } else if (hasAdvantage()) {
            return "ADV for " + playerHaveHighestScore();
        } else if (isDeuce()) {
            return "DEUCE";
        } else if (player1Points == player2Points) {
            return toAnnouncedScore(player1Points) + " ALL";
        } else {
            return toAnnouncedScore(player1Points) + "-" + toAnnouncedScore(player2Points);
        }
    }

    public String playerHaveHighestScore() {
        return (player1Points > player2Points) ? player1Name : player2Name;
    }

    public String toAnnouncedScore(int score) {
        String scoreInLetter;
        switch (score) {
            case 0:
                scoreInLetter = "LOVE";
                break;
            case 1:
                scoreInLetter = "FIFTEEN";
                break;
            case 2:
                scoreInLetter = "THIRTY";
                break;
            case 3:
                scoreInLetter = "FORTY";
                break;
            default:
                throw new NumberFormatException("Invalid score number!");

        }
        return scoreInLetter;
    }

    public void finishTheGame() {
        Subscriber<Game> listener=new OnGameFinished();
        publisher.subscribe(listener);
        publisher.submit(this);
        publisher.close();

    }
}

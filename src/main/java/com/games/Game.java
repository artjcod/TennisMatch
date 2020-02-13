package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;

import java.util.Observable;


@EqualsAndHashCode(exclude = {"relatedSet"}, callSuper = false)
@Data
public class Game extends Observable implements Cloneable {

    private TennisSet relatedSet;
    private String player1Name;
    private String player2Name;

    private int player1Score = 0;
    private int player2Score = 0;

    public Game(String player1Name, String player2Name, TennisSet relatedSet) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.relatedSet = relatedSet;
        this.addObserver(new OnGameFinished());
    }

    public void player1Scores() {
        player1Score++;
        if (isFinishedGame()) {
            finishTheGame();
        }
    }

    public void player2Scores() {
        player2Score++;
        if (isFinishedGame()) {
            finishTheGame();
        }
    }

    public boolean isFinishedGame() {
        return (player1Score >= 4 && (player1Score >= player2Score + 2))
                || (player2Score >= 4 && (player2Score >= player1Score + 2));
    }

    public boolean isDeuce() {
        return player1Score >= 3 && (player1Score == player2Score);
    }

    public boolean hasAdvantage() {
        if (player1Score >= 4 || player2Score >= 4) {
            int diff = player1Score - player2Score;
            return Math.abs(diff) == 1;
        } else {
            return false;
        }
    }

    public String getGameWinner() {

        if (!isFinishedGame()) {
            return "No Winner";
        } else if (player1Score > player2Score) {
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
        } else if (player1Score == player2Score) {
            return toAnnouncedScore(player1Score) + " ALL";
        } else {
            return toAnnouncedScore(player1Score) + "-" + toAnnouncedScore(player2Score);
        }
    }

    public String playerHaveHighestScore() {
        return (player1Score > player2Score) ? player1Name : player2Name;
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
        setChanged();
        notifyObservers();
    }

    @Override
    public Game clone() {
        try {
            Game clonedGame = (Game) super.clone();
            clonedGame.setPlayer1Score(0);
            clonedGame.setPlayer2Score(0);
            return clonedGame;
        } catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException("Invalid Clone Operation!");
        }
    }
}

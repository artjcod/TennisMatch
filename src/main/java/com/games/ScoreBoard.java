package com.games;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.Observable;


/**
 * This class represents an object of type Score Board.
 * @author snaceur
 * @see java.util.Observable
 * @see lombok.Lombok
 */
@EqualsAndHashCode(exclude = {"currentSet","previousSets"}, callSuper = false)
@Data
public class ScoreBoard extends Observable {

	private TennisSet currentSet; // games;
	private LinkedList<TennisSet> previousSets = new LinkedList<>(); // previous sets;
	private String player1Name, player2Name;

	// Set Scores
	private int player1SetScore = 0;
	private int player2SetScore = 0;

	public ScoreBoard(String player1Name, String player2Name) {
		initBoard(player1Name, player2Name);
	}

	public void initBoard(String player1Name, String player2Name) {
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		this.currentSet = new TennisSet(this, player1Name, player2Name);        
	}
	private void startNextSet() {
		this.currentSet = new TennisSet(this, player1Name, player2Name);
	}

	public void closeCurrentSet() {
		previousSets.add(currentSet);
		String setWinner = currentSet.getSetWinner();
		if (setWinner.equals(player1Name)) {
			player1SetScores();
		} else {
			player2SetScores();
		}
		startNextSet();
	}

	public void player1SetScores() {
		player1SetScore++;
		if(player1SetScore == 3) {
			setChanged();
			notifyObservers();
		}
	}
	public void player2SetScores() {
		player2SetScore++;
		if(player2SetScore == 3) {
			setChanged();
			notifyObservers();
		}
	}
	public boolean isFinishedMatch() {
		if (player1SetScore == 3) {
			return true;
		} else return player2SetScore == 3;
	}
}

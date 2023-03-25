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
 * This class represents an object of type Score Board.
 * @author snaceur
 * @see java.util.Observable
 * @see lombok.Lombok
 */
@EqualsAndHashCode(exclude = {"currentSet","previousSets"}, callSuper = false)
@ToString(exclude = {"currentSet","previousSets"})
@Data
public class ScoreBoard  {

	private TennisSet currentSet;
	private TennisMatch tennisMatch;
	private List<TennisSet> previousSets = new LinkedList<>();
	SubmissionPublisher<TennisMatch> publisher = new SubmissionPublisher<>(Runnable::run, Flow.defaultBufferSize());
	private String player1Name;
	private String player2Name;

	// Set Scores
	private int player1WonSets = 0;
	private int player2WonSets = 0;

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
		String currentSetWinner = currentSet.getSetWinner();
		if (currentSetWinner.equals(player1Name)) {
			player1WinsCurrentSet();
		} else {
			player2WinsCurrentSet();
		}
		if(!isFinishedMatch()) {
			startNextSet();
		}
	}

	public void player1WinsCurrentSet() {
		player1WonSets++;
		if(player1WonSets == 3) {
			Subscriber<TennisMatch> listener=new OnMatchFinished();
			publisher.subscribe(listener);
			publisher.submit(tennisMatch);
		};
	}
	public void player2WinsCurrentSet() {
		player2WonSets++;
		if(player2WonSets == 3) {
			Subscriber<TennisMatch> event=new OnMatchFinished();
			publisher.subscribe(event);
			publisher.submit(tennisMatch);
		}
	}

	public void player1WinsOnePoint(){
		currentSet.player1WinsOnePoint();
	}

	public void player2WinsOnePoint(){
		currentSet.player2WinsOnePoint();
	}

	public boolean isFinishedMatch() {
		if (player1WonSets == 3) {
			return true;
		} else return player2WonSets == 3;
	}

	public String getLeader(){
		if (this.getPlayer1WonSets() > this.getPlayer2WonSets()) {
			return player1Name;
		} else if (this.getPlayer2WonSets() > this.getPlayer1WonSets()) {
			return player2Name;
		} else {
			return "No Leader!";
		}
	}
}

package com.games;

import java.util.Observable;

/**
 * This class is the listener or the observer that will called once
 * the event (finish) has occurred.
 * @author snaceur
 * @see com.games.FinishedListener
 */

public class OnSetFinished implements FinishedListener {
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof TennisSet) {
			TennisSet tennisSet = (TennisSet) o;
			ScoreBoard relatedScoreBoard = tennisSet.getRelatedScoreBoard();
			relatedScoreBoard.closeCurrentSet();
		} else {
			throw new IllegalArgumentException("Invalid object type received!");
		}
	}
}

package com.games;

import java.util.Observable;

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

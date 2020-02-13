package com.games;

import java.util.Observable;

public class OnGameFinished implements FinishedListener {
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			TennisSet relatedSet = game.getRelatedSet();
			if (game.getGameWinner().equals(game.getPlayer1Name())) {
				relatedSet.player1Scores();
			} else {
				relatedSet.player2Scores();
			}
			System.out.println(game.getGameWinner()+" wins the game!");
			if (!relatedSet.isFinishedSet()) {
				relatedSet.startNewGame();
			}
		} else {
			throw new IllegalArgumentException("Invalid object type received!");
		}
	}
}

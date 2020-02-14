package com.games;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Observable;

/**
 * This class is the listener or the observer that will called once
 * the event (finish) has occurred.
 * @author snaceur
 * @see com.games.FinishedListener
 */

public class OnGameFinished implements FinishedListener {

	private static final Logger logger = LogManager.getLogger(OnGameFinished.class);

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			TennisSet relatedSet = game.getRelatedSet();
			if (game.getGameWinner().equals(game.getPlayer1Name())) {
				relatedSet.player1WinsCurrentGame();
			} else {
				relatedSet.player2WinsCurrentGame();
			}
			logger.info("{} wins the game!",game.getGameWinner());

			if (!relatedSet.isFinishedSet()) {
				relatedSet.startNewGame();
			}
		} else {
			throw new IllegalArgumentException("Invalid object type received!");
		}
	}
}

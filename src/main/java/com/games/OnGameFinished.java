package com.games;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * This class is the listener or the observer that will called once
 * the event (finish) has occurred.
 * @author snaceur
 * @see com.games.FinishedListener
 */
public class OnGameFinished  implements Subscriber<Game> {

	private static final Logger logger = LogManager.getLogger(OnGameFinished.class);
	
	private Subscription subscription;


	   public Subscription getSubscription() {
		   return subscription;
	   }


	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription=subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(Game game) {
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
		
	}

	@Override
	public void onError(Throwable throwable) {
		throw new RuntimeException(throwable);
	}

	@Override
	public void onComplete() {
		logger.info("Set closed");
	}
}

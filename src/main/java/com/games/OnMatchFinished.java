package com.games;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is the listener or the observer that will called once
 * the event (finish) has occurred.
 * @author snaceur
 * @param <T>
 * @see com.games.FinishedListener
 */

public class OnMatchFinished implements Subscriber<TennisMatch> {

	private Subscription subscription;
	private Logger logger=LogManager.getLogger(OnSetFinished.class);


public Subscription getSubscription() {
	return subscription;
}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(TennisMatch tennisMatch) {
        tennisMatch.setFinished(true); 
        tennisMatch.generateMatchReport();
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

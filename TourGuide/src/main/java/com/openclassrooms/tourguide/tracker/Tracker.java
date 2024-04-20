package com.openclassrooms.tourguide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;

public class Tracker extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);
	private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;

		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				LOGGER.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			LOGGER.debug("Begin Tracker. Tracking {} users.", users.size());
			stopWatch.start();
			users.forEach(tourGuideService::trackUserLocation);
			stopWatch.stop();
			LOGGER.debug("Tracker Time Elapsed: {} seconds.", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
			stopWatch.reset();
			try {
				LOGGER.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
			} catch (InterruptedException e) {
				break;
			}
		}

	}
}

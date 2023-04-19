package com.neu.psa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Benchmark {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private long time = 0;
	private boolean isStartCheck = false;
	private boolean isEndCheck = false;

	public Benchmark() {

	}

	long getResultTime() {
		if (isStartCheck && isEndCheck)
			return ChronoUnit.MILLIS.between(startTime, endTime);
		else
			System.out.print("Benchmark is not passing");
		return time;
	}

	void startTimer() {
		isStartCheck = true;
		startTime = LocalDateTime.now();
	}

	void endTimer() {
		isEndCheck = true;
		endTime = LocalDateTime.now();
	}

	long getCurrentTime() {
		return ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
	}

}
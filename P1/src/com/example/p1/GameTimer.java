package com.example.p1;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.SystemClock;

public class GameTimer {

	public static final int TIMER_PERIOD = 50;
	private static Timer timer;
	private static List<ITimingListener> listeners = new ArrayList<GameTimer.ITimingListener>();
	private static boolean isRunning = false;

	public interface ITimingListener {
		public void onMainLoop(long milliSecond);

	}

	public interface IAdvancedTimingListener extends ITimingListener {
		public void onStop();

		public void onStart();
	}

	public static void startTimer() {
		stopTimer();
		timer = new Timer();
		timer.scheduleAtFixedRate(new GameTimerTask(), 1, TIMER_PERIOD);
		setRunning(true);

		for (ITimingListener l : listeners) {
			if (l instanceof IAdvancedTimingListener) {
				((IAdvancedTimingListener) l).onStart();
			}
		}
	}

	public static void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		setRunning(false);
		for (ITimingListener l : listeners) {
			if (l instanceof IAdvancedTimingListener) {
				((IAdvancedTimingListener) l).onStop();
			}
		}
	}

	public static void pause() {
		stopTimer();
	}

	public static void resume() {
		startTimer();
	}

	public static boolean isRunning() {
		return isRunning;
	}

	public static int addTimingListener(ITimingListener listener) {
		if (listeners == null)
			listeners = new ArrayList<GameTimer.ITimingListener>();
		listeners.add(listener);
		return listeners.size() - 1;
	}

	public static boolean removeTimingListener(ITimingListener listener) {
		if (listeners == null)
			return false;
		return listeners.remove(listener);
	}

	public static boolean removeTimingListener(int listenerIndex) {
		if (listeners == null)
			return false;
		try {
			listeners.remove(listenerIndex);
			return true;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void onMainLoop() {
		setRunning(true);
		long uptimeMillis = SystemClock.uptimeMillis();
		// Log.i("timer", uptimeMillis + "");
		try {
			for (ITimingListener l : listeners) {
				l.onMainLoop(uptimeMillis);
			}
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
	}

	private static void setRunning(boolean isRunning) {
		GameTimer.isRunning = isRunning;
	}

	private static class GameTimerTask extends TimerTask {
		public void run() {
			onMainLoop();
		}
	}

	public static void togglePause() {
		if (isRunning())
			pause();
		else
			resume();
	}
}

package timerx.common;

import timerx.stopwatch.StopwatchBuilder;
import timerx.timer.TimerBuilder;

/**
 * Interface to run some action at the certain time
 *
 * @see TimerBuilder
 * @see StopwatchBuilder
 */
public interface Action {

  void run();
}

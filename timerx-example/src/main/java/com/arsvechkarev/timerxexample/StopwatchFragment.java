package com.arsvechkarev.timerxexample;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.arsvechkarev.timerx.Action;
import com.arsvechkarev.timerx.Stopwatch;
import com.arsvechkarev.timerx.StopwatchBuilder;
import com.arsvechkarev.timerx.StopwatchTickListener;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class StopwatchFragment extends Fragment {

  private TextView textTime;
  private Stopwatch stopwatch;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_stopwatch, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    textTime = view.findViewById(com.arsvechkarev.timerxexample.R.id.text_time);

    stopwatch = new StopwatchBuilder()
        .startFormat("Time: LL")
        .tickListener(new StopwatchTickListener() {
          @Override
          public void onTimeTick(String time) {
            textTime.setText(time);
          }
        })
//        .changeFormatWhen(10, TimeUnit.SECONDS, "Time: MM:SS")
//        .changeFormatWhen(1, TimeUnit.MINUTES, "LLLLLL")
        .actionWhen(5, TimeUnit.SECONDS, new Action() {
          @Override
          public void execute() {
            Toast.makeText(getContext(), "5 seconds action: now!", Toast.LENGTH_SHORT)
                .show();
          }
        })
        .actionWhen(30, TimeUnit.SECONDS, new Action() {
          @Override
          public void execute() {
            Toast.makeText(getContext(), "30 seconds action: now!", Toast.LENGTH_SHORT)
                .show();
          }
        }).actionWhen(70, TimeUnit.SECONDS, new Action() {
          @Override
          public void execute() {
            Toast.makeText(getContext(), "1 minute 10 seconds seconds action: now!",
                Toast.LENGTH_SHORT).show();
          }
        })
        .build();

    textTime.setText(stopwatch.getFormattedStartTime());

    view.findViewById(
        com.arsvechkarev.timerxexample.R.id.btn_start)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            stopwatch.start();
          }
        });

    view.findViewById(com.arsvechkarev.timerxexample.R.id.btn_stop)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            stopwatch.stop();
            Toast.makeText(getActivity(),
                "time = " + stopwatch.getTimeIn(TimeUnit.MILLISECONDS),
                Toast.LENGTH_SHORT).show();
          }
        });

    view.findViewById(com.arsvechkarev.timerxexample.R.id.btn_reset)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            stopwatch.reset();
            textTime.setText(stopwatch.getFormattedStartTime());
          }
        });
  }
}

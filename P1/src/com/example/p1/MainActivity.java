package com.example.p1;

import android.app.Activity;
import android.os.Bundle;

import com.example.p1.R.id;

public class MainActivity extends Activity {

	private BV bv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bv = (BV) findViewById(id.the_view);
		GameTimer.addTimingListener(bv);

	}

	@Override
	protected void onResume() {
		super.onResume();
		GameTimer.startTimer();

	}

	@Override
	protected void onPause() {
		GameTimer.stopTimer();
		super.onPause();
	}
}

package hu.daroczi.demo.vcheckers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TesztActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teszt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teszt, menu);
		return true;
	}

}

package hu.daroczi.demo.vcheckers;

import hu.daroczi.demo.vcheckers.components.Checker;
import hu.daroczi.demo.vcheckers.components.CheckerTable;
import hu.daroczi.demo.vcheckers.components.CheckerToggleListener;
import hu.daroczi.demo.vcheckers.components.DragView;
import hu.daroczi.demo.vcheckers.game.Engine;
import hu.daroczi.demo.vcheckers.io.LevelLoader;
import hu.daroczi.demo.vcheckers.model.Cell;
import hu.daroczi.demo.vcheckers.util.MyLog;
import hu.daroczi.demo.vcheckers.util.SystemUiHider;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MenuActivity extends Activity implements CheckerToggleListener {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private Checker selected = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.logMessage("onCreate");
		setContentView(R.layout.activity_menu);

		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();

        dragView = (DragView) findViewById(R.id.drag_view);

		tableLayout = (CheckerTable) contentView; //findViewById(R.id.fullscreen_content);

		tableLayout.setStretchAllColumns(true);
		tableLayout.requestFocus();

		LevelLoader levelLoader = new LevelLoader(1);
		table = levelLoader.loadLevel(this);

		// TableRow tableRow = (TableRow)
		// tableLayout.findViewById(R.id.tableRow1);
		List<Cell> cells = new ArrayList<Cell>();

		MyLog.logMessage("Density: "
				+ String.valueOf(getResources().getDisplayMetrics().density));
		MyLog.logMessage("DensityDpi: "
				+ String.valueOf(getResources().getDisplayMetrics().densityDpi));
		MyLog.logMessage("xDpi: "
				+ String.valueOf(getResources().getDisplayMetrics().xdpi));
		MyLog.logMessage("yDpi: "
				+ String.valueOf(getResources().getDisplayMetrics().ydpi));
		MyLog.logMessage("ScreenLayout size mask: "
				+ String.valueOf(getResources().getConfiguration().screenLayout
						& Configuration.SCREENLAYOUT_SIZE_MASK));
		MyLog.logMessage("ScreenWidthDp: "
				+ String.valueOf(getResources().getConfiguration().screenWidthDp));
		MyLog.logMessage("SmallestScreenWidthDp: "
				+ String.valueOf(getResources().getConfiguration().smallestScreenWidthDp));
		MyLog.logMessage("UIMode: "
				+ String.valueOf(getResources().getConfiguration().uiMode));

		TableRow tableRow = null;
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams imgLayout = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		int row = 0;
		int dimRow = 0;
		int dimCol = 0;
		for (LevelLoader.Cell cell : table) {
			if (row != cell.row) {
				if (row != 0) {
					//layoutParams.weight = (float)tableRow.getChildCount();
					tableRow.setWeightSum(tableRow.getChildCount());
					tableLayout.addView(tableRow, layoutParams);
				}
				row++;
				tableRow = new TableRow(MenuActivity.this);
				tableRow.setGravity(Gravity.CENTER);
			}
			if (cell.value != -1) {
				// Checker checker = new Checker(MenuActivity.this);
				// LayoutInflater layoutInflater = (LayoutInflater)
				// getBaseContext()
				// .getSystemService(LAYOUT_INFLATER_SERVICE);
				// View popupView =
				// layoutInflater.inflate(R.layout.popup_layout, null);
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				Checker checker = (Checker) layoutInflater.inflate(
						R.layout.cell, null);
                checker.setAdjustViewBounds(true);
				checker.setBackgroundResource(R.drawable.checker);
                checker.setImageResource(R.drawable.checker);
                checker.setScaleType(ImageView.ScaleType.FIT_CENTER);
				Cell model = new Cell((cell.value == 1), false, cell.row,
						cell.col);
				checker.setTag(model);
				checker.addOnCheckerToggleListener((CheckerToggleListener) this);
                checker.setLayoutParams(imgLayout);
				// checker.setLayoutParams(new LayoutParams(
				// LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				if (cell.value == 1) {
					checker.setFilled(true);
				} else {
					checker.setFilled(false);
				}

                //checker.setLayoutParams(imgLayout);
				tableRow.addView(checker); //, imgLayout);
				dragView.addChecker(checker);
				cells.add(model);
			} else {
                // Set placeholder empty cells (Space):
                Space sp = new Space(MenuActivity.this);
                tableRow.addView(sp, imgLayout);
            }
			if (cell.row > dimRow) {
				dimRow = cell.row;
			}
			if (cell.col > dimCol) {
				dimCol = cell.col;
			}
		}
		tableLayout.addView(tableRow, layoutParams);
		engine = new Engine(dimRow, dimCol);
		engine.addCells(cells);

		// Checker checker = new Checker(MenuActivity.this);
		// Checker checker2 = new Checker(MenuActivity.this);
		// Checker checker3 = new Checker(MenuActivity.this);
		//
		// tableRow.addView(checker);
		// tableRow.addView(checker2);
		// tableRow.addView(checker3);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	private CheckerTable tableLayout;

	private List<LevelLoader.Cell> table;

	private Engine engine;

	private DragView dragView;

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public Checker getSelected() {
		return selected;
	}

	public void onCheckerToggle(Checker checker) {
		if (null == this.selected) {
			// nincs előző
			if (checker.isChecked()) {
				this.selected = checker;
			} else {
				checker.setFilled(false);
			}
		} else {
			// van előző
			if (!checker.equals(this.selected)) {
				// ha az előző és az új nem ugyanaz
				if (checker.isChecked()) {
					// az új ki van jelölve
					this.selected.setChecked(false);
					this.selected = checker;
				} else {
					// az új üres volt
					// MOZGATÁS ha valid lépés
					if (engine.isValidMove((Cell) this.selected.getTag(),
							(Cell) checker.getTag())) {
						Cell middle = engine.move(
								(Cell) this.selected.getTag(),
								(Cell) checker.getTag());
						Checker middleButton = findCheckerByCell(middle);
						//middleButton.setFilled(false);
                        middleButton.setHiding();
						this.selected.setFilled(false);
						this.selected = null;
					} else {
						checker.setFilled(false);
					}
				}
			}
		}
	}

	public Checker findCheckerByCell(Cell cell) {
        String spClassName = String.valueOf("Space");
		for (int i = 0; i < tableLayout.getChildCount(); i++) {
			TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
			for (int j = 0; j < tableRow.getChildCount(); j++) {
                View child = (View) tableRow.getChildAt(j);
                if (!child.getClass().getSimpleName().equals(spClassName)) {
                    Checker checker = (Checker) child;
                    Cell test = (Cell) checker.getTag();
                    if (cell.getRow() == test.getRow()
                            && cell.getCol() == test.getCol()) {
                        return checker;
                    }
                }
			}
		}
		return null;
	}
}

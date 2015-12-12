package hu.daroczi.demo.vcheckers.components;

import hu.daroczi.demo.vcheckers.R;
import hu.daroczi.demo.vcheckers.model.Cell;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class Checker extends ImageButton implements OnTouchListener, Animation.AnimationListener {

	private static final int[] STATE_FILLED = { R.attr.state_filled };
	private static final int[] STATE_CHECKED = { R.attr.state_checked };
	private static final int[] STATE_DRAGGED = { R.attr.state_dragged };
	private static final int[] STATE_HIDING = { R.attr.state_hiding };

	private boolean filled = false;
	private boolean checked = false; // selected
	private boolean dragged = false;
    private boolean hiding = false;

	private int col;
	private int row;

	private Animation selectAnim;
	private Animation deselectAnim;
    private Animation hidingAnim;
	private int deselectColor = Color.argb(60, 0, 10, 10);

	private ArrayList<CheckerToggleListener> checkerToggleListeners = new ArrayList<CheckerToggleListener>();

	public Checker(Context context) {
		super(context);

		init();
	}

	public Checker(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public void addOnCheckerToggleListener(
			CheckerToggleListener checkerToggleListener) {
		checkerToggleListeners.add(checkerToggleListener);
	}

	private void init() {
		// setImageResource(R.drawable.earth);

		selectAnim = AnimationUtils.loadAnimation(getContext(),
				R.anim.checker_select_anim);
		deselectAnim = AnimationUtils.loadAnimation(getContext(),
				R.anim.checker_deselect_anim);
        hidingAnim = AnimationUtils.loadAnimation(getContext(),
                R.anim.checker_hiding_anim);
        hidingAnim.setAnimationListener(this);
		setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggle();
            }

        });

		// setOnTouchListener(this);

		// setBackgroundColor(Color.BLACK);
		// setColorFilter(deselectColor);

	}

	public void toggle() {
		if (isFilled()) {
			toggleChecked();
		} else {
			toggleFilled();
		}
		for (CheckerToggleListener checkerToggleListener : checkerToggleListeners) {
			checkerToggleListener.onCheckerToggle(this);
		}
	}

	public boolean toggleChecked() {
		boolean c = !checked;
		setChecked(c);
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		Cell cell = (Cell) getTag();
		if (this.checked) {
			// startAnimation(selectAnim);
			// setBackgroundColor(Color.CYAN);
			cell.setSelected(true);
			// setImageResource(R.drawable.fatmole_selected);
			// setColorFilter(null);
		} else {
			cell.setSelected(false);
			// setImageResource(R.drawable.fatmole);
			// setColorFilter(deselectColor);
			// setBackgroundColor(Color.BLACK);
			// startAnimation(deselectAnim);
		}
		// invalidate();
		refreshDrawableState();
	}

	public boolean isChecked() {
		return checked;
	}

	public boolean isFilled() {
		return filled;
	}

	public boolean isEmpty() {
		return !isFilled();
	}

	public boolean toggleFilled() {
		boolean f = !filled;
		setFilled(f);
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
		Cell cell = (Cell) getTag();
		if (this.filled) {
			cell.setFilled(true);
			// setImageResource(R.drawable.fatmole);
		} else {
			cell.setFilled(false);
			cell.setSelected(false);
			setChecked(false);
			// setImageResource(R.drawable.earth);
		}
		// invalidate();
		refreshDrawableState();
	}

    public void setHiding() {
        this.hiding = true;
        startAnimation(hidingAnim);
    }

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
		if (isFilled()) {
			mergeDrawableStates(drawableState, STATE_FILLED);
		}
		if (isChecked()) {
			mergeDrawableStates(drawableState, STATE_CHECKED);
		}
		if (isDragged()) {
			mergeDrawableStates(drawableState, STATE_DRAGGED);
		}
		return drawableState;
	}

	public boolean isDragged() {
		return dragged;
	}

	public void setDragged(boolean dragged) {
		this.dragged = dragged;
		refreshDrawableState();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			dragged = true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			dragged = false;
			toggle();
		}
		invalidate();
		return true;
	}

    @Override
    public void onAnimationEnd(Animation animation) {
        setFilled(false);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

}

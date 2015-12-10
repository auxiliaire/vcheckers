package hu.daroczi.demo.vcheckers.components;

import hu.daroczi.demo.vcheckers.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DragView extends ImageView implements OnTouchListener {

	private boolean isDragging = false;
	private float dragX;
	private float dragY;
	private Bitmap bitmap;
	private Paint paint = new Paint();
	private ArrayList<Checker> checkers;
	private Checker dragged;

	public DragView(Context context) {
		super(context);
		init();
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.fatmole);
		checkers = new ArrayList<Checker>();
		setWillNotDraw(false);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(this);
	}

	public void addChecker(Checker checker) {
		checkers.add(checker);
	}

	private boolean isPointInsideView(float x, float y, View view) {
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		int viewX = location[0];
		int viewY = location[1];

		// point is inside view bounds
		if ((x > viewX && x < (viewX + view.getWidth()))
				&& (y > viewY && y < (viewY + view.getHeight()))) {
			return true;
		} else {
			return false;
		}
	}

	private Checker getCheckerAt(float x, float y) {
		for (Checker checker : checkers) {
			if (isPointInsideView(x, y, checker)) {
				return checker;
			}
		}
		return null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Checker checker = getCheckerAt(event.getX(),
					event.getY() + bitmap.getHeight());
			if (null != checker && checker.isFilled()) {
				isDragging = true;
				checker.toggle();
				checker.setDragged(true);
				dragged = checker;
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// sketch.add(new dot(me.getX(), me.getY()));
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isDragging = false;
			Checker checker = getCheckerAt(event.getX(),
					event.getY() + bitmap.getHeight());
			if (null != checker) {
				checker.toggle();
			}
			if (null != dragged) {
				dragged.setDragged(false);
				dragged = null;
			}
		}
		dragX = event.getX();
		dragY = event.getY();
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isDragging) {
			float x = dragX - bitmap.getWidth() / 2;
			float y = dragY - bitmap.getHeight() / 2;
			canvas.drawBitmap(bitmap, x, y, paint);
		}
	}
}

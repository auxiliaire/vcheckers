package hu.daroczi.demo.vcheckers.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class CheckerTable extends TableLayout {

	private boolean isDragging = false;
	private float dragX;
	private float dragY;
	private Drawable mole;
	private Bitmap bitmap;
	private Paint paint = new Paint();

	public CheckerTable(Context context) {
		super(context);
	}

	public CheckerTable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

}

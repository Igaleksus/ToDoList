package ru.igaleksus.todolist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mac on 02.11.14.
 */
public class ToDoListItemView extends TextView {
    private Paint linePaint;
    private Paint paintMargin;
    private int paperColor;
    private float margin;




    public ToDoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        Resources myResources = getResources();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.notepad_lines));
        linePaint.setAlpha(100);
        paintMargin = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMargin.setColor(myResources.getColor(R.color.notepad_margin));

        paperColor = myResources.getColor(R.color.notepad_paper);
        margin = myResources.getDimension(R.dimen.notepad_margin);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(paperColor);
        canvas.save();
        canvas.translate(0, getMeasuredHeight());
        canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);
        canvas.restore();

        int some = (int) (margin*1.5f);

        canvas.drawLine(some, 0, some, getMeasuredHeight(), paintMargin);

        canvas.save();
        canvas.translate(some, 0);

        super.onDraw(canvas);
        canvas.restore();
    }


}

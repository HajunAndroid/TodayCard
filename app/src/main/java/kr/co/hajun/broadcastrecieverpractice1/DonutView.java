package kr.co.hajun.broadcastrecieverpractice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DonutView extends View {
    Context context;
    int value;
    int valuePercent;
    int size;
    int strokeSize;
    int textSize;
    int width;
    int height;

    public DonutView(Context context){
        super(context);
        this.context=context;
        init();
    }
    public DonutView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        init();
    }
    public DonutView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }
    private void init(){
        size=getResources().getDimensionPixelSize(R.dimen.donut_size);
        strokeSize=getResources().getDimensionPixelSize(R.dimen.donut_stroke_size);
        textSize=getResources().getDimensionPixelSize(R.dimen.donut_textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setValue(int value, int total){
        this.value = value;
        this.valuePercent = value *100 / total;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.alpha(Color.CYAN));
        RectF rectF = new RectF(20,20,size-20,size-20);

        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeSize);

        canvas.drawArc(rectF,0,360,false,paint);

        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawArc(rectF,-90,valuePercent * 360 / 100,false,paint);

        paint.setTextSize(textSize);
        paint.setStrokeWidth(5);

        String txt = String.valueOf(valuePercent)+"%";
        int xPos = width/2 - (int)(paint.measureText(txt)/2);
        int yPos = (int)(height/2 - ((paint.descent()+paint.ascent())/2));

        canvas.drawText(txt,xPos,yPos,paint);
    }
}

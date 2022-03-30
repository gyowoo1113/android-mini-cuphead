package kr.ac.tukorea.ge.sgp02.a2019182019.morecontrols;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {
    private Paint paint = new Paint();
    private Bitmap soccerBitmap;
    private Rect soccerSrcRect = new Rect();
    private Rect soccerDstRect = new Rect();
    private Paint leftCirclePaint = new Paint();
    private Paint rightCirclePaint = new Paint();
    private Paint textPaint = new Paint();
    private Rect textExtentRect = new Rect();

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint.setColor(0xFFCCCCCC);
        leftCirclePaint.setColor(Color.BLUE);
        rightCirclePaint.setColor(Color.RED);
        rightCirclePaint.setStyle(Paint.Style.STROKE);
        rightCirclePaint.setStrokeWidth(10);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.WHITE);
        Resources res = getResources();
        soccerBitmap = BitmapFactory.decodeResource(res,R.mipmap.soccer_ball_240);
        soccerSrcRect.set(0,0,soccerBitmap.getWidth(),soccerBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Paint paint = new Paint();
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = width - paddingLeft - paddingRight;
        int contentHeight = height + paddingTop - paddingBottom;

        int size = contentWidth < contentHeight ? contentWidth : contentHeight;


        int centerX = paddingLeft + contentWidth / 2;
        int centerY = paddingTop + contentHeight / 2;

        drawBackGround(canvas, paddingLeft, paddingTop, contentWidth, contentHeight);
        drawSoccerBall(canvas, size, centerX, centerY);
        drawLeftCircle(canvas, paddingLeft, paddingTop, contentWidth, contentHeight, size);
        drawRightCircle(canvas, paddingTop, contentWidth, contentHeight, centerX, size);
        drawCenterText(canvas, contentHeight, centerX, centerY);
    }

    private void drawBackGround(Canvas canvas, int paddingLeft, int paddingTop, int contentWidth, int contentHeight) {
        canvas.drawRoundRect(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight,30,30,paint);
    }

    private void drawSoccerBall(Canvas canvas, int size, int centerX, int centerY) {
        int ballRadius = size /10;
        soccerDstRect.set(centerX - ballRadius, centerY - ballRadius,
                centerX + ballRadius, centerY + ballRadius);
        canvas.drawBitmap(soccerBitmap,soccerSrcRect, soccerDstRect, null);
    }

    private void drawLeftCircle(Canvas canvas, int paddingLeft, int paddingTop, int contentWidth, int contentHeight, int size) {
        int leftCenterX = paddingLeft + contentWidth / 4;
        int leftCenterY = paddingTop + contentHeight / 4;
        int circleRadius = size / 16;
        canvas.drawCircle(leftCenterX,leftCenterY,circleRadius,leftCirclePaint);
    }

    private void drawRightCircle(Canvas canvas, int paddingTop, int contentWidth, int contentHeight, int centerX, int size) {
        int rightCenterX = centerX + contentWidth / 4;
        int rightCenterY = paddingTop + contentHeight / 4;
        int circleRadius = size / 16;
        canvas.drawCircle(rightCenterX,rightCenterY, circleRadius,rightCirclePaint);
    }

    private void drawCenterText(Canvas canvas, int contentHeight, int centerX, int centerY) {
        String text ="GAME START";
        textPaint.getTextBounds(text,0,text.length(),textExtentRect);
        int textX = centerX - textExtentRect.width() / 2;
        int textY = centerY;
        canvas.drawText(text,textX,textY,textPaint);
    }
}
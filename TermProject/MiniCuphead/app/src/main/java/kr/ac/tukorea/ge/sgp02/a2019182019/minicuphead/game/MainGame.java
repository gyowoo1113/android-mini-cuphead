package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.R;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.CollisionHelper;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.GameView;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.RangeBox;

public class MainGame {
    private boolean isTouchPlayer = false;
    private RangeBox moveBoundingBox;

    public static MainGame getInstance() {
        if (singleton == null) {
            singleton = new MainGame();
        }
        return singleton;
    }

    public float frameTime;

    private MainGame() {

    }

    private static MainGame singleton;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private Cuphead cuphead;
    private Paint collisionPaint;

    public void init() {
        objects.clear();

        float cupheadY = Metrics.height - Metrics.size(R.dimen.cuphead_y_offset);
        cuphead = new Cuphead(Metrics.width / 2, cupheadY);
        objects.add(cuphead);

        moveBoundingBox = new RangeBox(Metrics.width / 2,cupheadY);
        objects.add(moveBoundingBox);

        collisionPaint = new Paint();
        collisionPaint.setColor(Color.RED);
        collisionPaint.setStyle(Paint.Style.STROKE);
        collisionPaint.setStrokeWidth(10);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (CollisionHelper.isPointInBox(cuphead,x,y)
                && !isTouchPlayer) {
                    isTouchPlayer = true;
                    moveBoundingBox.setPosition(x,y);
                    cuphead.fire();
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (!isTouchPlayer){
                    return false;
                }
                cuphead.setPosition(x, y,moveBoundingBox);
                if (action == MotionEvent.ACTION_DOWN) {
                    cuphead.fire();
                }
                return true;

            case MotionEvent.ACTION_UP:
                isTouchPlayer = false;
                moveBoundingBox.setPosition(x,y);
        }
        return false;
    }

    public void update(int elapsedNanos) {
        frameTime = elapsedNanos * 1e-9f;
        for (GameObject gobj : objects){
            gobj.update();
        }
    }

    public void draw(Canvas canvas) {
        for (GameObject gobj : objects) {
            gobj.draw(canvas);
            if (gobj instanceof BoxCollidable) {
                RectF rect = ((BoxCollidable) gobj).getBoundingRect();

                canvas.drawRect(rect, collisionPaint);
            }
        }
    }

    public void add(GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                objects.add(gameObject);
            }
        });
    }

    public void remove(GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                objects.remove(gameObject);
            }
        });
    }

}

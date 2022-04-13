package kr.ac.tukorea.ge.sgp02.a2019182019.dragonflight.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kr.ac.tukorea.ge.sgp02.a2019182019.dragonflight.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.dragonflight.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.dragonflight.R;

public class Bullet implements GameObject {
    protected float x, y;
    protected final float length;
    protected final float dy;
    protected static Paint paint;
    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        this.length = Metrics.size(R.dimen.laser_length);
        this.dy = -Metrics.size(R.dimen.laser_speed);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(Metrics.size(R.dimen.laser_width));
        }
    }
    @Override
    public void update() {
        float frameTime = MainGame.getInstance().frameTime;
        y += dy * frameTime;

        if ( y < 0 ){
            MainGame.getInstance().remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(x, y, x, y -length, paint);
    }
}
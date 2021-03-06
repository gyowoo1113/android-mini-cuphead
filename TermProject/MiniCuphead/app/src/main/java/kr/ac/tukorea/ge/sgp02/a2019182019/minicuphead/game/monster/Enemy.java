package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.monster;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.R;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.Gauge;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.sprites.AnimSprite;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.BaseGame;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.BitmapPool;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.interfaces.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.interfaces.Recyclable;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.RecycleBin;


public class Enemy extends AnimSprite implements BoxCollidable, Recyclable {
    public static final float FRAMES_PER_SECOND = 10.0f;
    public static final int TOUCH_MONSTER = 5;
    public static final int NORMAL_MONSTER = 100;
    private static final String TAG = Enemy.class.getSimpleName();
    public static float size;
    public int level;
    protected float dx;
    protected int life, maxlife;
    protected int maxAlpha = 255;
    protected RectF boundingBox = new RectF();
    protected static int[] bitmapIds = {
            R.mipmap.monster_normal, R.mipmap.monster_touch
    };
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = bitmapIds.length;
    private Gauge gauge;

    public static Enemy get(int level, float y, float speed) {
        Enemy enemy = (Enemy) RecycleBin.get(Enemy.class);
        if (enemy != null) {
            enemy.set(level, y, speed);
            return enemy;
        }
        return new Enemy(level, y, speed);
    }

    private void set(int level, float y, float speed) {
        bitmap = BitmapPool.get(bitmapIds[level - 1]);
        this.y = y;
        this.x = -size;
        this.dx = speed;
        this.level = level;
        this.life = (level == 1) ? NORMAL_MONSTER : TOUCH_MONSTER;
        this.maxlife = (level == 1) ? NORMAL_MONSTER : TOUCH_MONSTER;
        setAlpha(this.maxAlpha);
    }

    private Enemy(int level, float y, float speed) {
        super(Metrics.width+size,y, size, size, bitmapIds[level - 1], FRAMES_PER_SECOND, 16,true);
        this.level = level;
        dx = speed;
        this.life = (level == 1) ? NORMAL_MONSTER : TOUCH_MONSTER;
        this.maxlife = (level == 1) ? NORMAL_MONSTER : TOUCH_MONSTER;

        gauge = new Gauge(
                Metrics.size(R.dimen.fly_gauge_thickness_fg),
                R.color.fly_gauge_fg,
                Metrics.size(R.dimen.fly_gauge_thickness_bg),
                R.color.fly_gauge_bg,
                size * 0.9f
        );
        gauge.setValue(life / maxlife);
    }

    @Override
    public void update(float frameTime) {
        x -= dx * frameTime;
        setDstRectWithRadius();
        boundingBox.set(dstRect);
        boundingBox.inset(size/16, size/16);
        if (dstRect.top > Metrics.height) {
            BaseGame.getInstance().remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        gauge.setValue((float)life / maxlife);
        gauge.draw(canvas, x, y + size*0.5f);
    }

    @Override
    public RectF getBoundingRect() {
        return boundingBox;
    }

    @Override
    public void finish() {

    }

    public boolean decreaseLife(int power) {
        life -= power;
        gauge.setValue((float)life / maxlife);
        if (life <= 0) return true;
        return false;
    }
}

package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.BaseGame;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.sprites.Sprite;

public class HorzScrollBackground extends Sprite {
    private final float speed;
    private final int width;
    public HorzScrollBackground(int bitmapResId, float speed) {
        super(Metrics.width / 2, Metrics.height / 2, Metrics.width, Metrics.height, bitmapResId);
        this.width = bitmap.getHeight() * Metrics.height / bitmap.getHeight();
        setDstRect(width, Metrics.height);
        this.speed = speed;
    }

    @Override
    public void update(float frameTime) {
        this.x -= speed * BaseGame.getInstance().frameTime;
    }

    @Override
    public void draw(Canvas canvas) {
        int curr = (int)x % width;
        if (curr > 0) curr -= width;
        while (curr < Metrics.width) {
            dstRect.set(curr,0, curr + width, Metrics.height);
            canvas.drawBitmap(bitmap, null, dstRect, null);
            curr += width;
        }
    }
}

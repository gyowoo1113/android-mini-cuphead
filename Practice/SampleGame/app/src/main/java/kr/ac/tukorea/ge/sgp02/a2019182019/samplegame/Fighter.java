package kr.ac.tukorea.ge.sgp02.a2019182019.samplegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Fighter {
    private static Bitmap bitmap;
    private static Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    public Fighter(){
        dstRect.set(0,0,200,200);

        if (bitmap == null) {
            Resources res = GameView.view.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.mipmap.plane_240);
            srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}

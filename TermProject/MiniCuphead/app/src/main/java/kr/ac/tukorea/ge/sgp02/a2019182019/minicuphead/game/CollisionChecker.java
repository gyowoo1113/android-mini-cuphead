package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.CollisionHelper;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.Bullet;

public class CollisionChecker implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();

    @Override
    public void update() {
        MainGame game = MainGame.getInstance();
        ArrayList<GameObject> bullets = game.objectsAt(MainGame.Layer.bullet);
        ArrayList<GameObject> enemies = game.objectsAt(MainGame.Layer.enemy);
        ArrayList<GameObject> player = game.objectsAt(MainGame.Layer.player);

        for (GameObject o1 : enemies) {
            if (!(o1 instanceof Enemy)) {
                continue;
            }
            Enemy enemy = (Enemy) o1;
            if (enemy.level == 2) continue;
            boolean collided = false;
            for (GameObject o2 : bullets) {
                if (!(o2 instanceof Bullet)) {
                    continue;
                }
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(enemy, bullet)) {
                    //Log.d(TAG, "*Collision*");
                    game.remove(bullet);
                    boolean dead = enemy.decreaseLife(bullet.getPower());
                    if (dead) {
                        game.remove(enemy);
                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }

        for (GameObject o1 : enemies) {
            if (!(o1 instanceof Enemy)) {
                continue;
            }
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for (GameObject o2 : player) {
                if (!(o2 instanceof Cuphead)) {
                    continue;
                }
                Cuphead cuphead = (Cuphead) o2;
                if (CollisionHelper.collides(enemy, cuphead)) {
                    Log.d(TAG, "collision!");
                    boolean dead = cuphead.decreaseLife(1);
                    if (dead) {

                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }

        if (!MainGame.getInstance().isTouch) return;
        float x = MainGame.getInstance().tx;
        float y = MainGame.getInstance().ty;

        for (GameObject o1 : enemies) {
            if (!(o1 instanceof Enemy)) {
                continue;
            }
            Enemy enemy = (Enemy) o1;
            if (enemy.level == 1) continue;


            boolean collided = false;
            if (CollisionHelper.isPointInBox(enemy,x,y)) {
                boolean dead = enemy.decreaseLife(1);
                if (dead) {
                    game.remove(enemy);
                }
                MainGame.getInstance().isTouch = false;
                collided = true;
                break;
            }

            if (collided) {
                continue;
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {

    }
}

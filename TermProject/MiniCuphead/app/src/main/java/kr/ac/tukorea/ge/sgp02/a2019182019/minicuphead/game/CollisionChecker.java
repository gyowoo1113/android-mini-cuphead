package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game;

import android.content.Intent;
import android.graphics.Canvas;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.R;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.app.GameActivity;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.app.ResultActivity;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.CollisionHelper;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.GameView;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.interfaces.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.Sound;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.Bullet;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.monster.Boss;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.monster.Enemy;

public class CollisionChecker implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();

    @Override
    public void update(float frameTime) {
        MainGame game = MainGame.get();
        ArrayList<GameObject> bullets = game.objectsAt(MainGame.Layer.bullet);
        ArrayList<GameObject> enemies = game.objectsAt(MainGame.Layer.enemy);
        ArrayList<GameObject> player = game.objectsAt(MainGame.Layer.player);
        ArrayList<GameObject> boss = game.objectsAt(MainGame.Layer.boss);
        ArrayList<GameObject> bossBullets = game.objectsAt(MainGame.Layer.boss_bullet);

        checkPlayerByBossBullets(game, player, bossBullets);
        checkBulletByMonsters(game, bullets, enemies);
        checkBulletByBoss(game, bullets, boss);
        checkPlayerByMonsters(enemies, player);
        checkPlayerByBoss(player, boss);
        checkTouchByMonstsers(game, enemies);

    }

    private void checkPlayerByBossBullets(MainGame game, ArrayList<GameObject> player, ArrayList<GameObject> bossBullets) {
        for (GameObject o1 : player) {
            if (!(o1 instanceof Cuphead)) {
                continue;
            }
            Cuphead cuphead = (Cuphead) o1;
            boolean collided = false;
            for (GameObject o2 : bossBullets) {
                if (!(o2 instanceof Bullet)) {
                    continue;
                }
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(cuphead, bullet)) {
                    game.remove(bullet);
                    boolean dead = cuphead.decreaseLife(1);
                    if (dead) {
                        Sound.playEffect(R.raw.announcer_player_fail,0);
                        MainGame.get().setGameClear(false);
                        FadeEffect fade = new FadeEffect();
                        MainGame.get().add(MainGame.Layer.fade, fade);
                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }
    }

    private void checkPlayerByBoss(ArrayList<GameObject> player, ArrayList<GameObject> boss) {
        for (GameObject o1 : boss) {
            if (!(o1 instanceof Boss)) {
                continue;
            }
            Boss b1 = (Boss) o1;
            boolean collided = false;
            for (GameObject o2 : player) {
                if (!(o2 instanceof Cuphead)) {
                    continue;
                }
                Cuphead cuphead = (Cuphead) o2;
                if (CollisionHelper.collides(b1, cuphead)) {
                    //Log.d(TAG, "collision!");
                    boolean dead = cuphead.decreaseLife(1);
                    if (dead) {
                        Sound.playEffect(R.raw.announcer_player_fail,0);
                        MainGame.get().setGameClear(false);
                        FadeEffect fade = new FadeEffect();
                        MainGame.get().add(MainGame.Layer.fade, fade);
                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }
    }

    private void checkBulletByBoss(MainGame game, ArrayList<GameObject> bullets, ArrayList<GameObject> boss) {
        for (GameObject o1 : boss) {
            if (!(o1 instanceof Boss)) {
                continue;
            }
            Boss b1 = (Boss) o1;
            boolean collided = false;
            for (GameObject o2 : bullets) {
                if (!(o2 instanceof Bullet)) {
                    continue;
                }
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(b1, bullet)) {
                    game.remove(bullet);
                    boolean dead = b1.decreaseLife(bullet.getPower());
                    if (dead) {
                        MainGame.get().setGameClear(true);
                        Sound.playEffect(R.raw.announcer_knockout,0);
                        FadeEffect fade = new FadeEffect();
                        MainGame.get().add(MainGame.Layer.fade, fade);
                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }
    }

    private void checkTouchByMonstsers(MainGame game, ArrayList<GameObject> enemies) {
        if (!MainGame.get().isTouch) return;
        float x = MainGame.get().tx;
        float y = MainGame.get().ty;

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
                    MainGame.get().updateEventKill();
                    game.remove(enemy);
                }
                MainGame.get().isTouch = false;
                collided = true;
                break;
            }

            if (collided) {
                continue;
            }
        }
    }

    private void checkPlayerByMonsters(ArrayList<GameObject> enemies, ArrayList<GameObject> player) {
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
                    //Log.d(TAG, "collision!");
                    boolean dead = cuphead.decreaseLife(1);
                    if (dead) {
                        Sound.playEffect(R.raw.announcer_player_fail,0);
                        MainGame.get().setGameClear(false);
                        FadeEffect fade = new FadeEffect();
                        MainGame.get().add(MainGame.Layer.fade, fade);
                    }
                    collided = true;
                    break;
                }
            }
            if (collided) {
                continue;
            }
        }
    }

    private void checkBulletByMonsters(MainGame game, ArrayList<GameObject> bullets, ArrayList<GameObject> enemies) {
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
                        MainGame.get().updateMonsterKill();
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
    }

    @Override
    public void draw(Canvas canvas) {

    }
}

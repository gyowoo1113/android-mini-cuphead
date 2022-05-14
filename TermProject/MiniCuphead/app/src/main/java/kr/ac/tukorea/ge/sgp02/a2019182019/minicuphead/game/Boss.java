package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.R;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.AnimSprite;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.Bullet;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.HandgunBullet;

public class Boss implements BoxCollidable, GameObject {
    private ArrayList<AnimSprite> states = new ArrayList<>();
    private State curState = State.idle;
    private AnimSprite currentSprite;
    float x, y;
    boolean isDown = true;
    static float updateElapsedTime;
    private int loop_count = 1;
    static final int inoutCount = 1;

    private enum State{
        idle,handgun,handgun_outro,flap_intro,flap_loop,flap_outro,COUNT;
        static float[] w = {
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_flap_w),
                Metrics.size(R.dimen.boss_flap_w),
                Metrics.size(R.dimen.boss_idle_w),
        };

        static float[] h = {
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_flap_h),
                Metrics.size(R.dimen.boss_flap_h),
                Metrics.size(R.dimen.boss_idle_h),
        };

        static int[] resIds = {
                R.mipmap.boss_normal,
                R.mipmap.boss_attack_handgun,
                R.mipmap.boss_attack_handgun_outro,
                R.mipmap.boss_attack_flap_intro,
                R.mipmap.boss_attack_flap_loop,
                R.mipmap.boss_attack_flap_outro,
        };

        static float[] fps = {
                12.0f,
                12.0f,
                12.0f,
                12.0f,
                9.0f,
                12.0f,
        };

        static int[] frameCount = {
                17,
                17,
                17,
                9,
                9,
                3,
        };
    }

    Boss(float x, float y) {
        this.x = x;
        this.y = y;
        for (int i=0; i<State.COUNT.ordinal(); ++i)
        {
            AnimSprite state = new AnimSprite(x,y, State.w[i], State.h[i], State.resIds[i], State.fps[i], State.frameCount[i]);
            states.add(state);
        }
        updateElapsedTime = Metrics.size(R.dimen.boss_update_speed);
        currentSprite = states.get(curState.ordinal());
    }

    @Override
    public void update() {

        switch(curState)
        {
            case idle:
                updateHeight();
                break;
        }

        if (isAnimEnd()){
            ++loop_count;
            switchState();
        }

        currentSprite.UpdateDstRect(this.x,this.y);
    }

    private void switchState() {

        int cnt = (curState == State.idle || curState == State.flap_loop) ?
                GetRandCount(): inoutCount;
        Random r = new Random();

        if (loop_count == cnt)
        {
            switch(curState)
            {
                case idle:
                    boolean check = r.nextBoolean();
                    State st = (check) ? State.handgun : State.flap_intro;
                    setState(st);

                    if (st == State.handgun)
                        handgunFire();

                    break;
                case handgun:
                    setState(State.handgun_outro);
                    break;

                case flap_intro:
                    setState(State.flap_loop);
                    break;

                case flap_loop:
                    setState(State.flap_outro);
                    break;

                case handgun_outro:
                case flap_outro:
                    setState(State.idle);
                    break;
            }
        }
    }

    private void handgunFire() {
        Bullet bullet = HandgunBullet.get(x, y);
        MainGame.getInstance().add(MainGame.Layer.boss_bullet, bullet);
    }

    private int GetRandCount() {
        Random r = new Random();
        int cnt = 4;
        return cnt;
    }

    private boolean isAnimEnd() {
        return State.frameCount[curState.ordinal()] == currentSprite.getIndex() + 1;
    }

    private void setState(State handgun_outro) {
        curState = handgun_outro;
        currentSprite = states.get(curState.ordinal());
        currentSprite.setCreatedOn(System.currentTimeMillis());
        currentSprite.setIndex(0);
        loop_count = 0;
    }

    private void updateHeight() {
        float h = currentSprite.getH();
        float frameTime = MainGame.getInstance().frameTime;
        if (isDown)
        {
            if (this.y + h/2 > Metrics.height)
                isDown = false;
            else
                this.y += frameTime * updateElapsedTime;
        }
        else
        {
            if (this.y - h/2 < 0)
                isDown = true;
            else
                this.y -= frameTime * updateElapsedTime;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentSprite.draw(canvas);
    }

    @Override
    public RectF getBoundingRect() {
        return null;
    }
}

package kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.monster;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.R;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.Gauge;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.Sound;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.sprites.AnimSprite;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.game.BaseGame;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.interfaces.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.interfaces.GameObject;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.framework.resource.Metrics;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.MainGame;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.Bullet;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.FeatherBullet;
import kr.ac.tukorea.ge.sgp02.a2019182019.minicuphead.game.bullet.HandgunBullet;

public class Boss implements BoxCollidable, GameObject {
    private static final String TAG = Boss.class.getSimpleName();
    private ArrayList<AnimSprite> states = new ArrayList<>();
    private State curState = State.idle;
    private AnimSprite currentSprite;
    float x, y;
    boolean isDown = true;
    static float updateElapsedTime;
    private int loop_count = 1;
    static final int inoutCount = 1;
    private float elapsedTimeForFire;
    private float interval;
    private RectF boundingBox = new RectF();
    protected int life, maxlife;
    private boolean isToggle = false;
    private Gauge gauge;
    public static float size;

    private enum State{
        idle,handgun,handgun_outro,flap_intro,flap_loop,flap_outro,death,COUNT;
        static float[] w = {
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_flap_w),
                Metrics.size(R.dimen.boss_flap_w),
                Metrics.size(R.dimen.boss_idle_w),
                Metrics.size(R.dimen.boss_idle_w),
        };

        static float[] h = {
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_flap_h),
                Metrics.size(R.dimen.boss_flap_h),
                Metrics.size(R.dimen.boss_idle_h),
                Metrics.size(R.dimen.boss_death_h),
        };

        static int[] resIds = {
                R.mipmap.boss_normal,
                R.mipmap.boss_attack_handgun,
                R.mipmap.boss_attack_handgun_outro,
                R.mipmap.boss_attack_flap_intro,
                R.mipmap.boss_attack_flap_loop,
                R.mipmap.boss_attack_flap_outro,
                R.mipmap.boss_death
        };

        static float[] fps = {
                12.0f,
                12.0f,
                12.0f,
                12.0f,
                9.0f,
                12.0f,
                4.0f
        };

        static int[] frameCount = {
                17,
                17,
                17,
                9,
                9,
                3,
                29,
        };
    }

    public Boss(float x, float y) {
        this.x = x;
        this.y = y;
        for (int i=0; i<State.COUNT.ordinal(); ++i)
        {
            AnimSprite state = new AnimSprite(x,y, State.w[i], State.h[i], State.resIds[i], State.fps[i], State.frameCount[i], false);
            states.add(state);
        }
        updateElapsedTime = Metrics.size(R.dimen.boss_update_speed);
        currentSprite = states.get(curState.ordinal());
        interval = Metrics.floatValue(R.dimen.feather_fire_interval);
        life = maxlife = 2000;

        size = states.get(State.idle.ordinal()).getDstRect().width();

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

        switch(curState)
        {
            case idle:
                updateHeight();
                break;

            case flap_loop:
                elapsedTimeForFire += frameTime;
                if (elapsedTimeForFire > interval) {
                    featherFire();
                    elapsedTimeForFire -= interval;
                }
                break;
        }

        if (isAnimEnd()){
            ++loop_count;
            switchState();
        }

        currentSprite.updateDstRect(this.x,this.y);
        boundingBox.set(currentSprite.getDstRect());
    }

    private void featherFire() {
        int shotNum = 12;
        float angle = 360 / shotNum;

        for(int i =0;i<shotNum; ++i) {
            float valAngle = angle*i;
            if (isToggle) {valAngle += angle/2;}
            float dx = (float) Math.sin(valAngle);
            float dy = (float) Math.cos(valAngle);
            Bullet bullet = FeatherBullet.get(x, y,dx,dy);
            MainGame.get().add(MainGame.Layer.boss_bullet, bullet);
        }
        isToggle = !isToggle;
    }

    private void handgunFire() {
        Sound.playEffect(R.raw.handgun_sound,0);

        float dy = Metrics.size(R.dimen.handgun_speed_y);
        float[] vals = {dy,-dy,0};

        for(int i=0; i<3; ++i)
        {
            Bullet bullet = HandgunBullet.get(x, y,vals[i]);
            MainGame.get().add(MainGame.Layer.boss_bullet, bullet);
        }
    }

    private void switchState() {
        int cnt = getLoopCnt();
        Random r = new Random();

        if (loop_count > cnt)
            loop_count = loop_count%cnt;

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
                    Sound.playLoopEffect(R.raw.flap_loop_sound);
                    setState(State.flap_loop);
                    break;

                case flap_loop:
                    setState(State.flap_outro);
                    break;

                case handgun_outro:
                    setState(State.idle);
                    break;

                case flap_outro:
                    setState(State.idle);
                    Sound.stopLoopEffect(R.raw.flap_loop_sound);
                    break;
            }
        }
    }

    private int getLoopCnt() {
        Random r = new Random();
        if (curState == State.idle) {
            int cnt = r.nextInt(2)+3;
            return cnt;
        }
        if (curState == State.flap_loop) {
            int cnt =  r.nextInt(2)+10;
            return cnt;
        }
        return inoutCount;
    }

    private boolean isAnimEnd() {
        return State.frameCount[curState.ordinal()] == currentSprite.getIndex() + 1;
    }

    private void setState(State state) {
        curState = state;
        currentSprite = states.get(curState.ordinal());
        currentSprite.setCreatedOn(System.currentTimeMillis());
        currentSprite.setIndex(0);
        loop_count = 0;
        elapsedTimeForFire = 0;
    }

    private void updateHeight() {
        float h = currentSprite.getH();
        float frameTime = BaseGame.getInstance().frameTime;
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
        gauge.setValue((float)life / maxlife);
        gauge.draw(canvas, x, y + size*0.5f);
    }

    @Override
    public RectF getBoundingRect() {
        return boundingBox;
    }

    public boolean decreaseLife(int power) {
        life -= power;
        gauge.setValue((float)life / maxlife);
        if (life <= 0){
            setState(State.death);
            return true;}
        return false;
    }
}
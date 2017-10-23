package se.mah.couchpotato.activtysettings;

import android.animation.Animator;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import se.mah.couchpotato.R;

public class ActivitySettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final int x = getIntent().getIntExtra("revealX", 0);
        final int y = getIntent().getIntExtra("revealY", 0);
        final int startRadius = getIntent().getIntExtra("startRadius", 0);
        final ScrollView sv = (ScrollView) findViewById(R.id.sv_settings);
        sv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Point p = new Point();
                getWindowManager().getDefaultDisplay().getSize(p);
                Animator anim = ViewAnimationUtils.createCircularReveal(findViewById(R.id.sv_settings), x, y, startRadius, Math.max(p.x, p.y));
                anim.setDuration(300);
                anim.setInterpolator(new LinearInterpolator());
                anim.start();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });

        /*v = findViewById(R.id.settings_view_animation);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setStartOffset(200);
        animation.setDuration(500);
        v.startAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) v.getParent()).removeView(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }
}

package com.hyungjun212naver.finedustproject.Utility;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by hyung on 2017-09-05.
 */

public class Utility {

    public static Animation getBlinkAnimation(){
        Animation animation = new AlphaAnimation(1, 0);//투명도변환
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());// do not alter animation rate
        animation.setRepeatCount(4);
        animation.setRepeatMode(Animation.REVERSE);// Reverse animation at the end so the button will fade back in

        return animation;
    }

}

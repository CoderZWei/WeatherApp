package com.example.zw.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zw on 2016/9/24.
 */
public class AddBtn extends View {
    private Paint mPaint;



    public AddBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(getResources().getColor(R.color.base_action_bar_title_color));
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        mPaint.setColor(getResources().getColor(R.color.night_base_action_bar_title_color));
        canvas.drawRect(0,0,getWidth(),1,mPaint);
        mPaint.setColor(getResources().getColor(R.color.night_base_list_title_readed_color));
        canvas.drawRect(getWidth()/2-20,getHeight()/2-2,getWidth()/2+20,getHeight()/2+2,mPaint);
        canvas.drawRect(getWidth()/2-2,getHeight()/2-20,getWidth()/2+2,getHeight()/2+20,mPaint);


    }



}

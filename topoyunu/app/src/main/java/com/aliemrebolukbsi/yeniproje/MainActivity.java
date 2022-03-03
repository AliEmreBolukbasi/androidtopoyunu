package com.aliemrebolukbsi.yeniproje;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import  android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity  extends AppCompatActivity implements SensorEventListener {

    private SensorManager nSensorManager;
    private Sensor nAccelerometer;
    private AnimatedView mAnimatedView=null;
    //sensor için gerekli olan değişkenler oluşturdum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        nAccelerometer = nSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAnimatedView = new AnimatedView(this);
        setContentView(mAnimatedView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nSensorManager.registerListener(this,nAccelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
            mAnimatedView.onSensorEvent(event);

        }
    }

    public class AnimatedView extends View{
        Context context;
        private static  final int CIRCLE_RADIUS = 24;
        private Paint mPaint,p2,p3 , p4;
        private int x;
        private int y;
        private int viewWidth;
        private int viewHeight;
        private int ballPoint=1 , pointsayac=10;
        public AnimatedView(Context context){
            super(context);
            this.context=context;
            mPaint = new Paint();
            p2 = new Paint();
            p3 = new Paint();
            p4 = new Paint();
            mPaint.setColor(Color.BLUE);
            p2.setColor(Color.YELLOW);
            p3.setColor(Color.RED);
            p4.setColor(Color.BLACK);
        }

        public void  newGame( int pointsayac){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage( "Puanın : " + pointsayac + " " + "Tekrar oynamak istermisin ?" );
            builder.setCancelable(false);
            builder.setPositiveButton("cıkıs", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("yeni oyun", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    x=0;
                    y=0;
                }

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            pointsayac = 10;

        }


        @Override
        protected void onSizeChanged(int w , int h, int oldw,int oldh){
            super.onSizeChanged(w,h,oldw,oldh);
            viewWidth = w;
            viewHeight =h;


        }
        public  void  onSensorEvent(SensorEvent event){
            x = x-(int) event.values[0];
            y = y +(int) event.values[1];
            if(x <= 0 + CIRCLE_RADIUS){
                x= 0 + CIRCLE_RADIUS;
            }
            if(x >=viewWidth - CIRCLE_RADIUS){
                x = viewWidth - CIRCLE_RADIUS;
            }
            if(y <= 0 + CIRCLE_RADIUS){
                y = 0 + CIRCLE_RADIUS;
            }
            if(y >=viewHeight - CIRCLE_RADIUS){
                y = viewHeight - CIRCLE_RADIUS;
            }

        }
        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawCircle(x,y,CIRCLE_RADIUS,mPaint);
            Rect r1 = new Rect(500,15,524,400);
            Rect r2 = new Rect(20,700,750,724);
            Rect r3 = new Rect(750,450,774,724);
            Rect r4 = new Rect(500,1100,1200,1124);
            Rect r5 = new Rect(300,1300,324,1600);
            canvas.drawRect(r1,p2);
            canvas.drawRect(r2,p2);
            canvas.drawRect(r3,p2);
            canvas.drawRect(r4,p2);
            canvas.drawRect(r5,p2);

            if (checkIntersect(x,y,r1 ) || checkIntersect(x,y,r2)||
                    checkIntersect(x,y,r3)|| checkIntersect(x,y,r4)||
                    checkIntersect(x,y,r5)){
                pointsayac = pointsayac-2;
                canvas.drawCircle(x,y,CIRCLE_RADIUS,p3);
            }

            ballPoint = checkIntersectBallPoint(x,y,ballPoint);

            if (pointsayac==50 || pointsayac==0){
                newGame(pointsayac);
                pointsayac = 10;
            }
            invalidate();

        }
        public boolean checkIntersect(int x , int y ,Rect r){
            boolean intersects = false;
            if (y >= r.top){
                if(y <= r.bottom){
                    if(x >=r.left){
                        if (x <= r.right){
                            intersects = true;
                        }
                    }
                }
            }
            return intersects;
        }

        public int checkIntersectBallPoint(int x , int y ,int ballPoint){
            if (ballPoint==0){
                if (x<=30 && y<=30){
                    ballPoint=1;
                    pointsayac++;
                } }
            else if (ballPoint==1){
                if (y>=viewHeight-30 && x>=viewWidth-30){
                    ballPoint=0;
                    pointsayac++;
                }
            }
            return ballPoint;
        }
    }



}
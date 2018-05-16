package nincevic.com.homescreentoys;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Tyler on 12/01/2017.
 * 1: implement physics to send icon same speed as finger stroke when letting go
 * 2: set up UI platform for the rest of the application
 *      -listView with different characters
 *      -open corresponding character when list selected
 * 3: design airplane and physics
 */

public class PaperPlane extends Service {

    WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private ImageView PaperPlaneImage;
    private int maxY;
    private int maxX;
    private VelocityTracker mVelocityTracker = null;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    //Upon creating the floating object
    @Override public void onCreate() {
        super.onCreate();

        final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

        maxX= MainActivity.getMyInstance().getScreenSizeWidth();
        maxY= MainActivity.getMyInstance().getScreenSizeHeight();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        PaperPlaneImage = new ImageView(this);
        PaperPlaneImage.setImageResource(R.drawable.android_head);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        PaperPlaneImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            //track the user's interatin with the users object
            @Override public boolean onTouch(View v, MotionEvent event) {
                int index = event.getActionIndex();
                int action = event.getActionMasked();
                int pointerId = event.getPointerId(index);
                gestureDetector.onTouchEvent(event);

                //When the user pushes down on screen, track finger location and update imageView
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Filter this so icon follows finger(like fb app)
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        if(mVelocityTracker == null){
                            mVelocityTracker = VelocityTracker.obtain();
                        }
                        else{
                            mVelocityTracker.clear();
                        }
                        mVelocityTracker.addMovement(event);
                        return true;
                    //Upon release of finger
                    case MotionEvent.ACTION_UP:
                        //If release point is equal to delete circle and velocity<C then delete

                        //Continue gliding object till certain velocity
                        //Then trigger physics behaviors class

                        /*
                        Use same velocity and direction
                        updateViewLayout
                         */

                        //While the object doesn't hit the edge of screen
                        while(params.x != maxX || params.y != maxY){
                            break;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Filter this movement

                        mVelocityTracker.computeCurrentVelocity(1000);//Not really needed since gesture captures velocity

                        Log.d("", "X velocity: " +
                                VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                                        pointerId));
                        Log.d("", "Y velocity: " +
                                VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                                        pointerId));

                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(PaperPlaneImage, params);


                        return true;

                    case MotionEvent.ACTION_CANCEL:
                        // Return a VelocityTracker object back to be re-used by others.
                        mVelocityTracker.recycle();
                        break;
                }
                return false;
            }
        });
        windowManager.addView(PaperPlaneImage, params);
    }

    //Testing the max and min values
    //private static final int SWIPE_MIN_DISTANCE = 120;
    //private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //This test works!!!
            /*
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(PaperPlane.this, "Swipe right to left detected", Toast.LENGTH_SHORT).show();
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(PaperPlane.this, "Swipe left to write detected", Toast.LENGTH_SHORT).show();
                return false; // Left to right
            }
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(PaperPlane.this, "Swipe Bottom to top detected", Toast.LENGTH_SHORT).show();
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(PaperPlane.this, "Swipe top to bottom detected", Toast.LENGTH_SHORT).show();
                return false; // Top to bottom
            }
            */

            //Update the view depending on the flick
            //Loop till coordinates match the max x and y
            
            return false;
            //with gesture detection move image with 2d physics coding
            //first detect if it works lol
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (PaperPlaneImage != null) windowManager.removeView(PaperPlaneImage);
    }
}
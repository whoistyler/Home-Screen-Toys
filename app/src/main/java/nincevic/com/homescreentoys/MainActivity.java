package nincevic.com.homescreentoys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    private static MainActivity myInstance = null;
    private int maxX;
    private int maxY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;

        //Create list view and start the appropriate service with list item
        //startService(new Intent(this, ServiceChatHead));
        startService(new Intent(this, PaperPlane.class));

        //Create a listView,
        //Depending on the list item change, delete the old running and service and commence a new one
    }

    //MainActivity as signleton class
    public static MainActivity getMyInstance(){
        if (myInstance == null){
            myInstance = new MainActivity();
        }
        return myInstance;
    }

    //returns the following values for the service to determine the size of the screen
    //needed for plane to spawn to other side of screen after gesture detection
    public int getScreenSizeWidth(){
        return maxX;
    }

    public int getScreenSizeHeight(){
        return maxY;
    }

    @Override
    public void onDestroy() {
        super.onStop();
        stopService(new Intent(this, PaperPlane.class));
    }
}

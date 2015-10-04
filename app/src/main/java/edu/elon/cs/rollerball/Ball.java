package edu.elon.cs.rollerball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Ball {

    protected float x, y;
    private float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 0.1f;

    public Ball(Context context) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        // scale the size
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start in middle
        x = screenWidth/2;
        y = screenHeight/2;
    }

    public void doDraw(Canvas canvas) {
        // draw the ball
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public void doUpdate(float tiltX, float tiltY) {
        float newX, newY;
        newX = x + (tiltX*5);
        newY = y + (tiltY*5);

        if(newX < (width/2)){
            x = width/2;
        } else if(newX > screenWidth - (width/2)) {
            x = screenWidth - (width/2);
        } else {
            x = newX;
        }

        if(newY < (height/2)) {
            y = height/2;
        } else if(newY > screenHeight - (height*1.5)){
            y = screenHeight - (int)(height*1.5);
        } else {
            y = newY;
        }
    }

    public int[] getLocation() {
        int[] ballLocation = {(int) (x - width/2), (int) (y- height/2),
                (int) (x + width/2), (int) (y + height/2)};
        return ballLocation;
    }
}
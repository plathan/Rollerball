package edu.elon.cs.rollerball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Spot {

    protected float x, y;
    private float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 0.15f;

    public Spot(Context context) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spot);

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
        // draw the spot
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public void doUpdate() {

        x = (float) Math.random()*(screenWidth - width) + (width/2);
        y = (float) Math.random()*(screenWidth - height) + (height/2);

    }

    public int[] getLocation() {
        int[] spotLocation = {(int) (x - width/2), (int) (y- height/2),
                (int) (x + width/2), (int) (y + height/2)};
        return spotLocation;
    }
}
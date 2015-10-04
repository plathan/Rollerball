package edu.elon.cs.rollerball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class BoardView extends SurfaceView implements SurfaceHolder.Callback {

    private BoardViewThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // remember the context for finding resources
        this.context = context;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // game loop thread
        thread = new BoardViewThread();
    }


    // SurfaceHolder.Callback methods:
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // thread exists, but is in terminated state
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new BoardViewThread();
        }

        // start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    // Game Loop Thread
    private class BoardViewThread extends Thread {

        private float tiltX, tiltY;
        private boolean wonGame;
        private double counter;

        private boolean isRunning = false;
        private long lastTime;

        private Ball ball;
        private Spot spot;

        private int width;
        private int height;
        private Bitmap board = BitmapFactory.decodeResource(context.getResources(), R.drawable.board);
        private WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = wm.getDefaultDisplay();

        public BoardViewThread() {
            ball = new Ball(context);
            spot = new Spot(context);
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {

            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;

                    //if the ball is on the spot add to a counter
                    int[] ballLocation = ball.getLocation();
                    int[] spotLocation = spot.getLocation();
                    if(spotLocation[0] < ballLocation[0]
                            && spotLocation[1] < ballLocation[1]
                            && ballLocation[2] < spotLocation [2]
                            && ballLocation[3] < spotLocation[3]){
                        counter = counter + elapsed;
                    } else {
                        counter = 0;
                    }
                    if(counter >= 3){
                        wonGame = true;
                    }

                    lastTime = now;

                    // update/draw
                    doUpdate();
                    doDraw(canvas);
                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        /* THE GAME */

        // move all objects in the game
        private void doUpdate() {

            tiltX = MainActivity.tiltX;
            tiltY = MainActivity.tiltY;
            ball.doUpdate(tiltX, tiltY);
            if(wonGame) {
                spot.doUpdate();
                wonGame = false;
            }
        }

        // draw all objects in the game
        private void doDraw(Canvas canvas) {

            display.getSize(size);
            width = size.x;
            height = size.y;
            canvas.drawBitmap(board, null, new Rect(0, 0, width, height), null);

            spot.doDraw(canvas);
            ball.doDraw(canvas);
        }
    }
}
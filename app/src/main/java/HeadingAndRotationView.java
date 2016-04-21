import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by Guest on 4/21/16.
 */


public class HeadingAndRotationView {
    Context context;

    Thread gameThread = null;

    SurfaceHolder ourHolder;

    volatile boolean playing;

    boolean paused = true;

    Canvas canvas;
    Paint paint;

    long fps;
    private long timeThisFrame;

    int screenX;
    int screenY;

    Ship ship;

    public HeadingAndRotationView(Context context, int x, int y) {
        super(context);
        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;

        ship = new Ship(context, screenX, screenY);
    }

    @Override
    public void run() {
        while(playing) {
            long startFrameTime = System.currentTimeMillis();
            if(!paused) {
                update();
            }
            draw();
            if(timeThisFrame >= 1) {
                fps = 1000/timeThisFrame;
            }
        }
    }

    private void update() {
        ship.update(fps);
    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawLine(ship.getA().x, ship.getA().y, ship.getB().x, ship.getB().y, paint);
            canvas.drawLine(ship.getB().x, ship.getB().y, ship.getC().x, ship.getC().y, paint);
            canvas.drawLine(ship.getC().x, ship.getC().y, ship.getA().x, ship.getA().y, paint);
            canvas.drawPoint(ship.getCentre().x, ship.getCentre().y, paint);
            paint.setTextSize(60);
            canvas.drawText("facingAngle = " + (int)ship.getFacingAngle() + " degrees", 20, 70, paint);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error: ", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                paused = false;

                if(motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX / 2) {
                        ship.setMovementState(ship.RIGHT);
                    } else {
                        ship.setMovementState(ship.LEFT);
                    }

                }

                if(motionEvent.getY() < screenY - screenY / 8) {
                    // Thrust
                    ship.setMovementState(ship.THRUSTING);

                }

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                ship.setMovementState(ship.STOPPED);

                break;
        }
        return true;
    }
}

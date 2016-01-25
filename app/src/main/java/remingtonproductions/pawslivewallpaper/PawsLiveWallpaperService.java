package remingtonproductions.pawslivewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.authorwjf.live_wallpaper_p1.R;

import java.util.ArrayList;
import java.util.Random;

public class PawsLiveWallpaperService extends WallpaperService {
    private Bitmap imageBackground = null;
    private Bitmap imagePawPrint = null;
    private final int UPDATE_INTERVAL = 5;
    private float x, y;
    private int bg_size = 0;

    //    public long UPDATE_TIME = 100;
//    public long LAST_UPDATE_TIME = 0;
    public static int color;
    public static float speed;
    public static int alpha;
    public static boolean paws;

    @Override
    public Engine onCreateEngine() {
        imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg_jagged);
        imagePawPrint = BitmapFactory.decodeResource(getResources(), R.drawable.pawprint);
        x = y = 0;
        bg_size = imageBackground.getWidth() * 2;

        //DEBUGGING -------------------
        //global user settings
        color = Tools.getColorPreference(PawsLiveWallpaperService.this);
        speed = Tools.getAnimationSpeedPreference(PawsLiveWallpaperService.this);
        alpha = Tools.getOpacityPreference(PawsLiveWallpaperService.this);
        paws = Tools.getPawAnimationPreference(PawsLiveWallpaperService.this);
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine {
        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        Paint p = new Paint();
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
//                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setColor(color);
                    c.drawRect(0, 0, bg_size, bg_size, p);
                    p.setAlpha(alpha);
                    c.drawBitmap(imageBackground, x, y, p);
                    c.drawBitmap(imageBackground, x - imageBackground.getWidth(), y, p);
                    c.drawBitmap(imageBackground, x, y - imageBackground.getHeight(), p);
                    c.drawBitmap(imageBackground, x - imageBackground.getWidth(), y - imageBackground.getHeight(), p);
                    if (paws) {
                        for (int i = 0; i < pawprints.size(); i++) {
                            p.setAlpha(pawprints.get(i).getOpacity());
                            c.drawBitmap(Bitmap.createScaledBitmap(Tools.RotateBitmap(imagePawPrint, pawprints.get(i).getAngle()), pawprints.get(i).getSize(), pawprints.get(i).getSize(), false), pawprints.get(i).getX(), pawprints.get(i).getY(), p);
                            pawprints.get(i).setOpacity(pawprints.get(i).getOpacity() - 10);
                            if (pawprints.get(i).getOpacity() < 10) {
                                pawprints.remove(i);
                            }
                        }
                    }
                    if (Math.abs(x) >= Math.abs(imageBackground.getWidth())) {
                        x = y = 0;
                    }
                    x += speed;
                    y += speed;
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, UPDATE_INTERVAL);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            mHandler.removeCallbacks(mUpdateDisplay);
            assignValues();
            if (visible) {
                draw();
            } else {
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
            dump();
        }

        ArrayList<PawPrint> pawprints = new ArrayList<>();
        Random rand = new Random();

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (paws) {
                if (pawprints.size() < 8) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        pawprints.add(new PawPrint(event.getX() + Tools.randInt(-100, 100, rand), event.getY() + Tools.randInt(-100, 100, rand), Tools.randInt(240, 255, rand), Tools.randInt(0, 360, rand), Tools.randInt(64, 256, rand)));
                    }
                }
            }
        }

        public void assignValues() {
            try {
                color = Tools.getColorPreference(getApplicationContext());
                speed = Tools.getAnimationSpeedPreference(getApplicationContext());
                alpha = Tools.getOpacityPreference(getApplicationContext());
                paws = Tools.getPawAnimationPreference(getApplicationContext());
                imageBackground = null;
                imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg_jagged);
                imagePawPrint = null;
                imagePawPrint = BitmapFactory.decodeResource(getResources(), R.drawable.pawprint);
            } catch (Exception e) {
            }
        }

        public void dump() {
            x = y = 0;
            imagePawPrint = null;
            imageBackground = null;
        }
    }
}

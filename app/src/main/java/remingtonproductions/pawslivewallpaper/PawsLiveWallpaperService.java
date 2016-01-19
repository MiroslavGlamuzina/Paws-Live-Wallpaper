package remingtonproductions.pawslivewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.authorwjf.live_wallpaper_p1.R;

public class PawsLiveWallpaperService extends WallpaperService {
    Bitmap image = null;
    float x, y;
    int pad = 100;
    int color;
    @Override
    public Engine onCreateEngine() {
        image = BitmapFactory.decodeResource(getResources(), R.drawable.more_tiny_paws);
        x = 0;
        y = 0;
        color = Color.parseColor("#FFB266");
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

        private void draw() {
            x += 0.5;
            y += 0.5;
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setColor(color);
                    c.drawRect(0, 0, image.getWidth() * 2, image.getHeight() * 2, p);
                    p.setAlpha(150);
                    c.drawBitmap(image, x, y, p);
                    c.drawBitmap(image, x - image.getWidth(), y, null);
                    c.drawBitmap(image, x, y - image.getHeight(), null);
                    c.drawBitmap(image, x - image.getWidth(), y - image.getHeight(), null);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, 10);
            }
            if (Math.abs(x) >= Math.abs(image.getWidth())) {
                x = y = 0;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mUpdateDisplay);
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
        }
    }
}

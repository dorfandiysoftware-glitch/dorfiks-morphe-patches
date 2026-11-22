package dev.dorfandiy.extension.heliboard;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;

/** Called from the patched HeliBoard: see-through keyboard with the background blurred. */
@SuppressWarnings("unused")
public final class BlurKeyboard {

    private static final String TAG = "DorfikBlur";

    /** Blur radius in pixels. */
    private static final int BLUR_RADIUS = 90;
    /** Opacity of the keyboard frame and the suggestion strip, 0..255. */
    private static final int FRAME_ALPHA = 110;
    /** Opacity of the keys, 0..255. */
    private static final int KEY_ALPHA = 170;

    private BlurKeyboard() {
    }

    /** The keyboard window must be translucent for the blur to show through. */
    public static void applyWindow(InputMethodService ime) {
        try {
            if (ime.getWindow() == null) {
                return;
            }
            Window window = ime.getWindow().getWindow();
            if (window == null) {
                return;
            }
            window.setFormat(PixelFormat.TRANSLUCENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window.setBackgroundBlurRadius(BLUR_RADIUS);
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "Could not set up the blurred window", e);
        }
    }

    /** After HeliBoard coloured a view's background: make the main and strip backgrounds see-through. */
    public static void afterBackground(View view, Object colorType) {
        try {
            String name = ((Enum<?>) colorType).name();
            if (!name.equals("MAIN_BACKGROUND") && !name.equals("STRIP_BACKGROUND")) {
                return;
            }
            Drawable background = view.getBackground();
            if (background != null) {
                background.mutate().setAlpha(FRAME_ALPHA);
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "Could not fade a background", e);
        }
    }

    /** After HeliBoard coloured a key background drawable. */
    public static void afterKeyColor(Drawable drawable, Object colorType) {
        try {
            String name = ((Enum<?>) colorType).name();
            if (name.equals("KEY_BACKGROUND") || name.equals("FUNCTIONAL_KEY_BACKGROUND")
                    || name.equals("SPACE_BAR_BACKGROUND") || name.equals("ACTION_KEY_BACKGROUND")) {
                drawable.setAlpha(KEY_ALPHA);
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "Could not fade a key", e);
        }
    }
}

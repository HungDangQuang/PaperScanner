package uit.app.demoopencv;

import android.content.res.Resources;

public class ScanConstants {
    public static final String SCANNED_RESULT = "scannedResult";
    public static final String IMAGE_NAME = "image";
    public static final String IMAGE_DIR = "imageDir";
    public static final int HIGHER_SAMPLING_THRESHOLD = 2200;
    public static final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int KSIZE_BLUR = 3;
    public static int KSIZE_CLOSE = 10;
    public static final int CANNY_THRESH_L = 85;
    public static final int CANNY_THRESH_U = 185;
    public static final int TRUNC_THRESH = 150;
    public static final int CUTOFF_THRESH = 155;

}

package uit.app.demoopencv;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class RectView extends FrameLayout {

    //        Bitmap resultBitmap = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(m, resultBitmap);
//        img.setImageBitmap(resultBitmap);
//        addView(img);
    private Paint paint;
    private final Context context;
//    private MatOfPoint point;
    ImageView point1;
    ImageView point2;
    ImageView point3;
    ImageView point4;

    Rect rect;

    public RectView(Context context) {
        super(context);
        this.context = context;
        this.setWillNotDraw(false);
        init();
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setWillNotDraw(false);
        init();
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.setWillNotDraw(false);
        init();
    }


    private void init(){
        point1 = new ImageView(context);
        point2 = new ImageView(context);
        point3 = new ImageView(context);
        point4 = new ImageView(context);
        initPaint();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void getPoint(MatOfPoint p){
        rect = Imgproc.boundingRect(p);
        point1 = getImageView(rect.x,rect.y);
        point2 = getImageView(rect.x + rect.width,rect.y);
        point3 = getImageView(rect.x,rect.y + rect.height);
        point4 = getImageView(rect.x + rect.width,rect.y + rect.height);

        addView(point1);
        addView(point2);
        addView(point3);
        addView(point4);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawBitmap(background,0,0,null);

    }

    private ImageView getImageView(int x, int y) {
        ImageView imageView = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        imageView.setX(x);
        imageView.setY(y);
//        imageView.setOnTouchListener(new MidPointTouchListenerImpl());
        return imageView;
    }


}

package uit.app.demoopencv;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class ImagePreview extends Activity {
//    ImageView imgView;
    public final static Stack<PolygonPoints> allDraggedPointsStack = new Stack<>();

    private static Mat morph_kernel = new Mat(new Size(ScanConstants.KSIZE_CLOSE, ScanConstants.KSIZE_CLOSE), CvType.CV_8UC1, new Scalar(255));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        String imgURI = bundle.getString("imageURI");

        File imgFile = new File(imgURI);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    myBitmap,ScanConstants.SCREEN_WIDTH , ScanConstants.SCREEN_HEIGHT, false);
            Mat rgba = new Mat();
            Mat temp = new Mat();
            Size scaleSize = new Size(Resources.getSystem().getDisplayMetrics().widthPixels,Resources.getSystem().getDisplayMetrics().heightPixels);
            Utils.bitmapToMat(myBitmap, temp);
            Imgproc.resize(temp,rgba,scaleSize);
            temp.release();

//            imgView.setImageBitmap(myBitmap);

            ScanUtils scanUtils = new ScanUtils();

            List<MatOfPoint> lst = scanUtils.detectEdges(rgba);

            MatOfPoint point = scanUtils.findTheLargestContour(lst);

//            Rect rect = Imgproc.boundingRect(point);
//            Imgproc.rectangle(rgba, new Point(rect.x, rect.y),new Point(rect.x + rect.width, rect.y+ rect.height), new Scalar(0,255,0,0),3);

            // notes

//        Imgproc.drawContours(rgba, contours, maxValIdx, new Scalar(0,255,0), 5);
//

            RectView rect = new RectView(this);
//
            rect.getPoint(point);

//            rect.getImage(myBitmap);

            setContentView(rect);

//            Utils.bitmapToMat(myBitmap,originalMat);
//
//            // s1
//            Imgproc.blur(originalMat, originalMat, new Size(ScanConstants.KSIZE_BLUR, ScanConstants.KSIZE_BLUR));
//            Core.normalize(originalMat, originalMat, 0, 255, Core.NORM_MINMAX);
//
//            // s2
//            Imgproc.threshold(originalMat,originalMat, ScanConstants.TRUNC_THRESH,255,Imgproc.THRESH_TRUNC);
//            Core.normalize(originalMat, originalMat, 0, 255, Core.NORM_MINMAX);
//
//            // s3
//            Imgproc.Canny(originalMat, originalMat, ScanConstants.CANNY_THRESH_U, ScanConstants.CANNY_THRESH_L);
//
//            // s4
//            Imgproc.threshold(originalMat,originalMat,ScanConstants.CUTOFF_THRESH,255,Imgproc.THRESH_TOZERO);
//
//            Bitmap bmp = null;
//            try {
//                //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
//
//                Utils.matToBitmap(originalMat, bmp);
//                imgView.setImageBitmap(bmp);
//            }
//            catch (CvException e){
//                Log.d("Exception",e.getMessage());}

//            imgView.setImageBitmap(myBitmap);


        }


    }

    private void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);
        Imgproc.blur(rgba, rgba, new Size(ScanConstants.KSIZE_BLUR, ScanConstants.KSIZE_BLUR));
        Core.normalize(rgba, rgba, 0, 255, Core.NORM_MINMAX);
//
        // s2
        Imgproc.threshold(rgba,rgba, ScanConstants.TRUNC_THRESH,255,Imgproc.THRESH_TRUNC);
        Core.normalize(rgba, rgba, 0, 255, Core.NORM_MINMAX);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        Imgproc.threshold(edges,edges,ScanConstants.CUTOFF_THRESH,255,Imgproc.THRESH_TOZERO);

        Imgproc.morphologyEx(edges, edges, Imgproc.MORPH_CLOSE, morph_kernel, new Point(-1,-1),1);

//        List<MatOfPoint> largestContour = findLargestContours(edges, 10);
//        if (null != largestContour) {
//            Quadrilateral mLargestRect = findQuadrilateral(largestContour);
//            if (mLargestRect != null){
//
//            }
//
//        }

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(edges,contours,hierarchy,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

        Scalar color = new Scalar(0,0,255);

        // sol1

//        hierarchy.release();

//        for (int contourIdx=0;contourIdx<contours.size();contourIdx++){
//            MatOfPoint2f approxCurve = new MatOfPoint2f();
//            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(contourIdx).toArray());
//            double approxDistance = Imgproc.arcLength(contour2f,true) * 0.01;
//            Imgproc.approxPolyDP(contour2f,approxCurve,approxDistance,true);
//
//            MatOfPoint points = new MatOfPoint(approxCurve.toArray());
//
//            Rect rect = Imgproc.boundingRect(points);
//            Imgproc.rectangle(rgba, new Point(rect.x, rect.y),new Point(rect.x + rect.width, rect.y+ rect.height), new Scalar(0,255,0,0),3);
//        }

        // sol2
        double maxVal = 0;
        int maxValIdx = 0;

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
        {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            if (maxVal < contourArea)
            {
                maxVal = contourArea;
                maxValIdx = contourIdx;
            }


        }

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(maxValIdx).toArray());
        double approxDistance = Imgproc.arcLength(contour2f,true) * 0.01;
        Imgproc.approxPolyDP(contour2f,approxCurve,approxDistance,true);
        MatOfPoint points = new MatOfPoint(approxCurve.toArray());

        Rect rect = Imgproc.boundingRect(points);
        Imgproc.rectangle(rgba, new Point(rect.x, rect.y),new Point(rect.x + rect.width, rect.y+ rect.height), new Scalar(0,255,0,0),3);

        // notes

//        Imgproc.drawContours(rgba, contours, maxValIdx, new Scalar(0,255,0), 5);
//
        Bitmap resultBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba, resultBitmap);
//        imgView.setImageBitmap(resultBitmap);





//        List<MatOfPoint> mHullList = new ArrayList<>();
//        MatOfInt tempHullIndices = new MatOfInt();
//
//        for (int i = 0; i < contours.size(); i++) {
//            Imgproc.convexHull(contours.get(i), tempHullIndices);
//            mHullList.add(hull2Points(tempHullIndices, mContourList.get(i)));
//        }

//        List<MatOfPoint> hullList = new ArrayList<>();
//        for (MatOfPoint contour : contours) {
//            MatOfInt hull = new MatOfInt();
//            Imgproc.convexHull(contour, hull);
//            Point[] contourArray = contour.toArray();
//            Point[] hullPoints = new Point[hull.rows()];
//            List<Integer> hullContourIdxList = hull.toList();
//            for (int i = 0; i < hullContourIdxList.size(); i++) {
//                hullPoints[i] = contourArray[hullContourIdxList.get(i)];
//            }
//            hullList.add(new MatOfPoint(hullPoints));
//        }
//        Mat drawing = Mat.zeros(edges.size(), CvType.CV_8UC3);
//        for (int i = 0; i < contours.size(); i++) {
////            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
//            Imgproc.drawContours(drawing, contours, i, color);
////            Imgproc.drawContours(drawing, hullList, i, color );
//        }

//        Bitmap resultBitmap = Bitmap.createBitmap(drawing.cols(), drawing.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(drawing, resultBitmap);
//        imgView.setImageBitmap(resultBitmap);



//        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(edges, resultBitmap);
//        imgView.setImageBitmap(resultBitmap);
    }

    private static List<MatOfPoint> findLargestContours(Mat inputMat, int NUM_TOP_CONTOURS) {
        Mat mHierarchy = new Mat();
        List<MatOfPoint> mContourList = new ArrayList<>();
        //finding contours - as we are sorting by area anyway, we can use RETR_LIST - faster than RETR_EXTERNAL.
        Imgproc.findContours(inputMat, mContourList, mHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Convert the contours to their Convex Hulls i.e. removes minor nuances in the contour
        List<MatOfPoint> mHullList = new ArrayList<>();
        MatOfInt tempHullIndices = new MatOfInt();
        for (int i = 0; i < mContourList.size(); i++) {
            Imgproc.convexHull(mContourList.get(i), tempHullIndices);
            mHullList.add(hull2Points(tempHullIndices, mContourList.get(i)));
        }
        // Release mContourList as its job is done
        for (MatOfPoint c : mContourList)
            c.release();
        tempHullIndices.release();
        mHierarchy.release();

        if (mHullList.size() != 0) {
            Collections.sort(mHullList, new Comparator<MatOfPoint>() {
                @Override
                public int compare(MatOfPoint lhs, MatOfPoint rhs) {
                    return Double.compare(Imgproc.contourArea(rhs),Imgproc.contourArea(lhs));
                }
            });
            return mHullList.subList(0, Math.min(mHullList.size(), NUM_TOP_CONTOURS));
        }
        return null;
    }

    private static MatOfPoint hull2Points(MatOfInt hull, MatOfPoint contour) {
        List<Integer> indexes = hull.toList();
        List<Point> points = new ArrayList<>();
        List<Point> ctrList = contour.toList();
        for(Integer index:indexes) {
            points.add(ctrList.get(index));
        }
        MatOfPoint point= new MatOfPoint();
        point.fromList(points);
        return point;
    }

    public static int dp2px(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(px);
    }

    private static Quadrilateral findQuadrilateral(List<MatOfPoint> mContourList) {
        for (MatOfPoint c : mContourList) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true);
            Point[] points = approx.toArray();
            // select biggest 4 angles polygon
            if (approx.rows() == 4) {
                Point[] foundPoints = sortPoints(points);
                return new Quadrilateral(approx, foundPoints);
            }
        }
        return null;
    }

    private static Point[] sortPoints(Point[] src) {
        ArrayList<Point> srcPoints = new ArrayList<>(Arrays.asList(src));
        Point[] result = {null, null, null, null};

        Comparator<Point> sumComparator = new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y + lhs.x).compareTo(rhs.y + rhs.x);
            }
        };

        Comparator<Point> diffComparator = new Comparator<Point>() {

            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y - lhs.x).compareTo(rhs.y - rhs.x);
            }
        };

        // top-left corner = minimal sum
        result[0] = Collections.min(srcPoints, sumComparator);
        // bottom-right corner = maximal sum
        result[2] = Collections.max(srcPoints, sumComparator);
        // top-right corner = minimal difference
        result[1] = Collections.min(srcPoints, diffComparator);
        // bottom-left corner = maximal difference
        result[3] = Collections.max(srcPoints, diffComparator);

        return result;
    }

}

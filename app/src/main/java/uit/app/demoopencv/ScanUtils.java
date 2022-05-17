package uit.app.demoopencv;

import android.content.res.Resources;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ScanUtils {

    private static Mat morph_kernel = new Mat(new Size(ScanConstants.KSIZE_CLOSE, ScanConstants.KSIZE_CLOSE), CvType.CV_8UC1, new Scalar(255));

    public ScanUtils(){

    }


    public List<MatOfPoint> detectEdges (Mat rgba) {

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


        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(edges,contours,hierarchy,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;

    }

    public MatOfPoint findTheLargestContour(List<MatOfPoint> contours){

        Scalar color = new Scalar(0,0,255);


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

        return points;

//        Rect rect = Imgproc.boundingRect(points);
//        Imgproc.rectangle(rgba, new Point(rect.x, rect.y),new Point(rect.x + rect.width, rect.y+ rect.height), new Scalar(0,255,0,0),3);
//
//
//        Bitmap resultBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(rgba, resultBitmap);

    }



}

package uit.app.demoopencv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

//android:src="@drawable/ic_baseline_camera_24"
// AppCompatActivity
public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2
{
    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;

    ImageView captureImageBtn;
    ImageView getImagesBtn;


//    Mat mRGBA, mRGBAT,dst;
    Mat mRGBA,mGrey;
//    DigitRecognizer mnist = new DigitRecognizer();

    Bundle bundle = new Bundle();

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private int capture_image = 0;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(MainActivity.this) {
        @Override
        public void onManagerConnected(int status)
        {
            if (status == BaseLoaderCallback.SUCCESS) {
                javaCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    static
    {
        if (OpenCVLoader.initDebug())
        {
            Log.d(TAG, "OpenCV is Configured or Connected successfully.");
        }
        else
        {
            Log.d(TAG, "OpenCV not Working or Loaded.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImageBtn = findViewById(R.id.capture_image);
        getImagesBtn = findViewById(R.id.get_images);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);

        captureImageBtn.setId(View.generateViewId());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)  {
            Log.d(TAG, "Permissions granted");
            javaCameraView.setCameraPermissionGranted();
            javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
            javaCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
            javaCameraView.setCvCameraViewListener(this);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            Log.d(TAG, "Permission prompt");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)  {
            Log.d(TAG, "Permissions granted");
        }
        else {
            Log.d(TAG, "Permission prompt");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)  {
            Log.d(TAG, "Permissions granted");
        }
        else {
            Log.d(TAG, "Permission prompt");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }

        captureImageBtn.setOnClickListener(view -> {
//                if(capture_image == 0) {
                    capture_image = 1;
                    Mat save_mat = new Mat();
                    Core.flip(mGrey.t(),save_mat,1);

                    File folder = new File(Environment.getExternalStorageDirectory().getPath() + "ImagePro");
                    boolean success = true;
                    if(!folder.exists()){
                        success = folder.mkdir();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                    String currentDateAndTime = sdf.format(new Date());
                    String fileName = Environment.getExternalStorageDirectory().getPath() + "/ImagePro/" + currentDateAndTime + ".jpg";
                    Imgcodecs.imwrite(fileName,save_mat);

                    bundle.putString("imageURI",fileName);
//
//
//                    capture_image = 0;
//                }
//                else {
//                    capture_image = 0;
//                }

                Intent imagePreview = new Intent(MainActivity.this,ImagePreview.class);

                imagePreview.putExtras(bundle);
//                startActivity(imagePreview);

            startActivity(imagePreview);
        });
    }

    public MainActivity(){
        Log.i(TAG,"Instantiated new " + this.getClass());
    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
//        mRGBAT = new Mat();
//        dst = new Mat();
        mRGBA = new Mat(height,width, CvType.CV_8UC4);
        mGrey = new Mat(height,width,CvType.CV_8UC1);

    }

    @Override
    public void onCameraViewStopped()
    {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
//        mRGBA = inputFrame.rgba();
//        Core.transpose(mRGBA, mRGBAT);
//        Core.flip(mRGBAT, mRGBAT, 1);
//        Imgproc.resize(mRGBAT, dst, mRGBA.size());
//        mRGBA.release();
//        mRGBAT.release();
//        return dst;
//
//        Imgproc.rectangle(dst, new Point(dst.cols()/2 - 200, dst.rows()/2 - 200), new Point(dst.cols()/2 + 200, dst.rows()/2 + 200), new Scalar(255,255,255),1);
//        Mat digit = dst.submat(dst.rows()/2-180,dst.rows()/2 + 180, dst.cols()/2 -180, dst.cols()/2 + 180).clone();
//        Core.transpose(digit,digit);
//        mnist.FindMatch(digit);
//        return mRGBA;
//        return dst;

//        Imgproc.rectangle(mRGBA, new Point(mRGBA.cols()/2 - 200, mRGBA.rows()/2 - 200), new Point(mRGBA.cols()/2 + 200, mRGBA.rows()/2 + 200), new Scalar(255,255,255),1);
//        Mat digit = mRGBA.submat(mRGBA.rows()/2-180,mRGBA.rows()/2 + 180, mRGBA.cols()/2 -180, mRGBA.cols()/2 + 180).clone();
//        Core.transpose(digit,digit);
//        Imgproc.resize(mRGBAT, dst, mRGBA.size());
//        mnist.FindMatch(digit);
//        return mRGBA;
        mRGBA = inputFrame.rgba();
        mGrey = inputFrame.gray();

//        capture_image = capture_image(capture_image,mGrey);

        return mRGBA;
    }

    private int capture_image(int capture_image,Mat mGrey){
        if(capture_image == 1){
            Mat save_mat = new Mat();
            Core.flip(mGrey.t(),save_mat,1);

            File folder = new File(Environment.getExternalStorageDirectory().getPath() + "ImagePro");
            boolean success = true;
            if(!folder.exists()){
                success = folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateAndTime = sdf.format(new Date());
            String fileName = Environment.getExternalStorageDirectory().getPath() + "/ImagePro/" + currentDateAndTime + ".jpg";
            Imgcodecs.imwrite(fileName,save_mat);

            bundle.putString("imageURI",fileName);


            capture_image = 0;
        }
        return capture_image;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug())
        {
            Log.d(TAG, "OpenCV is Configured or Connected successfully.");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else
        {
            Log.d(TAG, "OpenCV not Working or Loaded.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseLoaderCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            // camera can be turned on
            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            javaCameraView.setCameraPermissionGranted();
            javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
            javaCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
            javaCameraView.setCvCameraViewListener(this);
        } else {
            //camera will stay off
            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
    }


}
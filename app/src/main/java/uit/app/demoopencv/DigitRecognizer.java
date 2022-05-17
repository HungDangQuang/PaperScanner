package uit.app.demoopencv;

import static org.opencv.ml.ANN_MLP.create;

import android.os.Environment;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DigitRecognizer {

    int width = 0;
    int height = 0;
    int total_images = 0;
    KNearest knn = KNearest.create();

    public void readMNISTData() throws FileNotFoundException {

        File mnist_images_file = new File("app/dataset/train-images-idx3-ubyte");
        FileInputStream images_reader = new FileInputStream(mnist_images_file);

        Mat training_images = null;

        try {
            byte[] header = new byte[16];
            images_reader.read(header,0,16);
            ByteBuffer temp = ByteBuffer.wrap(header,4,12);
            total_images = temp.getInt();
            width = temp.getInt();
            height = temp.getInt();
            int pixel_count = width * height;
            training_images = new Mat(total_images,pixel_count,CvType.CV_8U);

            for(int i = 0; i < total_images; i++){
                byte[] image = new byte[pixel_count];
                images_reader.read(image,0,pixel_count);
                training_images.put(i,0,image);
            }
            training_images.convertTo(training_images,CvType.CV_32FC1);
            images_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mnist_labels_file = new File("app/dataset/train-labels-idx1-ubyte");

        FileInputStream labels_reader = new FileInputStream(mnist_labels_file);

        // Labels

        byte[] labels_header = new byte[8];

        Mat training_labels = null;
        byte[] labels_data = new byte[total_images];

        try {
            training_labels = new Mat(total_images,1,CvType.CV_8U);
            Mat temp_labels = new Mat(1,total_images,CvType.CV_8U);
            byte[] header = new byte[8];
            labels_reader.read(header,0,8);
            labels_reader.read(labels_data,0,total_images);
            temp_labels.put(0,0,labels_data);
            Core.transpose(temp_labels,training_labels);
            training_labels.convertTo(training_labels,CvType.CV_32FC1);
            labels_reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        knn.train(training_images, Ml.ROW_SAMPLE, training_labels);


    }

    public void FindMatch(Mat test_image){
        // Dilate the image

        Imgproc.dilate(test_image,test_image,Imgproc.getStructuringElement(Imgproc.CV_SHAPE_CROSS,new Size(3,3)));

        // Resize the image to match it with the sample image size
        Imgproc.resize(test_image,test_image,new Size(width,height));

        Imgproc.cvtColor(test_image,test_image,Imgproc.COLOR_RGB2GRAY);

        Imgproc.adaptiveThreshold(test_image,test_image,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV,15,2);

        Mat test =  new Mat(1, test_image.rows() * test_image.cols(), CvType.CV_32FC1);

        int count = 0;

        for (int i=0; i < test_image.rows(); i++){

            for (int j = 0; j < test_image.cols(); j++){
                test.put(0,count,test_image.get(i,j)[0]);
                count++;
            }
        }

        Mat results = new Mat(1,1,CvType.CV_8U);
        knn.findNearest(test,10,results, new Mat(), new Mat());
        Log.i("Result:","" + results.get(0,0)[0]);

    }
}

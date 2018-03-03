package com.dexmohq.imadex.recognize;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.Buffer;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.FisherFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.EigenFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

public class Test {


    public static void main(String[] args) {

        final String testImage = "";
        final String trainingDir = "";

        final Mat imageMat = imread(testImage, CV_LOAD_IMAGE_GRAYSCALE);
        final File file = new File(trainingDir);
        final File[] imageFiles = file.listFiles((dir, name) -> {
            String n = name.toLowerCase();
            return n.endsWith(".jpg") || n.endsWith(".png");
        });
        if (imageFiles == null) {
            throw new IllegalStateException("Training dir invalid");
        }
        final MatVector images = new MatVector(imageFiles.length);
        final Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        final IntBuffer labelsBuffer = labels.createBuffer();
        int count = 0;
        for (File image : imageFiles) {
            final Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            final int label = Integer.parseInt(image.getName().split("-")[0]);
            images.put(count, img);
            labelsBuffer.put(count, label);
            count++;
        }
        final FisherFaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
        faceRecognizer.train(images, labels);
        final IntPointer label = new IntPointer(1);
        final DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(imageMat, label, confidence);
        final int predictedLabel = label.get(0);

        System.out.println("Predicted label: " + predictedLabel);

    }

}

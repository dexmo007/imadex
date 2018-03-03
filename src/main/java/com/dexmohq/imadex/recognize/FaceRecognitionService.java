package com.dexmohq.imadex.recognize;

import com.dexmohq.imadex.recognize.data.PersonalFaceRecognizer;
import com.dexmohq.imadex.recognize.data.PersonalFaceRecognizerRepository;
import com.dexmohq.imadex.recognize.data.StoredEigenFaceRecognizer;
import com.dexmohq.imadex.storage.StorageProperties;
import com.dexmohq.imadex.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_face.BasicFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

@Service
public class FaceRecognitionService {


    private final PersonalFaceRecognizerRepository personalFaceRecognizerRepository;
    private final StorageService storageService;

    @Value("classpath:haarcascade_frontalface_alt.xml")
    private Resource cascadeResource;
    private CvHaarClassifierCascade cascade;
    private static final int TRAIN_IMAGE_PIXELS = 200;
    private static final Size TRAIN_IMAGE_SIZE = new Size(TRAIN_IMAGE_PIXELS, TRAIN_IMAGE_PIXELS);

    @Autowired
    public FaceRecognitionService(PersonalFaceRecognizerRepository personalFaceRecognizerRepository, StorageService storageService) {
        this.personalFaceRecognizerRepository = personalFaceRecognizerRepository;
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() throws IOException {
        final String absolutePath = cascadeResource.getFile().getAbsolutePath();
        cascade = new CvHaarClassifierCascade(cvLoad(absolutePath));
    }

    public BasicFaceRecognizer getFaceRecognizer(String userId) {
        final PersonalFaceRecognizer personalFaceRecognizer = personalFaceRecognizerRepository.findByUserId(userId);
        final opencv_face.EigenFaceRecognizer faceRecognizer = opencv_face.EigenFaceRecognizer.create();
        if (personalFaceRecognizer == null) {
            return faceRecognizer;
        }
        final StoredEigenFaceRecognizer storedEigenFaceRecognizer = personalFaceRecognizer.getStoredEigenFaceRecognizer();
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        storageService.executeWithTempFile(file -> {
            try {
                mapper.writeValue(file, storedEigenFaceRecognizer);
                faceRecognizer.read(file.getAbsolutePath());
            } catch (IOException e) {
                throw new InternalError(e);
            }
        });
        return faceRecognizer;
    }

    public void saveFaceRecognizer(opencv_face.EigenFaceRecognizer faceRecognizer) {

    }

    private List<Mat> extractFaces(String imagePath) {

        final opencv_core.IplImage gray = cvLoadImage(imagePath, CV_LOAD_IMAGE_GRAYSCALE);
//            final IplImage small = IplImage.create(gray.width() / SCALE, gray.height() / SCALE, IPL_DEPTH_8U, 1);
//            cvResize(gray, small, CV_INTER_LINEAR);
//            cvEqualizeHist(small, small);
        final opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        final opencv_core.CvSeq faces = cvHaarDetectObjects(gray, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
        cvClearMemStorage(storage);
        final Mat whole = new Mat(gray);
        final List<Mat> faceList = new ArrayList<>(faces.total());
        for (int i = 0; i < faces.total(); i++) {
            final BytePointer face = cvGetSeqElem(faces, i);
            final opencv_core.Rect faceRect = new opencv_core.Rect(face);
            final Mat extracted = new Mat(whole, faceRect);
            final Mat resized = new Mat();
            resize(extracted, resized, TRAIN_IMAGE_SIZE);
            faceList.add(resized);
        }
        return faceList;
    }

    public FaceRecognizer train(String userId, String image, String name) throws IOException {
        if (!storageService.existsImage(userId, image)) {
            throw new IllegalArgumentException("Image does not exist");
        }
        final Resource resource = storageService.loadImage(userId, image);
        final BasicFaceRecognizer faceRecognizer = getFaceRecognizer(userId);
        final List<Mat> faces = extractFaces(resource.getFile().getAbsolutePath());
        if (faces.size() != 1) {
            throw new IllegalArgumentException("Detected multiple faces, but you provided only one label");
        }
        final Mat face = faces.get(0);
        final Mat labels = faceRecognizer.getLabels();
//        labels.createBuffer().
        final opencv_core.MatVector trainImage = new opencv_core.MatVector(1);
        final Mat newLabel = new Mat(1, 1, CV_32SC1);

//        faceRecognizer.update();
        return null;//todo wip
    }

}

package com.dexmohq.imadex.recognize.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.*;
import java.util.List;

@Getter
@Setter
@ToString
public class StoredEigenFaceRecognizer {

    @JsonProperty("opencv_eigenfaces")
    private EigenFaces eigenFaces;

    @Getter
    @Setter
    @ToString
    public static class EigenFaces {
        private double threshold;
        @JsonProperty("num_components")
        private int numComponents;
        private Matrix mean;
        private Matrix eigenvalues;
        private Matrix eigenvectors;
        private List<Matrix> projections;
        private Matrix labels;
        private List<LabelInfo> labelsInfo;
    }

    @Getter
    @Setter
    @ToString
    public static class Matrix {
        private int rows;
        private int cols;
        private String dt;
        private double[] data;
    }

    @Getter
    @Setter
    @ToString
    public  static class LabelInfo {
        private int label;
        @JsonProperty("value")
        private String info;
    }

    public static StoredEigenFaceRecognizer read(InputStream in) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.readLine();//skip "%YAML:1.0"
        return mapper.readValue(br, StoredEigenFaceRecognizer.class);
    }

}

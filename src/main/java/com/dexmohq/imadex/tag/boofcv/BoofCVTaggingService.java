package com.dexmohq.imadex.tag.boofcv;

import boofcv.abst.scene.ImageClassifier;
import boofcv.factory.scene.FactoryImageClassifier;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.Planar;
import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingProcessingException;
import com.dexmohq.imadex.tag.TaggingService;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
public class BoofCVTaggingService implements TaggingService {

    private static final String TRAINING_DATA_ROOT = "D:\\data\\imadex-boof\\";

    private static final String NIN_IMAGENET = TRAINING_DATA_ROOT + "nin_imagenet";
    private static final String VGG_CIFAR10 = TRAINING_DATA_ROOT + "likevgg_cifar10";

    private final ImageClassifier<Planar<GrayF32>> cifarClassifier;
    private final ImageClassifier<Planar<GrayF32>> ninImageNetClassifier;

    public BoofCVTaggingService() {
        cifarClassifier = FactoryImageClassifier.vgg_cifar10().getClassifier();
        ninImageNetClassifier = FactoryImageClassifier.nin_imagenet().getClassifier();
    }

    @PostConstruct
    public void init() throws IOException {
//        System.out.println(FileSizes.readableFileSize(Runtime.getRuntime().totalMemory()));
        cifarClassifier.loadModel(new File(VGG_CIFAR10));
        ninImageNetClassifier.loadModel(new File(NIN_IMAGENET));
//        System.out.println(FileSizes.readableFileSize(Runtime.getRuntime().totalMemory()));
    }

    @Override
    public String getSource() {
        return "boof";
    }

    private static final double CIFAR_THRESHOLD = 0;
    private static final double NIN_IMAGENET_THRESHOLD = 5;

    @Override
    public Stream<? extends Tag> extractTags(Resource image) throws IOException, TaggingProcessingException {
        final BufferedImage buffered = ImageIO.read(image.getInputStream());
        final Planar<GrayF32> planar = new Planar<>(GrayF32.class, buffered.getWidth(), buffered.getHeight(), 3);
        ConvertBufferedImage.convertFromMulti(buffered, planar, true, GrayF32.class);
        final List<ImageClassifier.Score> ninResults;
        synchronized (ninImageNetClassifier) {
            ninImageNetClassifier.classify(planar);
            ninResults = ninImageNetClassifier.getAllResults();
        }
        final List<ImageClassifier.Score> rawCifarResults;
        synchronized (cifarClassifier) {
            cifarClassifier.classify(planar);
            rawCifarResults = cifarClassifier.getAllResults();
        }
        final List<String> categories = ninImageNetClassifier.getCategories();
        final List<String> cifar10Categories = cifarClassifier.getCategories();
        final Map<String, Double> cifarResults = enrichResults(cifar10Categories, rawCifarResults);

        return ninResults.stream()
                .filter(s -> {
                    final String category = categories.get(s.category);
                    return s.score > NIN_IMAGENET_THRESHOLD &&
                            (!cifarResults.containsKey(category) || cifarResults.get(category) > CIFAR_THRESHOLD);
                })
                .flatMap(s -> Arrays.stream(categories.get(s.category).split(","))
                        .map(c -> new BoofTag(c.trim(), ((float) s.score))));
    }

    @Data
    private static class Result {
        private final String tag;
        private final double score;
    }

    private static Map<String, Double> enrichResults(List<String> categories, List<ImageClassifier.Score> results) {
        return results.stream()
                .collect(toMap(s -> categories.get(s.category), s -> s.score));
    }

    @Override
    public CompletableFuture<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return extractTags(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TaggingProcessingException e) {
                // is never thrown
                return null;
            }
        });
    }

    public static void main(String[] args) throws IOException, TaggingProcessingException {
        final BoofCVTaggingService service = new BoofCVTaggingService();
        service.init();
        service.extractTags(new UrlResource("file:\\D:\\data\\train.jpg")).forEach(System.out::println);
        System.out.println("==========================");
        service.extractTags(new UrlResource("file:\\D:\\data\\tree.jpg")).forEach(System.out::println);
    }
}

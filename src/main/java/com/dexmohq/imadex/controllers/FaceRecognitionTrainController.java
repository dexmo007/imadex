package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.recognize.data.PersonalFaceRecognizerRepository;
import com.dexmohq.imadex.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.dexmohq.imadex.auth.IdTokenEnhancer.getUserId;

@RestController
@RequestMapping("/face")
public class FaceRecognitionTrainController {

    private final PersonalFaceRecognizerRepository personalFaceRecognizerRepository;
    @Autowired
    private StorageService storageService;


    @Autowired
    public FaceRecognitionTrainController(PersonalFaceRecognizerRepository personalFaceRecognizerRepository) {
        this.personalFaceRecognizerRepository = personalFaceRecognizerRepository;
    }

    @PostMapping
    public ResponseEntity<?> train(String image,
                                   String labelInfo,
                                   OAuth2Authentication authentication) {
        final String userId = getUserId(authentication);
        final Resource resource;
        try {
           resource = storageService.loadImage(userId, image);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
//        final String imagePath = resource.getFile().getAbsolutePath();
        return null;//todo wip
    }

}

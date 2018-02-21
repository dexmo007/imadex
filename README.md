# imadex
Smart image gallery

# Prerequisites
Under resources/security
Generate AES 256 Key for password encryption
```
keytool -genseckey -alias jceksaes -keyalg aes 
-keysize 256 -keystore aes-keystore.jceks -storetype jceks
```
Generate RSA keypair for JWT signing and verification
```
keytool -genkeypair -alias jwt 
                    -keyalg RSA 
                    -keypass \<see properties\> 
                    -keystore jwt-keystore.jks 
                    -storepass \<see properties\>
```
extract public key
``	
keytool -list -rfc --keystore mytest.jks | openssl x509 -inform pem -pubkey``

Add resources/tagging.properties:
```
## Azure secrets
azure.cognitive.tag-base-url=<your url>
azure.cognitive.face-subscription-key=<your key>
azure.cognitive.vision-subscription-key=<your key>
azure.cognitive.emotion-subscription-key=<your key>

## Clarifai API key
clarifai.api-key=<your key>

## IBM Watson API key
ibm.watson.visual-recognition.api-key=<your key>
```

Add service credentials for Google Cloud Vision as:
resources/Imadex-1bbf90f0848d.json
//TODO file name in tagging.properties
# Chuck Norris International Application

### Abstract

This is a showcase application written in plain Java using Spring-Boot. 

The app has one http endpoint and relies on two other services. The first one provides random Chuck Norris jokes (https://api.chucknorris.io/jokes/random). The second one is a translation service (hosted as local docker container https://github.com/LibreTranslate/LibreTranslate).

### Prerequisites and usage

1. Clone this repo
2. Run or build the [LibreTranslate](https://github.com/LibreTranslate/LibreTranslate) Docker image. The image will download a set of language models for translation during startup. This could take some time. If the startup fails you can build the LibreTranslate Docker image locally with a reduced set of language models.
    
    ```bash
    docker run -ti --rm -p 5000:5000 libretranslate/libretranslate
    ```
    docker image with reduced language models (here de,en,es)
    ```bash
    git clone git@github.com:LibreTranslate/LibreTranslate.git
    cd LibreTranslate
    docker build --build-arg with_models=true --build-arg  models=de,en,es -t libretranslate .
    docker run -it -p 5000:5000 libretranslate
    ```
4. Start the application
    ```bash
    ./gradlew bootRun
    ```
   
5. Tell me something about Chuck Norris ...

   ```http request
   GET localhost:8080/aboutchuck
   HTTP/1.1 200
   Content-Type: application/json  

   {
      "value": "Chuck Norris can surf the net on his abacus"
   }
   ```

   ```http request
   GET localhost:8080/aboutchuck?lang=de
   HTTP/1.1 200
   Content-Type: application/json

   {
     "value": "Chuck Norris kann um Ecken sehen."
   }
   ```

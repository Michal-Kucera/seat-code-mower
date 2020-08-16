# seat-code-mower
> This repository contains code for solving the technical challenge for SEAT:CODE's Backend Developer position.
> Please check out [this document](SEAT_CODE Backend Code Challenge.pdf) for more info.

## My thoughts on the challenge
I found out that there is a couple of operations needed to accomplish our goal:
- Create a plateau in a specific size
- Create a mower in a plateau
- Send instructions to a mower to move around a plateau
- Get a final destination of a mower
- Do it over again with another mower

As a Spring boot fun, my first thought went to "how this challenge could be solved with using Spring and maybe 
a fancy REST API" thus I transformed the requirements into an Open API specification, which is available [here](etc/open-api/specs/mower-api.yml).

Once I had an API specification ready, I used [OpenAPI Generator Gradle Plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
to generate all the Java stubs around (`generateOpenApiDataModel` Gradle task in [build.gradle](build.gradle) file is responsible for generating API data model in [build/generated/sources/open-api/java](build/generated/sources/open-api/java) directory).

## Run instructions
> For running this application we will use Gradle in version `6.6` and its wrapper.

1. First we need to package our application into a JAR executable file, therefore we need to run the following command:
    ```
    ./gradlew build
    ```

2. After that, we can easily run the application on port `8080` with the following command:
    ```
    ./gradlew bootRun
    ```

3. To stop the application you can use this command:
    ```
    ./gradlew -stop
    ```

## Code formatting
> While developing this application, I used IntelliJ IDEA and its useful plugin called [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions).
>
> This plugin helps us keep the code clean and consistent by performing automatic code formatting, 
> according to the rules defined in [saveactions_settings.xml](.idea/saveactions_settings.xml) file.
> 
> To use benefits of this plugin you have to install `Save Actions` plugin from `Marketplace` 
> and restart your IDE to load the plugin's configuration.

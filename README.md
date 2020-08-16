# seat-code-mower
> This repository contains code for solving the technical challenge for SEAT:CODE's Backend Developer position. Please check out [this document](SEAT_CODE Backend Code Challenge.pdf) for more info.

## Run instructions
1. First we need to package our application into a JAR executable file, therefore we need to run the following command:
    ```
    ./gradlew build
    ```

2. After that, we can easily run the application on port `8080` with the following command:
    ```
    ./gradlew bootRun
    ```

## Code formatting
> While developing this application, I used IntelliJ IDEA and its useful plugin called [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions).
>
> This plugin helps us keep the code clean and consistent by performing automatic code formatting, according to the rules defined in [saveactions_settings.xml](.idea/saveactions_settings.xml) file.
> 
> To use benefits of this plugin you have to install `Save Actions` plugin from `Marketplace` and restart your IDE to load the plugin's configuration.

# seat-code-mower
This repository contains code for solving the technical challenge for SEAT:CODE's Backend Developer position.
Please check out [this document](SEAT_CODE Backend Code Challenge.pdf) for more info.

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

As a next step I defined the data mode we want wo sture under a database and then I started implementing 
the business logic for each endpoint, while I tried to benefit from TDD methodology.

On the top of our application I put [Open API UI](https://github.com/springfox/springfox) to provide a user-friendly UI for testing our endpoints.

## Database
As we want to store states of plateaus as well as mowers between each endpoint's execution, it's been decided to use
an H2 in-memory database for this purpose. This decision keeps our options open and enables us to make the non-in-memory
DB choice down the road (e.g. if we would like to either recover states 
after the application is restarted or have a historical data for further analyses).

### Database data model

#### `plateau` table
> This table is used for storing plateau's related data.

| Column name       | Column Java data-type | Column purpose                                            |
|-------------------|-----------------------|-----------------------------------------------------------|
| id                | UUID                  | Unique ID of plateau                                      |
| created_date_time | LocalDateTime         | Timestamp recording the creation date and time of plateau |
| name              | String                | Name of plateau                                           |
| length            | Integer               | Length of plateau                                         |
| width             | Integer               | Width of plateau                                          |

#### `mower` table
> This table is used for storing mower's related data.

| Column name       | Column Java data-type | Column purpose                                          |
|-------------------|-----------------------|---------------------------------------------------------|
| id                | UUID                  | Unique ID of mower                                      |
| created_date_time | LocalDateTime         | Timestamp recording the creation date and time of mower |
| name              | String                | Name of mower                                           |
| plateau_id        | UUID                  | ID of plateau, where mower is operating                 |

#### `mower_position` table
> This table is used for storing mower position's related data. Basically we do store here every single one move of
> a mower around the plateau so at the end we can collect all moves, order them by date and track 
> the mower's path down.

| Column name       | Column Java data-type | Column purpose                                                   |
|-------------------|-----------------------|------------------------------------------------------------------|
| id                | UUID                  | Unique ID of mower position                                      |
| created_date_time | LocalDateTime         | Timestamp recording the creation date and time of mower position |
| mower_id          | UUID                  | ID of mower this position belongs to                             |
| latitude          | Integer               | Latitude of mower at plateau                                     |
| longitude         | Integer               | Longitude of mower at plateau                                    |
| orientation       | MowerOrientation      | Mower's orientation (NORTH, SOUTH, EAST, WEST)                   |

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

## Testing endpoints via Open API UI
Once the application is up and running, we can access the Open API UI [here](http://localhost:8080/swagger-ui/index.html) 
and test our endpoints in a super easy way.

## Code formatting
While developing this application, I used IntelliJ IDEA and its useful plugin called [Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions).

This plugin helps us keep the code clean and consistent by performing automatic code formatting, 
according to the rules defined in [saveactions_settings.xml](.idea/saveactions_settings.xml) file.

To use benefits of this plugin you have to install `Save Actions` plugin from `Marketplace` 
and restart your IDE to load the plugin's configuration.

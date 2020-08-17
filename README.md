# 1 - Seat: Code Mower
This repository contains code for solving the technical challenge for SEAT:CODE's Backend Developer position.
Please check out [this document](SEAT_CODE Backend Code Challenge.pdf) for more info.

# 2 - My thoughts on the challenge
While analyzing the challenge I found out that there is a couple of operations needed to accomplish the goal:
- Create a plateau in a specific size (length x width)
- Create a mower in a plateau under a specific location (longitude, latitude) and a specific orientation (N, E, S, W)
- Send instructions to a mower to move around a plateau (L, R, M)
- Get a final destination of a mower
- Do it over again with another mower

As a Spring boot fun, my first thought went to "how this challenge could be solved with using Spring and maybe 
a fancy REST API around" thus I transformed the requirements into an Open API specification,
which is available [here](etc/open-api/specs/mower-api.yml).

Once I had an API specification ready, I used 
[OpenAPI Generator Gradle Plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin)
to generate all the Java stubs (`generateOpenApiDataModel` Gradle task in [build.gradle](build.gradle) file 
is responsible for generating API data model in
[build/generated/sources/open-api/java](build/generated/sources/open-api/java) directory).

As a next step I defined the data model we want to store under a database (check out [Database](#4---database) section)
and then I started implementing the business logic for each endpoint (check out [Business validation](#5---api-business-validations) section
for more info about business logic validations), while I tried to benefit from TDD methodology.

On the top of the application I put [Open API UI](https://github.com/springfox/springfox) to provide 
a user-friendly UI for testing our endpoints (check out [Testing endpoints via Open API UI](#61---via-open-api-ui) section).

There are also a couple of other things I'd like to implement, but in terms of the scope of this challenge,
I'd rather only mention them as new features:
- Improve error handling and return nicer error messages to the user (I just keep it simple for the moment)
- Secure REST API (e.g. JWT, user access groups for each API operation)
- Store data such as creation date, created by, modification date, modified by in each entity to have a track
    of who changed/created what and when (e.g. Spring Data Envers)
- Store history of mowers' positions to have a track of all mower's moves (e.g. Spring Data Envers)
- Extend REST API by HATEOAS (e.g. Spring HATEOAS)
- Configure Open API UI properly (ATM it's just a default configuration which is not the best one yet)
- Non-in-memory database + DB schema manager (E.g. Liquibase/Flyway) so we could keep data also after restarting the app

# 3 - Run instructions
> For running this application we use Gradle in version `6.6` and its wrapper.

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

# 4 - Database
As we want to store states of plateaus as well as mowers between each endpoint's execution, it's been decided to use
an H2 in-memory database for this purpose. This decision keeps our options open and enables us to make the non-in-memory
DB choice down the road (e.g. if we would like to either recover states after the application is restarted 
or have a historical data for further analyses).

## 4.1 - Database data model
> All tables are extending the base entity, which is defined as following:

| Column name       | Column Java data-type | Column purpose           |
|-------------------|-----------------------|--------------------------|
| id                | UUID                  | Unique ID of entity      |
| version           | Integer               | Version of entity record |

> `version` column provides us a support for Optimistic Locking in JPA, so if there was a conflict detected 
> by the JPA provider, it would throw a `javax.persistence.OptimisticLockException`.

### 4.1.1 - `plateau` table
> This table holds plateau's related data.

| Column name       | Column Java data-type | Column purpose    |
|-------------------|-----------------------|-------------------|
| name              | String                | Name of plateau   |
| length            | Integer               | Length of plateau |
| width             | Integer               | Width of plateau  |

### 4.1.2 - `mower` table
> This table contains mower's related data.

| Column name       | Column Java data-type | Column purpose                                 |
|-------------------|-----------------------|------------------------------------------------|
| name              | String                | Name of mower                                  |
| plateau_id        | UUID                  | ID of plateau, where mower is operating        |
| latitude          | Integer               | Latitude of mower at plateau                   |
| longitude         | Integer               | Longitude of mower at plateau                  |
| orientation       | MowerOrientation      | Mower's orientation (NORTH, SOUTH, EAST, WEST) |

# 5 - API business validations
## 5.1 - Plateau
- [X] Returns `Bad Request` when trying to create a plateau with invalid data (e.g. size is less than 1x1,
    name length is less than 1 character)
- [x] Returns `Bad Request` when trying to access a plateau which doesn't exist

## 5.2 - Mower
- [X] Returns `Bad Request` when trying to create a mower with invalid data (e.g. starting position is not filled up, 
    starting position latitude or longitude is less than 1)
- [x] Returns `Bad Request` when trying to create a mower for a non-existing plateau ID
- [x] Returns `Bad Request` when trying to create a mower which has a starting position out of range of plateau's size
- [x] Returns `Bad Request` when trying to create a mower which has the exactly same position as an already existing 
    mower at targeting plateau
- [x] Returns `Bad Request` when trying to access a mower which doesn't exist or is not associated with
    a targeting plateau

## 5.3 - Mower instruction
- [x] Returns `Bad Request` when trying to move a mower which doesn't exist or is not associated with
    a targeting plateau
- [x] Returns `Bad Request` when trying to move a mower out of a range of plateau's size
- [x] Returns `Bad Request` when trying to move a mower to an exactly same position as another mower
    has already taken at targeting plateau

# 6 - Testing endpoints
The way how API is designed allows us to focus on simple actions and first create a plateau and get a `Location`
header from successful response (this header contains ID of created plateau):
```
Location: http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3
```

Next step is to either get an info about a created plateau or create a mower for this plateau,
while once again we will get back a response with a location header of created mower
(this header contains ID of plateau as well as mower):
```
Location: http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3/mowers/a7f6b79e-d6c5-4228-a35e-7c70e78f2bbc
```

Once we have our mower in place, we can send instruction to move mover around plateau or get a final destination
info for this mower.

There are also some business validations which are not allowing us perform some mess ups,
please check out [API business validations](#5---api-business-validations) section for more info.

## 6.1 - Via Open API UI
Once the application is up and running, we can access the Open API UI [here](http://localhost:8080/swagger-ui/index.html) 
and test our endpoints in a super easy way.

## 6.2 - Sample testing with data from first test case scenario in attached PDF
### 6.2.1 - Creating a plateau in `5x5` size:
- request:
    ```
    $ curl --request POST \
        --url http://localhost:8080/v1/plateaus \
        --header 'accept: application/json' \
        --header 'Content-Type: application/json' \
        --verbose \
        --data '{
                  "name": "SEAT Martorell Factory",
                  "size": {
                    "length": 5,
                    "width": 5
                  }
                }'
    ```

- response with `Location` header:
    ```
    HTTP/1.1 201
    Location: http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3
    ```

### 6.2.2 - Create a mower for this plateau in location `1, 2, N`:
- request:
    ```
    $ curl --request POST \
        --url http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3/mowers \
        --header 'accept: application/json' \
        --header 'Content-Type: application/json' \
        --verbose \
        --data '{
                  "name": "Mower Nr. 1",
                  "position": {
                    "longitude": 1,
                    "latitude": 2,
                    "orientation": "N"
                  }
                }'
    ``` 

- response with `Location` header:
    ```
    HTTP/1.1 201
    Location: http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3/mowers/a7f6b79e-d6c5-4228-a35e-7c70e78f2bbc
    ```

### 6.2.3 - Send instructions `LMLMLMLMM` to mower:
- request:
    ```
    $ curl --request POST \
        --url http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3/mowers/a7f6b79e-d6c5-4228-a35e-7c70e78f2bbc/instructions \
        --header 'accept: application/json' \
        --header 'Content-Type: application/json' \
        --verbose \
        --data '[
                  "L",
                  "M",
                  "L",
                  "M",
                  "L",
                  "M",
                  "L",
                  "M",
                  "M"
                ]'
    ```
- response:
    ```
    HTTP/1.1 204
    ```

### 6.2.4 - Get information about a final position of mower:
- request:
    ```
    $ curl --request GET \
        --url http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3/mowers/a7f6b79e-d6c5-4228-a35e-7c70e78f2bbc \
        --header 'accept: application/json' \
        --verbose
    ```

- response (we can see mower's final position is `1, 3, N`, just like is expected from our test case):
    ```json
    {
      "name" : "Mower Nr. 1",
      "position" : {
        "longitude" : 1,
        "latitude" : 3,
        "orientation" : "N"
      }
    }
    ```

### 6.2.5 - Get information about plateau:
- request:
    ```
    $ curl --request GET \
        --url http://localhost:8080/v1/plateaus/e640bcc9-280e-4924-a147-202493a1a4f3 \
        --header 'accept: application/json' \
        --verbose
    ```

- response (we can see that there is only one mower at this plateau - that one we recently created though):
    ```json
    {
      "name" : "SEAT Martorell Factory",
      "size" : {
        "length" : 5,
        "width" : 5
      },
      "mowers" : [
        "a7f6b79e-d6c5-4228-a35e-7c70e78f2bbc"
      ]
    }
    ```

# 7 - Code formatting
While developing this application, I used IntelliJ IDEA and its useful plugin called 
[Save Actions](https://plugins.jetbrains.com/plugin/7642-save-actions).

This plugin helps us keep the code clean and consistent by performing automatic code formatting, 
according to the rules defined in [saveactions_settings.xml](.idea/saveactions_settings.xml) file.

To use benefits of this plugin you have to install `Save Actions` plugin from `Marketplace` 
and restart your IDE to load the plugin's configuration.

# 8 - Contact
- [Email - Michal Kucera](mailto:karolkokucera@gmail.com)
- [LinkedIn](https://www.linkedin.com/in/michal-kucera)

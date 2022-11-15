# Scaffold
This project is setup as a multi-module maven project containing maven modules for the microservice application 
& the http client. DTOs are kept in http client so that it can be shared with both the microservice and the http client.  

Modules
* service - the microservice application
* client - the http client

## Local setup
### Setup access to [tracc-maven-repo](https://github.com/Competitive-Capabilities-International/tracc-maven-repo)
* Request access from Devops to [tracc-maven-repo](https://github.com/Competitive-Capabilities-International/tracc-maven-repo)
* [Create a personal access token on github](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
  and save the token somewhere safe like in your keeper account. When selecting permissions, ensure you have ticked the permission
  "read:packages" to allow you to download packages from GitHub Package Registry
* Add your personal access token to your maven settings.xml. if you are using the embedded maven in Intellij,
  this will be found at ${IntelliJ-Home}/plugins/maven/lib/maven3/conf/settings.xml. Add below config with your details.
```xml
    <servers>
        <server>
            <id>github</id>
            <username>your-github-username@ccint.net</username>
            <password>your-personal-access-token</password>
        </server>
    </servers>
```
With the above in place, you will be able to download artifacts
from the [tracc-maven-repo](https://github.com/Competitive-Capabilities-International/tracc-maven-repo)
### Run the project
* Checkout the project into your favorite IDE
* Install [JDK 17](https://www.oracle.com/java/technologies/downloads/#jdk17-windows)
* Install [Docker](https://www.docker.com/products/docker-desktop)
* Run required docker containers (e.g. postgres & redis). Open "scaffold/docker" directory in a terminal & run the below
```shell
docker-compose up -d
```
if you are recreating the containers run
```shell
docker compose up -d --force-recreate --renew-anon-volumes
```

* [Optional] Install [RedisInsight](https://redislabs.com/redis-enterprise/redis-insight/) 

Add properties the application requires AWS Parameter store
- confirm that you can connect to the parameter store of the dev aws account.
```shell
aws ssm describe-parameters
```
- update all parameter values to the ones required for the application. See FAQ "How do I add a new application property ?"
for how to guide.

Run the project from your IDE or Maven
```shell
./mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
```

Note: Docker needs to be installed for the integration tests to run successfully

To expose your service to the frontend, add a route into the [apigateway](https://github.com/Competitive-Capabilities-International/apigateway)
and send for review to someone from devops.

## FAQ
### Where should set up instructions for a microservice be documented ?
Add setup instructions into the readme.md of the repo. Please do not include images there. 
If you need to include screenshots, try to link to the page that contains a screenshot of what you're 
talking about. e.g. if you're putting down a process for changing a intellij config, 
there should be an existing guide that shows us that. If you absolutely require a guide that contains images,
you can capture it on confluence and link to that page which has the images.
This is a last resort though. Let's try to keep the readme.mds concise. 
Images get outdated quickly so try to avoid.


### How do I version my API ?
We do not version our APIs. There is no need to version APIs at this time as it complicates maintenance 
& our APIs are not consumed publicly.

Design considerations before attempting versioning :
* Do we run multiple containers one for each version of the application
* How does it impact source control (separate branch per version)
* How does it impact the database - e.g. say you rename a table's column in v2, 
  how do you ensure V1 still works using the previous column 

Further information can be found at
* [Zalando guidelines - SHOULD avoid versioning](https://opensource.zalando.com/restful-api-guidelines/#113)
* [Use versioning only as a last resort](https://martinfowler.com/articles/enterpriseREST.html)


### I've created some new endpoints. How do I share my postman collection with the team ?
* create a new workspace in our [teams postman account](https://ccintech.postman.co/home) for your subproduct if one is not there already (e.g. LSW)
* add postman calls there to an existing collection or create a new collection 

### [In IntelliJ, how do I debug a maven test goal?](https://stackoverflow.com/questions/3784781/in-intellij-how-do-i-debug-a-maven-test-goal)
Add -DforkCount=0 to your vm arguments

### Why is a [shared database instance](https://www.baeldung.com/spring-boot-testcontainers-integration-test#common-configuration) used for the integration tests ? 
When running tests using the @SpringBootTest annotation, a single Spring Application context is created. 
This Hikari Datasource is initialized based on system properties set by PostgreSQLExtension when a unit test is run.
So if you have 2 unit test classes that both depend on TestContainerExtension running,when the first unit test runs, it sets the system properties for the db connection.
When the second unit test runs, even if it creates a new test container and overwrites the system properties for jdbc url,
it has no effect and any DB connection made via spring data repositories will continue to connect to the initial database.
This is because the application context has already been initialized.

### What docker entrypoint is generated by buildpacks for my image ?
run this and replace the docker image tag with the one you interested in
```shell
docker inspect -f '{{.Config.Entrypoint}}' tracc-platform/scaffold:0.0.1-SNAPSHOT
```

### Shouldn't random port (0) be used for running the microservice with the local spring boot profile?
you can do this to avoid port conflicts when running on localhost, 
but each time that port number changes, you would need to change the corresponding postman collection.
For this reason, we specify a port, keeping in mind which ports are already in use for the local profile to avoid port conflicts.
See FAQ "How do I choose a port number to use for the local spring boot profile?" for instructions on how to choose a port. 

### How do I choose a port number to use for the local spring boot profile?
Check which port numbers are taken on the confluence page and pick something else.
This will ensure that developers can clone multiple repos and not need to worry about port conflicts
when running multiple microservices. see [repo to in container port mapping](https://cci-products.atlassian.net/wiki/spaces/CCiAR/pages/1553039375/repo+to+in+container+port+mapping)

### How do I build the docker image ? 
Ensure Docker is running, then run the below command 
```shell
mvn clean package
```
### How do I run this application inside a docker container ?
Note: The container may not be able to see your postgres host though 
```shell
docker run -e spring_profiles_active=local tracc-platform-scaffold:0.0.1-SNAPSHOT
```
### How do I add a new application property ?
#### Steps involved
Application properties must be added to each AWS Parameter Store per environment.
The aws ssm commands to insert the properties are inside a file per spring profile under the directory ".devops/aws-parameter-store/spring-profile".
To add a property, open up the shell script for the spring profile you want to add the property for.

| Spring Profile | File containing commands for spring profile                     |
|----------------|:----------------------------------------------------------------|
| default        | .devops/aws-parameter-store/spring-profile/application.sh       |
| local          | .devops/aws-parameter-store/spring-profile/application-local.sh |
| dev            | .devops/aws-parameter-store/spring-profile/application-dev.sh   |
| stage          | .devops/aws-parameter-store/spring-profile/application-stage.sh |
| prod           | .devops/aws-parameter-store/spring-profile/application-prod.sh  |

Once you have a file open, add a line for the microservice & spring profile. 
* To add the property named spring.application.name, for default spring profile , with value scaffold add a line like this
```shell
aws ssm put-parameter --name "/config/scaffold/spring.application.name" --value "scaffold" --type "SecureString" --description "application name" --overwrite
```
* To add the property named spring.application.name, for dev spring profile , with value scaffold add a line like this  
```shell
aws ssm put-parameter --name "/config/scaffold_dev/spring.application.name" --value "scaffold" --type "SecureString" --description "application name" --overwrite
```

If the value is a password, reference an environment variable for the value like so.
see $DB_PASSWORD below
```shell
aws ssm put-parameter --name "/config/scaffold_dev/spring.datasource.password" --value "$DB_PASSWORD" --type "SecureString" --description "JDBC URL of the database" --overwrite
```

Avoid $ in field values.
If you must use it, ensure you escape it. See [Escaping Characters in Bash](https://www.baeldung.com/linux/bash-escape-characters).
AWS CLI will ignore values which have a "$" in them, starting from the $ sign until the end.
The generator.xlsx mentioned in "Want to generate the ssm commands for a property ?" section has a formula which will escape "$" for you.

For each environment variable you reference in your param store scripts, you will need a secret added per GitHub environment.
To do this :
* First capture the secret name and value in [Keeper](https://keepersecurity.com/vault/#) as once you capture in GitHub,
you can never view it again.
* [Create a GitHub Environment if one does not exist](https://docs.github.com/en/actions/deployment/targeting-different-environments/using-environments-for-deployment#creating-an-environment). 
You need one per environment the app will be deployed to.
Environment names we currently use are Dev, Production & Stage.
* To add the secret, Under the environment secrets, click "Add Secret"
** Enter the secret name as the environment variable name you defined in the param store script (application*.sh) 
** Enter value for secret
* Open each [GitHub Action](https://github.com/features/actions) script (ecr-push*.yml) in the directory .github/workflows and
** under the job named "Update AWS Parameter Store Config",add a reference to your new GitHub environment secret. 
e.g. below we added DB_PASSWORD
```yaml
    - name: Update AWS Parameter Store Config
      env:
        DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
      run: |
        cd .devops/aws-parameter-store
        chmod -R +x ./
        ./aws-dev-account-params.sh
```
confirm that the correct aws account script is referenced for your environment.

| Environment | GitHub Action yaml | AWS Account Param Store script |
|-------------|:-------------------|:-------------------------------|
| Dev         | ecr-push.yml       | aws-dev-account-params.sh      |
| Stage       | ecr-push-stage.yml | aws-stage-account-params.sh    |
| Production  | ecr-push-prod.yml  | aws-prod-account-params.sh     |
 
Once you merge your code into master branch, the GitHub action will trigger.
You can view that in the Actions tab of your git repo.
If not, contact DevOps to assist.

#### Property name convention
The parameter store keys are named using the default naming convention as per the [official aws spring docs page](https://docs.awspring.io/spring-cloud-aws/docs/2.3.2/reference/html/index.html#integrating-your-spring-cloud-application-with-the-aws-parameter-store).
Examples are below.

| AWS Parameter store key                      | Spring property         | Description                                                           |
|----------------------------------------------|:------------------------|:----------------------------------------------------------------------|
| /config/scaffold/spring.application.name     | spring.application.name | Default spring profile property that is shared by all spring profiles |
| /config/scaffold_dev/spring.application.name | spring.application.name | Dev spring profile property. overrides default profile property       |

The default set of application properties required to run cci microservice are included in this project already.
you will find them under the directory ".devops/aws-parameter-store/spring-profile".
There should be no need to delete any of these, unless your microservice is not talking to a database 
or does not use a feature scaffold provides. 
you should only need to update the application name from scaffold to your microservices name.

#### Want to generate the ssm commands for a property ?
Open .devops/aws-parameter-store/generator.xlsx & fill in values for these fields
- spring application name
- spring profile
- spring property name
- property value
- parameter description

copy & paste the existing formula from these columns for all new rows you add & the corresponding commands will be
generated :
- AWS Parameter Store Key
- put-parameter command
- Environments to add config to

The corresponding aws ssm command will be generated, along with which environment to add the parameter into, 
in the "escaped put-parameter command" column

Copy the entries for the required spring profiles into the script indicated below

| Spring Profile | Script to copy lines into                                       |
|----------------|:----------------------------------------------------------------|
| default        | .devops/aws-parameter-store/spring-profile/application.sh       |
| local          | .devops/aws-parameter-store/spring-profile/application-local.sh |
| dev            | .devops/aws-parameter-store/spring-profile/application-dev.sh   |
| stage          | .devops/aws-parameter-store/spring-profile/application-stage.sh |
| prod           | .devops/aws-parameter-store/spring-profile/application-prod.sh  |


Each script will execute the spring profile specific scripts which are relevant for the given AWS account.
e.g. in Dev, default ,local and dev profile values are added. In other environments default 
and the environment's parameter file is referenced.


| AWS Account | Script to run                                           |
|-------------|:--------------------------------------------------------|
| Dev         | .devops/aws-parameter-store/aws-dev-account-params.sh   |
| Stage       | .devops/aws-parameter-store/aws-stage-account-params.sh |
| Prod        | .devops/aws-parameter-store/aws-prod-account-params.sh  |



#### Referencing a property in java code
Please add getters for properties in ApplicationProperties.java and inject this into any component you need.
DO NOT use @Value to reference application properties from  Spring Beans that have the default scope (Singleton / Refresh) 
as the Spring Actuator refresh endpoint will not refresh bean properties and will only refresh what is inside Environment.
Examples and further description can be found inside the class.
Reference Environment always so that when an actuator refresh is performed, the latest value will available.

#### Refreshing properties
The application properties may be refreshed by calling the /scaffold-api/actuator/refresh endpoint 

#### View existing properties for a spring application
```shell
aws ssm get-parameters-by-path --path "/config/scaffold" --recursive
```
#### View all properties
```shell
aws ssm describe-parameters
```
Also see [describe parameters](https://docs.aws.amazon.com/cli/latest/reference/ssm/describe-parameters.html)

### How do I install AWS CLI on a WSL2 distro ?
Open Linux terminal and run  

```shell
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
sudo apt install unzip
unzip awscliv2.zip
sudo ./aws/install

```
Also see this guide on [Installing AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)

To view file from Windows Explorer, enter \\wsl$ in your explorer's address bar.

### My local application server cannot connect to the AWS Parameter Store ?
if the log message you are seeing is logged at the WARN level, the app may just be informing you that it is making its way through the credential provider chain.
It will eventually look up your .aws/credentials to connect

See [AWS credentials provider chain](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html)
If it is an ERROR level log entry, that looks something like this "Unable to load config data from 'aws-parameterstore:'"
try running this port knock to see if it makes any difference
```shell
aws ec2-instance-connect send-ssh-public-key --region eu-west-1 --instance-id i-07eb3d02756fa02ae --availability-zone eu-west-1b --instance-os-user ec2-user --ssh-public-key file://%USERPROFILE%/.ssh/id_rsa.pub
```

Confirm if you can see any parameters in AWS Parameter store
```shell
aws ssm describe-parameters
```

### How do I refresh the cached application properties from AWS Parameter Store?

POST http://localhost:8881/scaffold-api/actuator/refresh

### Where is the health check endpoint? 
http://localhost:8881/scaffold-api/actuator/health

### What is the naming convention for the context path ?
/{service domain}-api/, so for Scaffold it would be /scaffold-api

### What is the naming convention for resource urls ?
/{domain-model}-api/{resource}

e.g. To fetch Scaffold for an area GET /scaffold-api/scaffolds?area-guid={area-guid}

### Test cases are failing when trying to initialize a Test container?
Ensure Docker is running

### How do I do validate request body data for my web service and add exception handling ?
See Validating a Request Body section in [Spring Boot Validation](https://reflectoring.io/bean-validation-with-spring-boot/#validating-a-request-body)
and [Custom Error Message Handling for REST API](https://www.baeldung.com/global-error-handler-in-a-spring-rest-api).
This approach has been used in this project already.  

### How do I confirm the application is available after deploy?
#### Test an authorized call
Fill in basic auth username & password

GET http://localhost:8881/scaffold-api/actuator

#### Test an unauthorized call
GET http://localhost:8881/scaffold-api/actuator/health

#### Where do I document my API ?
Document in our postman team workspace using [Postman](https://learning.postman.com/docs/publishing-your-api/documenting-your-api)

### How do I create the DB application user that my microservice can use ?

DevOps can help with executing the commands to create a user for you using the SQL in these files .devops/postgres/database-setup.sql

Ensure the credentials for this application user is added to [Keeper](https://keepersecurity.com/vault/#)

The above script will be stored for the time being in this repo under .devops/postgres/database-setup.sql

### How do I generate the DDL for JPA Entities ?

Add the below application properties. DO NOT COMMIT THIS CHANGE. USE IT ONLY FOR LOCAL TESTING.

```properties
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=src/main/resources/db/changelog/generated-ddl.sql
spring.jpa.properties.hibernate.format_sql=true
```

### How do I auto update my local DB from my JPA Entities ?
Add the below application properties. DO NOT COMMIT THIS CHANGE. USE IT ONLY FOR LOCAL TESTING.
```properties
spring.jpa.hibernate.ddl-auto = update
```
Note, using the options to create the DDL file will disable the schema auto update, 
so you can either generate the DDL file or auto update the DB schema but never both at the same time. 

### How do I use the http client supplied with this project ?
* The service using the http client must be another spring project based off the Scaffold project 
or having the required dependencies from it.
* Add the client artifact as a maven dependency to the project that needs to call this service 
* Add a property to aws parameter store for the url referenced by the feign http client. 
  e.g. in this project, you would add a property named scaffold.api.baseUrl.
  You probably need to add one aws parameter store property per environment.

```java
/**
 *  the url property must be supplied by the service that uses this http client
 */
@FeignClient(name ="scaffold-api-client", url = "${scaffold.api.baseUrl}")
public interface ScaffoldAPIClient {
    @GetMapping(value = "/parent/{id}")
    List<ParentResponseDTO> getWorkBundles(@PathVariable("id") UUID id);
}
```
* Add @EnableFeignClient to the service's SpringBoot Application class if not there already
client for the base url of service - scaffold here
added EnableFeignClient with base package in service that uses the client

* Autowire the http client in your service and call the method you need
```java
public class ScaffoldAPIClientTest {
  @Autowired
  private ScaffoldAPIClient scaffoldAPIClient;

  void callClient() {
    List<ParentResponseDTO> parents = scaffoldAPIClient.getParent(UUID.randomUUID());
  }
}
```
### How is Logging implemented to track & trace requests coming into this service ?

Request logging is configured in RequestLoggingConfig.java

#### Logging request interceptor application parameters available

logging.request.enabled=true - enable/disables request logging

logging.request.headers.include=false - enables/disables logging of headers

logging.request.payload.include=false - enables/disables logging of payload

logging.request.payload.maxlength=10000 - sets max length of payload characters to log

See [Spring Http logging](https://www.baeldung.com/spring-http-logging#1-configure-spring-boot-application)

[Spring Cloud Sleuth](https://www.baeldung.com/spring-cloud-sleuth-single-application) has also been added to add in trace and span ids into log messages 
so that log messages belongs to the same call chain may be correlated.

[Structured Logging](https://www.innoq.com/en/blog/structured-logging/) has been added for dev,stage & prod spring profiles as well so that logs can be consumed by Cloudwatch more readily.
See [Logback configuration](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/boot-features-logging.html#boot-features-custom-log-configuration) to understand
the configuration in logback-spring.xml. Other profiles like local and test still use the vanilla console appender as Cloudwatch would not be consuming those logs.

### How do I map my DTO to an entity ?
Check the mapper package in this repo for an example.

Also see
- [Quick Guide to MapStruct](https://www.baeldung.com/mapstruct) 
- [Mapping customization with before-mapping and after-mapping methods](https://mapstruct.org/documentation/dev/reference/html/#customizing-mappings-with-before-and-after)

### I am using MapStruct and Lombok but the MapStruct generated mapper class does not have setter method calls to my DTO or Entity ?
Ensure the Lombok processor is first in the list of annotation processors, then MapStruct.
The order of compilation matters and if you place it the other way around,
Mapstruct will fail to see the setters and getters you have.

## References
[Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### AWS Parameter Store Integration
[Integrating your spring cloud application with the aws parameter store](https://docs.awspring.io/spring-cloud-aws/docs/2.3.2/reference/html/index.html#integrating-your-spring-cloud-application-with-the-aws-parameter-store)

[How to use aws parameter store](https://enlear.academy/how-to-use-aws-parameter-store-39a123f1c57a)

[Reloading Properties by Actuator and Cloud](https://www.baeldung.com/spring-reloading-properties#reloading-cloud)


### Database
#### Liquibase

[Use Liquibase to Safely Evolve Your Database Schema](https://www.baeldung.com/liquibase-refactor-schema-of-java-app)

[SQL format changelogs example](https://docs.liquibase.com/concepts/changelogs/sql-format.html)

#### JPA & Hibernate

[Hibernate Second-Level Cache](https://www.baeldung.com/hibernate-second-level-cache)

[Defining Unique Constraints in JPA](https://www.baeldung.com/jpa-unique-constraints#:~:text=A%20unique%20constraint%20can%20be,%3Dtrue)%20and%20%40UniqueConstraint.)

[Save Hibernate DDL schema to a file](https://keepgrowing.in/tools/save-hibernate-ddl-schema-to-a-file/)

[Hibernate One to Many Annotation Tutorial](https://www.baeldung.com/hibernate-one-to-many)

[The best way to map a @OneToMany relationship with JPA and Hibernate](https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/)

[Difference Between @JoinColumn and mappedBy](https://www.baeldung.com/jpa-joincolumn-vs-mappedby)

[High-Performance Java Persistence Tips](https://vladmihalcea.com/14-high-performance-java-persistence-tips/)

[Show Hibernate/JPA SQL Statements from Spring Boot](https://www.baeldung.com/sql-logging-spring-boot)

[@JoinColumn Annotation Explained](https://www.baeldung.com/jpa-join-column)

[How to name Foreign Key in JPA](https://stackoverflow.com/questions/6608812/jpa-give-a-name-to-a-foreign-key-on-db)

[JPA Annotation for the PostgreSQL TEXT Type](https://www.baeldung.com/jpa-annotation-postgresql-text-type)

[Safely manage the database for your eXo add-ons with Liquibase Maven Plugin](https://www.exoplatform.com/blog/safely-manage-the-database-for-your-exo-add-ons-with-liquibase-maven-plugin/)

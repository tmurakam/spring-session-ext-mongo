Usage
=====

Sample web application is located at samples/httpsession-xml.

Add Dependency
--------------

Add dependency to build.gradle:

```groovy
dependencies {
    compile 'org.tmurakam:spring-session-ext-mongo:2.3.2'
}
```

Configuration
-------------

You can choose from enabling HttpSession filter using either Java Based Configuration,
or XML Based configuration.

For more details for Spring Session configuration, refer Spring Session documentations. 

### Java Based Configuration

First, create Java configuration class for MongoDB.
This must annotated with @EnableMongoHttpSession.

```java
@EnableMongoHttpSession
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "mongoSession";
    }
}
```

Next, create Initializer class extends AbstractHttpSessionApplicationInitializer.

```java
public class Initializer extends AbstractHttpSessionApplicationInitializer {
    public Initializer() {
        super(MyMongoConfiguration.class);
    }
}
```


### XML Based Configuration

First, create Java configuration class for MongoDB, as with Java Based Configuration.

Next, create spring XML configuration file, and place it in resources/spring/ dir.

```xml
<context:annotation-config/>

<!-- Your MongoDB configuration class -->
<bean class="com.example.MyMongoConfiguration"/>
```

Add spring configuration for WEB-INF/web.xml.

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/*.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>    
```

Last you need to ensure the servlet container uses springSessionRepositoryFilter.

```xml
<filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```


                      

 

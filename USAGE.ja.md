使用手順
========

使用手順を示します。

サンプルは samples/httpsession-xml にあります。

Dependencyの追加
----------------

build.gradle に dependency を追加します。
(maven の場合は pom.xml に同様の設定を追加)

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'org.tmurakam:spring-session-mongodb:0.9.3'
}
```

コンフィグレーション
--------------------

HttpSession フィルタを有効にするための設定を行います。
Javaベースまたは XML ベースの設定のいずれかが必要です。

基本手順は Spring Session の Redis 設定に似ているので、
詳細は Spring Session のドキュメントを参照してください。

### Java ベースのコンフィグレーション

MongoDB に接続するための設定用 Java クラスを作成してください。
@EnableMongoHttpSession を必ず指定する必要があります。

```java
package com.example;

@EnableMongoHttpSession
public class MyMongoConfiguration {
    @Bean
    public MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost");
        return mongo;
    }

    @Bean
    public MongoTemplate mongoTemplate(Mongo mongo) {
        MongoTemplate template = new MongoTemplate(mongo, "mongoSession");
        return template;
    }
}
```

次に AbstractHttpSessionApplicationInitializer を継承した
Initializer クラスを作成します。
コンストラクタ内から上記 Configuration クラスを指定してください。

```java
public class Initializer extends AbstractHttpSessionApplicationInitializer {
    public Initializer() {
        super(MyMongoConfiguration.class);
    }
}
```

### XML Based Configuration

MongoDB に接続するための設定用 Java クラスを作成してください。
これは上記 Java ベース設定と同じです。

次に Spring XML 設定ファイルを作成し、resources/spring ディレクトリに配置
してください。

```xml
<context:annotation-config/>

<!-- Your MongoDB configuration class -->
<bean class="com.example.MyMongoConfiguration"/>
```

この設定を読み込むための設定を WEB-INF/web.xml に追加します。

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/*.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>    
```

最後に、springSessionRepositoryFilter を使用するためのフィルタ設定を
web.xml に追加します。このフィルタは他のフィルタよりも前に配置する
必要があります。

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


                      

 

# spring-boot-data-jdbc-externalize-query
Externalize query in the @Query annotation for spring-data-jdbc


## How to use
1 - Import the dependency into your project
````
    <dependency>
        <groupId>com.cjcalmeida.spring</groupId>
        <artifactId>spring-boot-data-jdbc-externalize-query</artifactId>
        <version>1.0.0</version>
    </dependency>
````

2 - Enable JdbcRepositories with library JdbcExternalizedRepositoryFactoryBean and queries files
````
    @EnableJdbcRepositories(
    		repositoryFactoryBeanClass = JdbcExternalizedRepositoryFactoryBean.class,
    		namedQueriesLocation = "classpath:queries.xml")
````

3 - After this configuration you can create your queries in the file and
    use theys key in property value on Query annotation

File query.xml (can use .properties too)
````
    <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
    <properties>
        <entry key="Person.findByFirstname">
            <![CDATA[
                SELECT * FROM person WHERE firstname = :firstname
            ]]>
        </entry>
    </properties>
````

Query annotation
````
    @Query(value="Person.findByFirstname", rowMapperClass = PersonMapper.class)
````

### To Learn more, please see sample dir.


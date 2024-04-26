This is small lib for integration PostgreSQL types like jsonb and enum with JdbcClient/Template.
It provides easy way to map  jsonb/json/enum in parameter
Also it's possible to create own types avoiding PGObject getter/setter methods
Full implemented list is in package `io.github.breninsul.jdbctemplatepostgresqltypes.type`
 - PGBigDecimal.kt
 - PGBigInteger.kt
 - PGEnum.kt
 - PGInt.kt
 - PGJson.kt
 - PGJsonb.kt
 - PGLong.kt
 - PGUuid.kt
 - PGBoolean.kt
 - PGArray.kt 
 - PGDuration.kt
 - PGDuration.kt
 - PGZonedDateTime.kt
 - PGLocalTime.kt
 - PGLocalDateTime.kt
 - PGLocalDateRange.kt
 - PGLocalDate.kt
Also it's provide easy way to map jsonb/json ResultSet to any object

`pgDefaultObjectMapper` bean is registered as default ObjectMapper from Spring Context, if it's not provided primary bean will be used.
If there is no ObjectMapper bean in context or it's not Spring app,  `jacksonMapperBuilder().findAndAddModules().build()` will be used
add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("jdbc-template-postgresql-types:${version}")
//Other dependencies
}

````
# Example of usage
````kotlin
//Mapping JSON(b) parameter
{
    val paramJson = anyJacksonSerializebleObject.toPGJsonb()//toPGJsonb() is extension in PGJsonb class
    jdbcClient.sql("insert into test_table(id,jsonb_column) values (1,:paramJson)")
        .param("paramJson", paramJson)
        .update()
}
//Mapping JSON(b) parameter using PGJsonb constructor
{
    val paramJson = PGJsonb(anyJacksonSerializebleObject)
    jdbcClient.sql("insert into test_table(id,jsonb_column) values (1,:paramJson)")
        .param("paramJson", paramJson)
        .update()
}
//Mapping Enum
{
    val paramEnum = TestEnum.ONE.toPGEnum("pg_enum_type")//replace pg_enum_type with your real PG custom enum type
    //toPGEnum() is extension in PGEnum class
    jdbcClient.sql("insert into test_table(id,enum_column) values (1,:paramEnum)")
        .param("paramEnum", paramEnum)
        .update()
}

//Mapping Enum using PGEnum constructor
{
    val paramEnum = PGEnum(TestEnum.ONE, "pg_enum_type")//replace pg_enum_type with your real PG custom enum type
    jdbcClient.sql("insert into test_table(id,enum_column) values (1,:paramEnum)")
        .param("paramEnum", paramEnum)
        .update()
}

//One column row mapper
{
    val testJson = jdbcClient.sql("select jsonb_column from test_table where ... ")
        //... params here
        .query(JsonRowMapper(TestJson::class.java)) //or TestJson::class.toRowMapper()
        .single()
    //testJson will be TestJson type with 
}
//Multi column row mapper (by names)
{
    val testJson = jdbcClient.sql("select jsonb_column_1,jsonb_column_2,int_column from test_table where ... ")
        //... params here
        .query((JsonRowMapper(listOf(JsonRow("jsonb_column_1",TestJson::class.java),JsonRow("jsonb_column_2",TestJson::class.java),JsonRow("int_column",Int::class.java,true))))
        .single()
    //testJson will be Map<String,TestJson/Int> type column name as key and value as value 
    //Take attention that Int column is configured as rawSqlType so standard JDBC mechanism is used to map it
}
//Multi column row mapper (by column index)
{
    val testJson = jdbcClient.sql("select jsonb_column_1,jsonb_column_2,int_column from test_table where ... ")
        //... params here
        .query((JsonRowMapper(listOf(JsonRow(null,TestJson::class.java),JsonRow(null,TestJson::class.java),JsonRow("null,Int::class.java,true))))
            .single()
//testJson will be Map<Int,TestJson/Int> type column index as key and value as value 
````

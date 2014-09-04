Darks ORM
===========

Darks is a comprehensive type of lightweight ORM data persistence layer framework, integrated Hibernate "automation" ORM framework for the convenience, stability, as well as the Mybatis ORM framework "semi-automatic" efficient, high concurrency, set two ORM patterns as a whole.The framework to simplify the cumbersome configuration process, using Jython script language extensions, as a dynamic SQL language realization scheme.Construction of a highly efficient multi function domain data cache, and integrated EhCache third party data cache.

Feature 1.0.0
----------------

* Support SQL language query, the result set for the JAVA object automatic mapping.
* Support chain data source configuration.When the main data source connection access failure, will automatically switch to the associated data source access node.
* Support annotation data entity class form of annotation, annotation support a one-to-one, one-to-many, many-to-one relationship mapping model, support query annotations lazy.
* Support lazy loading, whole frame using lazy loading mechanism.
* Support the use of SQL statements for a single entity, the entity list query, query paging query, query entity list various cascades.In a concatenated query, framework will automatically find the result set save In the associated entity data, if there will be automatically cascaded mapping, effective use of the result set data.
* Support for normal JDBC services.
* Support for multiple data source configuration. Framework default integration of BoneCP data connection pool framework. In addition, able to use JDBC, BoneCp, JNDI, custom and so on many kinds of data source type.
* Support with the Spring framework integration.
* Support general (non object relational mapping) JDBC query and update method.
* Support the SQL and source code separation to XML mapping configuration, i.e. using SqlMap mode of mapping the query update.
* SqlMap support normal query, cascade query, select query, combination query, dynamic SQL query and other enquiries.
* SqlMap dynamic SQL query support Jython script (Python JAVA), Javascript, JavaBean, custom and other scripting language of the dynamic blocking modification.
* Support SqlMap with JAVA interface and the JAVA abstract class mapping.The JAVA abstract class mapping in SqlMap mapping, join BaseDAO encapsulation method for calling.
* Support the integration of EhCache data cache, data cache framework.Can choose to use the framework itself cache mechanism or the third cache mechanism.The caching mechanism includes application level cache, thread cache level as well as the session level cache (WEB application support).

Get Package
-----------------
You can get latest jar package from [darks-orm/release](https://github.com/liulhdarks/darks-orm/releases).

Configuration
---------------------
You should create configuration file "darks.xml" under root source directory.

### <dataSource> Tag

dataSource tag is used to configure database source information. It also can be built chain structure, 
which can use next dataSource node when current node's connection is invalid or occur error.<br/>
Examples:
<pre>
&lt;dataSource type="jdbc" id="jdbc" main="true"&gt;
	&lt;property name="driver" value="com.mysql.jdbc.Driver"&gt;&lt;/property&gt;
	&lt;property name="url" value="xxxxxx"/&gt;
	&lt;property name="username" value="xxxxxx"/&gt;
	&lt;property name="password" value="xxxxxx"/&gt;
	&lt;property name="fetchSize" value="0"&gt;&lt;/property&gt;
	&lt;property name="autoCommit" value="true"&gt;&lt;/property&gt;
	&lt;resultSet type="scroll" sensitive="false" concurrency="read"&gt;&lt;/resultSet&gt;
&lt;/dataSource&gt;

&lt;dataSource type="bonecp" id="bonecp" chainref="jdbc"&gt;
	&lt;property name="driver" value="com.mysql.jdbc.Driver"&gt;&lt;/property&gt;
	&lt;property name="url" value="xxxxxx"&gt;&lt;/property&gt;
	&lt;property name="username" value="xxxxxx"&gt;&lt;/property&gt;
	&lt;property name="password" value="xxxxxx"&gt;&lt;/property&gt;
	......
	&lt;resultSet type="scroll" sensitive="false" concurrency="read"&gt;&lt;/resultSet&gt;
&lt;/dataSource&gt;

&lt;dataSource type="jndi" id="jndi" chainref="bonecp"&gt;
	&lt;property name="fetchSize" value="0"&gt;&lt;/property&gt;
	&lt;property name="autoCommit" value="true"&gt;&lt;/property&gt;
	&lt;property name="jndiPoolName" value="java:comp/env/jdbc/xxxxxx"&gt;&lt;/property&gt;
	&lt;resultSet type="scroll" sensitive="false" concurrency="read"&gt;&lt;/resultSet&gt;
&lt;/dataSource&gt;
</pre>

The above example indicate that if JDBC fail to get connection, bonecp connection will try again. 
And if bonecp failed, jndi will try again.

### <entities> Tag

If you want to load entities when startup or use entity's alias for sqlmap, you should use entities to define 
which class will be loaded when startup. You even can use <package> child tag to define all classes under target package.<br/>
Examples:

<pre>
&lt;entities&gt;
	&lt;entity alias="User" class="darks.orm.test.model.User"/&gt;
	&lt;entity alias="Depart" class="darks.orm.test.model.Depart"/&gt;
	....
	&lt;package name="darks.orm.test.model"/&gt;
&lt;/entities&gt;
</pre>

### <cacheGroup> Tag

cacheGroup tag is used to configure global or local cache configuration. Cache can act on all executing method as global cache, 
it also can act on specify method as local cache. <br/>
Cache has some action scope. <appCache> will live on the application scope, <threadCache> will just live on the current thread 
scope, <ehCache> will call EhCache to manage cache objects.<br/>
Examples:

<pre>
&lt;cacheGroup use="true" type="auto" cacheId="application" synchronous="true"&gt;
	&lt;appCache strategy="Lru" 
			  ....
			  copyStrategy="serial"/&gt;
	&lt;threadCache  strategy="Lru" 
				  ....
				  entirety="true"
				  copyStrategy="serial"/&gt;
	&lt;ehCache id="ehcache1"
			 maxElementsInMemory="10000"
			 eternal="false"
			 ....
             memoryStoreEvictionPolicy="LRU"/&gt;
&lt;/cacheGroup&gt;
</pre>

### <sqlMapGroup> Tag

sqlMapGroup tag is used to configure sqlmap configuration files paths. <br/>
Examples:

<pre>
&lt;sqlMapGroup&gt;
	&lt;sqlMap&gt;/sqlmap-*.xml&lt;/sqlMap&gt;
&lt;/sqlMapGroup&gt;
</pre>

### Spring Configuration

If you have configured datasource both darks.xml and springContext.xml, the spring's datasource will be the main
datasource, and other datasource configured in darks.xml will be its child node.<br/>
Examples:

<pre>
&lt;bean id="testDataSource" class="org.apache.commons.dbcp.BasicDataSource"&gt;
	&lt;property name="driverClassName" value="com.mysql.jdbc.Driver" /&gt;
	&lt;property name="url" value="xxxxxx" /&gt;
	&lt;property name="username" value="xxxxx" /&gt;
	&lt;property name="password" value="xxxxx" /&gt;
&lt;/bean&gt;

&lt;bean class="darks.orm.spring.SqlSessionFactoryBean"&gt;
	&lt;property name="dataSource" ref="testDataSource" /&gt;
	&lt;property name="scanPackages" value="darks.orm.test.mapper,darks.orm.examples.mapper" /&gt;
	&lt;property name="configLocation" value="classpath:darks.xml" /&gt;
	&lt;property name="dataParamConfig.autoCommit" value="true" /&gt;
	&lt;property name="dataParamConfig.resultSetType" value="scroll" /&gt;
	&lt;property name="dataParamConfig.sensitive" value="false" /&gt;
	&lt;property name="dataParamConfig.concurrency" value="read" /&gt;
&lt;/bean&gt;
</pre>

Define Entity
------------------------

<pre>
@Entity("users")
public class User
{
	//@Id(feedBackKey = FeedBackKeyType.SELECT, select = "SELECT LAST_INSERT_ID()")
    //@Id(type = GenerateKeyType.SELECT, select = "SELECT LAST_INSERT_ID()")
    @Id
    @Column("id")
    private int userId;
    
    @Column("name")
    private String userName;
    
    @Column(value="pwd", nullable=false)
    private String userPwd;
    
    @Column("depart_id")
    private Depart depart;
    ....
    
    //@OneToOne(resultType = Depart.class, SQL = "select * from t_depart where depart_id = ?")
    @OneToOne(resultType = Depart.class, mappedBy = "departId", mappedType = MappedType.EntityType)
    public Depart getDepart()
    {
        return depart;
    }
    
    //@ManyToOne(classType=Depart.class, SQL="select * from t_depart where depart_id = ?")
    //@ManyToOne(resultType = Depart.class, mappedBy = "departId", mappedType = MappedType.EntityType)
    
    @OneToMany(resultType = User.class, mappedBy = "depart", mappedType = MappedType.EntityType)
    public List<User> getUsers(){...}
    ....
    
    @Query(SQL="select * from users where parent_id = ?", 
			resultType=User.class, queryType=QueryType.SingleType, paramType={"id"})
	public List<User> getUsers()
	{
		return null;
	}
}
</pre>

Basic Call
----------------------
You can call executeQuery or executeUpdate to execute original JDBC method directly. And you can call queryList, 
queryById, queryPageList etc to execute ORM method. queryCascadeXXX can query entities object by cascade way.<br/>
Examples:

<pre>
List<User> users = session.queryList(User.class, "select * from users where name = ?", "darks");
Page<User> page = session.queryPageList(User.class, "select * from users", page, pageSize);
session.save(new User(....));
session.update(user);
session.delete(User.class, userId);
session.delete(user);
</pre>


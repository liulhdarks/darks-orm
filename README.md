Darks ORM
===========

Darks is a comprehensive type of lightweight ORM data persistence layer framework, integrated Hibernate "automation" ORM framework for the convenience, stability, as well as the Mybatis ORM framework "semi-automatic" efficient, high concurrency, set two ORM patterns as a whole.The framework to simplify the cumbersome configuration process, using Jython script language extensions, as a dynamic SQL language realization scheme.Construction of a highly efficient multi function domain data cache, and integrated EhCache third party data cache.

Feature
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

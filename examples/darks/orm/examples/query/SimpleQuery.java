package darks.orm.examples.query;

import java.sql.SQLException;

import darks.orm.app.SqlSession;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.examples.model.Department;
import darks.orm.examples.model.Product;

public class SimpleQuery
{

    public static void main(String[] args) throws SQLException
    {
        insertData();
    }
    
    public static void insertData() throws SQLException
    {
        SqlSession session = SqlSessionFactory.getSession();
        Department depart = new Department("depart1", 1);
        Integer departId = (Integer)session.save(depart, true);
        System.out.println("depart:" + departId);
        Product pt1 = new Product("product1", 1, depart, 64, 64, 12.6f);
        session.save(pt1);
        Product pt2 = new Product("product2", 1, depart, 128, 128, 22.6f);
        session.save(pt2);
        Product pt3 = new Product("product3",2, depart, 256, 256, 33.6f);
        session.save(pt3);
        session.close();
    }
    
}

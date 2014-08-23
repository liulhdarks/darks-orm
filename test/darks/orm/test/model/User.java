
package darks.orm.test.model;

import java.io.Serializable;
import java.util.List;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.Id.GenerateKeyType;
import darks.orm.annotation.MappedType;
import darks.orm.annotation.OneToOne;

@Entity("users")
public class User implements Serializable
{
    
    private static final long serialVersionUID = -5399513570653821112L;
    
    // SQL查询主键字段声明
    @Id(type = GenerateKeyType.SELECT, select = "SELECT MAX(USER_ID)+1 FROM T_USER")
    @Column("id")
    private int userId;
    
    // 默认参数
    @Column("name")
    private String userName;
    
    @Column("pwd")
    private String userPwd;
    
    @Column("depart_id")
    private Depart depart;
    
    private List<Depart> departs;
    
    public User()
    {
        
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    
    
    // @ManyToOne(classType=Depart.class,SQL="select * from t_depart where depart_id = ?")
    // @ManyToOne(resultType = Depart.class, mappedBy = "departId", mappedType =
    // MappedType.EntityType)
    
    // 半自动化一对一关系映射
    // @OneToOne(resultType = Depart.class, SQL =
    // "select * from t_depart where depart_id = ?")
    
    public String getUserPwd()
    {
        return userPwd;
    }

    public void setUserPwd(String userPwd)
    {
        this.userPwd = userPwd;
    }

    // 自动化一对一关系映射
    @OneToOne(resultType = Depart.class, mappedBy = "departId", mappedType = MappedType.EntityType)
    public Depart getDepart()
    {
        return depart;
    }
    
    public void setDepart(Depart depart)
    {
        this.depart = depart;
    }
    
    public List<Depart> getDeparts()
    {
        return departs;
    }
    
    public void setDeparts(List<Depart> departs)
    {
        this.departs = departs;
    }
    
}

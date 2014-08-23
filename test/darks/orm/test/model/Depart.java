
package darks.orm.test.model;

import java.io.Serializable;
import java.util.List;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.Id.FeedBackKeyType;
import darks.orm.annotation.ManyToOne;
import darks.orm.annotation.MappedType;
import darks.orm.annotation.OneToMany;

@Entity("depart")
public class Depart implements Serializable
{
    
    private static final long serialVersionUID = -6767968150262747359L;
    
    // @Id(feedBackKey = FeedBackKeyType.GENERATEDKEY)
    
    // 采用SQL查询反馈主键值
    //@Id(feedBackKey = FeedBackKeyType.SELECT, select = "select last_insert_id() from t_depart")
    @Id(feedBackKey = FeedBackKeyType.GENERATEDKEY)
    @Column("id")
    private int departId;
    
    @Column("name")
    private String departName;
    
    @Column(value = "boss", nullable = true, insertable = true, queryable = true, updatable = true)
    private User departManager;
    
    private List<User> users;
    
    public Depart()
    {
        
    }
    
    @ManyToOne(SQL = "select * from users where id = ?", resultType = User.class)
    public User getDepartManager()
    {
        return departManager;
    }
    
    public void setDepartManager(User departManager)
    {
        this.departManager = departManager;
    }
    
    // @OneToMany(resultType=User.class,SQL="select * from t_user where user_depart_id = ?")
    
    // 自动化一对多关系映射
    @OneToMany(resultType = User.class, mappedBy = "depart", mappedType = MappedType.EntityType)
    public List<User> getUsers()
    {
        return users;
    }
    
    public void setUsers(List<User> users)
    {
        this.users = users;
    }
    
    public int getDepartId()
    {
        return departId;
    }
    
    public void setDepartId(int departId)
    {
        this.departId = departId;
    }
    
    public String getDepartName()
    {
        return departName;
    }
    
    public void setDepartName(String departName)
    {
        this.departName = departName;
    }
}

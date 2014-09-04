package darks.orm.examples.model;

import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.Id.FeedBackKeyType;

@Entity("depart")
public class Department
{
    @Id(feedBackKey=FeedBackKeyType.GENERATEDKEY)
    private Integer id;
    
    private String name;
    
    private Integer type;
    
    public Department()
    {
        
    }

    public Department(String name, Integer type)
    {
        super();
        this.name = name;
        this.type = type;
    }

    public Department(Integer id, String name, Integer type)
    {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }
    
    
}

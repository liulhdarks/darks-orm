package darks.orm.examples.model;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;

@Entity("products")
public class Product
{
    @Id
    private Integer id;
    
    private String name;
    
    private Integer type;
    
    @Column("depart_id")
    private Department depart;
    
    private int width;
    
    private int height;
    
    private float weight;
    
    public Product()
    {
        
    }

    public Product(String name, Integer type, Department depart, int width, int height, float weight)
    {
        super();
        this.name = name;
        this.type = type;
        this.depart = depart;
        this.width = width;
        this.height = height;
        this.weight = weight;
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

    public Department getDepart()
    {
        return depart;
    }

    public void setDepart(Department depart)
    {
        this.depart = depart;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }
    
    
}

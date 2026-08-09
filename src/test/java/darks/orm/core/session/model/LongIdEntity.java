package darks.orm.core.session.model;

import java.io.Serializable;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.Id.GenerateKeyType;

@Entity("long_id_entity")
public class LongIdEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id(type = GenerateKeyType.ASSIGNED)
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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
}

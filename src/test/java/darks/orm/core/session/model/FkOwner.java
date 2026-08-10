package darks.orm.core.session.model;

import java.io.Serializable;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.ManyToOne;

@Entity("fk_owner")
public class FkOwner implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Integer id;

    @Column("name")
    private String name;

    @Column(value = "ref_id", nullable = true, insertable = true, queryable = true, updatable = true)
    private FkRef ref;

    public FkOwner()
    {
    }

    @ManyToOne(SQL = "select * from fk_ref where id = ?", resultType = FkRef.class)
    public FkRef getRef()
    {
        return ref;
    }

    public void setRef(FkRef ref)
    {
        this.ref = ref;
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
}

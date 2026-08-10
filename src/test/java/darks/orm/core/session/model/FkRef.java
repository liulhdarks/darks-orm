package darks.orm.core.session.model;

import java.io.Serializable;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;

@Entity("fk_ref")
public class FkRef implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Integer id;

    @Column("title")
    private String title;

    public FkRef()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}

package darks.orm.core.session.model;

import java.io.Serializable;

import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.Id.GenerateKeyType;

@Entity("status_entity")
public class StatusEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id(type = GenerateKeyType.AUTO)
    @Column("id")
    private Integer id;

    @Column("status")
    private int status;

    @Column("score")
    private Integer score;

    @Column(value = "note", nullable = true)
    private String note;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}

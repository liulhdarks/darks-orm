package darks.orm.core.data;

import java.io.Serializable;

import darks.orm.annotation.Id.GenerateKeyType;
import darks.orm.annotation.Id.FeedBackKeyType;

public class PrimaryKeyData implements Serializable
{
    
    private static final long serialVersionUID = 6645248148795192529L;
    
    private GenerateKeyType type = GenerateKeyType.AUTO;
    
    private String seq;
    
    private FeedBackKeyType feedBackKey = FeedBackKeyType.NONE;
    
    private String select;
    
    public PrimaryKeyData()
    {
        
    }
    
    public PrimaryKeyData(GenerateKeyType type, String seq, FeedBackKeyType feedBackKey, String select)
    {
        super();
        this.type = type;
        this.seq = seq;
        this.feedBackKey = feedBackKey;
        this.select = select;
    }
    
    public GenerateKeyType getType()
    {
        return type;
    }
    
    public void setType(GenerateKeyType type)
    {
        this.type = type;
    }
    
    public String getSeq()
    {
        return seq;
    }
    
    public void setSeq(String seq)
    {
        this.seq = seq;
    }
    
    public FeedBackKeyType getFeedBackKey()
    {
        return feedBackKey;
    }
    
    public void setFeedBackKey(FeedBackKeyType feedBackKey)
    {
        this.feedBackKey = feedBackKey;
    }
    
    public String getSelect()
    {
        return select;
    }
    
    public void setSelect(String select)
    {
        this.select = select;
    }
    
}

/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
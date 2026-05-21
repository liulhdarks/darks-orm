package darks.orm.core.data.tags.express.impl;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OrderingOperTest
{
    
    @Test
    public void orderingOperatorsReturnFalseWhenEitherOperandIsNull()
    {
        assertFalse(new GtOper().compute(Integer.valueOf(1), null).booleanValue());
        assertFalse(new GtOper().compute(null, Integer.valueOf(1)).booleanValue());
        assertFalse(new GtOper().compute((Object)null, null).booleanValue());
        assertFalse(new GtOper().compute("a", (String)null).booleanValue());
        
        assertFalse(new GtEqualOper().compute(Integer.valueOf(1), null).booleanValue());
        assertFalse(new GtEqualOper().compute(null, Integer.valueOf(1)).booleanValue());
        assertFalse(new GtEqualOper().compute((Object)null, null).booleanValue());
        assertFalse(new GtEqualOper().compute("a", (String)null).booleanValue());
        
        assertFalse(new ItOper().compute(Integer.valueOf(1), null).booleanValue());
        assertFalse(new ItOper().compute(null, Integer.valueOf(1)).booleanValue());
        assertFalse(new ItOper().compute((Object)null, null).booleanValue());
        assertFalse(new ItOper().compute("a", (String)null).booleanValue());
        
        assertFalse(new ItEqualOper().compute(Integer.valueOf(1), null).booleanValue());
        assertFalse(new ItEqualOper().compute(null, Integer.valueOf(1)).booleanValue());
        assertFalse(new ItEqualOper().compute((Object)null, null).booleanValue());
        assertFalse(new ItEqualOper().compute("a", (String)null).booleanValue());
    }
}

package darks.orm.core.factory;

import net.sf.cglib.beans.BeanGenerator;

@SuppressWarnings("unchecked")
public class BeanGeneratorFactory
{
    
    public static BeanGenerator generateProxyBean(Class classOriginal)
    {
        BeanGenerator g = new BeanGenerator();
        g.setSuperclass(classOriginal);
        return g;
    }
}

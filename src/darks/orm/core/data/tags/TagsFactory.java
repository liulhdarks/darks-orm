package darks.orm.core.data.tags;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import darks.orm.core.data.tags.impl.ElseIfTag;
import darks.orm.core.data.tags.impl.ElseTag;
import darks.orm.core.data.tags.impl.ForTag;
import darks.orm.core.data.tags.impl.IfTag;
import darks.orm.core.data.tags.impl.WhereTag;

public final class TagsFactory
{

	static Map<String, Constructor<? extends AbstractTag>> tagsMap = new HashMap<String, Constructor<? extends AbstractTag>>();
	
	static
	{
		addTagClass("if", IfTag.class);
		addTagClass("elseif", ElseIfTag.class);
		addTagClass("else", ElseTag.class);
		addTagClass("foreach", ForTag.class);
		addTagClass("where", WhereTag.class);
	}
	
	private static void addTagClass(String key, Class<? extends AbstractTag> tagClass)
	{
		try
		{
			Constructor<? extends AbstractTag> crts = tagClass.getConstructor();
			tagsMap.put(key, crts);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static AbstractTag createTag(Element el, AbstractTag prevTag)
	{
		AbstractTag tag = createTag(el);
		if (tag != null)
		{
			tag.setPrevTag(prevTag);
		}
		return tag;
	}
	
	public static AbstractTag createTag(Element el)
	{
		String name = el.getName();
		Constructor<? extends AbstractTag> crts = tagsMap.get(name);
		if (crts == null)
		{
			return null;
		}
		
		try
		{
			return crts.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}

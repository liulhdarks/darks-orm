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

package darks.orm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import darks.orm.core.data.EntityData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.ProxyBeanFactory;

public class ByteHelper
{
    
    private static final int MASK = 0xf;
    
    /**
     * 判断是否可序列化
     * 
     * @param obj 对象
     * @return
     */
    public static boolean isSerializable(Object obj)
    {
        if (obj instanceof Serializable)
        {
            return true;
        }
        return false;
    }
    
    public static boolean isSerializable(EntityData data)
    {
        if (data == null || !data.isSerializable())
            return false;
        return true;
    }
    
    public static boolean isSerializable(Class<?> c)
    {
        Class<?> chk = c;
        if (c.getSuperclass() != null && !c.getSuperclass().equals(java.lang.Object.class))
        {
            chk = c.getSuperclass();
        }
        Class<?>[] cs = chk.getInterfaces();
        
        for (Class<?> s : cs)
        {
            if (s.equals(Serializable.class))
            {
                return true;
            }
        }
        return false;
    }
    
    public static java.lang.Object ByteToObject(byte[] bytes)
    {
        java.lang.Object obj = null;
        try
        {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            
            obj = oi.readObject();
            
            bi.close();
            oi.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj;
    }
    
    public static byte[] ObjectToByte(java.lang.Object obj)
    {
        byte[] bytes = null;
        try
        {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            
            bytes = bo.toByteArray();
            
            bo.close();
            oo.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (bytes);
    }
    
    public static void fieldCopyToOriginal(Field f, Object proxy, Object src)
        throws Exception
    {
        if (Modifier.isFinal(f.getModifiers()))
            return;
        f.setAccessible(true);
        Object val = f.get(proxy);
        // System.out.println(f.getName()+" val:"+val);
        if (val == null)
        {
            f.set(src, val);
            return;
        }
        if (f.getType().equals(Clob.class))
        {
            String text = SqlHelper.clobToStr((Clob)val);
            val = SqlHelper.stringToClob(text);
        }
        String className = val.getClass().getName();
        className = ClassFactory.filterClassName(className);
        Class<?> valc = Class.forName(className);
        if (ClassFactory.getEntity(className) != null)
        {
            val = ProxyToOriginal(valc, val);
        }
        f.set(src, val);
    }
    
    @SuppressWarnings("unchecked")
    public static Object ProxyToOriginal(Class<?> c, Object obj)
        throws Exception
    {
        EntityData data = ClassFactory.getEntity(c.getName());
        Object n = ReflectHelper.newInstance(data.getClassProxy());
        Field[] fs1 = n.getClass().getDeclaredFields();
        Field[] fs2 = c.getDeclaredFields();
        for (Field f : fs1)
        {
            fieldCopyToOriginal(f, obj, n);
        }
        for (Field f : fs2)
        {
            fieldCopyToOriginal(f, obj, n);
        }
        return n;
    }
    
    @SuppressWarnings("unchecked")
    public static void fieldCopyToProxy(Connection conn, Field f, Object src, Object proxy)
        throws Exception
    {
        if (Modifier.isFinal(f.getModifiers()))
            return;
        f.setAccessible(true);
        Object val = f.get(src);
        // System.out.println(f.getName()+" val:"+val);
        if (val == null)
        {
            f.set(proxy, val);
            return;
        }
        String className = val.getClass().getName();
        className = ClassFactory.filterClassName(className);
        Class valc = Class.forName(className);
        if (ClassFactory.getEntity(className) != null)
        {
            val = OriginalToProxy(conn, valc, val);
        }
        f.set(proxy, val);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T OriginalToProxy(Connection conn, Class<T> c, T obj)
        throws Exception
    {
        EntityData data = ClassFactory.getEntity(c.getName());
        if (!data.isUseProxy())
            return obj;
        Class<T> cls = (Class<T>)data.getClassProxy();
        if (cls == null)
            return null;
        T n = ProxyBeanFactory.getProxyEntity(cls);
        Field[] fs1 = cls.getDeclaredFields();
        Field[] fs2 = c.getDeclaredFields();
        for (Field f : fs1)
        {
            fieldCopyToProxy(conn, f, obj, n);
        }
        for (Field f : fs2)
        {
            fieldCopyToProxy(conn, f, obj, n);
        }
        return n;
    }
    
    public static <T> byte[] ObjectToByte(Class<T> c, Object obj)
        throws Exception
    {
        Object op = ProxyToOriginal(c, obj);
        // System.out.println(op.hashCode()+" "+op);
        byte[] opbt = ByteHelper.ObjectToByte(op);
        // System.out.println(opbt.length);
        return opbt;
    }
    
    public static <T> byte[][] ListToByte(Class<T> c, List<?> list)
        throws Exception
    {
        int size = list.size();
        byte[][] tmp = new byte[size][];
        for (int i = 0; i < size; i++)
        {
            Object obj = list.get(i);
            tmp[i] = ObjectToByte(c, obj);
        }
        return tmp;
    }
    
    @SuppressWarnings("unchecked")
    public static Object ByteToObject(Connection conn, Class c, byte[] bytes)
        throws Exception
    {
        Object obj = ByteHelper.ByteToObject(bytes);
        // System.out.println(obj.hashCode()+" "+obj);
        Object ret = OriginalToProxy(conn, c, obj);
        // System.out.println(ret.hashCode()+" "+ret);
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    public static List ByteToList(Connection conn, Class c, byte[][] bytes)
        throws Exception
    {
        List<Object> list = new ArrayList(bytes.length);
        for (byte[] bts : bytes)
        {
            Object obj = ByteToObject(conn, c, bts);
            list.add(obj);
        }
        return list;
    }
    
    public static Object[] readArray(ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        int len = in.readInt();
        
        Object[] arr = null;
        
        if (len > 0)
        {
            arr = new Object[len];
            
            for (int i = 0; i < len; i++)
            {
                arr[i] = in.readObject();
            }
        }
        
        return arr;
    }
    
    public static <T> void writeArray(ObjectOutput out, T[] arr)
        throws IOException
    {
        int len = arr == null ? 0 : arr.length;
        
        out.writeInt(len);
        
        if (arr != null && arr.length > 0)
        {
            for (T t : arr)
            {
                out.writeObject(t);
            }
        }
    }
    
    public static byte[] CollectionToByte(Collection<?> col)
    {
        byte[] bytes = null;
        try
        {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            
            writeCollection(oo, col);
            
            bytes = bo.toByteArray();
            
            bo.close();
            oo.close();
        }
        catch (Exception e)
        {
            System.out.println("translation:" + e.getMessage());
            e.printStackTrace();
        }
        return (bytes);
    }
    
    public static <E> Collection<E> ByteToCollection(byte[] bytes)
    {
        Collection<E> obj = null;
        try
        {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            
            obj = readCollection(oi);
            
            bi.close();
            oi.close();
        }
        catch (Exception e)
        {
            System.out.println("translation:" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
    
    public static void writeCollection(ObjectOutput out, Collection<?> col)
        throws IOException
    {
        // Write null flag.
        out.writeBoolean(col == null);
        
        if (col != null)
        {
            out.writeInt(col.size());
            
            for (Object o : col)
            {
                out.writeObject(o);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <E> Collection<E> readCollection(ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        List<E> col = null;
        
        // Check null flag.
        if (!in.readBoolean())
        {
            int size = in.readInt();
            
            col = new ArrayList<E>(size);
            
            for (int i = 0; i < size; i++)
            {
                col.add((E)in.readObject());
            }
        }
        
        return col;
    }
    
    public static <K, V> Map<K, V> copyMap(Map<K, V> m)
    {
        return new HashMap<K, V>(m);
    }
    
    public static String byteArray2HexString(byte[] arr)
    {
        StringBuilder buf = new StringBuilder(arr.length << 1);
        
        for (byte b : arr)
        {
            buf.append(Integer.toHexString(MASK & b >>> 4)).append(Integer.toHexString(MASK & b));
        }
        
        return buf.toString().toUpperCase();
    }
    
    public static byte[] hexString2ByteArray(String hex)
        throws IllegalArgumentException
    {
        // If Hex string has odd character length.
        if (hex.length() % 2 != 0)
        {
            hex = '0' + hex;
        }
        
        char[] chars = hex.toCharArray();
        
        byte[] bytes = new byte[chars.length / 2];
        
        int byteCount = 0;
        
        for (int i = 0; i < chars.length; i += 2)
        {
            int newByte = 0;
            
            newByte |= hexCharToByte(chars[i]);
            
            newByte <<= 4;
            
            newByte |= hexCharToByte(chars[i + 1]);
            
            bytes[byteCount] = (byte)newByte;
            
            byteCount++;
        }
        
        return bytes;
    }
    
    private static byte hexCharToByte(char ch)
        throws IllegalArgumentException
    {
        switch (ch)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            {
                return (byte)(ch - '0');
            }
            
            case 'a':
            case 'A':
            {
                return 0xa;
            }
            
            case 'b':
            case 'B':
            {
                return 0xb;
            }
            
            case 'c':
            case 'C':
            {
                return 0xc;
            }
            
            case 'd':
            case 'D':
            {
                return 0xd;
            }
            
            case 'e':
            case 'E':
            {
                return 0xe;
            }
            
            case 'f':
            case 'F':
            {
                return 0xf;
            }
            
            default:
            {
                throw new IllegalArgumentException("Hex decoding wrong input character [character=" + ch + ']');
            }
        }
    }
    
    public static void writeMap(ObjectOutput out, Map<?, ?> map)
        throws IOException
    {
        // Write null flag.
        out.writeBoolean(map == null);
        
        if (map != null)
        {
            out.writeInt(map.size());
            
            for (Map.Entry<?, ?> e : map.entrySet())
            {
                out.writeObject(e.getKey());
                out.writeObject(e.getValue());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> readMap(ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        Map<K, V> map = null;
        
        // Check null flag.
        if (!in.readBoolean())
        {
            int size = in.readInt();
            
            map = new HashMap<K, V>(size, 1.0f);
            
            for (int i = 0; i < size; i++)
            {
                map.put((K)in.readObject(), (V)in.readObject());
            }
        }
        
        return map;
    }
    
    public static void writeObject(ObjectOutput out, Object obj)
        throws IOException
    {
        writeByteArray(out, ObjectToByte(obj));
    }
    
    public static Object readObject(ObjectInput in)
        throws IOException
    {
        return ByteToObject(readByteArray(in));
    }
    
    public static void writeByteArray(ObjectOutput out, byte[] arr)
        throws IOException
    {
        if (arr == null)
        {
            out.writeInt(-1);
        }
        else
        {
            out.writeInt(arr.length);
            
            out.write(arr);
        }
    }
    
    public static byte[] readByteArray(ObjectInput in)
        throws IOException
    {
        int len = in.readInt();
        
        if (len == -1)
        {
            return null; // Value "-1" indicates null.
        }
        
        byte[] res = new byte[len];
        
        in.readFully(res);
        
        return res;
    }
    
    @SuppressWarnings("unchecked")
    public static <V> ArrayList<V>[] splitArrayList(ArrayList<V> src, int limit)
    {
        int num = src.size() % limit == 0 ? (src.size() / limit) : (src.size() / limit + 1);
        ArrayList<V>[] list = new ArrayList[num];
        int index = 0;
        for (int i = 0; i < num; i++)
        {
            int size = (i + 1) * limit - 1;
            int len = src.size() < size ? src.size() : size;
            ArrayList<V> tmp = new ArrayList<V>(len);
            for (int j = i * limit; j < len; j++)
            {
                V obj = src.get(i);
                tmp.add(obj);
            }
            list[index++] = tmp;
        }
        return list;
    }
}
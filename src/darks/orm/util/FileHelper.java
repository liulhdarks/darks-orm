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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHelper
{
    
    /**
     * 获得资源文件绝对路径
     * 
     * @param filePath 文件路径
     * @return
     */
    public static String getResourcePath(String filePath)
    {
        String cfgPath = FileHelper.class.getResource(filePath).getFile();
        return cfgPath = cfgPath.replace("%20", " ");
    }
    
    /**
     * 获得通配符资源文件路径
     * 
     * @param filePath 文件路径
     * @return
     */
    public static List<String> getRegexResourceFiles(String filePath)
    {
        List<String> list = new LinkedList<String>();
        String dir = "";
        if (filePath.startsWith("/"))
        {
            dir = "/";
            filePath = filePath.substring(1);
        }
        String root = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File dirFile = new File(root);
        filePath = filePath.replace(".", "\\.");
        filePath = filePath.replace("*", ".*");
        filePath = filePath.replace("?", ".?");
        filePath = "^" + filePath + "$";
        Pattern reg = Pattern.compile(filePath);
        if (dirFile.isDirectory())
        {
            File[] files = dirFile.listFiles();
            for (File f : files)
            {
                if (f.isFile())
                {
                    Matcher m = reg.matcher(f.getName());
                    if (m.matches())
                    {
                        list.add(dir + f.getName());
                    }
                }
            }
        }
        return list;
    }
}
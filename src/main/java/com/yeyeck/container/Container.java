package com.yeyeck.container;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.yeyeck.annotations.Mapper;

public class Container {
  private final Map<Class<?>, Object> map = new HashMap<>();

  public void load(String packagePath) {
    String filePath = packagePath.replace(".", File.separator);
    URL root = Thread.currentThread().getContextClassLoader().getResource(filePath);
    if ("file".equals(root.getProtocol())) {
      try {
        filePath = URLDecoder.decode(root.getFile(), "UTF-8");
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    File dir = new File(filePath);
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      for (File file : files) {
        if (file.isFile() && file.getName().endsWith(".class")) {
          String className = packagePath + "." + file.getName().split(".")[0];
          try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Mapper.class)) {

            }
          } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
  }

  
}

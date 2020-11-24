package com.yeyeck.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import com.yeyeck.annotations.SelectMany;
import com.yeyeck.annotations.SelectOne;
import com.yeyeck.database.DBUtils;

public class ProxyFactory {

  public static Object proxyMapper(Class<?>... clazz) {

    return Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), clazz,new InvocationHandler(){

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.isAnnotationPresent(SelectOne.class)) {
       SelectOne one = method.getAnnotation(SelectOne.class);
       ParameterizedType returnType = (ParameterizedType)method.getGenericReturnType();
       Class<?> actualType = (Class<?>)returnType.getActualTypeArguments()[0];
       String sql = one.value();
       return DBUtils.selectOne(actualType, sql, args);
      } else if(method.isAnnotationPresent(SelectMany.class)) {
        SelectMany many = method.getAnnotation(SelectMany.class);
      }
			  return null;
		}
    });
  }
  
}

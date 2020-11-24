package com.yeyeck;

import com.yeyeck.dao.StudentDao;
import com.yeyeck.dao.StudentDaoImpl;
import com.yeyeck.database.DBClient;
import com.yeyeck.factory.ProxyFactory;

import io.vertx.sqlclient.Tuple;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
      Object obj = ProxyFactory.proxyMapper(StudentDao.class);
      StudentDao studentDao = (StudentDao)obj;
      studentDao.findById(1).onSuccess(System.out::println);
    //   System.out.println(obj.toString());
    //   dao.findAll().onSuccess(list -> {
    //       System.out.println(list);
    //   });
  }

  private static Tuple get(Object...objs) {
    Tuple tuple = Tuple.tuple();
    for (Object obj : objs) {
        tuple.addValue(obj);
    }
    return tuple;
  }
}

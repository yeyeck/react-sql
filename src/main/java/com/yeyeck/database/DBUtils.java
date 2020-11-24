package com.yeyeck.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

public class DBUtils {
  private static final char UNDER_LINE = '_';
  private static final String SETTER_PREFFIX = "set";
  public static <T> Future<T> selectOne(Class<T> clazz, String sql, Object...objs ) {
    Promise<T> promise = Promise.promise();
    DBClient.getInstance().getConnection()
    .onSuccess(connection -> {
      connection.preparedQuery(sql).execute(getTuple(objs))
      .onSuccess(rowSet -> {
        Iterator<Row> iterator = rowSet.iterator();
        if (iterator.hasNext()) {
          List<String> columns = rowSet.columnsNames();
          T obj = mapTo(clazz, iterator.next(), columns);
          promise.complete(obj);
        } else {
          promise.complete(null);
        }
      }).onFailure(promise::fail);
    });
    return promise.future();
  }

  public static <T> Future<List<T>> selectList(Class<T> clazz, String sql, Object...objs) {
    Promise<List<T>> promise = Promise.promise();
    Tuple tuple = Tuple.tuple();
    if (objs.length > 0) {
      for (Object object : objs) {
        tuple.addValue(object);
      }
    }
    DBClient.getInstance().getConnection()
    .onSuccess(connection -> {
      connection.preparedQuery(sql).execute(getTuple(objs))
      .onSuccess(rowSet -> {
        List<T> list = new LinkedList<>();
        Iterator<Row> iterator = rowSet.iterator();
        while (iterator.hasNext()) {
          List<String> columns = rowSet.columnsNames();
          T obj = mapTo(clazz, iterator.next(), columns);
          list.add(obj);
        }
        promise.complete(list);
      }).onFailure(promise::fail);
    });
    return promise.future();
  }

  public static <T> T mapTo(Class<T> clazz, Row row, List<String> columns) {
    try {

      Object obj = clazz.getConstructor().newInstance();
      for (String column: columns) {
        String fieldName = toCamelCase(column);
        Field field = clazz.getDeclaredField(fieldName);
        Class<?> fieldType = field.getType();
        Method setter = clazz.getMethod(getSetter(column), field.getType());
        setter.invoke(obj, row.get(fieldType, column));
      }
      return clazz.cast(obj);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Tuple getTuple(Object...objs) {
    Tuple tuple = Tuple.tuple();
    for (Object obj: objs) {
      tuple.addValue(obj);
    }
    return tuple;
  }
  private static String getSetter(String column) {
    StringBuilder builder = new StringBuilder(SETTER_PREFFIX);
    boolean upper = true;
    for (int i = 0; i < column.length(); i ++) {
      char c = column.charAt(i);
      if (c == UNDER_LINE) {
        upper = true;
        continue;
      }
      if (upper) {
        builder.append(Character.toUpperCase(c));
        upper = false;
      } else {
        builder.append(c);
      }
    }
    return builder.toString();
  }
  private static String toCamelCase(String column) {
    if (column.indexOf(UNDER_LINE) == -1) return column;
    StringBuilder builder = new StringBuilder();
    boolean upper = false;
    for (int i = 0; i < column.length(); i ++) {
      char c = column.charAt(i);
      if (c == UNDER_LINE) {
        upper = true;
        continue;
      }
      if (upper) {
        builder.append(Character.toUpperCase(c));
        upper = false;
      } else {
        builder.append(c);
      }
    }
    return builder.toString();
  }

  public static Future<Integer> selectCount(String sql, Object...objs) {
    Promise<Integer> promise = Promise.promise();
    DBClient.getInstance()
    .getConnection()
    .onSuccess(connection -> {
      connection.preparedQuery(sql).execute(getTuple(objs))
      .onSuccess(rowSet -> {
       Iterator<Row> iterator = rowSet.iterator();
       if (iterator.hasNext()) {
         Row row = iterator.next();
         Integer count = row.getInteger(0);
         promise.complete(count);
       } else {
         promise.complete(null);
       } 
      }).onFailure(promise::fail);
    }).onFailure(promise::fail);
    return promise.future();
  }
  
}

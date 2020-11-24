package com.yeyeck.database;

import io.vertx.core.Future;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnection;

public class DBClient {
  private static final DBClient instance = new DBClient();
  private MySQLPool mySQLPool;

  private DBClient() {
    MySQLConnectOptions connectionOptions = new MySQLConnectOptions()
    .setPort(3306)
    .setHost("47.106.185.245")
    .setDatabase("test")
    .setUser("root")
    .setPassword("cckk00522");
    PoolOptions poolOptions = new PoolOptions()
    .setMaxSize(10);
    this.mySQLPool = MySQLPool.pool(connectionOptions, poolOptions);
  }

  public static DBClient getInstance() {
    return instance;
  }

  public Future<SqlConnection> getConnection() {
    return mySQLPool.getConnection();
  }

}

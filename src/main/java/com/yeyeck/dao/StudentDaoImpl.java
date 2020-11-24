package com.yeyeck.dao;

import java.util.List;

import com.yeyeck.database.DBUtils;

import io.vertx.core.Future;

public class StudentDaoImpl implements StudentDao {

  @Override
  public Future<Student> findById(Integer id) {
    String sql = "select * from t_student where id = ?";
    return DBUtils.selectOne(Student.class, sql, id);
  }

  @Override
  public Future<List<Student>> findAll() {
    String sql = "select * from t_student";
    return DBUtils.selectList(Student.class, sql);
  }

  @Override
  public Future<Integer> countAll() {
    String sql = "select count(id) from t_student";
    return DBUtils.selectCount(sql);
  }
  
}

package com.yeyeck.dao;

import java.util.List;

import com.yeyeck.annotations.Count;
import com.yeyeck.annotations.Mapper;
import com.yeyeck.annotations.SelectOne;

import io.vertx.core.Future;

@Mapper
public interface StudentDao {

  @SelectOne("select * from t_student where id = ?")
  Future<Student> findById(Integer id);

  @SelectOne("select * from t_student")
  Future<List<Student>> findAll();

  @Count("select count(id) from t_student")
  Future<Integer> countAll();
  
}

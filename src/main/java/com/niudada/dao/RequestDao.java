package com.niudada.dao;

import com.niudada.entity.Request;

import java.util.List;
import java.util.Map;

public interface RequestDao {

    //插入
    int create(Request pi);

    //删除
    int delete(Map<String, Object> paramMap);

    //修改
    int update(Map<String, Object> paramMap);

    //查询所有
    List<Request> query(Map<String, Object> paramMap);

    //明细查询（只返回一条数据）
    Request detail(Map<String, Object> paramMap);

    //查询总记录条数
    int count(Map<String, Object> paramMap);
}
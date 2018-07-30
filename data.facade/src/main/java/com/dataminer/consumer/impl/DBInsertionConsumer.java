package com.dataminer.consumer.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.dataminer.consumer.IDataConsumer;
import com.dataminer.db.ConnectionPools;
import com.dataminer.db.PSApplyExplicitParam;
import com.dataminer.db.SQLExecutor;

public class DBInsertionConsumer<T> implements IDataConsumer {

	private Class<T> clazz;
	private String insertSQL;
	private PSApplyExplicitParam<PreparedStatement, T> paramApplyFunc;

	@SuppressWarnings("unchecked")
	public DBInsertionConsumer(String sql, PSApplyExplicitParam<PreparedStatement, T> paramApplyFunc) {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		this.clazz = (Class<T>) params[0];
		this.insertSQL = sql;
		this.paramApplyFunc = paramApplyFunc;
	}

	@Override
	public void consume(String dataAsJson) throws SQLException {
		try (Connection conn = ConnectionPools.get("poolname").getConnection();) {
			ArrayList<T> list = new ArrayList<>();
			String[] lines = dataAsJson.split("\n");
			for (String line : lines) {
				list.add(JSON.parseObject(line, clazz));
			}
			SQLExecutor.through(conn).sql(insertSQL).executeBatch(list, paramApplyFunc);
		}
	}

}

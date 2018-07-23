package com.dataminer.consumer.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.dataminer.consumer.IDataConsumer;
import com.dataminer.data.pojo.TrafficStatus;
import com.dataminer.db.ConnectionPools;
import com.dataminer.db.SQLExecutor;

public class DBInsertionConsumer<T> implements IDataConsumer {
	private String tableName;
	private Class<T> clazz;

	public DBInsertionConsumer(String tableName, Class<T> clazz) {
		this.tableName = tableName;
		this.clazz = clazz;
	}

	@Override
	public void consume(String dataAsJson) throws SQLException {
		try (Connection conn = ConnectionPools.get("poolname").getConnection();) {
			
			ArrayList<T> list = new ArrayList<>();
			String[] lines = dataAsJson.split("\n");
			for (String line : lines) {
				list.add(JSON.parseObject(line, clazz));
			}
			String insertSQL = String.format("insert into %s values (?, ?, ?, ?, ?)", tableName);

			SQLExecutor.through(conn).sql(insertSQL).executeBatch(list, (ps, ele) -> {

				ps.setLong(1, ((TrafficStatus) ele).getRoadId());
				ps.setTimestamp(2, ((TrafficStatus) ele).getTime());
				ps.setFloat(3, ((TrafficStatus) ele).getAvgSpeed());
				ps.setFloat(4, ((TrafficStatus) ele).getAvgDuration());
				ps.setInt(5, ((TrafficStatus) ele).getStatus());

				return ps;
			});
		}
	}
}

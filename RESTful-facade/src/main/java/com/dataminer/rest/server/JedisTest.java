package com.dataminer.rest.server;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class JedisTest {
	public static void main(String[] args) {
		JedisShardInfo shardInfo = new JedisShardInfo("192.168.111.106", 6379, false/* useSsl */);
		// shardInfo.setPassword("<key>"); /* Use your access key. */
		Jedis jedis = new Jedis(shardInfo);
		// jedis.select(1);
		jedis.set("foo", "barbar");
		jedis.close();
	}
}

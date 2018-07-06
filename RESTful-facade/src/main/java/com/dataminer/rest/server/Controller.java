package com.dataminer.rest.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataminer.rest.pojo.Greeting;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

@RestController
public class Controller {
	private Jedis jedis = createRedisClient();
    
    private Jedis createRedisClient() {
		JedisShardInfo shardInfo = new JedisShardInfo("192.168.111.106", 6379, false/*useSsl*/);
	    //shardInfo.setPassword("<key>"); /* Use your access key. */
	    Jedis jedis = new Jedis(shardInfo);
	    jedis.select(1);
	    return jedis;
    }
	
	//private Map<String, Greeting> mockRedisCache = new HashMap<>();
	private static final String GREETINGS = "/greetings";
	private static final String POST_ADD = "/add";

	@RequestMapping(method = RequestMethod.GET, value = GREETINGS)
	public <T> List<T> getGreetings(@RequestParam(value = "name", defaultValue = "latest") String fromWho) {
		return get(GREETINGS, "name", fromWho);
		//return get(GREETING_LATEST, "id=max(id)");
	}

	private <T> List<T> get(String resource, String queryKey, String queryValue) {
		List<T> cache = getCache(resource, queryValue);
		if (Objects.isNull(cache) || cache.isEmpty()) {
			List<T> result = fromDB(resource, query);
			asynCache(resource, query, result);
			return result;
		} else {
			// read from cache
			return cache;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getCache(String resource, String query) {
		// get from redis
		jedis.hget(resource, query);
		
		if (mockRedisCache.containsKey(query)) {
			return List.of((T) mockRedisCache.get(query));
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> fromDB(String resource, String query) {
		// read from database
		return List.of((T) "from DB");
	}

	private <T> boolean toDB(String resource, String query, List<T> result) {
		return true;
	}

	private <T> void asynCache(String resource, String query, List<T> result) {
		mockRedisCache.put(query, new Greeting((String) result.get(0)));
	}

	private <T> void cache(String resource, String query, List<T> result) {
		mockRedisCache.put(query, new Greeting((String) result.get(0)));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = POST_ADD)
	public <T> boolean post(@RequestParam(value = "key", defaultValue = "") String content) {
		// write into cache
		cache(POST_ADD, "key", List.of((T) content));
		// write into DB
		toDB(POST_ADD, "key", List.of((T) content));
		return true;
	}
}

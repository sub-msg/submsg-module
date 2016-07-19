package cn.submsg.message.service;


import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class MessageQueue<T> {
	
	private static Logger logger = Logger.getLogger(MessageQueue.class);

	private String key;
	private  Class<T> cls;
	private JedisPool pool;

	public MessageQueue(JedisPool pool,String key,Class<T> cls) {
		this.pool = pool;
		this.key = key;
    	this.cls =cls;
	}
    /**
     * 阻塞获取元素
     * @return
     */
	public T blockPopMsg() {
		Jedis jedis = pool.getResource();
		try {
			List<String> ret = jedis.blpop(2, key);
			if (ret != null) {
				String value =  ret.get(1);
				if(value!=null){
					return JSON.parseObject(value, cls);
				}
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}
	
    /**
     * 插入元素
     * @param t
     */
	public Long pushMsg(T t) {
		Jedis jedis = pool.getResource();
		try {
			String val = JSON.toJSONString(t);
			return jedis.rpush(key, val);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.returnResource(jedis);
		}
		return null;
	}
	
}

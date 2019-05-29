package redisSATools.basicLoadModeler;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Loader {
	
	private Jedis jedis;
	private JedisPool jedisPool;
	
	public Loader(String host, int port, String password) {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		if(password == null)
			jedisPool = new JedisPool(poolConfig, host, port);
		jedisPool = new JedisPool(poolConfig, host, port, 180, password);
		
	}
	
	public void implementLoad(long windowInSeconds) throws InterruptedException {

		jedis = jedisPool.getResource();
		long counter = 0;
		long timeToEnd = System.currentTimeMillis()+(windowInSeconds*1000);
		System.out.println("Starting window...");
		while(System.currentTimeMillis()<timeToEnd) {
			try {
				jedis.set(counter + "", counter + "");
				counter++;
			}
			catch(Exception e) {
				jedis.close();
				jedis = null;
				System.out.println("Connection lost, retrying...");
				long startTime = System.nanoTime();
				while (jedis == null) {
					try {
						jedis = jedisPool.getResource();
					} catch (Exception e2) {
						Thread.sleep(1000);
					}
				}
				long durationInNanos = System.nanoTime() - startTime;
				double durationInSec = (double) Duration.ofNanos(durationInNanos).toMillis() / 1000;
				System.out.println("Reconnected after "+ durationInSec +" seconds");

			}
			
		}

		System.out.println("Test Run Ended");
		System.out.println("Length of Run: " + windowInSeconds);
		System.out.println("Number of entries added: " + counter);

		try {
			long number_keys_written = jedis.dbSize();
			System.out.println("Number of keys successfully written: " + number_keys_written);
		}
		catch(Exception e) {
			System.out.println("Connection lost, trying to gather percentage lost... do the math on your own");
		}
		
		
		
	}

}

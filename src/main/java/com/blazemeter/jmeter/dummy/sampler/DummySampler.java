package com.blazemeter.jmeter.dummy.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Pipeline;
import java.text.SimpleDateFormat; 

public class DummySampler extends AbstractSampler {

  private static final Logger LOG = LoggerFactory.getLogger(DummySampler.class);

  private static final String RESPONSE_TIME_PROPERTY = "Dummy.responseTime";
  private static final String LABEL = "Dummy.label";
  private static final String RESPONSE_CODE = "Dummy.responseCode";
  private static final String SUCCESS = "Dummy.Success";
  //redis
  private static final String REDIS_SERVER = "Dummy.redisServer";
  private static final String REDIS_PORT = "Dummy.redisPort";
  // redis

  private static final String DEFAULT_ENCODING = null;

  public void setResponseTime(int responseTime) {
    setProperty(RESPONSE_TIME_PROPERTY, responseTime);
  }

  public Integer getResponseTime() {
    return getPropertyAsInt(RESPONSE_TIME_PROPERTY, 1000);
  }

  public void setLabel(String label) {
    setProperty(LABEL, label);
  }

  public String getLabel() {
    return getPropertyAsString(LABEL, Strings.EMPTY);
  }

// redis
  public void setRedisServer(String redisServer) {
    setProperty(REDIS_SERVER, redisServer);
  }

  public String getRedisServer() {
    return getPropertyAsString(REDIS_SERVER, "127.0.0.1");
  }
// redis

// redis
  public void setRedisPort(String redisPort) {
    setProperty(REDIS_PORT, redisPort);
  }

  public String getRedisPort() {
    return getPropertyAsString(REDIS_PORT, "6379");
  }
// redis

  public void setResponseCode(String responseCode) {
    setProperty(RESPONSE_CODE, responseCode);
  }

  public String getResponseCode() {
    return getPropertyAsString(RESPONSE_CODE, "200");
  }

  public void setSuccessful(boolean success) {
    setProperty(SUCCESS, success);
  }

  public boolean getSuccessful() {
    return getPropertyAsBoolean(SUCCESS, true);
  }

  public String getCheckRedisServer(){
  
  String pRedisHost = getRedisServer();
    int pRedisPort = Integer.parseInt(getRedisPort());
    JedisPoolConfig config = new JedisPoolConfig();
    JedisPool pool = new JedisPool(config, pRedisHost , pRedisPort);
    Jedis jedis = null;
    jedis = pool.getResource();
    String redisPong = jedis.ping();
    String redisStatus = getRedisServer() + ":" + getRedisPort() + " - " + redisPong;
    pool.close();
    jedis.close();
  return redisStatus;
  }
  



  public SampleResult sample(Entry entry) {
    SampleResult result = new SampleResult();
    result.setSampleLabel(getLabel());
    result.setResponseCode(getResponseCode());
    result.setResponseMessage("REDIS Test message");
    result.setResponseData(getCheckRedisServer(), "UTF-8"); 
    result.setSuccessful(getSuccessful());

    result.sampleStart();
    try {
      Thread.sleep(getResponseTime());
    } catch (InterruptedException e) {
      result.setSuccessful(false);
      result.setResponseMessage(e.getMessage());
      LOG.error("Error while sleep", e);
      Thread.currentThread().interrupt();
    }
    result.sampleEnd();
    return result;
  }

}


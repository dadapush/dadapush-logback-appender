package com.dadapush.client.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaDaPushAppenderTest {

  private static Logger logger = LoggerFactory.getLogger(DaDaPushAppenderTest.class);

  @Test
  public void testLog() {
    logger.error("test error",new RuntimeException("mock exception"));
  }
}

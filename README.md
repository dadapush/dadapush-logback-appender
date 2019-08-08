## Logback DaDaPush Notification Appender

## Usage

define appender

```xml
  <appender name="DADAPUSH" class="com.dadapush.client.logback.DaDaPushAppender">

    <!-- basePath default value: https://www.dadapush.com -->
     <basePath>https://www.dadapush.com</basePath> 
    <!-- failOnError default value: false-->
    <failOnError>false</failOnError>
    <channelToken>YOUR_CHANNEL_TOKEN</channelToken>

    <titleLayout class="ch.qos.logback.classic.PatternLayout">
      <pattern>[%-5level] %class</pattern>
    </titleLayout>
    <contentLayout class="ch.qos.logback.classic.PatternLayout">
      <pattern>Level: %-5level\nLoggerName: %class\nMessage: %msg%n</pattern>
    </contentLayout>
  </appender>

  <appender name="DADAPUSH_ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="DADAPUSH" />
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
      <level>ERROR</level>
    </filter>
  </appender>
```

use appender
```xml
  <root level="info">
    <appender-ref ref="DADAPUSH_ASYNC" />
  </root>
```
package com.dadapush.client.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.dadapush.client.ApiClient;
import com.dadapush.client.ApiException;
import com.dadapush.client.Configuration;
import com.dadapush.client.api.DaDaPushMessageApi;
import com.dadapush.client.model.MessagePushRequest;
import com.dadapush.client.model.ResultOfMessagePushResponse;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
public class DaDaPushAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

  private static Layout<ILoggingEvent> defaultTitleLayout = new LayoutBase<ILoggingEvent>() {
    public String doLayout(ILoggingEvent event) {
      return "" + event.getLevel();
    }
  };

  private static Layout<ILoggingEvent> defaultContentLayout = new LayoutBase<ILoggingEvent>() {
    public String doLayout(ILoggingEvent event) {
      return "Level: " + event.getLevel() + "\n" +
          "LoggerName: " + event.getLoggerName() + "\n" +
          "Message: " + event.getFormattedMessage() + "\n";
    }
  };

  private DaDaPushMessageApi apiInstance;
  private String basePath = "https://www.dadapush.com";
  private String channelToken;
  private Layout<ILoggingEvent> titleLayout = defaultTitleLayout;
  private Layout<ILoggingEvent> contentLayout = defaultContentLayout;

  public Layout<ILoggingEvent> getTitleLayout() {
    return titleLayout;
  }

  public void setTitleLayout(
      Layout<ILoggingEvent> titleLayout) {
    this.titleLayout = titleLayout;
  }

  public Layout<ILoggingEvent> getContentLayout() {
    return contentLayout;
  }

  public void setContentLayout(
      Layout<ILoggingEvent> contentLayout) {
    this.contentLayout = contentLayout;
  }

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getChannelToken() {
    return channelToken;
  }

  public void setChannelToken(String channelToken) {
    this.channelToken = channelToken;
  }

  protected void append(ILoggingEvent iLoggingEvent) {
    try {
      if (StringUtils.isEmpty(channelToken)) {
        addWarn("not set channelToken");
        return;
      }
      String title = titleLayout.doLayout(iLoggingEvent);
      String content = contentLayout.doLayout(iLoggingEvent);

      MessagePushRequest body = new MessagePushRequest();
      body.setTitle(StringUtils.substring(title, 0, 50));
      body.setContent(StringUtils.substring(content, 0, 500));
      body.setNeedPush(true);
      try {
        ResultOfMessagePushResponse result = apiInstance.createMessage(body, channelToken);
        if (result.getCode() == 0) {
          addInfo("send notification success, messageId=" + result.getData().getMessageId());
        } else {
          addWarn(
              "send notification fail, detail: " + result.getCode() + " " + result.getErrmsg());
        }
      } catch (ApiException e) {
        addError("send notification fail", e);
      }
    } catch (Exception e) {
      addError("send notification fail, iLoggingEvent=" + iLoggingEvent, e);
    }
  }

  @Override
  public void start() {
    super.start();
    ApiClient apiClient = Configuration.getDefaultApiClient();
    apiClient.setBasePath(basePath);
    apiInstance = new DaDaPushMessageApi(apiClient);
  }

}

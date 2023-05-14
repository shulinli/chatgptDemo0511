package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * counter控制器
 */
@RestController

public class CounterController {

  final CounterService counterService;
  final Logger logger;

  public CounterController(@Autowired CounterService counterService) {
    this.counterService = counterService;
    this.logger = LoggerFactory.getLogger(CounterController.class);
  }


  /**
   * 获取当前计数
   * @return API response json
   */
  @GetMapping(value = "/api/count")
  ApiResponse get() {
    logger.info("/api/count get request");
    Optional<Counter> counter = counterService.getCounter(1);
    Integer count = 0;
    if (counter.isPresent()) {
      count = counter.get().getCount();
    }

    return ApiResponse.ok(count);
  }


//  /**
//   * 更新计数，自增或者清零
//   * @param request {@link CounterRequest}
//   * @return API response json
//   */
//  @PostMapping(value = "/api/count")
//  ApiResponse create(@RequestBody CounterRequest request) {
//    logger.info("/api/count post request, action: {}", request.getAction());
//
//    Optional<Counter> curCounter = counterService.getCounter(1);
//    if (request.getAction().equals("inc")) {
//      Integer count = 1;
//      if (curCounter.isPresent()) {
//        count += curCounter.get().getCount();
//      }
//      Counter counter = new Counter();
//      counter.setId(1);
//      counter.setCount(count);
//      counterService.upsertCount(counter);
//      return ApiResponse.ok(count);
//    } else if (request.getAction().equals("clear")) {
//      if (!curCounter.isPresent()) {
//        return ApiResponse.ok(0);
//      }
//      counterService.clearCount(1);
//      return ApiResponse.ok(0);
//    } else {
//      return ApiResponse.error("参数action错误");
//    }
//  }
  /**
   * 更新计数，自增或者清零
   * @param request {@link CounterRequest}
   * @return API response json
   */
  @PostMapping(value = "/api/count")
  @ResponseBody
  Map<String, String> create(@RequestBody Map<String, String> request) throws IOException {
    logger.info("/api/count post request");
    Map<String, String> response = new HashMap<String, String>();
    response.put("ToUserName", request.get("FromUserName"));
    response.put("FromUserName", request.get("ToUserName"));
    response.put("CreateTime", "123456789");
    response.put("MsgType", request.get("MsgType"));


    System.setProperty("http.proxyHost", "127.0.0.1");
    System.setProperty("http.proxyPort", "7890");

    System.setProperty("https.proxyHost", "127.0.0.1");
    System.setProperty("https.proxyPort", "7890");

    OkHttpClient client = new OkHttpClient();

    String url = "https://api.openai.com/v1/chat/completions";

    String apiKey = "Bearer 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";

    String message = "[{\"role\": \"user\", \"content\": \"Hello!\"}]";

    ArrayList<Map<String, String>> list = new ArrayList<>();
    Map<String, String> map = new HashMap<>();
    map.put("role", "user");
    map.put("content", "hello");
    list.add(map);
    Map<String, Object> map2 = new HashMap<>();
    map2.put("model", "gpt-3.5-turbo");
    map2.put("messages", list);
    String str= new JSONObject(map2).toString();

    Request req = new Request.Builder()

            .url(url)

            .addHeader("Authorization", apiKey)
            .addHeader("Content-Type", "application/json")

            .post(okhttp3.RequestBody.create(MediaType.parse("application/json"), str))

            .build();

    Response res = client.newCall(req).execute();

    String responseBody = res.body().string();














    response.put("Content",responseBody);
    return response;
  }




//  @PostMapping(value = "/api/callback")
//  ApiResponse callback(@RequestBody Map<String, String> request) {
//    logger.info("/api/callback post request");
//    Map<String, String> response = new HashMap<String, String>();
//    response.put('ToUserName', request.get('FromUserName'));
//    response.put('FromUserName', request.get('ToUserName'));
//    response.put('CreateTime', 123456789);
//    response.put('MsgType', request.get('MsgType'));
//    response.put('Content','![CDATA[你好]]');
//    return ApiResponse.ok(response);
//
//  }
}
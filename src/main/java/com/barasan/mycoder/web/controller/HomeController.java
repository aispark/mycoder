package com.barasan.mycoder.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.barasan.mycoder.web.service.MycoderService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

  @Resource
  MycoderService mycoderService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() { // jsp 페이지 호출
    return "/home";
  }

  @RequestMapping(value = "/table", method = RequestMethod.POST)
  @ResponseBody
  public List<Map<String, String>> selectColumnList(@RequestBody Map<String, String> paramMap) throws Exception {
    return mycoderService.selectColumnList(paramMap);
  }

}

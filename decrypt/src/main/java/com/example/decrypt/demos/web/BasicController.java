/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.decrypt.demos.web;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {

    // http://127.0.0.1:8080/hello?name=lisi AVAILABLE_QUANTITY 地址
    @PostMapping("/test")
    public String test(@RequestBody String text) {
        text = text.trim();

        // 兼容 "data": "..." 或 data: "..." 等格式
        if (text.toLowerCase().contains("data") && (text.contains(":") || text.contains("："))) {
            String[] parts = text.split("[:：]", 2);
            if (parts.length > 1) {
                text = parts[1].trim();
            }
        }

        // 去掉引号和逗号
        text = text.replace("\"", "").replace("'", "");
        text = text.replace(",", "");
        text = text.trim();

        return SecretUtils.desEncrypt(text);
    }

    // public static void main(String[] arg){
    //
    // ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    // systemClassLoader.loadClass("23");
    // ConcurrentHashMap<Object, Object> objectObjectConcurrentHashMap = new
    // ConcurrentHashMap<>();
    // objectObjectConcurrentHashMap.put("1","1");
    // System.out.println(objectObjectConcurrentHashMap.get("1"));
    // objectObjectConcurrentHashMap.put("1","2");
    // System.out.println(objectObjectConcurrentHashMap.get("1"));
    // }

    public static void main(String[] args) {
        String jsonStr = JSONUtil.toJsonStr("{}");
        String post = HttpUtil.post(
                "https://dy.bii.com.cn:18481/plus/cgi-bin/open/callback?corpId=wwae49a5f164f53f25&secret=04865c9ce85c48719eb85b7973cd06ff&account=18588709127&applicationId=67ecd7a6d69758312986d5c9&taskId=key_1765261386434_80487",
                "");
        System.out.println(post);

    }
    /**
     * This file isn't in your working directory. Teammates you share this request
     * withwon't be able to use this file. To make collaboration easier you can
     * setup your
     * working directory in Settings.
     */

    // // http://127.0.0.1:8080/user
    // @RequestMapping("/user")
    // @ResponseBody
    // public User user() {
    // User user = new User();
    // user.setName("theonefx");
    // user.setAge(666);
    // return user;
    // }
    //
    // // http://127.0.0.1:8080/save_user?name=newName&age=11
    // @RequestMapping("/save_user")
    // @ResponseBody
    // public String saveUser(User u) {
    // return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    // }
    //
    // // http://127.0.0.1:8080/html
    // @RequestMapping("/html")
    // public String html() {
    // return "index.html";
    // }
    //
    // @ModelAttribute
    // public void parseUser(@RequestParam(name = "name", defaultValue = "unknown
    // user") String name
    // , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
    // user.setName("zhangsan");
    // user.setAge(18);
    // }
}

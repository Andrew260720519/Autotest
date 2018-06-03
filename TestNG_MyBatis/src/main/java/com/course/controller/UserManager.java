package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@Api(value = "v1", description = "用户管理系统")
@RequestMapping("v1")
public class UserManager {

    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登陆接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        log.info("查询的结果是" + i);
        if (i == 1){
            log.info("登陆的用户是: " + user.getUserName());
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Boolean addUser(HttpServletRequest request, @RequestBody User user){
        int i = 0;
        if(verifyCookies(request)) {
            i = template.insert("addUser", user);
        }
        if (i > 0){
            log.info("添加用户的数量是: " + i);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户(列表)信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user){
        if (verifyCookies(request)) {
            List<User> users = template.selectList("getUserInfo", user);
            log.info("获取到的用户数量是:" + users.size());
            return users;
        }
        return null;
    }

    @ApiOperation(value = "更新/删除用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(HttpServletRequest request, @RequestBody User user){
        int i = 0;
        if (verifyCookies(request)) {
            i = template.update("updateUserInfo", user);
        }
        log.info("更新用户信息的数量是: " + i);
        return i;
    }

    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)){
            log.info("Cookies为空");
            return false;
        }
        for (Cookie cookie:cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")){
                log.info("Cookies验证通过");
                return true;
            }
        }
        return false;
    }

}

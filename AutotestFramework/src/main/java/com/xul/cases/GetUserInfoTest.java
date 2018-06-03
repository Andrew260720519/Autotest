package com.xul.cases;

import com.xul.config.TestConfig;
import com.xul.model.GetUserInfoCase;
import com.xul.model.User;
import com.xul.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUserInfoTest {

    @Test(dependsOnGroups="loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        //下边为写完接口的代码
//        JSONArray resultJson = getJsonResult(getUserInfoCase);
        String result = getJsonResult(getUserInfoCase);

        Thread.sleep(2000);
        User user = session.selectOne(getUserInfoCase.getExpected(), getUserInfoCase.getUserID());
        System.out.println("自己查库获取用户信息:"+user.toString());

        List userList = new ArrayList();
        userList.add(user);
        JSONArray exceptJsonArray = new JSONArray(userList);
        JSONArray resultJsonArray = new JSONArray(result);

        Assert.assertEquals(exceptJsonArray.toString(),resultJsonArray.toString());
    }

    private String getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserID());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //设置cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
//        List resultList = Arrays.asList(result);
//        JSONArray array = new JSONArray(resultList);
        return result;
    }
}

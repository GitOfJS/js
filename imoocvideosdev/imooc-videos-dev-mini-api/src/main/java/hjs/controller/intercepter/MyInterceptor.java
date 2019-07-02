package hjs.controller.intercepter;

import hjs.utils.IMoocJSONResult;
import hjs.utils.JsonUtils;
import hjs.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    public final String USER_REDIS_SESSION = "user-redis-session";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader("userId");
        String userToken = httpServletRequest.getHeader("userToken");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(userToken)) {
            returnJason(httpServletResponse, IMoocJSONResult.errorMsg("用户未登陆..."));
            return false;
        } else {
            String token = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            if (StringUtils.isEmpty(token) || StringUtils.isBlank(token)) {
                returnJason(httpServletResponse,IMoocJSONResult.errorMsg("用户身份已失效..."));
                return false;
            } else {
                if (!token.equals(userToken)) {
                    returnJason(httpServletResponse,IMoocJSONResult.errorMsg("用户在别的地方登陆..."));
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private void returnJason(HttpServletResponse response, IMoocJSONResult result) {
        OutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package hjs.controller;

import hjs.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BasicController {
    @Autowired
    public RedisOperator redis;

    public final String USER_REDIS_SESSION = "user-redis-session";
}

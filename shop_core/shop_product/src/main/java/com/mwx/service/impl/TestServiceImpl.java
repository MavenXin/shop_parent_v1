package com.mwx.service.impl;

import com.mwx.service.TestService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Hojin
 * @date 2022/11/4 18:21
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 单点高并发:同步锁
     */
    public synchronized void getInfo1() {
        /**
         * 编写代码
         * 如果缓存中有值就加1
         * 没有就初始为1
         */
        String num = (String) redisTemplate.opsForValue().get("num");
        if (null == num) {
            redisTemplate.opsForValue().set("num", "1", 1, TimeUnit.HOURS);
        } else {
            int num1 = Integer.parseInt(num);
            redisTemplate.opsForValue().set("num", String.valueOf(++num1));
        }
    }

    /**
     * 1.分布式高并发:分布式锁 -> 存在的问题 执行业务代码时候 如果出现问题 会导致出现死锁 无法释放锁资源
     * 解决方案: 添加锁的超时时间 超时自动释放
     */
    public void getInfo2() {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", "lock");
        if (flag) {
            //拿到锁资源 执行业务代码
            doBusiness();
            //释放锁
            redisTemplate.delete("lock");
        } else {
            getInfo();
        }
    }

    /**
     * 2.分布式高并发:分布式锁 -> 存在的问题: 锁的持续时间如果少于业务代码执行时间 会导致其他线程 和当前线程一起执行业务代码
     * 解决方案: 给锁增加token 锁只能通过当前线程来释放
     */
    public void getInfo3() {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", "lock", 3, TimeUnit.MINUTES);
        if (flag) {
            //拿到锁资源 执行业务代码
            doBusiness();
            //释放锁
            redisTemplate.delete("lock");
        } else {
            getInfo();
        }
    }

    /**
     * 3.分布式高并发:分布式锁 -> 存在的问题: 判断和删除两句话不具备原子性 可能导致其他删除其他线程的锁
     * 解决方案: 使用lua脚本
     */
    public void getInfo4() {
        String token = UUID.randomUUID().toString();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.MINUTES);
        if (flag) {
            //拿到锁资源 执行业务代码
            doBusiness();
            String redisToken = (String) redisTemplate.opsForValue().get("lock");
            if (redisToken.equals(token)) {
                redisTemplate.delete("lock");
            }
            //释放锁
        } else {
            getInfo();
        }
    }

    /**
     * 3.分布式高并发:分布式锁 -> 存在的问题: 判断和删除两句话不具备原子性 可能导致其他删除其他线程的锁
     * 解决方案: 使用lua脚本
     */
    public void getInfo5() {
        String token = UUID.randomUUID().toString();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.MINUTES);
        if (flag) {
            //拿到锁资源 执行业务代码
            doBusiness();
            //做完业务之后需要删除锁
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            //把脚本放到redisScript中
            redisScript.setScriptText(luaScript);
            //设置脚本的返回类型
            redisScript.setResultType(Long.class);
            redisTemplate.execute(redisScript, Arrays.asList("lock"), token);
           /* String redisToken = (String) redisTemplate.opsForValue().get("lock");
            if (redisToken.equals(token)) {
                redisTemplate.delete("lock");
            }*/
            //释放锁
        } else {
            getInfo();
        }
    }

    /**
     * 解决了 递归调用方法时 会执行拿锁之前的代码 在高并发的环境下 非常的吃性能
     */
    public void getInfo6() {
        String token = UUID.randomUUID().toString();
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.MINUTES);
        if (flag) {
            //拿到锁资源 执行业务代码
            doBusiness();
            //做完业务之后需要删除锁
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            //把脚本放到redisScript中
            redisScript.setScriptText(luaScript);
            //设置脚本的返回类型
            redisScript.setResultType(Long.class);
            redisTemplate.execute(redisScript, Arrays.asList("lock"), token);
           /* String redisToken = (String) redisTemplate.opsForValue().get("lock");
            if (redisToken.equals(token)) {
                redisTemplate.delete("lock");
            }*/
            //释放锁
        } else {
            //自旋等待拿到锁资源
            while (true) {
                boolean retryAcquireLock = redisTemplate.opsForValue().setIfAbsent("lock", token);
                if (retryAcquireLock) {
                    break;
                }
            }
            getInfo();
        }

    }

    private Map<Thread, String> lockMap = new HashMap<>();

    @Override
    public void getInfo() {
        String token = lockMap.get(Thread.currentThread());
        boolean acquiredLock = false;
        if (token != null) {
            //token不为空说明已经拿到锁 不需要重复拿锁
            acquiredLock = true;
        } else {
            //说明token为空 需要拿锁(大概率为第一次进来 没有锁)
            token = UUID.randomUUID().toString();
            acquiredLock = redisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.MINUTES);
        }
        if (acquiredLock) {
            //执行业务代码
            doBusiness();
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            //把脚本放到redisScript中
            redisScript.setScriptText(luaScript);
            //设置脚本的返回类型
            redisScript.setResultType(Long.class);
            redisTemplate.execute(redisScript, Arrays.asList("lock"), token);
            lockMap.clear();
        } else {
            while (true) {
                boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.MINUTES);
                if (lock) {
                    lockMap.put(Thread.currentThread(), token);
                }
                getInfo();
            }
        }

    }


    /***
     * 业务代码
     */
    private void doBusiness() {
        String num = (String) redisTemplate.opsForValue().get("num");
        if (null == num) {
            redisTemplate.opsForValue().set("num", "1", 1, TimeUnit.HOURS);
        } else {
            int num1 = Integer.parseInt(num);
            redisTemplate.opsForValue().set("num", String.valueOf(++num1));
        }
    }
}

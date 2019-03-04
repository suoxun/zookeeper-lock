package com.job.web.scheduling;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.job.web.service.RedisService;


@Component
public class TestTask {
	
	@Autowired
	RedisService redisService;
	
	// 根节点锁节点
	private String ROOT_LOCK="/locks"; 
	
	private CuratorFramework curatorFramework;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void test() {
		try {
			// 创建zk的连接
			curatorFramework = CuratorFrameworkFactory.builder().
	        		// zk集群地址
	                connectString("192.168.138.128:2181,192.168.138.129:2181,192.168.138.130:2181").
	                // session超时时间,如果超过了4秒钟,则进行下面的配置重连操作
	                sessionTimeoutMs(4000).
	                // 如果服务端挂了重连10次,每次间隔1000毫秒
	                retryPolicy(new ExponentialBackoffRetry(1000,
	                        10)).build();
	        // 启动客户端连接zk
	        curatorFramework.start();
	        // 不存在创建一个跟节点
	        if(curatorFramework.checkExists().forPath(ROOT_LOCK) == null){
	            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_LOCK, "0".getBytes());
	        }
	        // 分布式锁
	        final InterProcessMutex lock = new InterProcessMutex(curatorFramework, ROOT_LOCK);
	        System.out.println("jbo-one启动了");
	        // 获取锁,如果获取不到就一直阻塞
	        lock.acquire();
	        // 获得了锁先判断任务是否已经执行完毕(这里可以加日期 xxx-日期 )
	        if(StringUtils.isNotBlank(redisService.getString("job-test"))){
	        	// 证明有其他机器再走定时任务或者执行完毕,直接退出
	        	return;
	        } else {
	        	// 抢占到了锁并且可以其他机器还没有执行任务
	        	// 模拟一分钟处理逻辑
		        System.out.println("jbo-one机器开始处理......");
		        Thread.sleep(60000);
		        System.out.println("jbo-one机器开处理结束......");
		        // 执行完毕赋值redis告诉其他机器定时任务走完了
		        redisService.setString("job-test", "0");
		        // 释放锁
		        lock.release();
		        // 退出定时任务等待下一次
		        return;
	        }
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// 关闭zk的连接
			curatorFramework.close();
		}
	}

}
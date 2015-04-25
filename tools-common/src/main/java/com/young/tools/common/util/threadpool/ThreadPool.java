package com.young.tools.common.util.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ThreadPool {
	
	private static final Log log = LogFactory.getLog(ThreadPool.class);
	
	private ExecutorService threadPool;

	private int corePoolSize = 2;
	private int maximumPoolSize = 4;
	private long keepAliveTime = 1;
	private TimeUnit unit = TimeUnit.HOURS;
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

	public ThreadPool() {
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, unit, workQueue);

	}

	public ThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		this.corePoolSize = corePoolSize;
		this.maximumPoolSize = maximumPoolSize;
		this.keepAliveTime = keepAliveTime;
		this.unit = unit;
		this.workQueue = workQueue;
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, unit, workQueue);
	}

	public void executeThread(Runnable thread) {
		threadPool.execute(thread);
	}

	public void shutDown(boolean is_now) {
		if (is_now) {
			threadPool.shutdownNow();
		} else {
			threadPool.shutdown();
		}
	}
	
	public void monitorThreadPool(int interval,TimeUnit unit) throws InterruptedException{
		long start = System.currentTimeMillis();
		while(true){
			if (!threadPool.awaitTermination(interval, unit)) {
				log.info("--threadPool execute cost time --["
						+ (System.currentTimeMillis() - start) + "]");
			}else{
				log.info("--threadPool execute over cost time  --["+(System.currentTimeMillis()-start)+"]");
				break;
			}
		}
	}
}

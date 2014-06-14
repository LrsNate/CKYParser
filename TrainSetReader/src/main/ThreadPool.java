package main;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class ThreadPool
{
	private static ExecutorService	_instance = null;

	private ThreadPool()
	{
	}
	
	public static ExecutorService getInstance()
	{
		if (ThreadPool._instance == null)
			ThreadPool._instance = Executors.newFixedThreadPool(
					Environment.getNThreads());
		return (ThreadPool._instance);
	}

	public static final void terminate()
	{
		ThreadPool.getInstance().shutdown();
		try
		{
			while (!ThreadPool.getInstance().awaitTermination(1,
					TimeUnit.SECONDS));
		}
		catch (InterruptedException e)
		{
			Messages.error(e.getMessage());
		}
	}
}

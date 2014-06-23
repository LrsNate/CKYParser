package main;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The thread pool.
 *
 */
public final class ThreadPool
{
	private static ExecutorService	_instance = null;

	private ThreadPool()
	{
	}

	/**
	 * Get the instance of the thread pool.
	 * @return The instance of the pool.
	 */
	public static ExecutorService getInstance()
	{
		if (ThreadPool._instance == null)
			ThreadPool._instance = Executors.newFixedThreadPool(
					Environment.getNThreads());
		return (ThreadPool._instance);
	}

	/**
	 * Terminate all tasks.
	 */
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

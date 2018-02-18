
package hello.ehcache;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.transaction.TransactionOptions;

public class HBank3 {

	public static void main(final String[] args) throws Exception {
		Config config = new Config();
		config.getGroupConfig().setName("admin").setPassword("miro1994");
		config.addMapConfig(new MapConfig("default").setReadBackupData(true).setBackupCount(0).setAsyncBackupCount(1));
		HazelcastInstance h2 = Hazelcast.newHazelcastInstance(config);
		IMap<Object, Object> cache = h2.getMap("appContextRegion");
		// cache.clear();
		System.out.println("AKTUALNY ROZMIAR " + cache.size());
		TransactionOptions options = new TransactionOptions()
				.setTransactionType(TransactionOptions.TransactionType.ONE_PHASE);
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
		Random random = new Random(1000);
		for (int i = 0; i < 50; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						long currentTimeMillis = System.nanoTime();
						Task task = new Task(cache, currentTimeMillis,
								"pl.com.stream.next.verto.admi.Script_execute(12,12,12)" + new Date());
						executor.schedule(task, 5, TimeUnit.SECONDS);
						try {
							Thread.sleep(random.nextInt(7000));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						task.stop();
					}
				}
			}).start();
		}

	}

	private static void lock(final long a1, final long a2, final IMap<Object, Object> cache) throws Exception {
		System.out.println("LOCKING " + a1 + " " + a2);
		cache.lock(a1);
		Thread.sleep(20);
		cache.lock(a2);
	}

	private static void unloack(final long a1, final long a2, final IMap<Object, Object> cache) throws Exception {
		cache.unlock(a1);
		cache.unlock(a2);
	}

	public static int random(final int acc1) {
		int i = HBank1.random.nextInt(HBank1.ACCOUNTS) + 1;
		while (i == acc1) {
			i = HBank1.random.nextInt(HBank1.ACCOUNTS) + 1;
		}
		return i;
	}
}


package hello.ehcache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.transaction.TransactionOptions;

public class HBank3 {

	public static void main(final String[] args) throws Exception {
		HazelcastInstance h2 = Hazelcast.newHazelcastInstance();
		IMap<Object, Object> cache = h2.getMap("appContextRegion");
		System.out.println("AKTUALNY ROZMIAR " + cache.size());
		TransactionOptions options = new TransactionOptions()
				.setTransactionType(TransactionOptions.TransactionType.ONE_PHASE);
		while (true) {
			long currentTimeMillis = System.currentTimeMillis();
			Account account = new Account(currentTimeMillis);
			cache.put(currentTimeMillis, account);
			Thread.sleep(500);
			cache.remove(currentTimeMillis);
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

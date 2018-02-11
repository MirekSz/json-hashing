
package hello.ehcache;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HBank2 {

	public static void main(final String[] args) throws Exception {
		HazelcastInstance h2 = Hazelcast.newHazelcastInstance();
		IMap<Object, Object> cache = h2.getMap("appContextRegion");
		System.out.println("AKTUALNY ROZMIAR " + cache.size());
		for (Long i = 1L; i <= Bank1.ITERATIONS; i++) {
			Thread.sleep(30);
			int acc1 = random(0);
			int acc2 = random(acc1);
			// lock(acc1, acc2, cache);

			Account account1 = (Account) cache.get(Long.valueOf(acc1));
			Account account2 = (Account) cache.get(Long.valueOf(acc2));

			account1.balance = account1.balance - 4L;
			account2.balance = account2.balance + 4L;

			cache.put(account1.id, account1);
			cache.put(account2.id, account2);
			// unloack(acc1, acc2, cache);
			System.out.println("FROM " + account1.id + " TO " + account2.id);
		}
		long sum = 0;
		for (Long i = 1L; i <= Bank1.ACCOUNTS; i++) {
			Account account1 = (Account) cache.get(i);
			sum += account1.balance;
			System.out.println(account1.id + " " + account1.balance);
		}
		System.out.println(sum);
	}

	private static void lock(final long a1, final long a2, final IMap<Object, Object> cache) throws Exception {
		if (a1 <= a2) {
			cache.tryLock(a1, 2000, TimeUnit.MILLISECONDS);
			cache.tryLock(a2, 2000, TimeUnit.MILLISECONDS);
		} else {
			cache.tryLock(a2, 2000, TimeUnit.MILLISECONDS);
			cache.tryLock(a1, 2000, TimeUnit.MILLISECONDS);
		}
	}

	private static void unloack(final long a1, final long a2, final IMap<Object, Object> cache) throws Exception {
		if (a1 <= a2) {
			cache.unlock(a1);
			cache.unlock(a2);
		} else {
			cache.unlock(a2);
			cache.unlock(a1);
		}
	}

	public static int random(final int acc1) {
		int i = Bank1.random.nextInt(Bank1.ACCOUNTS) + 1;
		while (i == acc1) {
			i = Bank1.random.nextInt(Bank1.ACCOUNTS) + 1;
		}
		return i;
	}
}

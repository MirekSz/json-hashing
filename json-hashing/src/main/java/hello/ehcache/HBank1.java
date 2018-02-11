
package hello.ehcache;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HBank1 {

	public static final int ACCOUNTS = 1000;
	public static final int ITERATIONS = 10000;
	public static Random random = new Random();

	public static void main(final String[] args) throws Exception {
		HazelcastInstance h1 = Hazelcast.newHazelcastInstance();
		IMap<Object, Object> map = h1.getMap("appContextRegion");
		System.out.println("AKTUALNY ROZMIAR " + map.size());
		for (Long i = 1L; i <= ACCOUNTS; i++) {
			Account account = new Account(i);
			map.put(account.id, account);
		}
		System.out.println("START");
		Thread.sleep(10000);
		for (Long i = 1L; i <= ITERATIONS; i++) {
			Thread.sleep(40);
			int acc1 = random(0);
			int acc2 = random(acc1);
			// lock(acc1, acc2, map);
			Account account1 = (Account) map.get(Long.valueOf(acc1));
			Account account2 = (Account) map.get(Long.valueOf(acc2));

			account1.balance = account1.balance - 3L;
			account2.balance = account2.balance + 3L;

			map.put(account1.id, account1);
			map.put(account2.id, account2);

			// unloack(acc1, acc2, map);
			System.out.println("FROM " + account1.id + " TO " + account2.id);
		}
		long sum = 0;
		for (Long i = 1L; i <= ACCOUNTS; i++) {
			Account account1 = (Account) map.get(i);

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
		int i = random.nextInt(ACCOUNTS) + 1;
		while (i == acc1) {
			i = random.nextInt(ACCOUNTS) + 1;
		}
		return i;
	}
}


package hello.ehcache;

import java.util.Random;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class Bank1 {

	public static final int ACCOUNTS = 100;
	public static final int ITERATIONS = 1000;
	public static Random random = new Random();

	public static void main(final String[] args) throws Exception {
		CacheManager manager = CacheManager.create(Bank1.class.getResource("/ehcache-bank1-cluster.xml"));
		Cache cache = manager.getCache("appContextRegion");
		for (Long i = 1L; i <= ACCOUNTS; i++) {
			Account account = new Account(i);
			cache.put(new Element(account.id, account));
		}
		Thread.sleep(10000);
		for (Long i = 1L; i <= ITERATIONS; i++) {
			Thread.sleep(40);
			int acc1 = random(0);
			int acc2 = random(acc1);
			lock(acc1, acc2, cache);
			Element element1 = cache.get(Long.valueOf(acc1));
			Element element2 = cache.get(Long.valueOf(acc2));

			Account account1 = (Account) element1.getObjectValue();
			Account account2 = (Account) element2.getObjectValue();
			account1.balance = account1.balance - 3L;
			account2.balance = account2.balance + 3L;

			cache.put(new Element(account1.id, account1));
			cache.put(new Element(account2.id, account2));

			unloack(acc1, acc2, cache);
			System.out.println("FROM " + account1.id + " TO " + account2.id);
		}
		long sum = 0;
		for (Long i = 1L; i <= ACCOUNTS; i++) {
			Element element1 = cache.get(i);

			Account account1 = (Account) element1.getObjectValue();
			sum += account1.balance;
			System.out.println(account1.id + " " + account1.balance);
		}
		System.out.println(sum);
	}

	private static void lock(final long a1, final long a2, final Cache cache) throws Exception {
		if (a1 <= a2) {
			cache.tryWriteLockOnKey(a1, 2000);
			cache.tryWriteLockOnKey(a2, 2000);
		} else {
			cache.tryWriteLockOnKey(a2, 2000);
			cache.tryWriteLockOnKey(a1, 2000);
		}
	}

	private static void unloack(final long a1, final long a2, final Cache cache) throws Exception {
		if (a1 <= a2) {
			cache.releaseWriteLockOnKey(a1);
			cache.releaseWriteLockOnKey(a2);
		} else {
			cache.releaseWriteLockOnKey(a2);
			cache.releaseWriteLockOnKey(a1);
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

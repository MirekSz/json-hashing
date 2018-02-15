
package hello.ehcache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;

public class HBank2 {

	public static void main(final String[] args) throws Exception {
		HazelcastInstance h2 = Hazelcast.newHazelcastInstance();
		IMap<Object, Object> cache = h2.getMap("appContextRegion");
		System.out.println("AKTUALNY ROZMIAR " + cache.size());
		TransactionOptions options = new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE);
		for (Long i = 1L; i <= HBank1.ITERATIONS; i++) {
			TransactionContext context = h2.newTransactionContext(options);
			try {
				Thread.sleep(30);
				int acc1 = random(0);
				int acc2 = random(acc1);
				// lock(acc1, acc2, cache);
				context.beginTransaction();

				TransactionalMap<Object, Object> map = context.getMap("appContextRegion");

				Account account1 = (Account) map.getForUpdate(Long.valueOf(acc1));
				Thread.sleep(20);
				Account account2 = (Account) map.getForUpdate(Long.valueOf(acc2));
				account1.balance = account1.balance - 4L;
				account2.balance = account2.balance + 4L;

				map.put(account1.id, account1);
				map.put(account2.id, account2);

				// unloack(acc1, acc2, cache);
				context.commitTransaction();
				System.out.println("FROM " + account1.id + " TO " + account2.id);
			} catch (Exception e) {
				context.rollbackTransaction();
				e.printStackTrace();
			}
		}
		long sum = 0;
		for (Long i = 1L; i <= HBank1.ACCOUNTS; i++) {
			Account account1 = (Account) cache.get(i);
			sum += account1.balance;
			System.out.println(account1.id + " " + account1.balance);
		}
		System.out.println(sum);
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

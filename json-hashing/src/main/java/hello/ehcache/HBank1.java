
package hello.ehcache;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.hazelcast.core.TransactionalMap;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.hazelcast.scheduledexecutor.IScheduledFuture;
import com.hazelcast.scheduledexecutor.ScheduledTaskStatistics;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;

public class HBank1 {

	public static final int ACCOUNTS = 1000;
	public static final int ITERATIONS = 1000;
	public static Random random = new Random();

	public static void main(final String[] args) throws Exception {
		HazelcastInstance h1 = Hazelcast.newHazelcastInstance();
		IScheduledExecutorService executorService = h1.getScheduledExecutorService("zadania");
		IScheduledFuture<?> scheduleAtFixedRate = executorService.scheduleAtFixedRate(new Task(), 5, 2,
				TimeUnit.SECONDS);

		h1.getCluster().addMembershipListener(new MembershipListener() {

			@Override
			public void memberAdded(final MembershipEvent membershipEvent) {
				System.out.println("Witaj " + membershipEvent.getMember().getUuid());

			}

			@Override
			public void memberRemoved(final MembershipEvent membershipEvent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void memberAttributeChanged(final MemberAttributeEvent memberAttributeEvent) {
				// TODO Auto-generated method stub

			}

		});
		TransactionOptions options = new TransactionOptions()
				.setTransactionType(TransactionOptions.TransactionType.ONE_PHASE);

		IMap<Object, Object> cache = h1.getMap("appContextRegion");
		cache.clear();
		System.out.println("AKTUALNY ROZMIAR " + cache.size());
		for (Long i = 1L; i <= ACCOUNTS; i++) {
			Account account = new Account(i);
			cache.put(account.id, account);
		}
		System.out.println("START");
		Thread.sleep(10000);
		ScheduledTaskStatistics stats = scheduleAtFixedRate.getStats();
		System.out.println(stats);
		for (Long i = 1L; i <= ITERATIONS; i++) {
			TransactionContext context = h1.newTransactionContext(options);
			try {
				context.beginTransaction();
				TransactionalMap<Object, Object> map = context.getMap("appContextRegion");
				Thread.sleep(40);
				int acc1 = random(0);
				int acc2 = random(acc1);
				// lock(acc1, acc2, map);
				Account account1 = (Account) map.getForUpdate(Long.valueOf(acc1));
				Thread.sleep(20);
				Account account2 = (Account) map.getForUpdate(Long.valueOf(acc2));

				account1.balance = account1.balance - 3L;
				account2.balance = account2.balance + 3L;

				map.put(account1.id, account1);
				map.put(account2.id, account2);

				// unloack(acc1, acc2, map);
				System.out.println("FROM " + account1.id + " TO " + account2.id);
				context.commitTransaction();
			} catch (Exception e) {
				context.rollbackTransaction();
				e.printStackTrace();
			}
		}
		long sum = 0;
		for (Long i = 1L; i <= ACCOUNTS; i++) {
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
		int i = random.nextInt(ACCOUNTS) + 1;
		while (i == acc1) {
			i = random.nextInt(ACCOUNTS) + 1;
		}
		return i;
	}
}

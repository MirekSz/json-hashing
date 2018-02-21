
package hello.ehcache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.hazelcast.core.QueueStore;

public class QueueStoreImpl implements QueueStore<Account> {

	@Override
	public void store(final Long key, final Account value) {
		System.out.println(key);

	}

	@Override
	public void storeAll(final Map<Long, Account> map) {
		System.out.println(map);

	}

	@Override
	public void delete(final Long key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(final Collection<Long> keys) {
		// TODO Auto-generated method stub

	}

	@Override
	public Account load(final Long key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Account> loadAll(final Collection<Long> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Long> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}

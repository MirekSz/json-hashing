
package hello;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;

public class Haz {

	public static void main(final String[] args) {
		// start the first member
		HazelcastInstance h1 = Hazelcast.newHazelcastInstance();
		// get the map and put 1000 entries
		Map map1 = h1.getMap("testmap");
		HazelcastInstance h2 = Hazelcast.newHazelcastInstance();
		// get the same map from the second member
		IMap map2 = h2.getMap("testmap");
		new Thread(new Runnable() {

			int i = 1;

			@Override
			public void run() {
				while (true) {
					i++;
					map1.put(i, "value" + i);
					try {
						Thread.sleep(20);
						LocalMapStats mapStatistics = map2.getLocalMapStats();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();
		// check the map size
		// start the second member

		LocalMapStats mapStatistics = map2.getLocalMapStats();
		System.out.println("number of entries owned on this member = " + mapStatistics.getOwnedEntryCount());
		// check the size of map2
	}
}

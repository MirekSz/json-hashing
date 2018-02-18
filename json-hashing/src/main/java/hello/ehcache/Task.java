
package hello.ehcache;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;

import com.hazelcast.core.IMap;

public class Task implements Runnable, Serializable {

	public boolean finished;
	private IMap<Object, Object> cache;
	private long currentTimeMillis;
	private String string;
	private boolean sended;

	public Task(IMap<Object, Object> cache, long currentTimeMillis, String string) {
		this.cache = cache;
		// TODO Auto-generated constructor stub
		this.currentTimeMillis = currentTimeMillis;
		this.string = string;
	}

	@Override
	public synchronized void run() {
		if (!finished) {
			this.sended = true;
			System.out.println("Poszlo");
			Map<String, String> map = new HashMap<>();
			map.put("method", "pl.com.stream.next.verto.admi.Script_execute");
			map.put("params", "(12,12,12)");
			map.put("date", FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM)
					.format(new Date()));
			map.put("id", currentTimeMillis + "");
			cache.put(currentTimeMillis, map);
		}
	}

	public synchronized void stop() {
		this.finished = true;
		if (sended) {
			System.out.println("Usuwam");
			cache.remove(currentTimeMillis);
		} else {
			System.out.println("Save");
		}

	}

}

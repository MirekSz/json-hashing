
package hello.ehcache;

import java.io.Serializable;
import java.util.Date;

public class Task implements Runnable, Serializable {

	@Override
	public void run() {
		System.out.println("Wykonanie " + new Date());

	}

}

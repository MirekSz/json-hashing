
package hello;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import ch.qos.logback.classic.LoggerContext;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration.class)
@EnableScheduling
public class Application {

	public static HazelcastInstance h1;
	public static IMap<Object, Object> cache;

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);

		h1 = Hazelcast.newHazelcastInstance();
		cache = h1.getMap("appContextRegion");
	}

	@PostConstruct
	public void init() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.getLogger("ROOT").detachAppender("STDOUT");
	}
}

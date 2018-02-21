
package hello;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration.class)
@EnableScheduling
public class Application {

	public static HazelcastInstance HAZELCAST_INSTANCE;
	public static IMap<Object, Object> CURRENT_OPERATIONS_REGION;

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);

		Config config = new Config();
		config.getGroupConfig().setName("admin").setPassword("randomPaSS");
		config.addMapConfig(new MapConfig("default").setMaxSizeConfig(new MaxSizeConfig(1000, MaxSizePolicy.PER_NODE))
				.setEvictionPolicy(EvictionPolicy.LRU).setReadBackupData(true).setBackupCount(0).setAsyncBackupCount(1));
		HAZELCAST_INSTANCE = Hazelcast.newHazelcastInstance(config);
		CURRENT_OPERATIONS_REGION = HAZELCAST_INSTANCE.getMap("current-operations-region");
		CURRENT_OPERATIONS_REGION.clear();
	}

	@PostConstruct
	public void init() {
		// LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// lc.getLogger("ROOT").detachAppender("STDOUT");
	}
}


package hello;

import java.util.HashMap;
import java.util.Map;

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

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);

		Config config = new Config();
		config.getGroupConfig().setName("admin").setPassword("randomPaSS");
		config.addMapConfig(new MapConfig("default").setMaxSizeConfig(new MaxSizeConfig(1000, MaxSizePolicy.PER_NODE))
				.setEvictionPolicy(EvictionPolicy.LRU).setReadBackupData(true).setBackupCount(0)
				.setAsyncBackupCount(1));
		HAZELCAST_INSTANCE = Hazelcast.newHazelcastInstance(config);
		CURRENT_OPERATIONS_REGION = HAZELCAST_INSTANCE.getMap("current-operations-region");
		// CURRENT_OPERATIONS_REGION.clear();

		Thread.sleep(10000);
		Map<String, String> prepareParams = prepareParams();
		CURRENT_OPERATIONS_REGION.put("1", prepareParams);
		prepareParams.put("type", "T");
		Thread.sleep(15000);

		Map<String, String> prepareParams2 = prepareParams();
		prepareParams2.put("type", "A");
		prepareParams2.put(STOP_DATE_PARAM, "2018-01-03 15:06");
		CURRENT_OPERATIONS_REGION.put("2", prepareParams2);
		Thread.sleep(15000);
		Map<String, String> prepareParams3 = prepareParams();
		prepareParams3.put(STOP_DATE_PARAM, "2018-01-03 14:59");
		CURRENT_OPERATIONS_REGION.put("3", prepareParams3);

		prepareParams.put(STOP_DATE_PARAM, "2018-01-04 15:13");
		CURRENT_OPERATIONS_REGION.put("1", prepareParams);
	}

	protected static final String STOP_DATE_PARAM = "stopDate";
	protected static final String START_DATE_PARAM = "startDate";
	protected static final String ARGUMENTS_PARAM = "arguments";
	protected static final String METHOD_NAME_PARAM = "methodName";
	protected static final String SERVICE_PARAM = "service";
	protected static final String OPERATOR_PARAM = "operator";
	private static final String ID_PARAM = "id";

	private static Map<String, String> prepareParams() {
		Map<String, String> operationInfo = new HashMap<>();
		operationInfo.put(OPERATOR_PARAM, "Mirek Szajowski");
		operationInfo.put(SERVICE_PARAM, "org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration");
		operationInfo.put(METHOD_NAME_PARAM, "insert");
		operationInfo.put(ARGUMENTS_PARAM, "");
		operationInfo.put(START_DATE_PARAM, "2018-01-03 14:56");
		operationInfo.put("type", "F");
		operationInfo.put("id", System.nanoTime() + "");
		return operationInfo;
	}

	@PostConstruct
	public void init() {
		// LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// lc.getLogger("ROOT").detachAppender("STDOUT");
	}
}

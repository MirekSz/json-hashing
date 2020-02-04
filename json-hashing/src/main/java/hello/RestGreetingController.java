
package hello;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.DistributedObject;

@RestController
public class RestGreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@PostMapping("/greeting")
	@ResponseBody
	public Person greeting(@RequestBody final Person person) {
		return person;
	}

	@PostMapping("/greeting/add")
	@ResponseBody
	public Person greeting2(@RequestBody final Person person) {
		return person;
	}

	@GetMapping("/state")
	@ResponseBody
	public Map<String, Object> getState() {
		Map<String, Object> map = new HashMap<>();
		map.put("members", Application.HAZELCAST_INSTANCE.getCluster().getMembers().size());
		map.put("membersView", Application.HAZELCAST_INSTANCE.getCluster().getMembers().stream()
				.map(i -> i.getAddress().getHost() + ":" + i.getAddress().getPort()).collect(Collectors.toList()));
		map.put("backups", Application.CURRENT_OPERATIONS_REGION.getLocalMapStats().getBackupEntryCount());
		map.put("local", Application.CURRENT_OPERATIONS_REGION.getLocalMapStats().getOwnedEntryCount());
		map.put("size", Application.CURRENT_OPERATIONS_REGION.size());
		map.put("killers", Application.CURRENT_OPERATIONS_REGION.entrySet().stream().map(i -> i.getValue()).collect(Collectors.toList()));
		map.put("maps",
				Application.HAZELCAST_INSTANCE.getDistributedObjects().stream().map(DistributedObject::getName).distinct()
						.map(name -> Application.HAZELCAST_INSTANCE.getMap(name))
						.map(el -> new MapDef(el.getName(), el.getLocalMapStats().getBackupEntryCount(),
								el.getLocalMapStats().getOwnedEntryCount(), el.getLocalMapStats().getEventOperationCount(), el.size()))
						.collect(toList()));
		return map;
	}

}

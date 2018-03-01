
package hello;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class HTMLController {

	@Value("${app.version}")
	String appVersion;

	private SseEmitter ssEmitter;

	// @Scheduled(fixedDelay = 5000, initialDelay = 10000)
	// private void getStats() throws Exception {
	// Map<String, Object> map = new HashMap<>();
	// map.put("members", Application.HAZELCAST_INSTANCE.getCluster().getMembers().size());
	// map.put("membersView", Application.HAZELCAST_INSTANCE.getCluster().getMembers().stream()
	// .map(i -> i.getAddress().getHost() + ":" + i.getAddress().getPort()).collect(Collectors.toList()));
	// map.put("backups", Application.CURRENT_OPERATIONS_REGION.getLocalMapStats().getBackupEntryCount());
	// map.put("local", Application.CURRENT_OPERATIONS_REGION.getLocalMapStats().getOwnedEntryCount());
	// map.put("size", Application.CURRENT_OPERATIONS_REGION.size());
	// map.put("killers", Application.CURRENT_OPERATIONS_REGION.entrySet().stream().map(i -> i.getValue())
	// .collect(Collectors.toList()));
	// if (ssEmitter != null) {
	// ssEmitter.send(map);
	// }
	//
	// }

	@RequestMapping("/hashing")
	public String welcome(final Map<String, Object> model) {
		model.put("message", "Abc");
		model.put("appVersion", appVersion);
		return "hashing";
	}

	@ResponseBody
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public SseEmitter stream() {
		ssEmitter = new SseEmitter();
		return ssEmitter;
	}

	@RequestMapping("/")
	public String stats() {
		return "vk";
	}
}

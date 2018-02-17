
package hello;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class GreetingController {

	@Value("${app.version}")
	String appVersion;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	private SseEmitter ssEmitter;

	@Scheduled(fixedDelay = 1000)
	private void getStats() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("members", Application.h1.getCluster().getMembers().size());
		map.put("membersView", Application.h1.getCluster().getMembers().stream()
				.map(i -> i.getAddress().getHost() + " " + i.getAddress().getPort()).collect(Collectors.toList()));
		map.put("backups", Application.cache.getLocalMapStats().getBackupEntryCount());
		map.put("local", Application.cache.getLocalMapStats().getOwnedEntryCount());
		map.put("size", Application.cache.size());
		ssEmitter.send(map);

	}

	@RequestMapping("/")
	public String welcome(final Map<String, Object> model) {
		model.put("message", "Abc");
		model.put("appVersion", appVersion);
		return "home";
	}

	@ResponseBody
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public SseEmitter stream() {
		ssEmitter = new SseEmitter();
		return ssEmitter;
	}

	@RequestMapping("/stats")
	public String stats() {
		return "stats";
	}
}

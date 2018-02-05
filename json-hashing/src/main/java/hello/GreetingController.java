
package hello;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {

	@Value("${app.version}")
	String appVersion;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/")
	public String welcome(final Map<String, Object> model) {
		model.put("message", "Abc");
		model.put("appVersion", appVersion);
		return "home";
	}
}

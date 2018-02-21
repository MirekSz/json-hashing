
package hello;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpJSONDecodingFilter extends OncePerRequestFilter implements Ordered {

	Logger logger = Logger.getLogger(HttpJSONDecodingFilter.class);
	private static final String SE_HEADER = "se";

	public static boolean ENABLED = true;

	@Value("${app.version}")
	String appVersion;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.CONTENT_TYPE);
		boolean isJsonType = header != null && request.getHeader(HttpHeaders.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE);

		if (ENABLED && isJsonType && request.getHeader(SE_HEADER) != null) {
			DecodingJSONRequestWrapper requestWrapper = new DecodingJSONRequestWrapper(request, appVersion, objectMapper);
			filterChain.doFilter(requestWrapper, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}

}

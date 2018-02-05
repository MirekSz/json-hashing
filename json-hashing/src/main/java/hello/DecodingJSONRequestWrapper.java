
package hello;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DecodingJSONRequestWrapper extends HttpServletRequestWrapper {

	private static final String UTF_8 = "UTF-8";

	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private TeeInputStream tee;

	public DecodingJSONRequestWrapper(final HttpServletRequest request, final String appVersion, final ObjectMapper mapper) {
		super(request);
		decodeJSONRequest(request, appVersion, mapper);
	}

	private void decodeJSONRequest(final HttpServletRequest request, final String appVersion, final ObjectMapper mapper) {
		try {

			AesUtil aesUtil = new AesUtil();
			JSONRequest readValue = mapper.readValue(request.getInputStream(), JSONRequest.class);

			String salt = readValue.getJwt();
			String four = readValue.getSha();
			String decoded = aesUtil.decrypt(salt, four, appVersion, readValue.getKerberos());

			tee = new TeeInputStream(IOUtils.toInputStream(decoded, Charset.forName(UTF_8)), bos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(final ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return tee.read();
			}
		};
	}

	public byte[] toByteArray() {

		return bos.toByteArray();
	}

	private static class JSONRequest {

		private String kerberos;
		private String sha;
		private String jwt;

		public String getKerberos() {
			return kerberos;
		}

		public void setKerberos(final String kerberos) {
			this.kerberos = kerberos;
		}

		public String getSha() {
			return sha;
		}

		public void setSha(final String sha) {
			this.sha = sha;
		}

		public String getJwt() {
			return jwt;
		}

		public void setJwt(final String jwt) {
			this.jwt = jwt;
		}
	}
}

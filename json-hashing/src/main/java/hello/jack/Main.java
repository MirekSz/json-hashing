
package hello.jack;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class Main {

	public static ObjectMapper objectMapper;

	public static void main(final String[] args) throws Throwable {
		objectMapper = new ObjectMapper();
		objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		SimpleModule module = new SimpleModule();
		module.addSerializer(OptionalValue.class, new OptionalValueSerializer());
		module.addDeserializer(OptionalValue.class, new OptionalValueDeserializer());
		objectMapper.registerModule(module);

		Data data = new Data();
		data.setVal("mirek");
		data.setVal2(BigDecimal.TEN);

		String writeValueAsString = objectMapper.writeValueAsString(data);
		System.out.println(writeValueAsString);

		Data readValue = objectMapper.readValue("{\"val\":\"mirek\"}", Data.class);
		System.out.println(readValue.getVal());
		System.out.println(readValue.getVal2());
	}

}

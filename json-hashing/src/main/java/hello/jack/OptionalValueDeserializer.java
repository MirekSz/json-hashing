
package hello.jack;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class OptionalValueDeserializer extends StdDeserializer<OptionalValue> implements ContextualDeserializer {

	public OptionalValueDeserializer() {
		this(null);
	}

	public OptionalValueDeserializer(final JavaType javaType) {
		super(javaType);
	}

	@SuppressWarnings("deprecation")
	@Override
	public OptionalValue deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String asText = node.asText();
		Class<?> valueClass = getValueClass();

		try {
			return OptionalValue.of(valueClass.getConstructor(String.class).newInstance(asText));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public JsonDeserializer<?> createContextual(final DeserializationContext ctxt, final BeanProperty property)
			throws JsonMappingException {
		JavaType type = ctxt.getContextualType() != null ? ctxt.getContextualType() : property.getMember().getType();
		JavaType javaType = type.getBindings().getTypeParameters().get(0);
		return new OptionalValueDeserializer(javaType);
	}

}

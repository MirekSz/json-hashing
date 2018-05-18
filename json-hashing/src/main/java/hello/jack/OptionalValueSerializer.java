
package hello.jack;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OptionalValueSerializer extends StdSerializer<OptionalValue> {

	public OptionalValueSerializer() {
		this(null);
	}

	public OptionalValueSerializer(final Class<OptionalValue> t) {
		super(t);
	}

	@Override
	public void serialize(final OptionalValue arg0, final JsonGenerator arg1, final SerializerProvider arg2) throws IOException {
		arg1.writeObject(arg0.getValue());
	}

}


package hello.jack;

public class OptionalValue<T> {

	private T value;

	public OptionalValue(final T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(final T value) {
		this.value = value;
	}

	public static <T> OptionalValue<T> of(final T string) {
		return new OptionalValue(string);
	}

	public static <T> OptionalValue<T> empty() {
		return new OptionalValue(null);
	}

	@Override
	public String toString() {
		return super.toString() + " " + value;
	}
}

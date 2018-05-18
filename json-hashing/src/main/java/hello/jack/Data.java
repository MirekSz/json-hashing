
package hello.jack;

import java.math.BigDecimal;

public class Data extends BaseWO {

	private OptionalValue<String> val;
	private OptionalValue<BigDecimal> val2;

	public OptionalValue<String> getVal() {
		return val;
	}

	public void setVal(final String string) {
		this.val = OptionalValue.of(string);
	}

	public OptionalValue<BigDecimal> getVal2() {
		return val2;
	}

	public void setVal2(final OptionalValue<BigDecimal> val2) {
		this.val2 = val2;
	}

	public void setVal2(final BigDecimal ten) {
		this.val2 = OptionalValue.of(ten);
	}
}


package hello.jack;

import java.lang.reflect.Field;

public class BaseWO {

	public BaseWO() {
		Field[] declaredFields = this.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				field.set(this, OptionalValue.empty());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

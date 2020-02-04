
package hello;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

@ApiModel
public class Person {

	@ApiModelProperty(required = false, notes = "wiek")
	private long age;
	@ApiModelProperty(required = true)
	private String name;
	@ApiModelProperty(required = false, notes = "Nazwisko", accessMode = AccessMode.READ_ONLY)
	private String lastName;
	@ApiModelProperty(required = true, value = "Grupa operatora")
	private Group group;

	public long getAge() {
		return age;
	}

	public void setAge(final long age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(final Group group) {
		this.group = group;
	}

}

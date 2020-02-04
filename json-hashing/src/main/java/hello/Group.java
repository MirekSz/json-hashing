
package hello;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class Group {

	private String name;
	@ApiModelProperty(required = true, value = "Nazwisko", accessMode = AccessMode.READ_ONLY)
	@Size(max = 10)
	private String lastName;

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

}

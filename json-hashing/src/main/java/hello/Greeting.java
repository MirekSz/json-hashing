package hello;

public class Greeting {

	private long id;
	private String content;

	private Greeting() {

	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}

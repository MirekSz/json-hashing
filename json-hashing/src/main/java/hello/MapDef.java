
package hello;

public class MapDef {

	private String name;
	private Long backups;
	private Long local;
	private int size;
	private final long events;

	public MapDef(final String name, final long backupEntryCount, final long ownedEntryCount, final long events, final int size) {
		this.name = name;
		backups = backupEntryCount;
		local = ownedEntryCount;
		this.events = events;
		this.size = size;
	}

	public Long getLocal() {
		return local;
	}

	public void setLocal(final Long local) {
		this.local = local;
	}

	public int getSize() {
		return size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public Long getBackups() {
		return backups;
	}

	public void setBackups(final Long backups) {
		this.backups = backups;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public long getEvents() {
		return events;
	}
}

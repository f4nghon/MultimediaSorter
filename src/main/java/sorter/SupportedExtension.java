package sorter;

public enum SupportedExtension {

	MP4("mp4"), JPG("jpg");

	private String extension;

	SupportedExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

}

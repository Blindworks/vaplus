package de.vaplus.client.pojo;

public class FEPlugin {
	
	private String library;
	private String file;
	
	public FEPlugin(String library, String file){
		this.setLibrary(library);
		this.setFile(file);
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
}

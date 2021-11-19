package models;

/**
 * License model for GitHub code repositories.
 * 
 * @author Tayeeb Hasan
 * @version 1.0.0
 */
public class License {
    public String key;
    public String name;
    /**
     * @return String key of the license
     */
	public String getKey() {
		return this.key;
	}

    /**
     * @return String name of the license
     */
	public String getName() {
		return this.name;
	}

    /**
     * This method sets the Key of the license.
     * @param key key of the license.
     * @return Nothing.
     */
	public void setKey(String key) {
		this.key = key;
	}

    /**
     * This method sets the Name of the license.
     * @param name name of the license.
     * @return Nothing.
     */
	public void setName(String name) {
		this.name = name;
	}
}

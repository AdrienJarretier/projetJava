package model;

/**
 *
 * on met des attributs public pour eviter d'avoir a faire des accessseurs pour
 * une classe ayant une utilite ephemere
 * 
 * @author Jarretier Adrien "jarretier.adrien@gmail.com"
 */
public class GitObjectProperty {

	public String name;
	public GitObjectPropertyType type;
	public Object value;

	public GitObjectProperty(String name, GitObjectPropertyType type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

}

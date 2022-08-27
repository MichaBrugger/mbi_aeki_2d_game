package aeki;

public class Material {
  private String name;

  /**
   * Constructor for objects of class Material
   * 
   * @param name the name of the material as a String
   */
  public Material(String name) {
    this.name = name;
  }

  /**
   * getter for the material name
   * 
   * @return the name of the material as a String
   */
  public String getName() {
    return name;
  }

  /**
   * setter for the material name
   * 
   * @param name the name of the material as a String
   */
  public void setName(String name) {
    this.name = name;
  }

}

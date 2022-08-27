package aeki;

import java.util.*;

public class Product {
  private static int counter;
  public String productId;
  public String name;
  public HashMap<Material, Integer> matRequired;
  public LinkedHashMap<Machine, Integer> machineRequired = new LinkedHashMap<Machine, Integer>();

  /**
   * Constructor for Product.
   * 
   * @param name        The name of the product.
   * @param matRequired The materials and the quantity of each material necessary
   *                    to create the product
   */
  public Product(String name, HashMap<Material, Integer> matRequired) {
    Product.counter++;
    this.productId = "PR" + Product.counter;
    this.name = name;
    this.matRequired = matRequired;

    System.out.println("Catalogue: " + name + " is now available as a product!");
  }

  /**
   * Print function for a product (it's a convenience thing). It allows us to
   * quickly get an overview over the product and the required material.
   */
  public void print() {
    System.out.println("-----\n" + this.name + ":");
    for (Material mat : matRequired.keySet()) {
      System.out.println(mat.getName() + " (" + matRequired.get(mat) + ")");
    }
    System.out.println("-----");
  }

  /**
   * Setter for the product name
   * 
   * @param name The new name of the product
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for the product name
   * 
   * @return The name of the product
   */
  public String getName() {
    return name;
  }

  /**
   * Adding a materialRequirement to the product
   * 
   * @param matName The name of the material
   * @param quant   The quantity of the material
   */
  public void addMaterialRequirement(Material matName, int quant) {
    this.matRequired.put(matName, quant);
  }

  /**
   * Allows us to remove requirements if necessary
   * 
   * @param matName The name of the material
   */
  public void removeMaterialRequirement(Material matName) {
    this.matRequired.remove(matName);
  }

  /**
   * returns the hashmap with the materials and the quantity required for a
   * specific product
   * 
   * @return The materials and the quantity of each material necessary to create
   */
  public HashMap<Material, Integer> getMaterialRequirements() {
    return this.matRequired;
  }

  /**
   * Adding a machineRequirement to the product
   * 
   * @param machineName The name of the machine
   * @param quant       The quantity of the machine
   */
  public void addMachineRequirement(Machine machine, int quant) {
    machineRequired.put(machine, quant);
  }

  /**
   * returns the hashmap with the machines and the quantity required for a
   * specific product
   * 
   * @return The machines and the quantity of each machine necessary to create
   */
  public LinkedHashMap<Machine, Integer> getMachineRequirements() {
    // return copy of machineRequired
    return (LinkedHashMap<Machine, Integer>) machineRequired.clone();

  }

  /**
   * Getter for the product id
   * 
   * @return The product id
   */
  public String getProductId() {
    return this.productId;
  }
}

package main;

import building.Building;
import controller.BuildingController;
import view.BuildingDisplay;

/**
 * MainConsole is the main class for the elevator simulation.
 * It initializes the building model, the view,
 * and the controller. It also starts the controller which manages the interaction between the view
 * and the model.
 */
public class MainConsole {
  /**
   * Main method for the elevator simulation. It initializes the building model,
   * the view, and the controller.
   * It also starts the controller which manages the interaction between the view and the model.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(() -> {
      // Define configuration for the building
      int numFloors = 11; // Example: 10 floors
      int numElevators = 8; // Example: 3 elevators
      int elevatorCapacity = 8; // Example: 8 people per elevator

      Building building;
      try {
        building = new Building(numFloors, numElevators, elevatorCapacity);
      } catch (IllegalArgumentException e) {
        System.out.println("Failed to initialize building: " + e.getMessage());
        return;
      }

      // Initialize the GUI view for the building's elevator system
      BuildingDisplay display = new BuildingDisplay();

      // Initialize the controller and connect it with the building and the display
      BuildingController controller = new BuildingController(building, display);

      // Display the GUI
      display.setVisible(true);

      // Start the controller which manages the interaction between the view and the model
      controller.go();
    });
  }
}

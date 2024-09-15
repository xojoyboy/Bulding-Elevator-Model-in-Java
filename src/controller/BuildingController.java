package controller;

import building.Building;
import building.BuildingReport;
import java.util.Objects;
import scanerzus.Request;
import view.BuildingDisplayInterface;

/**
 * BuildingController is the controller for the elevator simulation. It manages the interaction
 * between the view and the model. It listens for user actions and updates the model accordingly.
 */
public class BuildingController implements BuildingControllerInterface {
  private final Building building;
  private final BuildingDisplayInterface display;

  /**
   * Constructs a new BuildingController with the given building and display.
   *
   * @param building The building model
   * @param display The display view
   */
  public BuildingController(Building building, BuildingDisplayInterface display) {
    this.building = Objects.requireNonNull(building, "Building must not be null");
    this.display = Objects.requireNonNull(display, "Display must not be null");

    // Setup listeners that link user actions to model updates
    display.setUpdateListener(this::updateDisplay);
    display.setRequestListener(this::processRequest);
    display.setStepListener(this::step);
    display.setStartListener(this::startSystem);
    display.setStopListener(this::stopSystem);
  }

  private void updateDisplay() {
    BuildingReport report = building.getStatusElevatorSystem();
    display.update(report);
  }

  private void startSystem() {
    try {
      building.startElevatorSystem();
      display.showStatus("Elevator system started.");
    } catch (IllegalStateException e) {
      display.showError("Failed to start system: " + e.getMessage());
    }
  }

  private void stopSystem() {
    try {
      building.stopElevatorSystem();
      display.showStatus("Elevator system stopping.");
    } catch (IllegalStateException e) {
      display.showError("Failed to stop system: " + e.getMessage());
    }
  }

  private void step() {
    building.stepElevatorSystem();
    display.showStatus("Elevator system stepped.");
  }

  private void processRequest(Request request) {
    try {
      building.addRequestToElevatorSystem(request);
      display.showStatus("Request added: From " + request.getStartFloor()
          + " to " + request.getEndFloor());
    } catch (IllegalArgumentException | IllegalStateException e) {
      display.showError("Failed to add request: " + e.getMessage());
    }
  }

  public void go() {
    display.start();
  }
}

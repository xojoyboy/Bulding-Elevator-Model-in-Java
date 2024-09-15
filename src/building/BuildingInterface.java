package building;

import scanerzus.Request;

/**
 * This is the interface for the building class.
 * It defines the methods that are available to interact with the building object.
 * The building object is responsible for managing the elevator system and
 * distributing requests to the elevators.
 */
public interface BuildingInterface {
  int getNumberOfFloors();

  int getNumberOfElevators();

  int getElevatorCapacity();

  void startElevatorSystem();

  void stopElevatorSystem();

  BuildingReport getStatusElevatorSystem();

  void addRequestToElevatorSystem(Request paramRequest);

  void stepElevatorSystem();
}

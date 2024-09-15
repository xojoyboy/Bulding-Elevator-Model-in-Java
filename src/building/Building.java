package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import scanerzus.Request;

/**
 * This is the building class for the elevator system.
 * It is responsible for managing the elevator system and distributing requests to the elevators.
 * The building object is responsible for managing the elevator system and
 * distributing requests to the elevators.
 * It implements the BuildingInterface.
 */
public class Building implements BuildingInterface {
  private final int numberOfFloors;

  private final int numberOfElevators;

  private final int elevatorCapacity;

  private final ElevatorInterface[] elevators;

  private final List<Request> upRequests = new ArrayList<>();

  private final List<Request> downRequests = new ArrayList<>();

  private ElevatorSystemStatus elevatorsStatus;

  /**
   * The constructor for the building class.
   * It initializes the building with the given number of floors, elevators, and elevator capacity.
   *
   * @param numberOfFloors The number of floors in the building.
   * @param numberOfElevators The number of elevators in the building.
   * @param elevatorCapacity The capacity of each elevator.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity)
      throws IllegalArgumentException {
    if (numberOfFloors < 2) {
      throw new IllegalArgumentException("numberOfFloors must be greater than or equal to 2");
    }
    if (numberOfElevators < 1) {
      throw new IllegalArgumentException("numberOfElevators must be greater than or equal to 1");
    }
    if (elevatorCapacity < 1) {
      throw new IllegalArgumentException("maxOccupancy must be greater than or equal to 1");
    }
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new Elevator[numberOfElevators];
    IntStream.range(0, numberOfElevators).forEach(i -> this.elevators[i] =
        new Elevator(numberOfFloors, this.elevatorCapacity));
    this.elevatorsStatus = ElevatorSystemStatus.outOfService;
  }

  /**
   * Starts the elevator system.
   * Checks if the elevator system is already running or stopping and
   * throws an exception if it is.
   * If not, starts all the elevators in the system.
   */
  public void startElevatorSystem() {
    if (this.elevatorsStatus == ElevatorSystemStatus.running) {
      return;
    }
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator cannot be started until it is stopped");
    }
    Arrays.stream(this.elevators).forEach(ElevatorInterface::start);
    this.elevatorsStatus = ElevatorSystemStatus.running;
  }

  /**
   * Stops the elevator system.
   * Checks if the elevator system is already out of service or stopping and
   * throws an exception if it is.
   * If not, stops all the elevators in the system by taking them out of service.
   * Clears all the requests in the system.
   * Sets the elevator system status to stopping.
   */
  public void stopElevatorSystem() {
    if (this.elevatorsStatus != ElevatorSystemStatus.outOfService
        && this.elevatorsStatus != ElevatorSystemStatus.stopping) {
      for (ElevatorInterface elevator : this.elevators) {
        elevator.takeOutOfService();
        this.elevatorsStatus = ElevatorSystemStatus.stopping;
        this.upRequests.clear();
        this.downRequests.clear();
      }
    }
  }

  public int getNumberOfFloors() {
    return this.numberOfFloors;
  }

  public int getNumberOfElevators() {
    return this.numberOfElevators;
  }

  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  /**
   * Gets the status of the elevator system.
   * Returns a BuildingReport object that contains the status of the elevators,
   * the up and down requests, and the status of the elevator system.
   *
   * @return The status of the elevator system.
   */
  public BuildingReport getStatusElevatorSystem() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.elevators.length];
    for (int i = 0; i < this.elevators.length; i++) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }
    return new BuildingReport(this.numberOfFloors, this.numberOfElevators,
        this.elevatorCapacity, elevatorReports,
            this.upRequests, this.downRequests, this.elevatorsStatus);
  }

  /**
   * Adds a request to the elevator system.
   * Checks if the elevator system is out of service or stopping and
   * throws an exception if it is.
   * Checks if the request is null and throws an exception if it is.
   * Checks if the start floor is less than 0 or greater than or equal to the number of floors
   * and throws an exception if it is.
   * Checks if the end floor is less than 0 or greater than or equal to the number of floors
   * and throws an exception if it is.
   * Checks if the start floor is equal to the end floor and throws an exception if it is.
   * Adds the request to the up requests if the start floor is less than the end floor.
   * Adds the request to the down requests if the start floor is greater than the end floor.
   *
   * @param request The request to add to the elevator system.
   */
  public void addRequestToElevatorSystem(Request request) {
    if (this.elevatorsStatus == ElevatorSystemStatus.outOfService || this.elevatorsStatus
        == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system not accepting requests.");
    }
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    if (request.getStartFloor() < 0 || request.getStartFloor() >= this.numberOfFloors) {
      throw new IllegalArgumentException("Start floor must be between 0 and "
          + (this.numberOfFloors - 1));
    }
    if (request.getEndFloor() < 0 || request.getEndFloor() >= this.numberOfFloors) {
      throw new IllegalArgumentException("End floor must be between 0 and "
          + (this.numberOfFloors - 1));
    }

    if (request.getStartFloor() == request.getEndFloor()) {
      throw new IllegalArgumentException("Start floor and end floor cannot be the same");
    }
    if (request.getStartFloor() < request.getEndFloor()) {
      this.upRequests.add(request);
    } else {
      this.downRequests.add(request);
    }
  }

  /**
   * Steps the elevator system.
   * If the elevator system is out of service, it returns.
   * If the elevator system is not stopping, it distributes the requests to the elevators.
   * Steps each elevator in the system.
   * If the elevator system is stopping, it checks if all the elevators are on the ground floor.
   * If all the elevators are on the ground floor,
   * it sets the elevator system status to out of service.
   */
  public void stepElevatorSystem() {
    if (this.elevatorsStatus == ElevatorSystemStatus.outOfService) {
      return;
    }
    if (this.elevatorsStatus != ElevatorSystemStatus.stopping) {
      distributeRequests();
    }
    for (ElevatorInterface elevator : this.elevators) {
      elevator.step();
    }
    if (this.elevatorsStatus == ElevatorSystemStatus.stopping) {
      boolean allElevatorsOnGroundFloor = true;
      for (ElevatorInterface elevator : this.elevators) {
        if (elevator.getCurrentFloor() != 0) {
          allElevatorsOnGroundFloor = false;
          break;
        }
      }
      if (allElevatorsOnGroundFloor) {
        this.elevatorsStatus = ElevatorSystemStatus.outOfService;
      }
    }
  }

  private void distributeRequests() {
    if (this.upRequests.isEmpty() && this.downRequests.isEmpty()) {
      return;
    }
    for (ElevatorInterface elevator : this.elevators) {
      if (elevator.isTakingRequests()) {
        if (elevator.getCurrentFloor() == 0) {
          List<Request> upRequestsForElevator = getRequests(this.upRequests);
          elevator.processRequests(upRequestsForElevator);
        } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
          List<Request> downRequestsForElevator = getRequests(this.downRequests);
          elevator.processRequests(downRequestsForElevator);
        }
      }
    }
  }

  private List<Request> getRequests(List<Request> requests) {
    List<Request> requestsToReturn = new ArrayList<>();
    while (!requests.isEmpty() && requestsToReturn.size() < this.elevatorCapacity) {
      requestsToReturn.add(requests.remove(0));
    }
    return requestsToReturn;
  }
}

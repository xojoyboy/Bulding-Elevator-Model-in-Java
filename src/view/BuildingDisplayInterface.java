package view;

import building.BuildingReport;

/**
 * BuildingDisplayInterface is an interface that defines the methods
 * required for a building display interface.
 */
public interface BuildingDisplayInterface {
  // Method to start the display interface,
  // setting up the window and displaying initial state
  void start();

  // Method to update the display with the latest status from the building
  void update(BuildingReport report);

  // Method to show status messages to the user, like "System Started" or "Request Added"
  void showStatus(String message);

  // Method to show error messages resulting from operations like invalid input or system errors
  void showError(String message);

  // Set listener for updating the building status in the display
  void setUpdateListener(Runnable updateListener);

  // Set listener for adding a request to the elevator system
  void setRequestListener(RequestListener requestListener);

  // Set listener for stepping the elevator system
  void setStepListener(Runnable stepListener);

  // Set listener for starting the elevator system
  void setStartListener(Runnable startListener);

  // Set listener for stopping the elevator system
  void setStopListener(Runnable stopListener);
}


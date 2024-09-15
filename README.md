Building & Elevator System Simulation

OVERVIEW:
This project simulates an elevator system using a Model-View-Controller (MVC) architecture to demonstrate the software to potential clients. The application models multiple elevators operating within a building, handling requests and displaying the current status of each elevator, including position, direction, operational status, and scheduled stops.

FEATURES:
- Interactive GUI using Java Swing that displays the status of each elevator in the building.
- Real-time updates reflecting the model's state in response to user actions or system events.
- Controls to start a new simulation, and manage elevator operations including start, stop, and out of service.
- Step button to advance the simulation manually and see the immediate effects on the elevator system.

HOW TO RUN:
- To run the application, execute the provided BuildingSystem.jar file in the res folder. Ensure Java is installed on your machine.

HOW TO USE THE PROGRAM:
- Upon launching the application, you will see the main window displaying the elevator statuses.
- Use the "Start" button to initiate the simulation with default or specified parameters.
- "Step" button can be used to manually step through the simulation, updating the view with each press.
- Elevators can be put out of service or returned to service as needed during the simulation.

DESIGN/MODEL CHANGES:
- Transitioned to an MVC architecture to decouple logic and presentation, enhancing maintainability and scalability.
- Enhanced error handling and user input validation to prevent and manage operational exceptions.

ASSUMPTIONS:
- All inputs are valid integers representing floors within the building's range.
- Elevators start from the ground floor.
- Simulation does not account for real-time delays, simulating movement instantaneously.

LIMITATIONS:
- The GUI is simplistic and primarily functional; aesthetic improvements could enhance user experience.
- Current implementation does not support dynamic reconfiguration of the number of floors or elevators during runtime.

CITATIONS:
- Java Swing documentation was used extensively for the development of the GUI components.
- Elevator operation logic was adapted and extended from examples found in class and Java Programming Textbook and online resources like stack overflow.

Please ensure Java is correctly installed on your system to run the application without issues. Test the JAR file outside the development environment to confirm its portability.

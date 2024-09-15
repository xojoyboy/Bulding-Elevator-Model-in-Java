package view;

import building.BuildingReport;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import scanerzus.Request;

/**
 * BuildingDisplay is a GUI class that displays the building status
 * and provides controls to interact
 * with the building elevator system.
 */
public class BuildingDisplay extends JFrame implements BuildingDisplayInterface {
  private JTextArea statusArea;
  private JTextField startFloorField;
  private JTextField endFloorField;

  private Runnable updateListener;
  private Runnable stepListener;
  private Runnable startListener;
  private Runnable stopListener;
  private RequestListener requestListener;

  public BuildingDisplay() {
    initializeUI();
  }

  private void initializeUI() {
    setTitle("Building Elevator System");
    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Status area
    statusArea = new JTextArea();
    statusArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(statusArea);
    add(scrollPane, BorderLayout.CENTER);

    // Control panel
    JPanel controlPanel = new JPanel();

    // Local variable button declarations
    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Stop");
    JButton stepButton = new JButton("Step");
    JButton submitRequestButton = new JButton("Submit Request");
    startFloorField = new JTextField(5);
    endFloorField = new JTextField(5);

    controlPanel.add(startButton);
    controlPanel.add(stopButton);
    controlPanel.add(stepButton);
    controlPanel.add(new JLabel("Start Floor:"));
    controlPanel.add(startFloorField);
    controlPanel.add(new JLabel("End Floor:"));
    controlPanel.add(endFloorField);
    controlPanel.add(submitRequestButton);

    add(controlPanel, BorderLayout.SOUTH);

    // Action listeners
    startButton.addActionListener(e -> {
      startListener.run();
      updateStatus();
    });
    stopButton.addActionListener(e -> {
      stopListener.run();
      updateStatus();
    });
    stepButton.addActionListener(e -> {
      stepListener.run();
      updateStatus();
    });
    submitRequestButton.addActionListener(this::processSubmitAction);
  }

  private void updateStatus() {
    if (updateListener != null) {
      updateListener.run();
    }
  }

  private void processSubmitAction(ActionEvent e) {
    try {
      int startFloor = Integer.parseInt(startFloorField.getText());
      int endFloor = Integer.parseInt(endFloorField.getText());
      if (requestListener != null) {
        requestListener.onRequest(new Request(startFloor, endFloor));
        updateStatus();
      }
    } catch (NumberFormatException nfe) {
      showError("Invalid floor numbers.");
    }
  }

  @Override
  public void start() {
    setVisible(true);
  }

  @Override
  public void update(BuildingReport report) {
    statusArea.setText(report.toString());
  }

  @Override
  public void showStatus(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void setUpdateListener(Runnable updateListener) {
    this.updateListener = updateListener;
  }

  @Override
  public void setRequestListener(RequestListener requestListener) {
    this.requestListener = requestListener;
  }

  @Override
  public void setStepListener(Runnable stepListener) {
    this.stepListener = stepListener;
  }

  @Override
  public void setStartListener(Runnable startListener) {
    this.startListener = startListener;
  }

  @Override
  public void setStopListener(Runnable stopListener) {
    this.stopListener = stopListener;
  }
}

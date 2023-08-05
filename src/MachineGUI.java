import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Machine GUI (main interface)
 */
public class MachineGUI extends JFrame {
    // JLabel to represent the states
    public JLabel[] statesGUI;
    // The following are legends for start, current, and accept states
    public JPanel startStateLegend;
    public JPanel acceptStateLegend;
    public JPanel rejectStateLegend;
    public JPanel currentStateLegend;
    // The following are labels for start, current, and final states
    public JLabel startStateLegendLabel;
    public JLabel acceptStateLegendLabel;
    public JLabel rejectStateLegendLabel;
    public JLabel currentStateLegendLabel;
    public JLabel allTransitions;
    public JLabel readHead;
    // Run Btn
    public JButton runBtn;
    // Step Btn
    public JButton stepBtn;
    // String input text field
    public JTextField inputTF;

    // Label for current transition
    public JLabel currentTransitionLabel;

    // The following came from the input file
    public String[] states;
    public String[] inputSymbols;
    public String leftEndMarker;
    public String rightEndMarker;
    public String startState;
    public String acceptState;
    public String rejectState;
    public ArrayList<String> transitionFunctions;

    public MachineGUI(String[] states, String[] inputSymbols, String leftEndMarker, String rightEndMarker,
                     String startState, String acceptState, String rejectState,
                      ArrayList<String> transitionFunctions) {
        super("Two-Way DFA");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the frame to full screen with window decorations
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // assign the parameters
        this.states = states;
        this.inputSymbols = inputSymbols;
        this.leftEndMarker = leftEndMarker;
        this.rightEndMarker = rightEndMarker;
        this.startState = startState;
        this.acceptState = acceptState;
        this.rejectState = rejectState;
        this.transitionFunctions = transitionFunctions;

        layoutComponents();
        this.setVisible(true);
    }


    public void layoutComponents() {

        // center panel with GridBagLayout
        JPanel panelCenter = new JPanel(new GridBagLayout());

        // these are for setting the margin of each state (box)
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 40, 10, 40);

        // design the states
        statesGUI = new JLabel[states.length];
        for (int i = 0; i < states.length; i++) {
            statesGUI[i] = new JLabel(states[i]);
            statesGUI[i].setPreferredSize(new Dimension(70, 70));
            statesGUI[i].setHorizontalAlignment(JLabel.CENTER);
            statesGUI[i].setFont(new Font("Roboto", Font.BOLD, 40));
            statesGUI[i].setBorder(BorderFactory.createRaisedBevelBorder());
            // Add the component with the defined constraints
            panelCenter.add(statesGUI[i], constraints);
        }

        // change the color of the start state and the final states
        for(JLabel state: statesGUI){
            if(state.getText().equals(startState)){
                state.setBackground(Color.gray);
                state.setForeground(Color.gray);
            }

            if(state.getText().equals(acceptState)){
                state.setBackground(Color.green);
                state.setForeground(Color.green);
            }

            if(state.getText().equals(rejectState)){
                state.setBackground(Color.red);
                state.setForeground(Color.red);
            }

        }

        // place a legend on the gui for start state
        startStateLegend = new JPanel();
        startStateLegend.setBackground(Color.gray);
        startStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        startStateLegendLabel = new JLabel("Start State");
        startStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // place a legend on the gui for final state
        acceptStateLegend = new JPanel();
        acceptStateLegend.setBackground(Color.green);
        acceptStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        acceptStateLegendLabel = new JLabel("Accept State");
        acceptStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // place a legend on the gui for current state
        currentStateLegend = new JPanel();
        currentStateLegend.setBackground(Color.yellow);
        currentStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        currentStateLegendLabel = new JLabel("Current State");
        currentStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        // place a legend on the gui for reject state
        rejectStateLegend = new JPanel();
        rejectStateLegend.setBackground(Color.red);
        rejectStateLegend.setMaximumSize(new Dimension(30, 30)); // Set maximum size to preferred size
        // set the label
        rejectStateLegendLabel = new JLabel("Reject State");
        rejectStateLegendLabel.setFont(new Font("Roboto", Font.PLAIN, 10));

        /* Add the legend panels and labels to separate intermediate panels
           that will be placed next to each other horizontally*/
        JPanel startStatePanel = new JPanel();
        startStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Align components to the left
        startStatePanel.add(startStateLegend);
        startStatePanel.add(startStateLegendLabel);

        JPanel finalStatePanel = new JPanel();
        finalStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        finalStatePanel.add(acceptStateLegend);
        finalStatePanel.add(acceptStateLegendLabel);

        JPanel currentStatePanel = new JPanel();
        currentStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        currentStatePanel.add(currentStateLegend);
        currentStatePanel.add(currentStateLegendLabel);

        JPanel rejectStatePanel = new JPanel();
        rejectStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rejectStatePanel.add(rejectStateLegend);
        rejectStatePanel.add(rejectStateLegendLabel);

        // create a north panel with BoxLayout (Y_AXIS)
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));

        // Add the intermediate panels to the main panelNorth
        panelNorth.add(startStatePanel, BorderLayout.NORTH);
        panelNorth.add(finalStatePanel, BorderLayout.NORTH);
        panelNorth.add(currentStatePanel, BorderLayout.NORTH);
        panelNorth.add(rejectStatePanel, BorderLayout.NORTH);

        // add a run button to input a string
        runBtn = new JButton("Run");
        runBtn.setFocusable(false);
        runBtn.setFont(new Font("Roboto", Font.PLAIN, 30));
        runBtn.setBorder(new EmptyBorder(10,10,10,10));

        // add a step button to check input step by step
        stepBtn = new JButton("Step");
        stepBtn.setFocusable(false);
        stepBtn.setFont(new Font("Roboto", Font.PLAIN, 30));
        stepBtn.setBorder(new EmptyBorder(10,10,10,10));
        stepBtn.setVisible(false);

        // add a text field for string input
        inputTF = new JTextField();
        inputTF.setFont(new Font("Roboto", Font.PLAIN, 25));
        inputTF.setPreferredSize(new Dimension(800, 50));
        inputTF.setHorizontalAlignment(JTextField.CENTER);

        // add a JLabel to display all transitions of the machine
        allTransitions = new JLabel();
        allTransitions.setFont(new Font("Roboto", Font.PLAIN, 15));

        // add a read head JLabel
        readHead = new JLabel();
        readHead.setFont(new Font("Roboto", Font.PLAIN, 25));

        JLabel readHeadLabel = new JLabel("Read Head Position: ");
        readHeadLabel.setFont(new Font("Roboto", Font.BOLD, 25));

        // add a label for the current transition
        currentTransitionLabel = new JLabel();
        currentTransitionLabel.setFont(new Font("Roboto", Font.BOLD, 25));

        // add a new south panel with FlowLayout to place buttons next to each other
        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSouth.add(runBtn);
        panelSouth.add(stepBtn);
        panelSouth.add(inputTF);
        panelSouth.add(readHeadLabel);
        panelSouth.add(readHead);

        // add the current transition label
        panelSouth.add(currentTransitionLabel);

        // Add the north panel to the NORTH of the BorderLayout
        this.add(panelNorth, BorderLayout.NORTH);
        // Add the stack label to the SOUTH (bottom) of the BorderLayout
        this.add(panelSouth, BorderLayout.SOUTH);
        // Add the center panel to the CENTER of the BorderLayout
        this.add(panelCenter, BorderLayout.CENTER);
    }

    public void setReadHead(int readHead) {
        this.readHead.setText(String.valueOf(readHead));
    }

    /**
     * Sets the action listener of the buttons in gui
     * @param listener listener
     */
    public void setActionListener(ActionListener listener) {
        runBtn.addActionListener(listener);
        stepBtn.addActionListener(listener);
    }

    /**
     * This function returns the text in the text field input
     * @return the text in tf input
     */
    public String getInputTF() {
        return inputTF.getText();
    }

    /**
     * This function returns the array of final states
     * @return the array of final states
     */
    public String getAcceptState() {
        return acceptState;
    }

    /**
     * This function returns the GUI states of the program
     * @return the GUI states
     */
    public JLabel[] getStatesGUI() {
        return statesGUI;
    }


    /**
     * This function returns the transition functions
     * @return returns the transition functions
     */
    public ArrayList<String> getTransitionFunctions() {
        return transitionFunctions;
    }

    /**
     * This function returns the start state
     * @return return the start state in string format
     */
    public String getStartState() {
        return startState;
    }

    /**
     * This function sets the visibility of the step button to true or not
     * @param stepBtn boolean value to set visible
     */
    public void setStepBtnVisible(Boolean stepBtn) {
        this.stepBtn.setVisible(stepBtn);
    }

    /**
     * This function sets the text string of the text field
     * @param inputTF the text to be set
     */
    public void setInputTF(String inputTF) {
        this.inputTF.setText(inputTF);
    }

    /**
     * This function enables or disables the input text field
     * @param inputTF boolean function to disable or enable
     */
    public void enableInputTF(Boolean inputTF) {
        this.inputTF.setEditable(inputTF);
    }

    /**
     * This function enables or disables the run button
     * @param runBtn boolean function to disable or enable
     */
    public void enableRunBtn(Boolean runBtn) {
        this.runBtn.setVisible(runBtn);
    }

    /**
     * This function transitions the current state to the next state and resets the previous states
     * @param currentState the current state
     * @param nextState the next state
     * @param startState the start state
     */
    public void transitionToNextState(String currentState, String nextState, String startState){

        // for each state in statesGUI
        for(JLabel state: statesGUI){
            // finding the current state
            if(state.getText().equals(currentState)){
                // if the start state is equal to the current state
                if(currentState.equals(startState)) {
                    state.setBackground(Color.gray);
                    state.setForeground(Color.gray);
                }
                else if(currentState.equals(acceptState)) {
                    state.setBackground(Color.green);
                    state.setForeground(Color.green);
                }
                else if(currentState.equals(rejectState)) {
                    state.setBackground(Color.red);
                    state.setForeground(Color.RED);
                }
                else {
                    state.setBackground(Color.BLACK);
                    state.setForeground(Color.black);
                }
            }

            // finding the next state
            if(state.getText().equals(nextState)){
                state.setBackground(Color.ORANGE);
                state.setForeground(Color.ORANGE);
            }
        }
    }

    /**
     * Helper function to update the current transition label
     * @param currentTransition the current state and the current input
     * @return the current transition
     */
    public void updateTransitionLabel(String currentTransition) {
        currentTransitionLabel.setText("Current Transition: " + currentTransition);
    }

    /**
     * This function resets the states and resets the start and final states
     */
    public void resetStartAndFinalStates(){
        for(JLabel state: statesGUI){
            state.setBackground(Color.BLACK);
            state.setForeground(Color.black);
            if(state.getText().equals(startState)){
                state.setBackground(Color.gray);
                state.setForeground(Color.gray);
            }


            if(state.getText().equals(acceptState)){
                state.setBackground(Color.green);
                state.setForeground(Color.green);
            }

            if(state.getText().equals(rejectState)){
                state.setBackground(Color.red);
                state.setForeground(Color.red);
            }

        }
    }


    public String getLeftEndMarker() {
        return leftEndMarker;
    }

    public String getRightEndMarker() {
        return rightEndMarker;
    }

    public String getRejectState() {
        return rejectState;
    }
}

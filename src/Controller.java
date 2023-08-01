import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controller class of the program
 */
public class Controller implements ActionListener, DocumentListener {
    public MachineGUI machineGUI;
    public String[] inputSymbols;
    public JLabel currentState = new JLabel();
    public int currHeadPosition;


    /**
     * default constructor for Controller
     * @param machineGUI the machineGUI
     * @param inputSymbols the inputSymbols from machine definition
     */
    public Controller(MachineGUI machineGUI, String[] inputSymbols){
        this.machineGUI = machineGUI;
        this.inputSymbols = inputSymbols;

        machineGUI.setActionListener(this);
    }

    /**
     * This function handles the events
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // get string input from text field
        String input = machineGUI.getInputTF();

        // get the needed elements from machineGUI
        JLabel[] statesGUI = machineGUI.getStatesGUI();
        ArrayList<String> transitionFunctions = machineGUI.getTransitionFunctions();
        String startState = machineGUI.getStartState();

        // if run button is clicked
        if(e.getActionCommand().equals("Run")){
            // boolean variable for validity of string input
            boolean result = isStringValid(input);

            // if the string is valid
            if(result){
                machineGUI.setStepBtnVisible(true);

                // find the start state and assign it to current state
                for(JLabel state: statesGUI){
                    if(state.getText().equals(startState)){
                        currentState = state;
                        machineGUI.transitionToNextState(" ", currentState.getText(), startState);
                        input = machineGUI.getLeftEndMarker() + input + machineGUI.getRightEndMarker();
                        machineGUI.setInputTF(input);
                        currHeadPosition = 0;
                    }
                }
                // disable the run button and the text field
                machineGUI.enableRunBtn(false);
                machineGUI.enableInputTF(false);
            }
        }


        // if the step button is clicked
        if (e.getActionCommand().equals("Step")) {
            // Get the symbol read based on direction
            char singleInput = input.charAt(currHeadPosition);

            // initialize a boolean variable to check if there is such a transition
            boolean transitionFound = false;

            // get the transition for the input available
            for (String transition : transitionFunctions) {
                // split the elements
                String[] elements = transition.split(",");

                // get the transition current state
                String transitionCurrState = elements[0];

                // get the transition input symbol
                String transitionInputSymbol;
                if (elements[1].equals("λ"))
                    transitionInputSymbol = "";
                else
                    transitionInputSymbol = elements[1];

                // get the next state of the transition
                String transitionNextState = elements[2];

                // get the direction
                String headDirection = elements[3];

                // look for the transition coordinated with the singleInput (currently read symbol)
                if (currentState.getText().equals(transitionCurrState) && String.valueOf(singleInput).equals(transitionInputSymbol)) {

                    System.out.println(transition);

                    // MOVE THE CURRENT STATE TO THE NEXT STATE
                    machineGUI.transitionToNextState(transitionCurrState, transitionNextState, startState);
                    // for each state in statesGUI (and assign the next state to be the current state)
                    for (JLabel state : statesGUI) {
                        if (state.getText().equals(transitionNextState)) {
                            currentState = state;
                        }
                    }

                    // MOVE THE READ HEAD
                    //input = input.replace("H", "");
                    //StringBuilder stringBuilder = new StringBuilder(input);
                    if (headDirection.equals("R"))
                        currHeadPosition += 1;
                    else
                        currHeadPosition -= 1;


                    machineGUI.setReadHead(currHeadPosition);

                    break;
                    //stringBuilder.insert(currHeadPosition, "H");
                   // System.out.println(stringBuilder);
                    //machineGUI.setInputTF(String.valueOf(stringBuilder));

                }

            }

            System.out.println("accept state: " + machineGUI.getAcceptState() + " curr state: " + currentState.getText());
            if (currentState.getText().equals(machineGUI.getAcceptState())) {
                JOptionPane.showMessageDialog(null, "Accepted!", "Accepted", JOptionPane.INFORMATION_MESSAGE);
                resetProgram();
            } else if (currentState.getText().equals(machineGUI.getRejectState())) {
                JOptionPane.showMessageDialog(null, "Rejected!", "Error", JOptionPane.ERROR_MESSAGE);
                resetProgram();
            }


        }
    }

    /**
     * Helper function to reset the program
     */
    public void resetProgram(){
        // reset the following:
        machineGUI.setInputTF("");
        machineGUI.setStepBtnVisible(false);
        machineGUI.enableRunBtn(true);
        machineGUI.enableInputTF(true);
        machineGUI.resetStartAndFinalStates();
    }

    /**
     * Helper function to validate the string input
     * @param input the input string
     * @return true if valid and false if invalid
     */
    public boolean isStringValid(String input) {
        boolean found = true; // Assume all characters are found in inputSymbols initially

        if(input.length() != 0) {
            for (int i = 0; i < input.length(); i++) {
                boolean charFound = false; // Initialize for each character in input
                for (String symbol : inputSymbols) {
                    if (String.valueOf(input.charAt(i)).equals(symbol)) {
                        charFound = true;
                        break;
                    }
                }

                // If any character is not found in inputSymbols, set found to false
                if (!charFound) {
                    found = false;
                    JOptionPane.showMessageDialog(null, "You entered a non-input symbol");
                    break; // No need to check further, we can return false already
                }
            }
        } else{
            found = false;
            JOptionPane.showMessageDialog(null, "Please enter a string");
        }

        return found; // Return true if all characters are found, otherwise false
    }


    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}

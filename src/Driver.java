/**
 * STALGCM Case Study
 * @author Ernest Balderosa
 * @author Samantha Caasi
 * @author John Marcellana
 */

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Driver class of Two Way Deterministic Finite State Accepter
 */
public class Driver {
    public static String [] states;
    public static String [] inputSymbols;
    public static String leftEndMarker;
    public static String rightEndMarker;
    public static String startState;
    public static String acceptState;
    public static String rejectState;
    public static ArrayList<String> transitionFunctions;

    /**
     * main function
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // initialize a boolean variable for validity of file input
        boolean validFile = false;

        // loop until a valid input text file is selected
        while (!validFile) {
            JFileChooser fileChooser = new JFileChooser();

            // Set default directory to this class's directory
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            // Set file filter to only show text files
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Text Files";
                }
            });

            //Show file chooser dialog
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {

                // FORMAT OF THE FILE
                // A,B,C  => states
                // 0,1  => input symbols
                // < => left end marker
                // > => right end marker
                // A => start state
                // C => accept state
                // B => reject state
                // - => separator
                // A,0,B,L

                try {
                    Scanner sc = new Scanner(fileChooser.getSelectedFile());

                    // get machine definition from input file
                    states = sc.nextLine().split(",");
                    inputSymbols = sc.nextLine().split(",");
                    leftEndMarker = sc.nextLine();
                    rightEndMarker = sc.nextLine();
                    startState = sc.nextLine();
                    acceptState = sc.nextLine();
                    rejectState = sc.nextLine();

                    if(states.length != 0 &&
                            inputSymbols.length != 0 &&
                            leftEndMarker != null &&
                            rightEndMarker != null &&
                            startState != null &&
                            acceptState != null &&
                            rejectState != null){
                        validFile = true; // file is valid
                    }

                    // read the separator and check if valid
                    String separator = sc.nextLine();
                    if(separator != "-")
                        validFile = false;
                    else
                        validFile = true;


                    // read the transition functions
                    transitionFunctions = new ArrayList<>();
                    while(sc.hasNextLine())
                        transitionFunctions.add(sc.nextLine());

                    // and read if valid
                    if(!transitionFunctions.isEmpty())
                        validFile = true;
                    else
                        validFile = false;

                    System.out.println("valid file? " + validFile);
                    sc.close();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error reading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                System.exit(0);
            }
        }

        MachineGUI machineGUI = new MachineGUI(states, inputSymbols, leftEndMarker, rightEndMarker,
                                               startState, acceptState, rejectState, transitionFunctions);
        Controller controller = new Controller(machineGUI, inputSymbols);
    }


}

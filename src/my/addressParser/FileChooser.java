package my.addressParser;



import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class FileChooser extends JPanel implements ActionListener {
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;

	public FileChooser() {
		super(new BorderLayout());

		// Create the log first, because the action listeners
		// need to refer to it.
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create a file chooser
		fc = new JFileChooser();

		// Uncomment one of the following lines to try a different
		// file selection mode. The first allows just directories
		// to be selected (and, at least in the Java look and feel,
		// shown). The second allows both files and directories
		// to be selected. If you leave these lines commented out,
		// then the default mode (FILES_ONLY) will be used.
		//
		// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// Create the open button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		openButton = new JButton("Open a File...");
		openButton.addActionListener(this);

		// Create the save button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		// saveButton = new JButton("Save a File...",
		// createImageIcon("images/Save16.gif"));
		// saveButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(openButton);
		// buttonPanel.add(saveButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(FileChooser.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would open the file.
				log.append("Processing: " + file.getName() + "." + newline);
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					Searcher searcher = new Searcher();
					String path = file.getParent();
					FileWriter outputFile = new FileWriter(path
							+ "\\addresses.csv");
					outputFile.write("NMI,Street Number,Street Name,Street Type,\n");
					int counter = 0;
					String initialInput;
					while ((initialInput = br.readLine()) != null) {
						//main section where all the magic is performed
						counter++;
						System.out.println(counter + ": " + initialInput);
						String[] initialInputArray = initialInput.split(",");
						String input = initialInputArray[1];
						String nmi = initialInputArray[0];
						searcher.setInput_text(input);
						String outputString;
						log.append("Processing: " + input + newline);
						if (searcher.isPOBox() || searcher.isLockedBag()) {
							outputString = nmi + "," + null + "," + input + ",\n";
						} else {
							String streetNo = searcher.streetNumber();
							String streetName = searcher.streetName();
							String streetType = searcher.streetType();
							outputString = nmi + "," + streetNo + ","
									+ streetName + "," + streetType + ",\n";
						}
						outputFile.write(outputString);
						System.out.println("writting line: " + counter + " " + outputString);
						/*if (counter%1000 == 0){
							new java.util.Scanner(System.in).nextLine();
						}*/
						outputString = null;
						input = null;
						initialInput = null;
						initialInputArray = null;
					}
					br.close();
					outputFile.close();
					log.append(newline + "Processing successfully completed!"
							+ newline + "Results saved in addresses.csv"
							+ newline);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			} else {
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());

			// Handle save button action.
		} /*
		 * else if (e.getSource() == saveButton) { int returnVal =
		 * fc.showSaveDialog(FileChooser.this); if (returnVal ==
		 * JFileChooser.APPROVE_OPTION) { File file = fc.getSelectedFile();
		 * //This is where a real application would save the file.
		 * log.append("Saving: " + file.getName() + "." + newline); } else {
		 * log.append("Save command cancelled by user." + newline); }
		 * log.setCaretPosition(log.getDocument().getLength()); }
		 */
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FileChooser.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Addresses Parser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new FileChooser());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				FileChooser.createAndShowGUI();
			}
		});

	}

}
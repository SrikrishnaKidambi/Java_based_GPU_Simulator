import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimulatorGUI {
	private static JFrame frame=null;
	public static JTextArea console;
	private static int fontSize=14;
	private static final int MAX_FONT_SIZE=30;
	private static final int MIN_FONT_SIZE=10;
	private static JTable registerTable;
	private static String[][] registerData;
	private static Test test;
	private static JComboBox<String> displayTypeSelector;
	private static JTextPane codeEditor;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(SimulatorGUI::makeGUI);
	}
	public static void makeGUI() {
		if(frame!=null) {
			frame.toFront();
			return;
		}
		JFrame frame=new JFrame("Dual Core Krishnas GPU SIMULATOR");
		//Setting app logo
		ImageIcon icon = new ImageIcon("icon.jpg");
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,600);

		// Adding Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F'); // Allows access with Alt + F
        
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); // Ctrl + S
        saveItem.addActionListener(e -> saveCodeToFile());
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
		
		// implement the code editor and register view and navigation bar
		JPanel codeEditorPanel=new JPanel();
		JPanel registerPanel=new JPanel();
		JPanel navigationPanel= new JPanel();
		navigationPanel.setPreferredSize(new Dimension(30,600));
		
		// text area for printing the output.
		console=new JTextArea();
		console.setEditable(false);
		JScrollPane consoleScroll=new JScrollPane(console);

		// text area for writing the code
		codeEditor = new JTextPane();
		codeEditor.setFont(new Font("Monospaced",Font.PLAIN,fontSize));
		JScrollPane codeScrollPane= new JScrollPane(codeEditor);
		codeEditorPanel.setLayout(new BorderLayout());
		codeEditorPanel.add(codeScrollPane,BorderLayout.CENTER);
		codeEditor.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e){
				if (e.isControlDown()) {
					if (e.getKeyCode() == java.awt.event.KeyEvent.VK_EQUALS) { // Ctrl +
						if (fontSize < MAX_FONT_SIZE) {
							fontSize += 2;
							codeEditor.setFont(new Font("Monospaced", Font.PLAIN, fontSize));
						}
					} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_MINUS) { // Ctrl -
						if (fontSize > MIN_FONT_SIZE) {
							fontSize -= 2;
							codeEditor.setFont(new Font("Monospaced", Font.PLAIN, fontSize));
						}
					}
				}
			}
		});
		
		
		// creating a panel for the buttons 
		
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton run=new JButton("Run");
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				console.append("Running the program.\n");  //printing the output after running the program
				try{
					java.nio.file.Files.write(java.nio.file.Paths.get("program.asm"),codeEditor.getText().getBytes());
//					console.append("Assembly code saved to program.asm\n");
				}catch(Exception ex){
					console.append("Error saving file: "+ex.getMessage()+"\n");
				}
				test=new Test();
				test.RunSimulator();
				updateRegisters(0);
				updateRegisters(1);
				updateRegisters(2);
				updateRegisters(3);
//				console.append("The number of clock cycles taken for execution are "+test.sim.clock);
			}
			
		});
		
		JButton clear=new JButton("Clear");
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.setText("");
			}
			
		});
		run.setFocusPainted(false);
		clear.setFocusPainted(false);
		buttonPanel.add(run);
		buttonPanel.add(clear);
		
		// organising the components in the console panel
		JPanel consolePanel=new JPanel(new BorderLayout());
		consolePanel.add(buttonPanel,BorderLayout.NORTH);
		consolePanel.add(consoleScroll,BorderLayout.CENTER);
	
		JSplitPane leftVerticalSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,codeEditorPanel,consolePanel);
		leftVerticalSplitPane.setDividerLocation((int)(0.65*600));
		leftVerticalSplitPane.setResizeWeight(0.65);
		
		JSplitPane mainSplitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftVerticalSplitPane,registerPanel);
		mainSplitPane.setDividerLocation((int)(0.75*1000));
		mainSplitPane.setResizeWeight(0.75);

		JSplitPane finalSplitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,navigationPanel,mainSplitPane);
		finalSplitPane.setDividerLocation(30);
		finalSplitPane.setEnabled(false);
		mainSplitPane.setEnabled(true);

		frame.add(finalSplitPane);
		
		//Core selection options
		String[] coresAvailable= {"Core 0","Core 1","Core 2","Core 3"};
		JComboBox<String> coreSelector= new JComboBox<>(coresAvailable);
		coreSelector.addActionListener(e -> updateRegisters(coreSelector.getSelectedIndex()));
		JPanel coreSelectionPanel= new JPanel();
		coreSelectionPanel.setLayout(new BoxLayout(coreSelectionPanel, BoxLayout.X_AXIS));
		JLabel coreLabel= new JLabel("Choose a Core: ");
		coreSelectionPanel.add(coreLabel);
		coreSelectionPanel.add(Box.createHorizontalGlue());
		coreSelectionPanel.add(coreSelector);
		
		//Register table
		String[] columnNames= {"Name","Value"};
		registerData = new String[32][2];

		//Initialize the register table
		for(int i=0;i<32;i++){
			registerData[i][0] = "x"+i;
			registerData[i][1] = "0x00000000";
		}

		registerTable = new JTable(registerData,columnNames);
		registerTable.setEnabled(false);
		registerTable.setRowHeight(25);

		//selection options for display types
		String[] displayTypes= {"Hex","Binary","Signed"};
		displayTypeSelector = new JComboBox<>(displayTypes);
		displayTypeSelector.addActionListener(e -> updateRegisters(coreSelector.getSelectedIndex()));
		JPanel displayTypePanel= new JPanel();
		displayTypePanel.setLayout(new BoxLayout(displayTypePanel, BoxLayout.X_AXIS));
		JLabel dispLabel = new JLabel("Display type: ");
		displayTypePanel.add(dispLabel);
		displayTypePanel.add(Box.createHorizontalGlue());
		displayTypePanel.add(displayTypeSelector);

		//center align table data
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		registerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		registerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

		//Adjust the column width for visibility
		registerTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		registerTable.getColumnModel().getColumn(1).setPreferredWidth(100);

		JScrollPane registersPanelScrollPane= new JScrollPane(registerTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		registersPanelScrollPane.setPreferredSize(new Dimension(200, 400));
		registerPanel.setLayout(new BorderLayout());
		registerPanel.add(coreSelectionPanel,BorderLayout.NORTH);
		registerPanel.add(registersPanelScrollPane,BorderLayout.CENTER);
		registerPanel.add(displayTypePanel,BorderLayout.SOUTH);

		//navigation buttons
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
		
		//Memory button
		JButton memoryButton = new JButton("Memory");
		memoryButton.setFocusPainted(false);
		memoryButton.setMaximumSize(new Dimension(80, 50));
		memoryButton.addActionListener(e -> openMemoryGUI());

		//Add to panel
		navigationPanel.add(Box.createVerticalStrut(5)); 
		navigationPanel.add(memoryButton);

		frame.setVisible(true);
	}
	private static void updateRegisters(int coreID){
		System.out.println("Switched to core "+coreID);

		String selectedFormat = (String) displayTypeSelector.getSelectedItem();

		for(int i=0;i<32;i++){
			int value = test.sim.cores[coreID].registers[i];

			switch(selectedFormat){
				case "Hex":
					registerData[i][1] = String.format("0x%08X", value & 0xFFFFFFFFL); //To handle the negative numbers we are using the bitwise and
					break;
				case "Binary":
					registerData[i][1] = "0b" + String.format("%32s", Integer.toBinaryString(value)).replace(' ', '0');
					break;
				case "Signed":
					registerData[i][1] = String.valueOf(value);
			}
			
		}
		registerTable.repaint();
	}
	private static void openSimulatorGUI() {
		if (frame != null) {
			frame.dispose();  // Close current window
		}
		SwingUtilities.invokeLater(SimulatorGUI::makeGUI);
	}
	
	private static void openMemoryGUI() {
		if (frame != null) {
			frame.dispose();  // Close current window
		}
		SwingUtilities.invokeLater(()->new MemoryGUI(test.mem));
	}
	private static void saveCodeToFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save ASM File");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Assembly Files (*.asm)", "asm"));

		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();

			// Ensure the file has a .asm extension
			if (!fileToSave.getAbsolutePath().endsWith(".asm")) {
				fileToSave = new File(fileToSave.getAbsolutePath() + ".asm");
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
				writer.write(codeEditor.getText()); 
				JOptionPane.showMessageDialog(null, "File saved: " + fileToSave.getAbsolutePath());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
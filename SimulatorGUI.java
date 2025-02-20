import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class SimulatorGUI {
	private static JFrame frame=null;
	public static JTextArea console;
	private static int fontSize=14;
	private static final int MAX_FONT_SIZE=30;
	private static final int MIN_FONT_SIZE=10;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(SimulatorGUI::makeGUI);
	}
	public static void makeGUI() {
		if(frame!=null) {
			frame.toFront();
			return;
		}
		JFrame frame=new JFrame("Dual Core Krishnas GPU SIMULATOR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,600);
		
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
		JTextPane codeEditor = new JTextPane();
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
				console.append("Running the program");  //printing the output after running the program
				try{
					java.nio.file.Files.write(java.nio.file.Paths.get("program.asm"),codeEditor.getText().getBytes());
					console.append("Assembly code saved to program.asm\n");
				}catch(Exception ex){
					console.append("Error saving file: "+ex.getMessage()+"\n");
				}
				Test test=new Test();
				test.RunSimulator();
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
		
		frame.setVisible(true);
	}
}

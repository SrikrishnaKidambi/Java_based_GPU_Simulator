import javax.swing.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.*;


public class SimulatorGUI {
	private static JFrame frame=null;
	public static JTextArea console;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(SimulatorGUI::makeGUI);
	}
	public static void makeGUI() {
		if(frame!=null) {
			frame.toFront();
			return;
		}
		JFrame frame=new JFrame("Simulator Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,600);
		
		// implement the code editor and register view
		JPanel codeEditorPanel=new JPanel();
		JPanel registerPanel=new JPanel();
		
		// text area for printing the output.
		console=new JTextArea();
		console.setEditable(false);
		JScrollPane consoleScroll=new JScrollPane(console);
		
		
		// creating a panel for the buttons 
		
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton run=new JButton("Run");
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		
		frame.add(mainSplitPane);
		frame.setVisible(true);
	}
}

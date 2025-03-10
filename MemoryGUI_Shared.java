import javax.swing.*;
import java.awt.*;


public class MemoryGUI_Shared {
	
	public MemoryGUI_Shared(Memory mem) {
		this.mem=mem;
		frame=new JFrame("Memory Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		
		String[] formatOptions= {"Signed","HexaDecimal","Binary"};
		formatSelector=new JComboBox<>(formatOptions);
		formatSelector.addActionListener(e->updateTable());
		
		table=createMemoryTable("Signed");
		scrollPane=new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
		
		JPanel topPanel=new JPanel();
		topPanel.add(new JLabel("Display Format:"));
		topPanel.add(formatSelector);
		
		frame.add(topPanel,BorderLayout.NORTH);
		frame.add(scrollPane,BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	public MemoryGUI_Shared() {
		frame=new JFrame("Memory Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		
		String[] formatOptions= {"Signed","HexaDecimal","Binary"};
		formatSelector=new JComboBox<>(formatOptions);
		formatSelector.addActionListener(e->updateTable());
		
		table=createMemoryTable("Signed");
		scrollPane=new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));
		
		JPanel topPanel=new JPanel();
		topPanel.add(new JLabel("Display Format:"));
		topPanel.add(formatSelector);
		
		frame.add(topPanel,BorderLayout.NORTH);
		frame.add(scrollPane,BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private JTable createMemoryTable(String format) {
		String[] columnNames=new String[32];
		for(int i=0;i<32;i++) {
			columnNames[i]="Addr"+(i*4);
		}
		int numRows=(int)Math.ceil((double)mem.memory.length/32);
		Object[][] data=new Object[numRows][32];
		int row=0,col=0;
		for(int i=0;i<mem.memory.length;i+=4) {
			data[row][col]=formatting(mem.memory[i],format);
			col++;
			if(col==32) {
				col=0;
				row++;
			}
		}
		JTable table=new JTable(data,columnNames);
		table.setRowHeight(35);
		table.setFont(new Font("Arial",Font.PLAIN,14));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredScrollableViewportSize(new Dimension(frame.getWidth(),frame.getHeight()));
		return table;
	}
	
	private void updateTable() {
		String format=(String)formatSelector.getSelectedItem();
		table=createMemoryTable(format);
		scrollPane.setViewportView(table);
		frame.revalidate();
		frame.repaint();
	}
	
	private String formatting(int val,String format) {
		return switch(format) {
			case "HexaDecimal"->String.format("0x%X", val);
			case "Binary"->"0b"+Integer.toBinaryString(val);
			default->Integer.toString(val);
		};
	}
	
	private Memory mem;
	private JFrame frame;
	private JTable table;
	private JComboBox<String> formatSelector;
	private JScrollPane scrollPane;
	
}

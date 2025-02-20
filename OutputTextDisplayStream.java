import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class OutputTextDisplayStream extends OutputStream{
	private final JTextArea targetTextArea;
	private final String key;
	
	public OutputTextDisplayStream(JTextArea textArea,String key) {
		this.targetTextArea=textArea;
		this.key=key;
	}
	
	public void write(int b) {
		
	}
	
	public void write(byte[] b, int off, int len) {
		String msg=new String(b,off,len);
		if(msg.contains(key)) {
			SwingUtilities.invokeLater(()->{
					targetTextArea.append(msg);
					targetTextArea.setCaretPosition(targetTextArea.getDocument().getLength());
					
			});
		}
	}
}

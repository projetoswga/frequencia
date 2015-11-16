package br.com.frequencia.util;

import javax.swing.JLabel;


public class WaitPreview {
	private JLabel parentLabel; 
	
	public WaitPreview(JLabel parentLabel) {
		this.parentLabel = parentLabel;
	}
	
	public void start() {
		parentLabel.setVisible(true);
	}
	
	public void stop() {
		parentLabel.setVisible(false);
	}

}

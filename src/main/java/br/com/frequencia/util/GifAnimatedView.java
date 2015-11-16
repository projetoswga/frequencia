package br.com.frequencia.util;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class GifAnimatedView extends Thread {
	String gifPath;
	JLabel parentLabel;
	JPanel panel;
	
	public GifAnimatedView(String gifPath, JLabel parentLabel) {
		this.gifPath = gifPath;
		this.parentLabel = parentLabel;
	}

	public void run() {
		
		if (panel == null) {
			panel = new JPanel();
			parentLabel.getParent().add(panel);
			panel.setBounds(parentLabel.getBounds().x - 50, parentLabel.getBounds().y, 50, 50);
			Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource(gifPath));
			panel.getGraphics().drawImage(image,0,0,parentLabel.getParent());
			panel.repaint();
		}
		panel.setVisible(true);
		parentLabel.setVisible(true);
		parentLabel.getParent().repaint();
	}
	
	public void hide() {
		panel.setVisible(false);
		parentLabel.setVisible(false);
		this.interrupt();
	}
	
}

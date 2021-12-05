import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

// Janela em que o jogo ocorrerá de fato:

public class Game extends JPanel{
	
	public Game() {
		setFocusable(true);
		setLayout(null);
	}
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.red);
		g.fillOval(100, 100, 50, 50);
	}
}

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

// Janela em que o jogo ocorrer� de fato:

public class Game extends JPanel{
	
	public Game() {
		setFocusable(true);
		setLayout(null);
	}
	
	// M�TODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.red);
		g.fillOval(100, 100, 50, 50);
	}
}

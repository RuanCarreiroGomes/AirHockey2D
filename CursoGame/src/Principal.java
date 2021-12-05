import javax.swing.JFrame;
import java.awt.Dimension;

public class Principal {
	public static final int largura_tela = 640;
	public static final int altura_tela = 480;
	
	
	// Janela principal da aplicação
	public Principal() {
		JFrame janela = new JFrame("Jogo2D");
		
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(largura_tela, altura_tela));
		janela.getContentPane().add(game);
		janela.setResizable(false);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocation(400, 200);
		janela.setVisible(true);
		janela.pack();
	}
	public static void main(String[] args) {
		new Principal();
	}
}

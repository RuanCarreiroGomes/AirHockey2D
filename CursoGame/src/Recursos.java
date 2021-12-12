import java.awt.image.BufferedImage;

public class Recursos {
	private static Recursos singleton = null;
	
	// Construtor privado! s� pode ser invocado nessa classe.
	private Recursos() {
		
	}
	
	// Instanciando objeto �nico dentro da pr�pria classe.
	public static Recursos getInstance() {
		if(singleton == null) {
			singleton = new Recursos();
		}
		return singleton;
	}
	
	// M�todos -----------------------
	
	// M�todo para recorte das sprites
	public BufferedImage cortarImagem(int x1, int y1, int x2, int y2, BufferedImage img) {
		int largura = x2 - x1;
		int altura = y2 - y1;
		return img.getSubimage(x1,  y1,  largura,  altura); // Realiza o corte da img.
	}
}

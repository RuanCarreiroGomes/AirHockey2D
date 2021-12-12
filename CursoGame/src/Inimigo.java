import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Inimigo {
	public BufferedImage sprite;
	public BufferedImage img;
	public int posX;
	public int posY;
	public int raio;
	public int centroX;
	public int centroY;
	
	public Inimigo() {
		try{
			sprite = ImageIO.read(getClass().getResource("imgs/sprite.png"));
			img = Recursos.getInstance().cortarImagem(400, 100, 500, 200, sprite);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// Posição do inimigo centralizado na tela
		raio = 50;
		posX = (Principal.largura_tela / 2) - raio;
		posY = (Principal.altura_tela / 2) - raio;
		centroX = posX + raio;
		centroY = posY + raio;
	}
}

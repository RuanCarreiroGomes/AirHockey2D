import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Bola {
	public BufferedImage direita_baixo;
	public BufferedImage esquerda_baixo;
	public BufferedImage direita_cima;
	public BufferedImage esquerda_cima;
	public int posX;
	public int posY;
	public int raio;
	public int velX;
	public int velY;
	
	public Bola() {
		
		// Carregar a imagem "parada.gif" do arquivo imgs
		try {
			direita_baixo = ImageIO.read(getClass().getResource("imgs/direita_baixo.gif"));
			esquerda_baixo = ImageIO.read(getClass().getResource("imgs/esquerda_baixo.gif"));
			direita_cima = ImageIO.read(getClass().getResource("imgs/direita_cima.gif"));
			esquerda_cima = ImageIO.read(getClass().getResource("imgs/esquerda_cima.gif"));
		}catch(Exception e) {
			System.out.println("Erro ao carregar a imagem");
		}
		
		posX = 200;
		posY = 200;
		raio = 50;
		velX = 3;
		velY = 3;
	}
	
	// Método responsável pela mudança de imagens durante a execução:
	public BufferedImage obterImagem() {
		if(velX < 0) { // mov esquerda
			if (velY < 0) { // mov cima
				return esquerda_cima;
			} else { // mov baixo
				return esquerda_baixo;
			}
		} else { // mov direita
			if (velY < 0) { // mov cima
				return direita_cima;
			} else { // mov baixo
				return direita_baixo;
			}
		}
	}
}

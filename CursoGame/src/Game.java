import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

// Janela em que o jogo ocorrerá de fato:

public class Game extends JPanel{
	private Bola bola;
	private Inimigo inimigo;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage imgAtual;
	
	
	public Game() {
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: k_cima = false; break;
				case KeyEvent.VK_DOWN: k_baixo = false; break;
				case KeyEvent.VK_LEFT: k_esquerda = false; break;
				case KeyEvent.VK_RIGHT: k_direita = false; break;
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: k_cima = true; break;
				case KeyEvent.VK_DOWN: k_baixo = true; break;
				case KeyEvent.VK_LEFT: k_esquerda = true; break;
				case KeyEvent.VK_RIGHT: k_direita = true; break;
				}
			}
		});
		
		bola = new Bola();
		inimigo = new Inimigo();
		setFocusable(true);
		setLayout(null);
		
		/** O que são "Threads"?
		É um recurso da linguagem java que permite à aplicação tire vantagens
		dos modernos processadores com diversos núcleos de processamento. Em nosso game,
		o uso de Threads se faz necessário pricipalmente pelo recurso de pausa de execução
		que ele permite (Thread.sleep).
		*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				gameloop();
			}
		}).start(); // => start() => run() => [ gameloop() => handlerEvents() => update() => render() => pausa] 
	}
	
	// GAMELOOP ------------------------
	
	// Animação do objeto
	public void gameloop(){
		
		while (true) {
			
			handlerEvents();
			update();
			render();
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	// Movimentação da bola:
	public void handlerEvents(){
		bola.velX = 0;
		bola.velY = 0;
		imgAtual = bola.parada;
		
		// Todas as 8 possibilidades de movimento da bola:
		if (k_cima == true) {
			bola.velY = -3;
			imgAtual = bola.cima;
			
			if (k_direita == true) {
				bola.velX = 3;
				imgAtual = bola.direita_cima;
			}
			
			if (k_esquerda == true) {
				bola.velX = -3;
				imgAtual = bola.esquerda_cima;
			}
			
		}else if (k_baixo == true) {
			bola.velY = 3;
			imgAtual = bola.baixo;
			
			if (k_direita == true) {
				bola.velX = 3;
				imgAtual = bola.direita_baixo;
			}
			
			if (k_esquerda == true) {
				bola.velX = -3;
				imgAtual = bola.esquerda_baixo;
			}
			
		}else if (k_esquerda == true) {
			bola.velX = -3;
			imgAtual = bola.esquerda;
		
		}else if (k_direita == true) {
			bola.velX = 3;
			imgAtual = bola.direita;
		}
	}
	
	public void update(){
		bola.posX = bola.posX + bola.velX; // velocidade do objeto (horizontalmente)
		bola.posY = bola.posY + bola.velY; // velocidade do objeto (verticalmente)
		// calcular o centro da bola
		bola.centroX = bola.posX + bola.raio;
		bola.centroY = bola.posY + bola.raio;
		
		testeColisoes();
	}
	
	public void render(){
		repaint(); // Redesenha a tela a cada repetição do gameloop()
	}
	
	// OUTROS MÉTODOS ------------------
	
	public void testeColisoes() {
		
		// Colisão comum --------------
		if (bola.posX + bola.raio * 2 >= Principal.largura_tela || bola.posX <= 0) {
			
			bola.posX = bola.posX - bola.velX; // desfaz o movimento
		
		}
		if (bola.posY + bola.raio * 2 >= Principal.altura_tela || bola.posY <= 0) {
			
			bola.posY = bola.posY - bola.velY; // desfaz o movimento
		}
		
		// Colisão circular ------------
		int catetoH = bola.centroX - inimigo.centroX;
		int catetoV = bola.centroY - inimigo.centroY;
		double hipotenusa = Math.sqrt(Math.pow(catetoH, 2) + Math.pow(catetoV, 2));
		
		if (hipotenusa <= bola.raio + inimigo.raio) { // Verifica se houve colisão circular
			
			bola.posX = bola.posX - bola.velX; // desfaz o movimento
			bola.posY = bola.posY - bola.velY; // desfaz o movimento
		}
	}
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.red);
		
		// Posição e dimensão do obj "Bola" com parâmetros relativos ao obj
		g.drawImage(imgAtual, bola.posX, bola.posY, null);
		// Posição e dimensão do obj "Inimigo" com parâmetros relativos ao obj
		g.drawImage(inimigo.img, inimigo.posX, inimigo.posY, null);
	}
}

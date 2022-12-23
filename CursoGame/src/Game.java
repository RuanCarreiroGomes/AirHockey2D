import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
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
	private long tempoAtual;
	private long tempoAnterior;
	private double deltaTime;
	private double FPS_limit = 60;
	
	
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
		tempoAnterior = System.nanoTime();
		double tempoMinimo = (1e9) / FPS_limit; // duração mínima do quadro (em nanosegundos) 
				
		while (true) {
			tempoAtual = System.nanoTime();
			deltaTime = (tempoAtual - tempoAnterior) * (6e-8);
			handlerEvents();
			update(deltaTime);
			render();
			tempoAnterior = tempoAtual;
			
			try {
				int tempoEspera = (int) ((tempoMinimo - deltaTime) * (1e-6));
				Thread.sleep(tempoEspera);
			} catch (Exception e) {
				e.printStackTrace();
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
	
	public void update(double deltaTime){
		bola.posX = bola.posX + bola.velX * deltaTime; // velocidade do objeto (horizontalmente)
		bola.posY = bola.posY + bola.velY * deltaTime; // velocidade do objeto (verticalmente)
		// calcular o centro da bola
		bola.centroX = bola.posX + bola.raio;
		bola.centroY = bola.posY + bola.raio;
		
		testeColisoes(deltaTime);
	}
	
	public void render(){
		repaint(); // Redesenha a tela a cada repetição do gameloop()
	}
	
	// OUTROS MÉTODOS ------------------
	
	public void testeColisoes(double deltaTime) {
		
		// Colisão comum --------------
		if (bola.posX + bola.raio * 2 >= Principal.largura_tela || bola.posX <= 0) {
			
			bola.posX = bola.posX - bola.velX * deltaTime; // desfaz o movimento
		
		}
		if (bola.posY + bola.raio * 2 >= Principal.altura_tela || bola.posY <= 0) {
			
			bola.posY = bola.posY - bola.velY * deltaTime; // desfaz o movimento
		}
		
		// Colisão circular ------------
		double catetoH = bola.centroX - inimigo.centroX;
		double catetoV = bola.centroY - inimigo.centroY;
		double hipotenusa = Math.sqrt(Math.pow(catetoH, 2) + Math.pow(catetoV, 2));
		
		if (hipotenusa <= bola.raio + inimigo.raio) { // Verifica se houve colisão circular
			
			bola.posX = bola.posX - bola.velX * deltaTime; // desfaz o movimento
			bola.posY = bola.posY - bola.velY * deltaTime; // desfaz o movimento
		}
	}
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		AffineTransform af1 = new AffineTransform();
		AffineTransform af2 = new AffineTransform();
		af1.translate(bola.posX, bola.posY);
		af2.translate(inimigo.posX, inimigo.posY);
		
		setBackground(Color.LIGHT_GRAY);
		g2d.setColor(Color.RED);
		
		// Posição e dimensão do obj "Bola" com parâmetros relativos ao obj
		g2d.drawImage(imgAtual, af1, null);
		// Posição e dimensão do obj "Inimigo" com parâmetros relativos ao obj
		g2d.drawImage(inimigo.img, af2, null);
	}
}
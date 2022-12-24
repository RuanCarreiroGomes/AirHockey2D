import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Janela em que o jogo ocorrerá de fato:

public class Game extends JPanel{
	private Jogador jogador;
	private Inimigo inimigo;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage bg;
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
		
		jogador = new Jogador();
		inimigo = new Inimigo();
		
		try {
			bg = ImageIO.read(getClass().getResource("imgs/bg.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
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
		jogador.handlerEvents(k_cima, k_baixo, k_esquerda, k_direita);
	}
	
	public void update(double deltaTime){
		jogador.update(deltaTime);
		inimigo.update(deltaTime);
		testeColisoes(deltaTime);
	}
	
	public void render(){
		repaint(); // Redesenha a tela a cada repetição do gameloop()
	}
	
	// OUTROS MÉTODOS ------------------
	
	public void testeColisoes(double deltaTime) {
		if (jogador.posX + jogador.raio * 2 >= Principal.largura_tela || jogador.posX <= 0) {
			
			jogador.desmoverX(deltaTime);
		}
		if (jogador.posY + jogador.raio * 2 >= Principal.altura_tela || jogador.posY <= 0) {
			
			jogador.desmoverY(deltaTime);
		}
		
		// COLISÃO DO JOGADOR COM O LIMITE DIREITO DO CAMPO
		
		if(jogador.posX <= Principal.limite_direito) {
			jogador.desmoverX(deltaTime);
		}
	}
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		
		// desenha o chão do cenário
		g2d.drawImage(bg, 0, 0, Principal.largura_tela, Principal.altura_tela, null);
		// desenha as marcações de limite de movimentação
		g2d.setColor(Color.GRAY);
		g2d.fillRect(Principal.limite_direito, 0, 5, Principal.altura_tela);
		g2d.fillRect(Principal.limite_esquerdo, 0, 5, Principal.altura_tela);
		
		// Posição e dimensão do obj "Bola" com parâmetros relativos ao obj
		g2d.drawImage(jogador.imgAtual, jogador.af, null);
		// Posição e dimensão do obj "Inimigo" com parâmetros relativos ao obj
		g2d.drawImage(inimigo.img, inimigo.af, null);
	}
}

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
	private Bolinha bolinha;
	private boolean k_cima = false;
	private boolean k_baixo = false;
	private boolean k_direita = false;
	private boolean k_esquerda = false;
	private BufferedImage bg;
	private long tempoAtual;
	private long tempoAnterior;
	private double deltaTime;
	private double FPS_limit = 60;
	private char estado;
	
	
	public Game() {
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(estado == 'E') {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: k_cima = false; break;
					case KeyEvent.VK_DOWN: k_baixo = false; break;
					case KeyEvent.VK_LEFT: k_esquerda = false; break;
					case KeyEvent.VK_RIGHT: k_direita = false; break;
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(estado == 'E') {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: k_cima = true; break;
					case KeyEvent.VK_DOWN: k_baixo = true; break;
					case KeyEvent.VK_LEFT: k_esquerda = true; break;
					case KeyEvent.VK_RIGHT: k_direita = true; break;
					}
				} else if(estado == 'P') {
					
				}
				
			}
		});
		
		jogador = new Jogador();
		inimigo = new Inimigo();
		bolinha = new Bolinha();
		estado = 'S';
		agendarTransicao(3000, 'E');
		
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
		if(estado == 'E') {
			jogador.handlerEvents(k_cima, k_baixo, k_esquerda, k_direita);
		}
	}
	
	public void update(double deltaTime){
		if(estado == 'E') {
			jogador.update(deltaTime);
			inimigo.update(deltaTime);
			bolinha.update(deltaTime);
			testeColisoes(deltaTime);
		}else if(estado == 'G') {
			estado = 'R';
		}
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
		
		// COLISÃO DA BOLINHA COM O JOGADOR
		
		double ladoHorizontal = jogador.centroX - bolinha.centroX;
		double ladoVertical = jogador.centroY - bolinha.centroY;
		double hipotenusa = Math.sqrt(Math.pow(ladoHorizontal, 2) + Math.pow(ladoVertical, 2));
		
		if(hipotenusa <= jogador.raio + bolinha.raio) {
			jogador.desmoverX(deltaTime);
			jogador.desmoverY(deltaTime);
			double seno, cosseno;
			cosseno = ladoHorizontal / hipotenusa;
			seno = ladoVertical / hipotenusa;
			bolinha.velX = (- bolinha.velBase) * cosseno;
			bolinha.velY = (- bolinha.velBase) * seno;
		}
		
		// COLISÃO DA BOLINHA COM O INIMIGO
		
		ladoHorizontal = inimigo.centroX - bolinha.centroX;
		ladoVertical = inimigo.centroY - bolinha.centroY;
		hipotenusa = Math.sqrt(Math.pow(ladoHorizontal, 2) + Math.pow(ladoVertical, 2));
		
		if(hipotenusa <= inimigo.raio + bolinha.raio) {
			inimigo.desmoverX(deltaTime);
			inimigo.desmoverY(deltaTime);
			double seno, cosseno;
			cosseno = ladoHorizontal / hipotenusa;
			seno = ladoVertical / hipotenusa;
			bolinha.velX = (- bolinha.velBase) * cosseno;
			bolinha.velY = (- bolinha.velBase) * seno;
		}
		// COLISÃO DO JOGADOR COM O LIMITE DIREITO DO CAMPO
		
		if(jogador.posX <= Principal.limite_direito) {
			jogador.desmoverX(deltaTime);
		}
		
		// COLISÃO DO INIMIGO COM O LIMITE INFERIOR
		
		if(inimigo.posY + (inimigo.raio*2) >= Principal.altura_tela) {
			inimigo.desmoverY(deltaTime);
			inimigo.velY = inimigo.velY * -1;
		}
		
		// COLISÃO DO INIMIGO COM O LIMITE SUPERIOR
		
		if(inimigo.posY <= 0) {
			inimigo.desmoverY(deltaTime);
			inimigo.velY = inimigo.velY * -1;
		}
		
		// COLISÃO DA BOLINHA COM O LADO DIREITO DA TELA
		
		if(bolinha.posX + (bolinha.raio * 2) >= Principal.largura_tela) {
			bolinha.velX = bolinha.velX * -1;
			bolinha.posX = Principal.largura_tela - (bolinha.raio * 2);
		}
		
		// COLISÃO DA BOLINHA COM O LADO ESQUERDO DA TELA
		if(bolinha.posX <= 0) {
			bolinha.velX = bolinha.velX * -1;
			bolinha.posX = 0;
		}
		
		// COLISÃO DA BOLINHA COM O LADO INFERIOR DA TELA
		
		if(bolinha.posY + (bolinha.raio * 2) >= Principal.altura_tela) {
			bolinha.velY = bolinha.velY * -1;
			bolinha.posY = Principal.altura_tela - (bolinha.raio * 2);
		}
		
		// COLISÃO DA BOLINHA COM O LADO SUPERIOR DA TELA
		
		if(bolinha.posY <= 0) {
			bolinha.velY = bolinha.velY * -1;
			bolinha.posY = 0;
		}
	}
	
	
	public void agendarTransicao(int tempo, char novoEstado) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(tempo);
				} catch(Exception e) {
					e.printStackTrace();
				}
				estado = novoEstado;
			}
		});
		thread.start();
	}
	
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintComponent(g2d);
		
		
		if(estado == 'S') {
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, Principal.largura_tela, Principal.altura_tela);
		} else if(estado == 'R') {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, Principal.largura_tela, Principal.altura_tela);
		} else { // Se estiver n estado EXECUTANDO ou PAUSADO
			
			// desenha o chão do cenário
			g2d.drawImage(bg, 0, 0, Principal.largura_tela, Principal.altura_tela, null);
			// desenha as marcações de limite de movimentação
			g2d.setColor(Color.GRAY);
			g2d.fillRect(Principal.limite_direito, 0, 5, Principal.altura_tela);
			g2d.fillRect(Principal.limite_esquerdo, 0, 5, Principal.altura_tela);
			
			// Posição e dimensão do obj "Jogador" com parâmetros relativos ao obj
			g2d.drawImage(jogador.imgAtual, jogador.af, null);
			// Posição e dimensão do obj "Inimigo" com parâmetros relativos ao obj
			g2d.drawImage(inimigo.img, inimigo.af, null);
			// Posição e dimensão do obj "Bolinha" com parâmetros relativos ao obj
			g2d.drawImage(bolinha.img, bolinha.af, null);
			
			if(estado == 'E') { // Executando
				
			} else { // Pausado
				g2d.setColor(new Color(0,0,0,128));
				g2d.fillRect(0, 0, Principal.largura_tela, Principal.altura_tela);
			}
		}
	}
}

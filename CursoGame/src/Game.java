import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

// Janela em que o jogo ocorrerá de fato:

public class Game extends JPanel{
	private Bola bola;
	
	
	public Game() {
		bola = new Bola();
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
	
	public void handlerEvents(){
		
	}
	
	public void update(){
		bola.posX = bola.posX + bola.velX; // velocidade do objeto
	}
	
	public void render(){
		repaint(); // Redesenha a tela a cada repetição do gameloop()
	}
	
	// MÉTODO SOBESCRITO ---------------
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		g.setColor(Color.red);
		
		// Posição e dimensão do obj "Bola" com parâmetros relativos ao obj
		g.fillOval(bola.posX, bola.posY, bola.raio * 2, bola.raio * 2);
	}
}

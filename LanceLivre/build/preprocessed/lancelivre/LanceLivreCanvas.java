package lancelivre;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import lancelivre.util.Circunf;
import lancelivre.util.SegLinha;
import lancelivre.util.Vetor;

/*
 * TODO LIST:
 * 
 * - fazer o jogo responder corretamente às mudanças de orientação do jogo
 * - calibrar tudo pra ficar mais divertido
 *   - determinar a posição inicial da bola de uma forma decente
 *   - determinar a velocidade da bola após o lançamento de uma forma decente
 *   - ajustar a gravidade de acordo
 * - contar pontuação :)
 * - colocar fuckin' awesome graphics (opcional) \o/
 */

class LanceLivreCanvas extends GameCanvas implements Runnable {
    /** Duraçao do tick de simulaçao, em ms */
    private static final long TICK_DURATION = 25;
    
    /** Raio da bola */
    private static final double BALL_RADIUS = 5.0;
    
    /** Distância máxima que o jogador pode puxar a bola da origem */
    private static final double MAX_DIST = 50.0;
    
    private Graphics graphics;
    private volatile Thread thread;
    
    private long lastTick;
    private long currentTicks;
    
    private Vetor posicaoInicialBola;
    private Vetor posicaoBola;
    private Vetor velocidadeBola;
    
    private Vetor gravidade;
    
    /** Segmentos de linha representando a tabela e o aro */
    private SegLinha[] segLinhasTabela;
    
    private boolean bolaLancada;
    private boolean segurandoBola;

    public LanceLivreCanvas() {
        super(true);

        setFullScreenMode(true);

        graphics = getGraphics();
        
        init();
    }
    
    
    /** Inicializa o jogo */
    private void init() {
        // determina a posicao inicial da bola (TODO: fazer random)
        posicaoInicialBola = new Vetor(getWidth()/2, getHeight()/2);
        
        // poe a bola na posicao inicial
        posicaoBola = new Vetor(posicaoInicialBola);
        
        // a bola inicialmente fica parada
        velocidadeBola = new Vetor(0, 0);
        
        // vetor da aceleração da gravidade (px/s^2)
        gravidade = new Vetor(0, 150);
        
        // inicialmente a bola nao foi lancada
        bolaLancada = false;
        
        // inicialmente o jogador na esta selecionando a bola
        segurandoBola = false;
        
        // inicializa as linhas da tabela
        segLinhasTabela = new SegLinha[] {
            new SegLinha(new Vetor(220, 50), new Vetor(220, 110)),
            new SegLinha(new Vetor(215, 95), new Vetor(215, 100)),
            new SegLinha(new Vetor(200, 95), new Vetor(200, 100))
        };
    }
    
    private void updateGameState() {
        if(bolaLancada) {
            // dp = v*dt
            posicaoBola = posicaoBola.add(velocidadeBola.mul(TICK_DURATION/1000.0));
            // dv = a*dt
            velocidadeBola = velocidadeBola.add(gravidade.mul(TICK_DURATION/1000.0));
            
            // verifica se a bola (circunferencia c) colide com a tabela
            Circunf c = new Circunf(posicaoBola, BALL_RADIUS);
            for(int i = 0; i < segLinhasTabela.length; i++) {
                Vetor p = segLinhasTabela[i].getPontoMaisProximo(c);
                if(p.sub(posicaoBola).abs() - BALL_RADIUS < 1e-9) {
                    // faz a reflexao da velocidade
                    Vetor v = p.sub(posicaoBola).norm();
                    Vetor l = new Vetor(-v.getY(), v.getX());
                    velocidadeBola = l.mul(2*velocidadeBola.dot(l)/l.dot(l)).sub(velocidadeBola);
                }
            }
        }
        
        // se a bola sair da tela, recomeça o jogo
        if((int)posicaoBola.getX() >= getWidth() || (int)posicaoBola.getY() >= getHeight()) {
            init();
        }
    }
    
    private void drawScene() {
        // limpa o fundo pra preto
        graphics.setColor(0, 0, 0);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        
        // desenha uma bordinha batuta na posicao inicial da bola enquanto o
        // jogador segura ela
        if(segurandoBola) {
            graphics.setColor(128, 64, 19);
            graphics.drawArc((int)(posicaoInicialBola.getX() - BALL_RADIUS), (int)(posicaoInicialBola.getY() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
        }
        
        // desenha a bola
        graphics.setColor(255, 127, 39);
        graphics.fillArc((int)(posicaoBola.getX() - BALL_RADIUS), (int)(posicaoBola.getY() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
        
        // desenha as linhas da tabela
        for(int i = 0; i < segLinhasTabela.length; i++)
            segLinhasTabela[i].draw(graphics);
        
        flushGraphics();
    }

    public void run() {
        while(thread == Thread.currentThread()) {
            lastTick = System.currentTimeMillis();
            currentTicks++;

            updateGameState();
            drawScene();

            try {
                long sleepTime = Math.max(0, TICK_DURATION - (System.currentTimeMillis() - lastTick));
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void showNotify() {
        thread = new Thread(this);
        thread.start();
    }
    
    protected boolean dentroDaBola(int x, int y) {
        return posicaoBola.sub(new Vetor(x, y)).abs() - BALL_RADIUS < 1e-9;
    }
    
    protected void pointerPressed(int x, int y) {
        if(!bolaLancada && dentroDaBola(x, y)) {
            segurandoBola = true;
            pointerDragged(x, y);
        }
    }
    
    protected void pointerDragged(int x, int y) {
        if(!bolaLancada && segurandoBola) {
            // limita a distancia que da pra puxar a bola
            Vetor novaPos = new Vetor(x, y).sub(posicaoInicialBola);
            if(novaPos.abs() - MAX_DIST > 1e-9)
                novaPos = novaPos.norm().mul(MAX_DIST);
            posicaoBola = posicaoInicialBola.add(novaPos);
        }
    }
    
    protected void pointerReleased(int x, int y) {
        if(segurandoBola) {
            segurandoBola = false;
            bolaLancada = true;
            
            // TODO: criar uma modelagem melhor para a velocidade da bola
            // apos o lançamento
            Vetor dir = posicaoInicialBola.sub(posicaoBola);
            velocidadeBola = dir.mul(5.0);
        }
    }
}
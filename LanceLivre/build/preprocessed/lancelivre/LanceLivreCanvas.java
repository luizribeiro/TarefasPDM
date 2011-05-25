package lancelivre;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import lancelivre.util.Circunf;
import lancelivre.util.SegLinha;
import lancelivre.util.Vetor;

/*
 * TODO LIST:
 * 
 * - contar pontuação :)
 * - sprite (animada) para a bola
 */

class LanceLivreCanvas extends GameCanvas implements Runnable {
    /** Duraçao do tick de simulaçao, em ms */
    private static final long TICK_DURATION = 10;
    
    /** Raio da bola */
    private static final double BALL_RADIUS = 6.5;
    
    /** Coeficiente de restituição das colisões da bola com a tabela */
    private static final double COEF_REST = 0.8;
    
    /** Distância máxima que o jogador pode puxar a bola da origem */
    private static final double MAX_DIST = 60.0;
    
    /** Maximo de colisoes com o chao antes de acabar */
    private static final int MAX_COLISOES_CHAO = 5;
    
    private Graphics graphics;
    private volatile Thread thread;
    
    private Image imageBackground;
    private Image imageBackground2;
    private Image imageHoop;
    private Image imageHoop2;
    
    private long lastTick;
    private long currentTicks;
    
    private Vetor posicaoInicialBola;
    private Vetor posicaoBola;
    private Vetor velocidadeBola;
    
    private Vetor gravidade;
    
    /** Segmentos de linha representando a tabela e o aro */
    private SegLinha[] segLinhasTabela;
    private SegLinha segLinhaChao;
    private SegLinha segLinhaCesta;

    private int numColisoesChao;
    private boolean bolaLancada;
    private boolean segurandoBola;
    
    private int displayWidth;
    private int displayHeight;
    private boolean telaDeitada;

    private int pontos = 0;
    private Vetor posicoesBola[] = {new Vetor(116, 158), new Vetor(28, 158), new Vetor(45, 110),
                                    new Vetor(87, 170), new Vetor(240, 170), new Vetor(238, 160)};
    private int posicaoBolaAtual = 0;
    private Sprite digitos[];
    private Sprite bola;

    public LanceLivreCanvas() {
        super(true);

        setFullScreenMode(true);

        graphics = getGraphics();
        
        displayWidth = Math.max(getHeight(), getWidth());
        displayHeight = Math.min(getHeight(), getWidth());
        telaDeitada = getWidth() > getHeight();
        
        try {
            imageBackground = Image.createImage("/lancelivre/imgs/background.png");
            imageBackground2 = Image.createImage("/lancelivre/imgs/background2.png");
            imageHoop = Image.createImage("/lancelivre/imgs/hoop.png");
            imageHoop2 = Image.createImage("/lancelivre/imgs/hoop2.png");
            digitos = new Sprite[2];
            digitos[0] = new Sprite(Image.createImage("/lancelivre/imgs/placar.png"), 13, 20);
            digitos[1] = new Sprite(Image.createImage("/lancelivre/imgs/placar.png"), 13, 20);
            bola = new Sprite(Image.createImage("/lancelivre/imgs/ball2.png"), 13, 13);
        } catch (IOException ex) {
        }
        
        init();
    }
    
    
    /** Inicializa o jogo */
    private void init() {
        // determina a posicao inicial da bola (TODO: fazer random)
        posicaoInicialBola = posicoesBola[posicaoBolaAtual];
        posicaoBolaAtual = (posicaoBolaAtual+1)%6;
        
        // poe a bola na posicao inicial
        posicaoBola = new Vetor(posicaoInicialBola);
        
        // a bola inicialmente fica parada
        velocidadeBola = new Vetor(0, 0);
        
        // vetor da aceleração da gravidade (px/s^2)
        gravidade = new Vetor(0, 1000);
        
        // inicialmente a bola nao foi lancada
        bolaLancada = false;
        
        // inicialmente o jogador na esta selecionando a bola
        segurandoBola = false;
        
        // inicializa as linhas da tabela
        segLinhasTabela = new SegLinha[] {
            new SegLinha(new Vetor(300, 50), new Vetor(300, 110)), // tabela
            new SegLinha(new Vetor(295, 95), new Vetor(295, 100)), // aro esq
            new SegLinha(new Vetor(277, 95), new Vetor(277, 100))  // aro dir
        };
        
        // inicializa a linha do chão
        segLinhaChao = new SegLinha(new Vetor(0, 230), new Vetor(320, 230));
        
        // numero inicial de colisoes com o chao é zero
        numColisoesChao = 0;

        // segmento de linha correspondente a cesta, para verificação de ponto
        segLinhaCesta = new SegLinha(new Vetor(279, 96), new Vetor(293, 96));
    }
    
    private void updateGameState() {
        if(bolaLancada) {
            Vetor posicaoBolaAnt = new Vetor(posicaoBola);
            
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
                    velocidadeBola = l.mul(2*velocidadeBola.dot(l)/l.dot(l)).sub(velocidadeBola).mul(COEF_REST);
                    
                    // volta a bola pra posição anterior e para de procurar por colisões
                    posicaoBola = posicaoBolaAnt;
                    break;
                }
            }
            
            Vetor p = segLinhaChao.getPontoMaisProximo(c);
            if(p.sub(posicaoBola).abs() - BALL_RADIUS < 1e-9) {
                // faz a reflexao da velocidade
                Vetor v = p.sub(posicaoBola).norm();
                Vetor l = new Vetor(-v.getY(), v.getX());
                velocidadeBola = l.mul(2*velocidadeBola.dot(l)/l.dot(l)).sub(velocidadeBola).mul(COEF_REST);
                    
                // volta a bola pra posição anterior e para de procurar por colisões
                posicaoBola = posicaoBolaAnt;
                
                numColisoesChao++;
            }

            // Verifica se houve ponto
            if(velocidadeBola.getY() > 0){
                // bola na descendente
                if(segLinhaCesta.intersecta(posicaoBola, posicaoBolaAnt)){
                    pontos++;
                    digitos[0].nextFrame();;
                    if(pontos%10 == 0){
                        digitos[1].nextFrame();
                    }
                    //System.out.println(pontos);
                }
            }
        }
        
        // se a bola sair da tela, recomeça o jogo
        if((int)posicaoBola.getX() >= displayWidth || (int)posicaoBola.getY() >= displayHeight)
            init();
        else if(numColisoesChao >= MAX_COLISOES_CHAO)
            init();

    }
    
    private void drawScene() {
        // desenha o background
        if(telaDeitada)
            graphics.drawImage(imageBackground, 0, 0, Graphics.TOP | Graphics.LEFT);
        else
            graphics.drawImage(imageBackground2, 0, 0, Graphics.TOP | Graphics.LEFT);
        
        // desenha a tabela
        if(telaDeitada)
            graphics.drawImage(imageHoop, 346, 43, Graphics.TOP | Graphics.RIGHT);
        else
            graphics.drawImage(imageHoop2, 43, 346, Graphics.BOTTOM | Graphics.LEFT);
        
        // desenha uma bordinha batuta na posicao inicial da bola enquanto o
        // jogador segura ela
        if(segurandoBola) {
            graphics.setColor(128, 64, 19);
            if(telaDeitada)
                graphics.drawArc((int)(posicaoInicialBola.getX() - BALL_RADIUS), (int)(posicaoInicialBola.getY() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
            else
                graphics.drawArc((int)(posicaoInicialBola.getY() - BALL_RADIUS), (int)(posicaoInicialBola.getX() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
        }

        // Desenha placar
        graphics.setColor(0, 0, 180);
        if(telaDeitada){
            graphics.fillRect(34, 83, 37, 29);
            digitos[0].setTransform(Sprite.TRANS_NONE);
            digitos[0].setPosition(52, 87);
            digitos[0].paint(graphics);
            digitos[1].setTransform(Sprite.TRANS_NONE);
            digitos[1].setPosition(39, 87);
            digitos[1].paint(graphics);
        } else {
            graphics.fillRect(83, 34, 29, 37);
            digitos[0].setTransform(Sprite.TRANS_ROT270);
            digitos[0].setPosition(87, 39);
            digitos[0].paint(graphics);
            digitos[1].setTransform(Sprite.TRANS_ROT270);
            digitos[1].setPosition(87, 52);
            digitos[1].paint(graphics);
        }
        
        // desenha a bola
        graphics.setColor(255, 127, 39);
        if(telaDeitada)
            bola.setPosition((int)(posicaoBola.getX() - BALL_RADIUS), (int)(posicaoBola.getY() - BALL_RADIUS));
            //graphics.fillArc((int)(posicaoBola.getX() - BALL_RADIUS), (int)(posicaoBola.getY() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
        else
            bola.setPosition((int)(posicaoBola.getY() - BALL_RADIUS), (int)(posicaoBola.getX() - BALL_RADIUS));
            //graphics.fillArc((int)(posicaoBola.getY() - BALL_RADIUS), (int)(posicaoBola.getX() - BALL_RADIUS), (int)(2*BALL_RADIUS), (int)(2*BALL_RADIUS), 0, 360);
        bola.paint(graphics);
        if(bolaLancada && currentTicks % 10 == 0) bola.nextFrame();
        // desenha as linhas da tabela e do chao
        /*for(int i = 0; i < segLinhasTabela.length; i++)
            segLinhasTabela[i].draw(graphics, telaDeitada);
        segLinhaChao.draw(graphics, telaDeitada);*/

        flushGraphics();
    }

    public void run() {
        while(thread == Thread.currentThread()) {
            lastTick = System.currentTimeMillis();
            currentTicks++;

            updateGameState();
            drawScene();
            
            //System.out.println("v = " + velocidadeBola.abs());

            try {
                long sleepTime = Math.max(0, TICK_DURATION - (System.currentTimeMillis() - lastTick));
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
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
        if(!telaDeitada) { int t = x; x = y; y = t; }

        if(!bolaLancada && dentroDaBola(x, y)) {
            segurandoBola = true;
            if(!telaDeitada) { int t = x; x = y; y = t; }
            pointerDragged(x, y);
        }
        //System.out.println("X = " + x + " Y = " + y);
    }
    
    protected void pointerDragged(int x, int y) {
        if(!telaDeitada) { int t = x; x = y; y = t; }
        if(!bolaLancada && segurandoBola) {
            // limita a distancia que da pra puxar a bola
            Vetor novaPos = new Vetor(x, y).sub(posicaoInicialBola);
            if(novaPos.abs() - MAX_DIST > 1e-9)
                novaPos = novaPos.norm().mul(MAX_DIST);
//            if(!telaDeitada)
//                novaPos = new Vetor(novaPos.getY(), novaPos.getX());
            posicaoBola = posicaoInicialBola.add(novaPos);
        }
    }
    
    protected void pointerReleased(int x, int y) {
        if(!telaDeitada) { int t = x; x = y; y = t; }

        if(segurandoBola) {
            segurandoBola = false;
            bolaLancada = true;
            
            // TODO: criar uma modelagem melhor para a velocidade da bola
            // apos o lançamento
            Vetor dir = posicaoInicialBola.sub(posicaoBola);
            velocidadeBola = dir.mul(12.0);
        }
    }
    
    protected void sizeChanged(int width, int height) {
        telaDeitada = width > height;
    }
}
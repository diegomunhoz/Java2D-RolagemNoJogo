package br.com.diegomunhoz.game;

import br.com.diegomunhoz.core.AudioManager;
import br.com.diegomunhoz.core.InputManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class EntidadeJogador extends EntidadeQueMove {

    static final int STATE_STANDING = 0;
    static final int STATE_WALKING = 1;
    static final int STATE_JUMPING = 2;
    static final int STATE_FALLING = 3;
    int state;
    int stepInterval;
    int lastStepTick;
    ArrayList<BufferedImage> sprites;

    public EntidadeJogador(int x, int y) {
        super(x, y, 10);
        stepInterval = 20;
        sprites = new ArrayList<BufferedImage>();
    }

    @Override
    public void init() {
        // Inicializa o objeto, criando os sprites.
        // Os sprites consistem apenas de imagens com retângulos coloridos.
        BufferedImage img = new BufferedImage(30, 50,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        sprites.add(img);
        img = new BufferedImage(30, 50, BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.green);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        sprites.add(img);
        img = new BufferedImage(30, 50, BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.yellow);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        sprites.add(img);
        img = new BufferedImage(30, 50, BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.orange);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        sprites.add(img);
        // Inicializa o estado com STANDING (parado)
        state = STATE_STANDING;
        // Ajusta o tamanho do corpo de acordo com o tamanho sprite.
        pos.width = sprites.get(state).getWidth();
        pos.height = sprites.get(state).getHeight();
    }

    @Override
    public void update(int currentTick) {
        if (InputManager.getInstance().isPressed(KeyEvent.VK_RIGHT)) {
            // Se SETA DIREITA está pressionada, atribui um valor à aceleração.
            accel.x = 0.4;
        } else if (InputManager.getInstance().isPressed(
                KeyEvent.VK_LEFT)) {
            // Se SETA ESQUERDA está pressionada, atribui um valor negativo à aceleração.
            accel.x = -0.4;
        }
        if (InputManager.getInstance().isJustPressed(KeyEvent.VK_UP) && collidingEntities[COLLIDING_BELOW]
                != null) {
            // Se SETA ACIMA está pressionada, atribui um GRANDE valor negativo à aceleração vertical.
            accel.y = -10;
        }
        // Atualiza a velocidade e posição do objeto.
        super.update(currentTick);
        // Muda o estado de acordo com a velocidade resultando.
        if (speed.y < 0) {
            // Se a velocidade vertical é negativa, está subindo.
            if (state != STATE_JUMPING) {
                // Se acabou de mudar oara o estado JUMPING, executa o som de pulo.
                playSound("jump.wav");
            }
            state = STATE_JUMPING;
        } else if (speed.y > 0) {
            // Se a velocidade vertical é positiva, está subindo.
            state = STATE_FALLING;
        } else if (speed.x != 0) {
            // Se a velocidade horizontal é diferente de zero, está caminhando.
            if (state == STATE_FALLING) {
                // Se o estado anterior era FALLING, executa som de bater no chão.
                playSound("jumpEnd.wav");
            }
            state = STATE_WALKING;
            if (currentTick - lastStepTick > stepInterval) {
                // a cada "stepInterval" ticks, executa um som de passo.
                playSound("step.wav");
                lastStepTick = currentTick;
            }
        } else {
            // Se a velocidade horizontal é zero, está parado.
            if (state == STATE_FALLING) {
                // Se o estado anterior era FALLING, executa som de bater no chão.
                playSound("jumpEnd.wav");
            }
            state = STATE_STANDING;
        }
    }

    @Override
    public void render(Graphics2D g) {
        // Desenha o sprite pego da posição correspondente ao estado atual.
        int y = (int) (pos.y + pos.height) - sprites.get(state).getHeight();
        int x = (int) (pos.x + pos.width / 2) - (sprites.get(state).
                getWidth() / 2);
        g.drawImage(sprites.get(state), x, y, null);
    }

    public void playSound(String fileName) {
        try {
            AudioManager.getInstance().loadAudio(fileName).play();
        } catch (IOException ioe) {
        }
    }
}

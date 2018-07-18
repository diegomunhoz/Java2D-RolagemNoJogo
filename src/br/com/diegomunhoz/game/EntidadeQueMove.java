package br.com.diegomunhoz.game;

import java.awt.geom.Point2D;

public abstract class EntidadeQueMove extends Entidade {

    // Velocidade - quantidade de pixels que a entidade se move a cada tick.
    Point2D.Double speed;
    // Aceleração - taxa pela qual a velocidade aumenta a cada tick.
    Point2D.Double accel;
    // Velocidade máxima permitida.
    Point2D.Double maxSpeed;
    // Fricção - contante que diminui a velocidade horizontal (faz parar).
    double friction;
    // Gravidade - constante que aumenta a velocidade vertical (faz cair).
    double gravity;

    public EntidadeQueMove(int x, int y, double speedValue) {
        super(x, y);
        speed = new Point2D.Double(speedValue, speedValue);
        accel = new Point2D.Double();
        maxSpeed = new Point2D.Double(5, 15);
        friction = 0.3;
        gravity = 0.45;
    }

    @Override
    public void update(int currentTick) {
        // Para fazer o objeto andar basta informar a aceleração (accel)
        // antes de chamar este update.

        // Aumenta a velocidade com base na aceleração informada.
        speed.x += accel.x;
        // Não deixa a velocidade ultrapassar o máximo.
        if (speed.x < -maxSpeed.x) {
            speed.x = -maxSpeed.x;
        } else if (speed.x > maxSpeed.x) {
            speed.x = maxSpeed.x;
        }

        // Adiciona a gravidade na aceleração vertical.
        accel.y += gravity;
        // Aumenta a velocidade vertical com base na aceleração.
        speed.y += accel.y;
        // Não deixa a velocidade ultrapassar o máximo.
        if (speed.y < -maxSpeed.y) {
            speed.y = -maxSpeed.y;
        } else if (speed.y > maxSpeed.y) {
            speed.y = maxSpeed.y;
        }

        if (speed.y < 0) {
            // Se a velocidade é menor do que zero, o objeto está subindo.
            // Testa colisão na direção acima.
            if (collidingEntities[COLLIDING_ABOVE] != null) {
                // Se está colidindo, ajusta a posição.
                pos.y = collidingEntities[COLLIDING_ABOVE].pos.y
                        + collidingEntities[COLLIDING_ABOVE].pos.height;
                // Também zera a velocidade e a aceleração,
                // para interromper o movimento.
                speed.y = 0;
                accel.y = 0;
            }
        } else if (speed.y > 0) {
            // Se a velocidade é maior do que zero, o objeto está caindo.
            // Testa colisão na direção abaixo.
            if (collidingEntities[COLLIDING_BELOW] != null) {
                // Se está colidindo, ajusta a posição.
                pos.y = collidingEntities[COLLIDING_BELOW].pos.y
                        - pos.height
                        + 1;
                // Também zera a velocidade e a aceleração,
                // para interromper o movimento.
                speed.y = 0;
                accel.y = 0;
            }
        }

        if (speed.x < 0) {
            // Se a velocidade é menor do que zero,
            // o objeto está indo para a esquerda.
            // Testa colisão na direção esquerda.
            if (collidingEntities[COLLIDING_LEFT] != null) {
                // Se está colidindo, ajusta a posição.
                pos.x = collidingEntities[COLLIDING_LEFT].pos.x
                        + collidingEntities[COLLIDING_LEFT].pos.width - 1;
                // Também zera a velocidade e a aceleração,
                // para interromper o movimento.
                speed.x = 0;
                accel.x = 0;
            } else {
                // Se não está colidindo, deixa o movimento avançar,
                // mas diminui ele com base na fricção.
                speed.x += friction;
                // Não deixa a velocidade ficar maior do que zero.
                if (speed.x > 0) {
                    speed.x = 0;
                }
            }
        } else if (speed.x > 0) {
            // Se a velocidade é maior do que zero, o objeto está
            // indo para a direita.
            // Testa colisão na direção direita.
            if (collidingEntities[COLLIDING_RIGHT] != null) {
                // Se está colidindo, ajusta a posição.
                pos.x = collidingEntities[COLLIDING_RIGHT].pos.x
                        - pos.width
                        + 1;
                // Também zera a velocidade e a aceleração,
                // para interromper o movimento.
                speed.x = 0;
                accel.x = 0;
            } else {
                // Se não está colidindo, deixa o movimento avançar,
                // mas diminui ele com base na fricção.
                speed.x -= friction;
                // Não deixa a velocidade ficar menor do que zero.
                if (speed.x < 0) {
                    speed.x = 0;
                }
            }
        }
        // Muda a posição de acordo com a velocidade (que pode ser zero
        // dependendo dos casos acima).
        pos.x += speed.x;
        pos.y += speed.y;
        // Zera a aceleração para o próximo tick.
        accel.x = 0;
        accel.y = 0;
    }
}

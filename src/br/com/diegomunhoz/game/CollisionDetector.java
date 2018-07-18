package br.com.diegomunhoz.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionDetector {

    // Lista de objetos que deverão ser testados.
    ArrayList<Entidade> entidades;

    public CollisionDetector(ArrayList<Entidade> entidades) {
        // Recebe a lista de objetos ao ser criado.
        this.entidades = entidades;
    }

    public void update(int currentTick) {
        // Percorre a lista de objetos limpando os dados de colisão.
        // Atribui NULL para todos "collidingObjects".
        for (Entidade o : entidades) {
            for (int i = 0; i < 4; i++) {
                o.collidingEntities[i] = null;
            }
        }
        // Percorre a lista do primeiro até o penúltimo objeto.
        for (int i1 = 0; i1 < entidades.size() - 1; i1++) {
            Entidade o1 = entidades.get(i1);
            // Para cada objeto do FOR acima, percorre a lista do próximo objeto até o último.
            for (int i2 = i1 + 1; i2 < entidades.size(); i2++) {
                Entidade o2 = entidades.get(i2);
                // Testa se tem interseção entre os corpos dos objetos.
                if (o1.pos.intersects(o2.pos)) {
                    // Se sim, obtém o retângulo da interseção.
                    Rectangle2D rect = o1.pos.createIntersection(o2.pos);
                    // Verifica a relação entre alargura e a altura da interseção, para verificar o
                    // tipo de colisão, se vertical ou horizontal.
                    if (rect.getWidth() > rect.getHeight()) {
                        // Se a largura é maior que a altura, é colisão vertical.
                        if (o1.pos.getCenterY() < o2.pos.getCenterY()) {
                            // Se o1 está acima de o2...
                            // o1 está colidindo abaixo (below) com o2.
                            o1.collidingEntities[Entidade.COLLIDING_BELOW]
                                    = o2;
                            // o2 está colidindo acima (above) com o1.
                            o2.collidingEntities[Entidade.COLLIDING_ABOVE]
                                    = o1;
                        } else {
                            // Se o1 está abaixo de o2...
                            o1.collidingEntities[Entidade.COLLIDING_ABOVE]
                                    = o2;
                            o2.collidingEntities[Entidade.COLLIDING_BELOW]
                                    = o1;
                        }
                    } else {
                        // Se a altura é maior que a largura, é colisão horizontal.
                        if (o1.pos.getCenterX() < o2.pos.getCenterX()) {
                            // Se o1 está à esquerda de o2...
                            o1.collidingEntities[Entidade.COLLIDING_RIGHT]
                                    = o2;
                            o2.collidingEntities[Entidade.COLLIDING_LEFT]
                                    = o1;
                        } else {
                            // Se o1 está à direita de o2...
                            o1.collidingEntities[Entidade.COLLIDING_LEFT]
                                    = o2;
                            o2.collidingEntities[Entidade.COLLIDING_RIGHT]
                                    = o1;
                        }
                    }
                }
            }
        }
    }
}

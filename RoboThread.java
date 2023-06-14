/*Pontifícia Universidade Católica de Minas Gerais Instituto 
Ciências Exatas e Informática (ICEI) 
Engenharia de Computação
Disciplina: Sistemas Operacionais 
Professor: Diego Silva Caldeira Rocha
Aluno: Matheus Dias Soares
Trabalho Prático 3*/

import java.util.concurrent.Semaphore;

public class RoboThread extends Thread {

    private String nome;
    private Semaphore mySemaphore;
    private Semaphore nextSemaphore;

    public RoboThread(String nome, Semaphore mySemaphore, Semaphore nextSemaphore) {
        this.nome = nome;
        this.mySemaphore = mySemaphore;
        this.nextSemaphore = nextSemaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Aguarda o semáforo da thread anterior
                mySemaphore.acquire();

                // Movimento do robô
                move();

                // Libera o semáforo da próxima thread
                nextSemaphore.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        System.out.print(nome + " --> ");
    }

    public static void main(String[] args) {
        System.out.println("Aluno: Matheus Dias Soares");

        Semaphore bartSemaphore = new Semaphore(1);
        Semaphore lisaSemaphore = new Semaphore(0);
        Semaphore maggieSemaphore = new Semaphore(0);

        RoboThread bart = new RoboThread("Bart", bartSemaphore, lisaSemaphore);
        RoboThread lisa = new RoboThread("Lisa", lisaSemaphore, maggieSemaphore);
        RoboThread maggie = new RoboThread("Maggie", maggieSemaphore, bartSemaphore);

        bart.start();
        lisa.start();
        maggie.start();
    }
}

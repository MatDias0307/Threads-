/*Pontifícia Universidade Católica de Minas Gerais Instituto 
Ciências Exatas e Informática (ICEI) 
Engenharia de Computação
Disciplina: Sistemas Operacionais 
Professor: Diego Silva Caldeira Rocha
Aluno: Matheus Dias Soares
Trabalho Prático 3*/

import java.util.concurrent.Semaphore;

public class SelvagemThread {
    private static int N = 11; // número de porções no caldeirão
    private static Semaphore caldeirao = new Semaphore(1); // semáforo do caldeirão
    private static Semaphore cozinheiro = new Semaphore(0); // semáforo do cozinheiro
    private static Semaphore stopSemaphore = new Semaphore(1); // semáforo para exclusão mútua

    public static void main(String[] args) {
        System.out.println("Aluno: Matheus Dias Soares");

        // cria 20 selvagens
        for (int i = 1; i <= 20; i++) {
            new Thread(new Selvagem(i)).start();
        }
        // cria um cozinheiro
        new Thread(new Cozinheiro()).start();
    }

    static class Selvagem implements Runnable {
        private int id;

        public Selvagem(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep((long) (Math.random() * 10000)); // espera um tempo aleatório antes de tentar se servir
                    System.out.println("Selvagem " + id + " quer comer.");
                    caldeirao.acquire(); // adquire o semáforo do caldeirão
                    stopSemaphore.acquire(); // adquire o semáforo de exclusão mútua
                    if (N > 0) { // se ainda tiver porções no caldeirão
                        N--;
                        System.out.println("Selvagem " + id + " está comendo. Restam " + N + " porções no caldeirão.");
                    } else {
                        System.out.println("Caldeirão vazio. Selvagem " + id + " acorda o cozinheiro.");
                        cozinheiro.release(); // acorda o cozinheiro
                    }
                    stopSemaphore.release(); // libera o semáforo de exclusão mútua
                    caldeirao.release(); // libera o semáforo do caldeirão
                    Thread.sleep((long) (Math.random() * 10000)); // espera um tempo aleatório antes de comer novamente
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Cozinheiro implements Runnable {
        public void run() {
            while (true) {
                try {
                    cozinheiro.acquire(); // adquire o semáforo do cozinheiro
                    stopSemaphore.acquire(); // adquire o semáforo de exclusão mútua
                    N = 11; // enche o caldeirão com 10 porções
                    System.out.println("Cozinheiro encheu o caldeirão.");
                    stopSemaphore.release(); // libera o semáforo de exclusão mútua
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

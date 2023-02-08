import java.util.Random;

public class Main {
    /*
    Сделать банковское приложение.
    1 поток - пополняем
    2 поток - снимаем
    Если происходит overdraft - вывести сообщение об этом
    */

    public volatile static int cashValue = 500;
    public volatile static boolean overdraftCheck = false;

    public static void main(String[] args) {
        new Main().doStart();
    }

    void doStart() {
        System.out.println("Balance = " + cashValue);
        new Thread(putMoneyThread).start();
        System.out.println("Start putThread!");
        new Thread(getMoneyThread).start();
        System.out.println("Start getThread!");
    }

    Runnable putMoneyThread = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    Random random = new Random();
                    int putCash = random.nextInt(100) + 1;
                    System.out.println("[putMoneyThread] put = " + putCash);
                    if (overdraftCheck && cashValue + putCash > 0) {
                        System.out.println("[putMoneyThread] No more overdraft!");
                        overdraftCheck = false;
                    }
                    cashValue += putCash;
                    System.out.println("[putMoneyThread] Balance = " + cashValue);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    Runnable getMoneyThread = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                    Random random = new Random();
                    int withdrawCash = random.nextInt(300) + 1;
                    System.out.println("[getMoneyThread] get = " + withdrawCash);
                    if (cashValue - withdrawCash < 0) {
                        System.out.println("[getMoneyThread] Overdraft!");
                        overdraftCheck = true;
                    }
                    cashValue -= withdrawCash;
                    System.out.println("[getMoneyThread] Balance = " + cashValue);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
}
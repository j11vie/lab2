class Runner extends Thread {
    private static final int FINISH = 100;

    private final String title;
    private final int index;
    private final int[] positions;

    public Runner(String title, int startPriority, int index, int[] positions) {
        super(title);
        this.title = title;
        this.index = index;
        this.positions = positions;
        setPriority(startPriority);
    }

    @Override
    public void run() {
        int rival = (index == 0) ? 1 : 0;
        int step = 0;
        while (step < FINISH) {
            positions[index] = step;

            if (positions[index] < positions[rival]) {
                setPriority(Thread.MAX_PRIORITY); // отстаёт — повышаем приоритет
            } else {
                setPriority(Thread.MIN_PRIORITY); // лидирует — понижаем приоритет
            }
            Thread.yield();

            doWork();

            if (step % 10 == 0) {
                System.out.printf("%s: %d м, приоритет = %d%n", title, step, getPriority());
            }
            step++;
        }
        System.out.println(title + " пересёк финиш!");
    }

    // Имитация нагрузки
    private void doWork() {
        long counter = 0;
        for (int i = 0; i < 10000; i++) {
            counter += i;
        }
    }
}

public class Race {
    public static void main(String[] args) {
        int[] positions = new int[2];

        Runner rabbit = new Runner("Кролик", Thread.MIN_PRIORITY, 0, positions);
        Runner turtle = new Runner("Черепаха", Thread.MAX_PRIORITY, 1, positions);

        System.out.println("Старт гонки!\n");

        rabbit.start();
        turtle.start();
    }
}

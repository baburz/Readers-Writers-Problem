package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class ReadWriteLock{
    static Semaphore enter = new Semaphore(1);
    static Semaphore enter2 = new Semaphore(1);
    static Semaphore writer = new Semaphore(0);

    int s_reader = 0;
    int c_reader = 0;
    boolean w_writer = false;

    public void readLock() throws InterruptedException {
        enter.acquire();
        s_reader++;
        enter.release();
    }
    public void readUnLock() throws InterruptedException {
        enter2.acquire();
        c_reader++;
        if(w_writer && s_reader == c_reader) {
            writer.release();
        }
        enter2.release();
    }

    public void writeLock() throws InterruptedException {
        enter.acquire();
        enter2.acquire();
        if(s_reader == c_reader){
            enter2.release();
        }else{
            w_writer = true;
            enter2.release();
            writer.acquire();
            w_writer = false;
        }
    }
    public void writeUnLock() throws InterruptedException {
        enter.release();
    }

};

class Writer implements Runnable {
    private ReadWriteLock lock;

    public Writer(ReadWriteLock rw) {
        lock = rw;
    }

    public void run() {
        while (true){
            try {
                lock.writeLock();
                System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING");               //critical section
                Thread.sleep(2000);                                                                     //critical section
                System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING");    //critical section
                lock.writeUnLock();
                Thread.sleep(1000); //out of critical section
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Reader implements Runnable {
    private sync.ReadWriteLock lock;

    public Reader(sync.ReadWriteLock rw) { lock = rw; }

    public void run() {
        while (true) {
            try {
                lock.readLock();
                System.out.println("Thread "+Thread.currentThread().getName() + " is READING");               //critical section
                Thread.sleep(2000);                                                                     //critical section
                System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING");    //critical section
                lock.readUnLock();
                Thread.sleep(1000); //out of critical section
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


public class Test {
    public static void main(String [] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ReadWriteLock RW = new ReadWriteLock();

        executorService.execute(new Reader(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));

        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Writer(RW));

        executorService.shutdown();
    }
}


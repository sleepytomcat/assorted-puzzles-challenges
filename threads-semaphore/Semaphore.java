public class Semaphore {
    Semaphore(long initialCount) {
	counter = initialCount;
    }

    public static void main(String[] args) throws InterruptedException {
	Semaphore semaphore = new Semaphore(3);
	
	Thread[] threads = new Thread[10];
	for (int i = 0; i < threads.length; ++i) {
	    threads[i] = new Thread(new SemaphoreUser(semaphore));
	    threads[i].start();
	}

	for (Thread thread: threads) 
	    thread.join();
    }

    synchronized void acquire() throws InterruptedException {
	while (counter <= 0)
	    wait();
	--counter;
	System.out.println("Semaphore counter down to " + counter);
    }

    
    synchronized void release() {
	++counter;
	System.out.println("Semaphore counter up to " + counter);
	if (counter > 0)
	    notifyAll(); // let all waiting threads (if any) proceed
    }

    private long counter;
}

class SemaphoreUser implements Runnable {
    SemaphoreUser(Semaphore semaphore) {this.semaphore = semaphore;}

    @Override
    public void run() {
	System.out.println("Thread " + Thread.currentThread().getId() + " about to acquire semaphore");
	try {
	    semaphore.acquire();
	    System.out.println("Thread " + Thread.currentThread().getId() + " has acquired semaphore");
	    Thread.sleep(10000);
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	finally {
	    semaphore.release();
	    System.out.println("Thread " + Thread.currentThread().getId() + " has released semaphore");
	}
    }

    Semaphore semaphore;
}

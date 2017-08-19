public class CountdownLatch {
    CountdownLatch(long initialCount) {
	counter = initialCount;
    }

    public static void main(String[] args) throws InterruptedException {
	CountdownLatch latch = new CountdownLatch(10);
	
	Thread[] threads = new Thread[16];
	for (int i = 0; i < threads.length; ++i) {
	    threads[i] = new Thread(new LatchUser(latch));
	    threads[i].start();
	}

	while (latch.getCounter() > 0) {
	    Thread.sleep(1000);
	    latch.countDown();
	}

	for (Thread thread: threads) 
	    thread.join();
    }

    synchronized void acquire() throws InterruptedException {
	while (counter > 0)
	    wait();
    }
    
    synchronized void countDown() {
	--counter;
	System.out.println("Latch counter down to " + counter);
	if (counter == 0)
	    notifyAll(); // let all waiting threads (if any) proceed
    }

    synchronized long getCounter() {
	return counter;
    }

    private long counter;
}

class LatchUser implements Runnable {
    LatchUser(CountdownLatch latch) {this.latch = latch;}

    @Override
    public void run() {
	System.out.println("Thread " + Thread.currentThread().getId() + " about to acquire latch");
	try {
	    latch.acquire();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}

	System.out.println("Thread " + Thread.currentThread().getId() + " has acquired latch, and proceeding");
    }

    CountdownLatch latch;
}

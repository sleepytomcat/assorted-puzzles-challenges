import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;

class Pooling {
    public static void main(String[] args) throws InterruptedException {
	BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

	for (int i = 0; i < 500; ++i) {
	    Thread thread = new Thread(new MessageProcessor(messages));
	    thread.start();
	}

	Random randomNumbers = new Random();
	while (true) {
	    for (int i = 0; i < 1000; ++i) {
		messages.add(new Message(System.currentTimeMillis()));
	    }
	    Thread.sleep(1000);
	    System.out.println("Queue size is " + messages.size());
	}
    }
}

class Message {
    Message(long value) {this.value = value;}
    long getValue() {return value;}
    private long value;
}

class MessageProcessor implements Runnable {
    MessageProcessor(BlockingQueue<Message> messages) {
	this.messages = messages;
    }

    @Override
    public void run() {
	while(true) 
	    try {
		Message message = messages.poll();
		Thread.sleep(500);
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
	    }
    }

    private BlockingQueue<Message> messages;
}


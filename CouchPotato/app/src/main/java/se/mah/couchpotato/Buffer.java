package se.mah.couchpotato;

import java.util.LinkedList;

/**
 * @author Robin Johnsson & Jonatan Fridsten
 */

public class Buffer<T> {
    private LinkedList<T> buffer = new LinkedList<T>();

    public synchronized void put(T obj){
        buffer.addLast(obj);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException{
        while(buffer.isEmpty()){
            wait();
        }
        return buffer.removeFirst();
    }
}

package com.fame.plumbum.chataround.image_upload;

import java.util.ArrayList;

/**
 * This class is Custom queue implementation for making a Queue of ImageData files to be uploaded in
 * server
 * Created by Meghal on 5/28/2016.
 */
public class MyQueue<T> extends ArrayList<T> {


    /**
     * Returns the first element of queue
     *
     * @return
     */
    public T peek() {
        return get(size() - 1);
    }


    /**
     * Poll is a function that removes first element from queue and returns that at same time
     *
     * @return
     */
    public T poll() {
        T T = get(size() - 1);
        remove(size() - 1);
        return T;
    }

    /**
     * This method helps in adding new variable to Queue
     *
     * @param T
     */
    public void AddToQueue(T T) {

        for (int i = size(); i > 0; i--) {
            add(i, get(i - 1));
        }
        add(0, T);
    }


}

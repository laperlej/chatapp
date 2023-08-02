package chat;

import java.util.Iterator;

class RingBufferIterator<E> implements Iterator<E> {
  private RingBuffer<E> buffer;
  private int index;

  RingBufferIterator(RingBuffer<E> buffer) {
    this.buffer = buffer;
    this.index = 0;
  }

  @Override
  public boolean hasNext() {
    return this.index < this.buffer.length();
  }

  @Override
  public E next() {
    return this.buffer.get(this.index++);
  }
}

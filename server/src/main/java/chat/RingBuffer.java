package chat;

import java.util.Iterator;

class RingBuffer<E> implements Iterable<E> {
  private E[] buffer;
  private int length;
  private int reserved;
  private int head;

  RingBuffer(int reserved) {
    this.buffer = (E[]) new Object[reserved];
    this.reserved = reserved;
    this.length = 0;
    this.head = 0;
  }

  public void add(E item) {
    int idx = (this.head + this.length) % this.reserved;
    this.buffer[idx] = item;
    if (this.length < this.reserved) {
      this.length++;
    } else {
      this.head = (this.head + 1) % this.reserved;
    }
  }

  public E get(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= this.length) {
      throw new IndexOutOfBoundsException();
    }
    int idx = (this.head + index) % this.reserved;
    return this.buffer[idx];
  }

  public int length() {
    return this.length;
  }

  @Override
  public Iterator<E> iterator() {
    return new RingBufferIterator<E>(this);
  }
}

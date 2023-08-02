package chat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RingBufferTest {
  @Test
  void testAdd() {
    RingBuffer<String> buffer = new RingBuffer<String>(3);
    buffer.add("a");
    buffer.add("b");
    buffer.add("c");
    buffer.add("d");
    assertEquals("d", buffer.get(2), "The last item added should be the last item in the buffer");
  }

  @Test
  void testIterator() {
    RingBuffer<String> buffer = new RingBuffer<String>(3);
    buffer.add("a");
    buffer.add("b");
    buffer.add("c");
    buffer.add("d");
    String result = "";
    for (String item : buffer) {
      result += item;
    }
    assertEquals("bcd", result, "The iterator should iterate over the items in the buffer");
  }
}

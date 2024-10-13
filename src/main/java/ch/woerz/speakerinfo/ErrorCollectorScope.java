package ch.woerz.speakerinfo;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.StructuredTaskScope;

public class ErrorCollectorScope<T> extends StructuredTaskScope<T> {
  private final Collection<Throwable> exceptions = new ConcurrentLinkedDeque<>();

  @Override
  protected void handleComplete(Subtask<? extends T> subtask) {
    if (subtask.state() == Subtask.State.FAILED) {
      this.exceptions.add(subtask.exception());
    }
  }

  public RuntimeException collectedException() {
    var rte = new RuntimeException();
    this.exceptions.forEach(rte::addSuppressed);
    return rte;
  }
}

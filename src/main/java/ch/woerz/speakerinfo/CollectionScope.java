package ch.woerz.speakerinfo;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CollectionScope<T> extends ErrorCollectorScope<T> {

  private final Collection<T> results = new ConcurrentLinkedQueue<>();

  @Override
  protected void handleComplete(Subtask<? extends T> subtask) {
    super.handleComplete(subtask);
    if (subtask.state() == Subtask.State.SUCCESS) {
      this.results.add(subtask.get());
    }
  }

  @SneakyThrows
  public List<T> getResults() {
    this.join();
    return List.copyOf(results);
  }
}

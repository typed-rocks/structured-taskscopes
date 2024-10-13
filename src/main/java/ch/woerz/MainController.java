package ch.woerz;

import ch.woerz.oldsv.TestRecord;
import ch.woerz.oldsv.ThreadLocals;
import ch.woerz.speakerinfo.AggregatorService;
import ch.woerz.speakerinfo.AggregatorServiceAsync;
import ch.woerz.speakerinfo.Model.Speaker;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class MainController {

  @Autowired
  private AggregatorService aggregatorService;

  @Autowired
  private AggregatorServiceAsync aggregatorServiceAsync;

  @SneakyThrows
  @PostConstruct
  void start() {
    var t1 = Thread.ofPlatform().unstarted(() -> {
      TestRecord asdf = new TestRecord("asdf");
      ThreadLocals.USER_TL.set(asdf);

      var inner = Thread.ofPlatform().unstarted(() -> {
        System.out.println(ThreadLocals.USER_TL.get());
      });

      inner.start();
      try {
        inner.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    t1.start();
    t1.join();
  }

  @SneakyThrows
  @GetMapping("/speaker")
  public Speaker getSpeaker() {
    return aggregatorService.aggregate("speaker");
  }


  @SneakyThrows
  @GetMapping("/speaker-async")
  public CompletableFuture<Speaker> getSpeakerAsync() {
    return aggregatorServiceAsync.aggregateAsync("speaker");
  }

}

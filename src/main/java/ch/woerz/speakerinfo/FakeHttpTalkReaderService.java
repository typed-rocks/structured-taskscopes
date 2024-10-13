package ch.woerz.speakerinfo;

import ch.woerz.speakerinfo.Model.Talk;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.delayedExecutor;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class FakeHttpTalkReaderService {
  @SneakyThrows
  Talk readTalkForSpeaker(String speakerName) {
    Thread.sleep(500);
    return new Talk("Talk von " + speakerName);
  }

  @SneakyThrows
  CompletableFuture<Talk> readTalkForSpeakerAsync(String speakerName) {
    return supplyAsync(() -> new Talk("Talk von " + speakerName), delayedExecutor(1000, TimeUnit.MILLISECONDS));
  }
}

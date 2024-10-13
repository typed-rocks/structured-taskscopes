package ch.woerz.speakerinfo;

import ch.woerz.speakerinfo.Model.Speaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static ch.woerz.speakerinfo.Model.InfoProvider.*;
import static ch.woerz.speakerinfo.Model.SpeakerInfo;

@Service
public class AggregatorServiceAsync {

  @Autowired
  private FakeHttpTalkReaderService fakeHttpTalkReaderService;

  @Autowired
  private FakeSpeakerInfoHttpService fakeSpeakerInfoHttpService;

  public CompletableFuture<Speaker> aggregateAsync(String speakerName) {

    var talkFuture = fakeHttpTalkReaderService.readTalkForSpeakerAsync(speakerName);
    var speakerFuture = errorWrapped(fakeSpeakerInfoHttpService.readSpeakerInfoFromProviderAsync(speakerName, TWITTER));
    var speakerFuture2 = errorWrapped(fakeSpeakerInfoHttpService.readSpeakerInfoFromProviderAsync(speakerName, FACEBOOK));
    var speakerFutur3 = errorWrapped(fakeSpeakerInfoHttpService.readSpeakerInfoFromProviderAsync(speakerName, LINKEDIN));

    var speakerFutures = List.of(speakerFuture, speakerFuture2, speakerFutur3);
    return talkFuture
        .exceptionally(t -> {
          speakerFutures.forEach(future -> future.cancel(true));
          throw new RuntimeException(t);
        })
        .thenCompose(talk -> CompletableFuture.allOf(speakerFuture, speakerFuture2, speakerFutur3)
            .thenApply(_ -> speakerFutures
                .stream()
                .map(f -> {
                  try {
                    return f.get();
                  } catch (InterruptedException | ExecutionException e) {
                    return new SpeakerInfo(List.of());
                  }
                })
                .min(Comparator.comparingInt(info -> info.getInfo().size()))
                .orElseThrow())
            .thenApply(info -> new Speaker(talk, info)));
  }

  CompletableFuture<SpeakerInfo> errorWrapped(CompletableFuture<SpeakerInfo> infoFuture) {
    return infoFuture.exceptionally(error -> new SpeakerInfo());
  }
}

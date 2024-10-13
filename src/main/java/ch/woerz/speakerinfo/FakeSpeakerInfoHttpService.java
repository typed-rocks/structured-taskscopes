package ch.woerz.speakerinfo;

import ch.woerz.speakerinfo.Model.SpeakerInfo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static ch.woerz.speakerinfo.Model.InfoProvider;
import static java.util.concurrent.CompletableFuture.delayedExecutor;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class FakeSpeakerInfoHttpService {

  private final Map<InfoProvider, List<String>> infos = Map.of(
      InfoProvider.LINKEDIN, List.of("Christian Woerz", "Fullstack", "Java"),
      InfoProvider.TWITTER, List.of("Christian Woerz"),
      InfoProvider.FACEBOOK, List.of()
  );

  @SneakyThrows
  SpeakerInfo readSpeakerInfoFromProvider(String speakerName, InfoProvider provider) {
    Thread.sleep(500);
    return new SpeakerInfo(infos.get(provider));
  }

  @SneakyThrows
  CompletableFuture<SpeakerInfo> readSpeakerInfoFromProviderAsync(String speakerName, InfoProvider provider) {
    return supplyAsync(() -> new SpeakerInfo(infos.get(provider)), delayedExecutor(500, TimeUnit.MILLISECONDS));
  }
}

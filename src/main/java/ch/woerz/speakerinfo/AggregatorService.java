package ch.woerz.speakerinfo;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ch.woerz.speakerinfo.Model.*;

@Service
public class AggregatorService {

  @Autowired
  private SpeakerInfoService speakerInfoService;

  @Autowired
  private FakeHttpTalkReaderService fakeSpeakerInfoHttpService;

  @SneakyThrows
  public Speaker aggregate(String speakerName) {
    try (var scope = new SpeakerScope()) {
      scope.fork(() -> speakerInfoService.readBestSpeakerInfo(speakerName));
      scope.fork(() -> fakeSpeakerInfoHttpService.readTalkForSpeaker(speakerName));
      scope.join();
      return scope.speaker();
    }
  }
}

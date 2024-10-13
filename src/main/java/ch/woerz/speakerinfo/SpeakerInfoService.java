package ch.woerz.speakerinfo;

import ch.woerz.speakerinfo.Model.InfoProvider;
import ch.woerz.speakerinfo.Model.SpeakerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpeakerInfoService {

  @Autowired
  private FakeSpeakerInfoHttpService fakeSpeakerInfoHttpService;

  public SpeakerInfo readBestSpeakerInfo(String speakerName) {
    try (var scope = new SpeakerInfoScope()) {
      scope.fork(() -> fakeSpeakerInfoHttpService.readSpeakerInfoFromProvider(speakerName, InfoProvider.TWITTER));
      scope.fork(() -> fakeSpeakerInfoHttpService.readSpeakerInfoFromProvider(speakerName, InfoProvider.FACEBOOK));
      scope.fork(() -> fakeSpeakerInfoHttpService.readSpeakerInfoFromProvider(speakerName, InfoProvider.LINKEDIN));
      return scope.bestSpeakerInfo();
    }
  }
}

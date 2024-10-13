package ch.woerz.speakerinfo;

import ch.woerz.speakerinfo.Model.SpeakerInfo;

import java.util.Comparator;

public class SpeakerInfoScope extends CollectionScope<SpeakerInfo> {
  SpeakerInfo bestSpeakerInfo() {
    return this.getResults().stream()
        .max(Comparator.comparingInt(info -> info.getInfo().size()))
        .orElseThrow(this::collectedException);
  }
}

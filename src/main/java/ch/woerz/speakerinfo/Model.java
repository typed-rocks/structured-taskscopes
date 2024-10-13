package ch.woerz.speakerinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class Model {
  public enum InfoProvider {
    TWITTER, LINKEDIN, FACEBOOK
  }

  public record Speaker(Talk talk, SpeakerInfo speakerInfo) {
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static final class SpeakerInfo implements SpeakerPart {
    private List<String> info;
  }

  public sealed interface SpeakerPart permits SpeakerInfo, Talk {
  }

  public record Talk(String talkName) implements SpeakerPart {
  }

}

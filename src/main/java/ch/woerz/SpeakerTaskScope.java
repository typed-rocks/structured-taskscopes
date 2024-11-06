package ch.woerz;

import java.util.concurrent.StructuredTaskScope;

public class SpeakerTaskScope extends StructuredTaskScope<SpeakerPart> {

  private volatile Talk talk;
  private volatile Infos infos;

  @Override
  protected void handleComplete(Subtask<? extends SpeakerPart> subtask) {
    switch (subtask.get()) {
      case Talk talk -> this.talk = talk;
      case Infos infos -> this.infos = infos;
    }
  }

  Speaker getSpeaker() {
    return new Speaker(this.talk, this.infos);
  }

}

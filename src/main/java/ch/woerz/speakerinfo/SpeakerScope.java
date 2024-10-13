package ch.woerz.speakerinfo;

import static ch.woerz.speakerinfo.Model.*;

public class SpeakerScope extends ErrorCollectorScope<SpeakerPart> {
  private volatile SpeakerInfo speakerInfo;
  private volatile Talk talk;

  @Override
  protected void handleComplete(Subtask<? extends SpeakerPart> subtask) {
    if (subtask.state() == Subtask.State.SUCCESS) {
      switch (subtask.get()) {
        case SpeakerInfo info -> this.speakerInfo = info;
        case Talk t -> this.talk = t;
      }
    }
  }

  Speaker speaker() {
    if (this.talk == null) {
      throw collectedException();
    }
    return new Speaker(this.talk, this.speakerInfo);
  }
}

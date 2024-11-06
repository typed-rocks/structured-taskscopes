package ch.woerz;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.woerz.AllLocals.USER_SC;
import static ch.woerz.AllLocals.USER_TL;

@Service
public class FakeHttpService {
  @SneakyThrows
  public Talk retrieveTalk(String name) {
    Thread.sleep(1_000);
    return new Talk("Talk für " + name);
  }

  @SneakyThrows
  public Infos retrieveInfoFromGoogle(String name) {
    System.out.println("In Google: TL " + USER_TL.get());
    System.out.println("In Google: SV " + USER_SC.get());
    Thread.sleep(500);
    return new Infos(List.of(name, "18.10.1994"));
  }

  @SneakyThrows
  public Infos retrieveInfoFromLinkedin(String name) {
    Thread.sleep(500);
    return new Infos(List.of(name, "18.10.1994", "Fullstack"));
  }

  @SneakyThrows
  public Infos retrieveInfoFromFacebook(String name) {
    throw new RuntimeException("Keine Daten vorhanden");
  }
}

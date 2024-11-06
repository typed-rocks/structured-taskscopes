package ch.woerz;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ch.woerz.AllLocals.USER_SC;
import static ch.woerz.AllLocals.USER_TL;

@RestController
@RequestMapping("/api")
public class MainController {

  @Autowired
  private FakeHttpService fakeHttpService;

  @GetMapping("/talk/{name}")
  Talk getTalk(@PathVariable String name) {
    return fakeHttpService.retrieveTalk(name);
  }

  @SneakyThrows
  @GetMapping("/speaker/{name}")
  Speaker getSpeaker(@PathVariable String name) {
    System.out.println("MainController TL: " + USER_TL.get());
    System.out.println("MainController SV: " + USER_SC.get());
    try (var scope = new SpeakerTaskScope()) {
      scope.fork(() -> fakeHttpService.retrieveTalk(name));
      scope.fork(() -> getSpeakerInfos(name));
      scope.join();
      return scope.getSpeaker();
    }
  }

  @SneakyThrows
  Infos getSpeakerInfos(String name) {
    try (var scope = new InfoTaskScope()) {
      scope.fork(() -> fakeHttpService.retrieveInfoFromGoogle(name));
      scope.fork(() -> fakeHttpService.retrieveInfoFromFacebook(name));
      scope.fork(() -> fakeHttpService.retrieveInfoFromLinkedin(name));
      scope.join();
      return scope.bestInfos();
    }
  }

}

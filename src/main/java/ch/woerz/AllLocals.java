package ch.woerz;

public class AllLocals {
  public static final ThreadLocal<String> USER_TL = new ThreadLocal<>();
  public static final ScopedValue<String> USER_SC = ScopedValue.newInstance();
}

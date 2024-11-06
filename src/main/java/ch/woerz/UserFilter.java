package ch.woerz;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static ch.woerz.AllLocals.USER_SC;
import static ch.woerz.AllLocals.USER_TL;

@Component
public class UserFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    final var http = (HttpServletRequest) servletRequest;
    final var user = http.getHeader("user");

    USER_TL.set(user);
    ScopedValue.runWhere(USER_SC, user, () -> {
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } catch (IOException | ServletException e) {
        throw new RuntimeException(e);
      }
    });

  }
}

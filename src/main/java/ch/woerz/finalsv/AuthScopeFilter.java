package ch.woerz.finalsv;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthScopeFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain) {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    final var userHeader = httpRequest.getHeader("user");

    ScopedValue.runWhere(ScopedValues.USER_VALUE, userHeader, () -> {
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } catch (IOException | ServletException e) {
        throw new RuntimeException(e);
      }
    });
  }
}

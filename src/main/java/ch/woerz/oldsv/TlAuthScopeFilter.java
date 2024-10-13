package ch.woerz.oldsv;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


@Component
public class TlAuthScopeFilter implements Filter {

  @SneakyThrows
  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain) {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    final var userHeader = httpRequest.getHeader("user");
    ThreadLocals.USER_TL.set(new TestRecord(userHeader));

    filterChain.doFilter(servletRequest, servletResponse);

  }
}

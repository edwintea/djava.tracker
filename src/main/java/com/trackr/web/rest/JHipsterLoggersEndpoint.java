package com.trackr.web.rest;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.web.rest.vm.LoggerVM;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/management/loggers")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class JHipsterLoggersEndpoint {

  @GET
  @RolesAllowed(AuthoritiesConstants.ADMIN)
  public LoggersWrapper getLoggers() {
    Enumeration<String> loggerNames = LogManager.getLogManager().getLoggerNames();

    Map<String, LoggerVM> loggers = Collections
      .list(loggerNames)
      .stream()
      .filter(name -> !name.isBlank())
      .map(this::getLogger)
      .filter(Objects::nonNull)
      .collect(Collectors.toMap(logger -> logger.name, Function.identity()));

    return new LoggersWrapper(loggers);
  }

  private LoggerVM getLogger(String loggerName) {
    return Optional.ofNullable(Logger.getLogger(loggerName)).map(logger -> new LoggerVM(loggerName, logger)).orElse(null);
  }

  @POST
  @Path("/{name}")
  @RolesAllowed(AuthoritiesConstants.ADMIN)
  public Response updateLoggerLevel(@PathParam("name") String name, LoggerVM loggerVM) {
    Logger logger = Logger.getLogger(name);

    if (logger == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    Level level = Level.parse(loggerVM.configuredLevel);
    logger.setLevel(level);

    return Response.ok().build();
  }

  @RegisterForReflection
  public static class LoggersWrapper {
    public final Map<String, LoggerVM> loggers;

    public LoggersWrapper(Map<String, LoggerVM> loggers) {
      this.loggers = loggers;
    }
  }
}

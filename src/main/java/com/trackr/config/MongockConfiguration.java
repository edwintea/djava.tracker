package com.trackr.config;

import com.github.cloudyrock.mongock.MongockAnnotationProcessor;
import com.github.cloudyrock.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import com.github.cloudyrock.standalone.MongockStandalone;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.oracle.svm.core.annotate.AutomaticFeature;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.reflections.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MongockConfiguration {
  private static final List<Class<?>> changeLogs;

  static {
    changeLogs = new MongockAnnotationProcessor().getChangeLogAnnotationClass()
      .stream()
      .flatMap(changeLogClass ->
        new Reflections("com.trackr.config.dbmigrations").getTypesAnnotatedWith(changeLogClass).stream()
      )
      .collect(Collectors.toList());
  }

  @ConfigProperty(name = "quarkus.mongodb.database")
  String databaseName;

  @ConfigProperty(name = "quarkus.mongodb.connection-string")
  String connectionString;

  void onStart(@Observes StartupEvent ev) {
    MongoClientSettings.Builder builder = MongoClientSettings.builder();
    ConnectionString connectionStringObject = new ConnectionString(connectionString);
    builder.applyConnectionString(connectionStringObject);

    MongoClient mongoClient = MongoClients.create(builder.build());
    MongockStandalone
      .builder()
      .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, databaseName))
      .addChangeLogClasses(changeLogs)
      .buildRunner()
      .execute();
  }

  @AutomaticFeature
  private static class NativeSupport implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
      changeLogs.forEach(clazz -> {
        RuntimeReflection.register(clazz);
        RuntimeReflection.register(clazz.getConstructors());
        RuntimeReflection.register(clazz.getMethods());
      });
    }
  }
}

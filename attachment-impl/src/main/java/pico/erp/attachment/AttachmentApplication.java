package pico.erp.attachment;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Properties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.impl.ApplicationImpl;

@Slf4j
@SpringBootConfigs
public class AttachmentApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "attachment/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=attachment/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(AttachmentApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  @SneakyThrows
  public static void main(String[] args) {
    application().run(args);
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule("interface-implements", Version.unknownVersion());
    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
    module.setAbstractTypes(resolver);
    mapper.registerModule(module);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}

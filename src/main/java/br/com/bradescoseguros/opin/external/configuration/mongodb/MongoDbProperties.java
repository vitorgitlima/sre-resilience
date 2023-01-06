package br.com.bradescoseguros.opin.external.configuration.mongodb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties("mongo-properties")
public class MongoDbProperties {

	private String url;

	private String host;

	private String port;

	private String database;

	private String username;

	private String password;

	private Integer connectionPoolMaxConnectionLifeTime;

	private Integer connectionPoolMinSize;

	private Integer connectionPoolMaxSize;

	private Integer connectionPoolMaintenanceInitialDelay;

	private Integer connectionPoolMaintenanceFrequency;

	private Integer connectionPoolMaxConnectionIdleTime;

	private Integer connectionPoolMaxWaitTime;
}
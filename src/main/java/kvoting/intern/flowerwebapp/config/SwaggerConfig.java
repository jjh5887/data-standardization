package kvoting.intern.flowerwebapp.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {
	private final TypeResolver typeResolver;

	@Bean
	public Docket api() {
		ParameterBuilder header = new ParameterBuilder();
		Parameter build = header.name("X-AUTH-TOKEN")
			.description("사용자 인증용 토큰")
			.modelRef(new ModelRef("string"))
			.parameterType("header")
			.required(false)
			.build();

		return new Docket(DocumentationType.SWAGGER_2)
			.alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
				typeResolver.resolve(Page.class)))
			.consumes(getConsumeContentTypes())
			.produces(getProduceContentTypes())
			.apiInfo(getApiInfo())
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.ant("/**"))
			.build().globalOperationParameters(List.of(build));
	}

	private Set<String> getConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json;charset=UTF-8");
		consumes.add("application/x-www-form-urlencoded");
		return consumes;
	}

	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json;charset=UTF-8");
		return produces;
	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
			.title("API")
			.description("[DDaJa] REST API")
			.version("1.0")
			.build();
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder
			.builder()
			.operationsSorter(OperationsSorter.METHOD)
			.build();
	}

	@Getter
	@Setter
	@ApiModel
	static class Page {
		@ApiModelProperty(value = "페이지 번호(0, 1, 2 ... N)")
		private Integer page;

		@ApiModelProperty(value = "페이지 크기", allowableValues = "range[1, 50]")
		private Integer size;
	}
}
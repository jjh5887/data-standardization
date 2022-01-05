package kvoting.intern.flowerwebapp.config;

import static kvoting.intern.flowerwebapp.config.ItemUrl.*;

import lombok.Getter;

@Getter
public class RegUrl {

	public static final String REGISTRATION = "/reg";
	private static final String REG = "-reg";

	public static final String WORD_REG = WORD + REG;
	public static final String DOMAIN_REG = DOMAIN + REG;
	public static final String DICT_REG = DICT + REG;
	public static final String COMMON_CODE_REG = COMMON_CODE + REG;
	public static final String CONSTRAINT_REG = CONSTRAINT + REG;
	public static final String CUSTOM_DOMAIN_REG = CUSTOM_DOMAIN + REG;
}

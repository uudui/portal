package com.nx.core;

import com.nx.core.security.SecurityConfiguration;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Neal on 10/21 021.
 */
@Configuration
@Import(value = {RepositoryConfiguration.class, SecurityConfiguration.class})
public class RootConfiguration {

}

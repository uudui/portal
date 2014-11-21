package com.nx.core;

import com.nx.core.security.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Neal on 10/21 021.
 */
@Configuration
@Import(value = {RepositoryConfiguration.class, SecurityConfiguration.class})
public class RootConfiguration {

}

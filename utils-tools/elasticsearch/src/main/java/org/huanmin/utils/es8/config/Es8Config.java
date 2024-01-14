package org.huanmin.utils.es8.config;

import org.huanmin.utils.es8.utli.Es8Client;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author huanmin
 * @date 2024/1/5
 */

@Configuration
@Import({Es8Client.class})
public class Es8Config {
}

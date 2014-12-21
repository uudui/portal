package com.nx.core.utils;

import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;

import java.awt.*;

/**
 * Created by Neal on 10/19 019.
 */
public class CaptchaUtils {

    // 随机产生的字符串
    private static final String RANDOM_STRS = "1234567890";

    private static final int FONT_SIZE = 22;

    public static ConfigurableCaptchaService generateCaptchaService() {
        ConfigurableCaptchaService captchaService = new ConfigurableCaptchaService();
        captchaService.setColorFactory(new SingleColorFactory(new Color(63, 56, 161)));

        RandomWordFactory randomWordFactory = new RandomWordFactory();
        randomWordFactory.setCharacters(RANDOM_STRS);
        randomWordFactory.setMaxLength(4);
        randomWordFactory.setMinLength(4);
        captchaService.setWordFactory(randomWordFactory);

        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMaxSize(FONT_SIZE);
        fontFactory.setMinSize(FONT_SIZE);
        captchaService.setFontFactory(fontFactory);

        captchaService.setFilterFactory(new CurvesRippleFilterFactory(captchaService.getColorFactory()));
        captchaService.setHeight(30);
        captchaService.setWidth(70);
        return captchaService;
    }
}

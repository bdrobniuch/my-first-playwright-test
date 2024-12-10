package fesle.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;


import java.util.Arrays;

public class ChromeOptions implements OptionsFactory {
    @Override
    public Options getOptions(){
        return new Options().setLaunchOptions(
                new BrowserType.LaunchOptions()
                        //.setSlowMo(2000)
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        )
                .setTestIdAttribute("data-test");


    }
}

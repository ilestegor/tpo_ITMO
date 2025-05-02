package org.healthapp.util

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

enum class Browser{
    CHROME,
    FIREFOX
}

object WebDriverFactory {
    fun createDriver(browser: Browser): WebDriver{
        return when (browser) {
            Browser.CHROME -> {
                val options = ChromeOptions()
//                options.addArguments("--headless=new")
                ChromeDriver(options)
            }
            Browser.FIREFOX -> {
                val options = FirefoxOptions()
//                options.addArguments("-headless")
                FirefoxDriver(options)
            }
        }
    }

    fun createChromeDriver() : WebDriver{
        return ChromeDriver()
    }

    fun createFireFoxDriver(): WebDriver{
        return FirefoxDriver()
    }
}
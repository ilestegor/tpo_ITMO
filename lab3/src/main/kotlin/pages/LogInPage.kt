package org.healthapp.pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.bidi.log.Log
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page

class LogInPage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    @FindBy(xpath = "//*[@id=\"login_field\"]")
    private lateinit var loginField: WebElement

    @FindBy(xpath = "//*[@id=\"password\"]")
    private lateinit var passwordField: WebElement

    @FindBy(xpath = "//input[@type='submit']")
    private lateinit var submitLogInFormButton: WebElement

    @FindBy(xpath = "//a[contains(@href, 'signup')]")
    private lateinit var redirectToRegisterPageLink: WebElement


    fun enterLogin(value: String) : LogInPage{
        sendText(value, loginField)
        return this
    }
    fun enterPassword(value: String) : LogInPage{
        sendText(value, passwordField)
        return this
    }

    fun submitLogInForm() : HomePage{
        click(submitLogInFormButton)
        return HomePage(driver)
    }

    fun isLoginPage() : Boolean{
        wait.until(ExpectedConditions.urlContains("/login"))
        return driver.currentUrl?.contains("/login") ?: false
    }
}
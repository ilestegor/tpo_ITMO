package org.healthapp.pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import pages.Page


//https://github.com/
class MainPage(driver: WebDriver) : Page(driver){
    init {
        initPage(driver)
    }

    @FindBy(xpath = "//button[@data-hotkey='s,/']")
    private lateinit var searchButton: WebElement

    @FindBy(xpath = "//*[@id=\"query-builder-test\"]")
    private lateinit var searchInput: WebElement

    @FindBy(xpath = "(//a[contains(@href, 'login')])[2]")
    private lateinit var signInButton: WebElement

    fun searchRepository(repositoryName: String) : SearchPage{
        click(searchButton)
        sendText(repositoryName, searchInput)
        submitQuery(searchInput)
        return SearchPage(driver)
    }

    fun redirectToLogIn()  : LogInPage{
        click(signInButton)
        return LogInPage(driver)
    }
}
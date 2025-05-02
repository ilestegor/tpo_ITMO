package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page

class HomePage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    private val searchButtonElement = By.xpath("//button[@data-hotkey='s,/']")

    private val searchInput = By.xpath("//*[@id=\"query-builder-test\"]")

    private val homeDashBoard = By.xpath("//div[contains(@id, 'dashboard')]")

    fun search(repositoryName: String) : SearchPage{
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(searchButtonElement)))
        sendText(repositoryName, wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)))
        submitQuery(wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)))
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(searchInput)))
        return SearchPage(driver)
    }

    fun isHomePageVisible() : Boolean{
        return wait.until(ExpectedConditions.visibilityOfElementLocated(homeDashBoard)).isDisplayed
    }

}




package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page

class RepositoryPage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    private val forkCounter = By.xpath("//*[@id=\"repo-network-counter\"]")

    fun getForkCount() : Int{
        val forkCount =  wait.until(ExpectedConditions.visibilityOfElementLocated(forkCounter))
        return forkCount.getDomAttribute("title")?.replace(",", "")?.toInt() ?: 0
    }
}
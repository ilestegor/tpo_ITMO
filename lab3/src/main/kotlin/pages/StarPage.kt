package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page

class StarPage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    private val starredRepositoriesList = By.xpath("//div[contains(@class, 'col-12 d-block width-full py-4 border-bottom color-border-muted')]")

    private val repositoryName = By.xpath(".//h3/a")

    fun getRepositoriesAsElements(): List<WebElement> {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(starredRepositoriesList))
    }

    fun getUserStarredRepositoriesNames(): List<String> {
        val elements = getRepositoriesAsElements()
        return elements.map { element ->
            element.findElement(repositoryName).text.replace(" ", "")
        }
    }
}
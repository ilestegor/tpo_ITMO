package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import pages.Page
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

abstract class AbstractSearchPage(override val driver: WebDriver) : Page(driver) {
    override val wait: WebDriverWait = WebDriverWait(driver, Duration.ofSeconds(30))
    init {
        initPage(driver)
    }

    @FindBy(xpath = "//button[@data-testid='sort-button']")
    protected lateinit var sortByButton: WebElement


    protected val repositoryName = By.xpath(".//div[contains(@class, 'search-title')]//a")
    private val repositoryDescription = By.xpath(".//div[contains(@class, 'dcdlju')]//span[contains(@class, 'search-match')]")
    private val repositoryResult= By.xpath("//div[@data-testid='results-list']/div")
    private val updateDate = By.xpath(".//div[@title and contains(@title, 'GMT')]")

    fun getTopResultsAsElements(limit: Int = 10) : List<WebElement>{
        val elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(repositoryResult))
        return elements.take(limit)
    }

    fun getTopResultsNameAndDescription(limit: Int = 10) : List<Pair<String, String>>{
        val elements = getTopResultsAsElements(limit)
        return elements.mapNotNull { element ->
            try {
                val name = element.findElement(repositoryName).text
                val profileDescription = if (element.findElements(repositoryDescription).isNotEmpty()) {
                    element.findElement(repositoryDescription).text.trim()
                } else {
                    ""
                }
                Pair(name, profileDescription)
            } catch (e: Exception){
                null
            }
        }
    }

    fun getTopRepositoriesUpdateTime(limit: Int = 5): List<ZonedDateTime> {
        val elements = getTopResultsAsElements(limit)
        val formatter24 = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm O", Locale.ENGLISH)
        val formatter12 = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a O", Locale.ENGLISH)
        val dates = mutableListOf<ZonedDateTime>()

        elements.map { element ->
            val updateTime = element.findElement(updateDate).getDomAttribute("title") ?: ""
            try {
                println(updateTime)
                dates.add(ZonedDateTime.parse(updateTime, formatter24))
            } catch (_: DateTimeParseException) {
                try {
                    dates.add(ZonedDateTime.parse(updateTime, formatter12))
                } catch (e: DateTimeParseException) {
                    println("Failed to parse date: $updateTime, error: ${e.message}")
                }
            }
        }

        return dates
    }



}
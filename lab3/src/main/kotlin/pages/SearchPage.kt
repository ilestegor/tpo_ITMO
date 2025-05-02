package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

//https://github.com/search
class SearchPage(driver: WebDriver) : AbstractSearchPage(driver){
    init {
        initPage(driver)
    }


    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Most stars']")
    private lateinit var mostStarsSortButton: WebElement

    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Most forks']")
    private lateinit var mostForksSortButton: WebElement

    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Recently updated']")
    private lateinit var mostRecentlyUpdates: WebElement


    private val starsCount = By.xpath(".//a[contains(@aria-label, ' stars')]")
    private val repositoryResult= By.xpath("//div[@data-testid='results-list']/div")
    private val repositoryLink = By.xpath(".//div[contains(@class, 'search-title')]")
    private val repositoryProgrammingLanguage = By.xpath("//span[contains(@aria-label, 'language')]")
    private val starButton = By.xpath("//span[@data-component='text' and text()='Star']")
    private val usersFilterButton = By.xpath("//span[text()='Users']")
    private val issuesFilterButton = By.xpath("//a[contains(@data-testid, 'nav-item-issues')]")


    private val userNavMenu = By.xpath("//button[contains(@aria-label, 'Open user navigation menu')]")
    private val starNavSection = By.xpath("//a[contains(@href, 'stars')]")
    lateinit var starredRepositoryName: String

    private fun getFilterLanguageXpath(title: String) : By{
        return By.xpath("//div[@title='$title']")
    }


    fun getTopRepositoriesStarsCount(limit: Int = 10) : List<Int>{
        val elements = getTopResultsAsElements(limit)
        return elements.map { element ->
            element.findElement(starsCount).getDomAttribute("aria-label")?.split(" ")?.get(0)?.toInt() ?: 0
        }
    }

    fun redirectToRepositoryPage(link: WebElement) : RepositoryPage{
        click(link)
        return RepositoryPage(driver)
    }



    fun getTopRepositoriesForkCount(limit: Int = 2) : List<Int>{
        val forksCount = mutableListOf<Int>()
        for (index in 0..limit-1){
            val elements = getTopResultsAsElements(limit)
            if (index >= elements.size) break
            val repLink = redirectToRepositoryPage(elements[index].findElement(repositoryLink))

            forksCount.add(repLink.getForkCount())

            driver.navigate().back()
            wait.until {
                val currentElements = driver.findElements(repositoryResult)
                currentElements.size >= elements.size && currentElements[index].isDisplayed
            }
        }

        return forksCount
    }

    fun getTopRepositoriesLanguages(limit: Int = 5) : List<String>{
        val elements = getTopResultsAsElements(limit)
        val languagesList = mutableListOf<String>()
        elements.map { element ->
            try {
                languagesList.add(element.findElement(repositoryProgrammingLanguage).text)
            } catch (e: Exception){
                languagesList.add("")
            }
        }
        return languagesList
    }


    fun starRepositoryUnauthorized(repository: WebElement) : LogInPage{
        val starButtonElement = repository.findElement(starButton)
        click(starButtonElement)
        return LogInPage(driver)
    }

    fun starRepositoryAuthorised(repository: WebElement) : SearchPage{
        val starButtonElement = repository.findElement(starButton)
        click(starButtonElement)
        val repositoryName = repository.findElement(repositoryName).text
        starredRepositoryName = repositoryName
        return this
    }


    fun sortByMostStars() : SearchPage{
        click(sortByButton)
        click(mostStarsSortButton)
        return this
    }

    fun sortByMostForks() : SearchPage{
        click(sortByButton)
        click(mostForksSortButton)
        return this
    }

    fun sortByUpdateTimeDesc() : SearchPage{
        click(sortByButton)
        click(mostRecentlyUpdates)
        return this
    }

    fun filterByLanguage(language: String): SearchPage{
        val buttonXpath = getFilterLanguageXpath(language)
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(buttonXpath)))
        return this
    }
    fun filterByIssues() : IssuesSearchPage{
        val button = super.wait.until(ExpectedConditions.elementToBeClickable(issuesFilterButton))
        click(button)
        return IssuesSearchPage(driver)
    }



    fun filterByUsers(): UserSearchPage{
        try {
            val button = super.wait.until(ExpectedConditions.visibilityOfElementLocated(usersFilterButton))
            click(button)
        } catch (e: StaleElementReferenceException) {
            val button = super.wait.until(ExpectedConditions.visibilityOfElementLocated(usersFilterButton))
            click(button)
        }
        return UserSearchPage(driver)
    }



    fun openUserNavMenu() : SearchPage{
        val element = wait.until(ExpectedConditions.visibilityOfElementLocated(userNavMenu))
        click(element)
        return this
    }

    fun openStarNavPage() : StarPage{
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(starNavSection)))
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(starNavSection)))
        return StarPage(driver)
    }

}
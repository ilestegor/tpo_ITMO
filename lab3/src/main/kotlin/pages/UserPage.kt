package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserPage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    @FindBy(xpath = "//*[@id=\"sort-options\"]/summary")
    private lateinit var sortButton: WebElement

    private val followersCount = By.xpath("//a[contains(@href, 'followers')]")

    private val followingCount = By.xpath("//a[contains(@href, 'following')]")

    private val followButton = By.xpath("//a[@class='btn btn-block' and text()='Follow']")

    private val userRepositoriesNavButton = By.xpath("//nav[contains(@aria-label, 'User profile')]/a[2]")

    private val userRepositoriesList = By.xpath("//div[contains(@id, 'user-repositories-list')]/ul/li")
    private val userRepositoryUpdateTime = By.xpath(".//relative-time[@datetime]")

    private val lastUpdateTimeSortButton = By.xpath("//*[@id=\"sort-options\"]/details-menu/div/div/label[1]")
    private val nameSortButton = By.xpath("//*[@id=\"sort-options\"]/details-menu/div/div/label[2]")
    private val startSortButton = By.xpath("//*[@id=\"sort-options\"]/details-menu/div/div/label[3]")
    private val repositoryName = By.xpath(".//a[contains(@itemprop, 'name codeRepository')]")
    private val starCount = By.xpath(".//a[contains(@href, '/stargazers')]")

    fun isUserPage(username: String) : Boolean{
       return wait.until(ExpectedConditions.and(
           ExpectedConditions.visibilityOfElementLocated(followersCount),
                   ExpectedConditions.visibilityOfElementLocated(followingCount),
           ExpectedConditions.visibilityOfElementLocated(followButton),
           ExpectedConditions.urlContains("/${username}")
       ))

    }

    fun redirectToUserRepositoriesSection(): UserPage{
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(userRepositoriesNavButton)))
        return this
    }

    fun isRepositoriesSectionPage() : Boolean{
        return wait.until(ExpectedConditions.urlContains("repositories"))
    }

    fun followUserUnauthorised() : LogInPage{
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(followButton)))
        return LogInPage(driver)
    }


    fun getUserRepositoriesAsElements(limit: Int = 10) : List<WebElement>{
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(userRepositoriesList)).take(limit)
    }

    fun getUserRepositoriesLastUpdateDates(limit: Int = 10): List<LocalDateTime> {
        val elements = getUserRepositoriesAsElements(limit)
        return elements.mapNotNull { element ->
            try {
                val time = element.findElement(userRepositoryUpdateTime).getDomAttribute("datetime")
                LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getUserRepositoryNames(limit: Int = 10) : List<String>{
        val elements = getUserRepositoriesAsElements(limit)
        return elements.map { element ->
            wait.until {
                element.findElement(repositoryName).text
            }
        }
    }

    fun getStarsCount(limit: Int = 10) : List<Int>{
        val elements = getUserRepositoriesAsElements(limit)
        return elements.mapNotNull { element ->
            try {
                element.findElement(starCount).text.toInt()
            } catch (e: Exception){
                null
            }

        }
    }

    fun sortByLastUpdateTime() : UserPage{
        click(sortButton)
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(lastUpdateTimeSortButton)))
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(userRepositoriesList))
        return this
    }

    fun sortRepositoriesByName() : UserPage{
        click(sortButton)
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(nameSortButton)))
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(userRepositoriesList)))
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(userRepositoriesList))
        return this
    }

    fun sortByStarsCount() : UserPage{
        click(sortButton)
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(startSortButton)))
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(userRepositoriesList)))
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(userRepositoriesList))
        return this
    }



}
package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class UserSearchPage(driver: WebDriver) : AbstractSearchPage(driver) {
    init {
        initPage(driver)
    }
    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Most followers']")
    private lateinit var mostFollowers: WebElement

    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Most repositories']")
    private lateinit var mostRepositories: WebElement

    private val followersCountLabel = By.xpath(".//span[contains(@aria-label, 'followers')]")
    private val repositoriesCount = By.xpath(".//span[contains(@aria-label, 'repositories')]")

    fun sortByMostFollowers() : UserSearchPage{
        click(sortByButton)
        click(mostFollowers)
        return this
    }

    fun getFollowerCount(limit: Int = 10) : List<Int>{
        val elements = getTopResultsAsElements(limit)
        return elements.mapNotNull { element ->
            try {
                element.findElement(followersCountLabel).getDomAttribute("aria-label")?.split(" ")?.first()?.toInt()
            } catch (e : Exception){
                null
            }
        }
    }

    fun sortByMostRepositories() : UserSearchPage{
        click(sortByButton)
        click(mostRepositories)
        return this
    }

    fun redirectToUserPage(userPageElement: WebElement): UserPage{
        click(userPageElement)
        return UserPage(driver)

    }

    fun redirectToNUserPage(userPageNumber: Int ) : UserPage{
        val elements = getTopResultsAsElements()
        val link = elements[userPageNumber-1].findElement(repositoryName)
        redirectToUserPage(link)
        return UserPage(driver)
    }

    fun getRepositoriesCount(limit: Int = 10) : List<Int>{
        val elements = getTopResultsAsElements(limit)
        return elements.mapNotNull { element ->
            try {
                element.findElement(repositoriesCount).getDomAttribute("aria-label")?.split(" ")?.first()?.toInt()
            } catch (e: Exception){
                null
            }
        }
    }


}
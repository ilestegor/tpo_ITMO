package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class IssuesSearchPage(driver: WebDriver) : AbstractSearchPage(driver) {
    init {
        initPage(driver)
    }

    @FindBy(xpath = "//ul[contains(@role, 'menu')]//span[text()='Newest']")
    private lateinit var newestIssuesSortButton: WebElement

    fun sortByNewestIssues() : IssuesSearchPage{
        click(sortByButton)
        click(newestIssuesSortButton)
        return this
    }

    fun redirectToIssuesPage(repositoryLink: WebElement) : IssuesPage{
        click(repositoryLink)
        return IssuesPage(driver)
    }

    fun redirectToNIssuePage(issuePageNumber: Int) : IssuesPage{
        val elements = getTopResultsAsElements()
        val link = elements[issuePageNumber-1].findElement(repositoryName)
        redirectToIssuesPage(link)
        return IssuesPage(driver)
    }



}
package org.healthapp.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedConditions
import pages.Page

class IssuesPage(driver: WebDriver) : Page(driver) {
    init {
        initPage(driver)
    }

    
    private val issueViewerContainer = By.xpath("//div[@data-testid='issue-viewer-container']")
    
    private val issuesBody = By.xpath("//div[@data-testid='issue-body']")
    private val issuesTab = By.xpath("//*[@id=\"issues-tab\"]")

    private val newIssueButton = By.xpath("(//span[text()='New issue'])[2]")

    @FindBy(xpath = "//div[@data-testid='issue-body']")
    lateinit var divIssueBody: WebElement

    fun isIssuesPageVisible() : Boolean{
        return wait.until(
            ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(issueViewerContainer),
                ExpectedConditions.visibilityOfElementLocated(issuesBody),
                ExpectedConditions.visibilityOfElementLocated(issuesTab)
            )
        )
    }

    fun createNewIssueUnauthorized(): LogInPage{
        click(wait.until(ExpectedConditions.visibilityOfElementLocated(newIssueButton)))
        return LogInPage(driver)
    }
}
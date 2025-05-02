package pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

// page_url = https://www.selenium.dev/selenium/web/web-form.html
abstract class Page(open val driver: WebDriver) {
    protected open val wait: WebDriverWait = WebDriverWait(driver, Duration.ofSeconds(30))
    fun initPage(driver: WebDriver) {
        PageFactory.initElements(driver, this)
    }

    fun open(url: String){
        return driver.get(url)
    }

    fun sendText(text: String, element: WebElement){
        wait.until(ExpectedConditions.visibilityOf(element)).apply {
            sendKeys(text)
        }
    }

    fun click(element: WebElement){
         wait.until(ExpectedConditions.elementToBeClickable(element)).click()
    }

    fun submitQuery(element: WebElement){
         wait.until(ExpectedConditions.elementToBeClickable(element)).submit()
    }



}
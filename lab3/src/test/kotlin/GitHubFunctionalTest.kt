import org.healthapp.pages.MainPage
import org.healthapp.util.Browser
import org.healthapp.util.WebDriverFactory
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertTrue


class GitHubFunctionalTest {
    private val MAIN_PAGE_URL = "https://github.com"
    private val BASE_REPOSITORY_NAME = "kotlin"
    private val USERNAME = "user"
    private val LANGUAGR_FILTER_NAME = "Java"


    //Проверка того, что найдены релевантные репозитории
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun searchRepositoryTest(browser: Browser) {
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).getTopResultsNameAndDescription(5)
            searchPage.forEach { result  ->
                val title = result.first.lowercase()
                val description = result.second.lowercase()
                assertTrue { title.contains(BASE_REPOSITORY_NAME) }
                assertTrue { description.contains(BASE_REPOSITORY_NAME) }
            }
        } finally {
            driver.quit()
        }
    }

//    Проверка того, что репозитории были отсортированном в убывающем порядке по количеству звезд
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testSortByStarsDescTest(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).sortByMostStars().getTopRepositoriesStarsCount(2)
            assertTrue(searchPage[0] > searchPage[1])
        } finally {
            driver.quit()
        }
    }

//    Сортировка по количеству форков в убывающем порядке
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testSortByForkCountDescTest(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).sortByMostForks().getTopRepositoriesForkCount(2)
            assertTrue(searchPage[0] > searchPage[1])
        } finally {
            driver.quit()
        }
    }

//    Сортировка по дате обновления в убывающем порядке
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testSortByUpdateTimeDescTest(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).sortByUpdateTimeDesc().getTopRepositoriesUpdateTime(2)
            assertTrue(searchPage[0] >= searchPage[1])
        } finally {
            driver.quit()
        }
    }

//    Фильтрация по ЯП
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testFilterRepositoriesByLanguage(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByLanguage(LANGUAGR_FILTER_NAME).getTopRepositoriesLanguages()
            searchPage.filterNot { it.isEmpty() }.forEach { assertEquals(LANGUAGR_FILTER_NAME, it) }
        } finally {
            driver.quit()
        }
    }

//    Попробовать поставить звезду неавторизованный пользователь
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testStarRepository(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)

            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME)
            val topRepository = searchPage.getTopResultsAsElements(1)
            assertTrue(topRepository.all { searchPage.starRepositoryUnauthorized(it).isLoginPage() })
        } finally {
            driver.quit()
        }
    }

//    Фильтрация по issues
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testIssuesFilter(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByIssues().getTopResultsNameAndDescription(5)
            searchPage.forEach {
                assertTrue(it.first.lowercase().contains(BASE_REPOSITORY_NAME) || it.second.lowercase().contains(BASE_REPOSITORY_NAME))
            }
        } finally {
            driver.quit()
        }
    }

//    Сортировка issues по дате создания в убывающем порядке
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testIssuesNewestSortDesc(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByIssues().sortByNewestIssues().getTopRepositoriesUpdateTime()
            assertTrue(searchPage[0] >= searchPage[1])
        } finally {
            driver.quit()
        }
    }

//    Проверка того, что можно зайти на страницу issues будучи неавторизованным
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testIssuePageAvailability(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByIssues().redirectToNIssuePage(1)
            assertTrue(searchPage.isIssuesPageVisible())
        } finally {
            driver.quit()
        }
    }

//    Фильтрация по пользователям
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testUserFilter(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByUsers().getTopResultsNameAndDescription()
            searchPage.forEach {
                assertTrue("Either name or description should contain $BASE_REPOSITORY_NAME") {
                    (it.first.isNotEmpty() && it.first.lowercase().contains(BASE_REPOSITORY_NAME)) ||
                            (it.second.isNotEmpty() && it.second.lowercase().contains(BASE_REPOSITORY_NAME))
                }
            }
        } finally {
            driver.quit()
        }
    }

//    Сортировка по количеству подписчиков
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testFollowerCountDesc(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByUsers().sortByMostFollowers().getFollowerCount(2)
            assertTrue(searchPage[0] >= searchPage[1])
        } finally {
            driver.quit()
        }
    }

//    Сортировка по количеству репозиториев
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testMostRepositoriesDesc(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByUsers().sortByMostRepositories().getRepositoriesCount(2)
            assertTrue(searchPage[0] >= searchPage[1])
        } finally {
            driver.quit()
        }
    }

//  Созданию новой issues неавторизованным пользователем
    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testNewIssueCreate(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(BASE_REPOSITORY_NAME).filterByIssues().redirectToNIssuePage(1).createNewIssueUnauthorized()
            assertTrue(searchPage.isLoginPage())
        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testCheckUserPage(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1)
            assertTrue(searchPage.isUserPage(USERNAME))
        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testCheckUserRepositories(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1).redirectToUserRepositoriesSection()
            assertTrue(searchPage.isRepositoriesSectionPage())
        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun followUserTest(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1).followUserUnauthorised()
            assertTrue(searchPage.isLoginPage())
        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun sortLastUpdateTimeUserRepositories(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1).redirectToUserRepositoriesSection().sortByLastUpdateTime().getUserRepositoriesLastUpdateDates(2)
            assertTrue { searchPage[0] >= searchPage[1] }


        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testSortByNameRepositories(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1).redirectToUserRepositoriesSection().sortRepositoriesByName().getUserRepositoryNames()
            assertTrue { searchPage == searchPage.sortedWith(String.CASE_INSENSITIVE_ORDER) }


        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testSortByStarsCountDesc(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage = mainPage.searchRepository(USERNAME).filterByUsers().redirectToNUserPage(1).redirectToUserRepositoriesSection().sortByStarsCount().getStarsCount(2)
            assertTrue { searchPage.isNotEmpty() }
            assertTrue { searchPage[0] >= searchPage[1] }

        } finally {
            driver.quit()
        }
    }


    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testLogIn(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {

            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage =mainPage.redirectToLogIn().enterLogin(System.getenv("login")).enterPassword(System.getenv("password")).submitLogInForm()
            assertTrue { searchPage.isHomePageVisible() }
        } finally {
            driver.quit()
        }
    }

    @ParameterizedTest
    @EnumSource(Browser::class)
    fun testStarRepositoryAuthorised(browser: Browser){
        val driver = WebDriverFactory.createDriver(browser)
        try {
            val mainPage = MainPage(driver)
            driver.get(MAIN_PAGE_URL)
            val searchPage =mainPage.redirectToLogIn().enterLogin(System.getenv("login")).enterPassword(System.getenv("password")).submitLogInForm().search(USERNAME)
            val repository = searchPage.getTopResultsAsElements(1)[0]
            val name = searchPage.starRepositoryAuthorised(repository).starredRepositoryName
            val starredRepositoriesNames = searchPage.openUserNavMenu().openStarNavPage().getUserStarredRepositoriesNames()
            assertTrue {starredRepositoriesNames.contains(name) }
        } finally {
            driver.quit()
        }
    }



}
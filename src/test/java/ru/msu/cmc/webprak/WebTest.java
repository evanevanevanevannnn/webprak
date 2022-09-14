package ru.msu.cmc.webprak;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebTest {

    @Test
    void MainPage() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        assertEquals("Главная страница", driver.getTitle());
        driver.quit();
    }

    @Test
    void HeaderTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024,768));
        driver.get("http://localhost:8080/");

        WebElement peopleButton = driver.findElement(By.id("sportsmansListLink"));
        peopleButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Спортсмены", driver.getTitle());

        WebElement rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Главная страница", driver.getTitle());

        WebElement placesButton = driver.findElement(By.id("teamsListLink"));
        placesButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Команды", driver.getTitle());

        rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Главная страница", driver.getTitle());

        WebElement competitionsButton = driver.findElement(By.id("competitionsListLink"));
        competitionsButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Соревнования", driver.getTitle());

        rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Главная страница", driver.getTitle());

        driver.quit();
    }

    @Test
    void addSportsman() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\sukho\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/sportsmans?addButton=true&backLink=/sportsman?");
        assertEquals("Спортсмены", driver.getTitle());
        WebElement addSportsman = driver.findElement(By.id("addSportsmanButton"));
        addSportsman.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать спортсмена", driver.getTitle());

        driver.findElement(By.id("personName")).sendKeys("Тестовый спортсмен");
        driver.findElement(By.id("birthDate")).sendKeys("Тестовая дата рождения");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Информация о спортсмене", driver.getTitle());
        WebElement placeInfo = driver.findElement(By.id("personInfo"));
        List<WebElement> cells = placeInfo.findElements(By.tagName("p"));

        assertEquals(cells.get(0).getText(), "Имя: Тестовый спортсмен");
        assertEquals(cells.get(1).getText(), "Дата рождения: Тестовая дата рождения");

        WebElement teamPartHistory = driver.findElement(By.tagName("table"));
        List<WebElement> rows = teamPartHistory.findElements(By.tagName("tr"));
        WebElement lastRow = rows.get(rows.size() - 1);

        lastRow.findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Добавить участие в команде", driver.getTitle());

        driver.findElement(By.id("chooseTeamButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        driver.findElements(By.tagName("a")).get(7).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("partStart")).sendKeys("01.01.2005");
        driver.findElement(By.id("partFinish")).sendKeys("01.01.2006");

        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Информация о спортсмене",driver.getTitle());

        driver.findElement(By.id("deleteButton")).click();
        driver.quit();
    }

    @Test
    void addSport() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\sukho\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/sports?addButton=true&backLink=");
        assertEquals("Виды спорта", driver.getTitle());
        WebElement addSportButton = driver.findElement(By.id("addSportButton"));
        addSportButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Добавить спорт", driver.getTitle());

        driver.findElement(By.id("sportName")).sendKeys("Тестовый командный спорт");
        driver.findElement(By.id("isTeamSport")).click();
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Виды спорта", driver.getTitle());

        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        WebElement newSport = null;
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));

            if (cols.size() <= 1) {
                continue;
            }

            if (cols.get(0).findElement(By.tagName("span")).getText().equals("Тестовый командный спорт")) {
                newSport = row;
            }
        }

        assert newSport != null;

        List<WebElement> newSportInfo = newSport.findElements(By.tagName("td"));

        assertEquals("Тестовый командный спорт", newSportInfo.get(0).findElement(By.tagName("span")).getText());
        assertEquals("да", newSportInfo.get(1).findElement(By.tagName("span")).getText());

        newSportInfo.get(2).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Виды спорта", driver.getTitle());

        driver.quit();
    }

    @Test
    void getTeamInfo() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\sukho\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/team?teamId=1");
        assertEquals("Информация о команде", driver.getTitle());

        WebElement teamInfo = driver.findElement(By.id("teamInfo"));
        List<WebElement> cells = teamInfo.findElements(By.tagName("p"));

        assertEquals("Название: team 1", cells.get(0).getText());
        assertEquals("Тренер: coach 1", cells.get(1).getText());

        List<WebElement> tables = driver.findElements(By.tagName("table"));
        WebElement playersTable = tables.get(0);
        WebElement competitionsHistory = tables.get(1);

        List<WebElement> teamPlayers = playersTable.findElements(By.tagName("tr"));
        WebElement firstPlayer = null;
        for (WebElement elem : teamPlayers) {
            List<WebElement> cols = elem.findElements(By.tagName("td"));
            if (cols.size() == 0) {
                continue;
            }

            WebElement firstCol = cols.get(0);
            if (firstCol.findElement(By.tagName("span")).getText().equals("sportsman 1")) {
                firstPlayer = elem;
            }
        }

        assert firstPlayer != null;

        List<WebElement> firstPlayerInfo = firstPlayer.findElements(By.tagName("td"));
        assertEquals("sportsman 1", firstPlayerInfo.get(0).findElement(By.tagName("span")).getText());
        assertEquals("01.01.2000", firstPlayerInfo.get(1).findElement(By.tagName("span")).getText());

        List<WebElement> competitions = competitionsHistory.findElements(By.tagName("tr"));
        WebElement tournament2 = null;
        for (WebElement elem : competitions) {
            List<WebElement> cols = elem.findElements(By.tagName("td"));
            if (cols.size() == 0) {
                continue;
            }

            WebElement secondCol = cols.get(1);
            if (secondCol.findElement(By.tagName("a")).getText().equals("tournament 2")) {
                tournament2 = elem;
            }
        }

        assert tournament2 != null;

        List<WebElement> tournamentInfo = tournament2.findElements(By.tagName("td"));
        assertEquals("team 2", tournamentInfo.get(0).findElement(By.tagName("a")).getText());
        assertEquals("tournament 2", tournamentInfo.get(1).findElement(By.tagName("a")).getText());
        assertEquals("01.01.2015", tournamentInfo.get(2).findElement(By.tagName("span")).getText());
        assertEquals("15:10", tournamentInfo.get(3).findElement(By.tagName("span")).getText());

        tournamentInfo.get(1).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Информация о соревновании", driver.getTitle());

        WebElement personInfo = driver.findElement(By.id("competitionInfo"));
        List<WebElement> personCells = personInfo.findElements(By.tagName("p"));

        assertEquals(personCells.get(0).getText(), "Название турнира: tournament 2");
        assertEquals(personCells.get(1).getText(), "Дата соревнования: 01.01.2015");
        assertEquals(personCells.get(2).getText(), "Место проведения: venue 2");
        assertEquals(personCells.get(3).getText(), "Вид спорта: hockey");

        driver.quit();
    }

    @Test
    void byTickets() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\sukho\\Downloads\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/competition?competitionId=1");
        assertEquals("Информация о соревновании", driver.getTitle());

        WebElement tournament = driver.findElement(By.tagName("p"));
        assertEquals("Название турнира: tournament 1", tournament.getText());

        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("5,2000,3000");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Стоимость билетов каждой категории: 5,2000,3000", driver.findElement(By.id("price")).getText());

        driver.findElement(By.id("buyTicketButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Купить билет на соревнование", driver.getTitle());
        driver.findElement(By.id("firstCatSeats")).sendKeys("20");
        driver.findElement(By.id("passportData")).sendKeys("0101 000000");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Билет куплен", driver.getTitle());
        assertEquals("Общая стоимость билетов: 100", driver.findElement(By.id("price")).getText());

        List<WebElement> links = driver.findElements(By.tagName("a"));
        WebElement lastLink = links.get(links.size() - 1);
        lastLink.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals("Информация о соревновании", driver.getTitle());
        assertEquals("Количество свободных мест каждой категории: 0,20,20", driver.findElement(By.id("seats")).getText());

        driver.quit();
    }
}

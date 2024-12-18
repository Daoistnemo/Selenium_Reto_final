package com.nemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Plantilla {

    public static void main(String[] args) {
        // Configura la propiedad del sistema para el driver de Chrome
        System.setProperty("webdriver.chrome.driver", "nemo\\chromedriver.exe");

        // Configura ChromeOptions para usar un perfil temporal
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=path/to/temp-profile"); // Usa un perfil temporal

        WebDriver driver = null;

        try {
            // Inicializa el WebDriver con las opciones configuradas
            driver = new ChromeDriver(options);

            // Navega a una página web
            driver.get("https://example.com");

            // Espera explícita para asegurarse de que la página se haya cargado completamente
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.titleContains("Expected Title"));

            // Navega a otra página web
            driver.get("https://www.saucedemo.com/v1/index.html");

            Thread.sleep(3000);

            // Encuentra el campo de búsqueda
            WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#search")));

            // Escribe en el campo de búsqueda
            searchField.sendKeys("search query");

            // Encuentra el botón de búsqueda y haz clic
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#search-icon")));
            searchButton.click();

            System.out.println("Se encontró el botón y se hizo clic.");

            // Espera a que los resultados de búsqueda se carguen
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("result-element-selector")));
            // Encuentra el enlace del video usando XPath
            WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("XPath expression")));

            // Realiza una acción, como hacer clic en el elemento
            resultElement.click();

            System.out.println("Se encontró el elemento y se hizo clic.");

            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cierra el navegador
            if (driver != null) {
                driver.quit();
            }
        }
    }
}

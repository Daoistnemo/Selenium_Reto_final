package com.nemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Especificar la ruta donde descargaste el ChromeDriver manualmente
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Documents\\Selenium\\Proyecto_selenium_final\\nemo\\chromedriver.exe");
    
        // Si necesitas correr el navegador en modo headless (sin UI), usa las siguientes opciones:
        ChromeOptions options = new ChromeOptions();
 
    
        // Crear el driver usando las opciones configuradas
        driver = new ChromeDriver(options);
    }
    

    @AfterEach
    public void tearDown() {
        // Cerrar el navegador después de cada prueba
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @CsvSource({
        "standard_user,secret_sauce",  // Usuario estándar
        "locked_out_user,1245",  // Usuario bloqueado
        "problem_user,problem123",  // Usuario con problemas
        "performance_glitch_user,secret_sauce"   // Usuario con fallo de rendimiento
    })
    public void testLogin(String username, String password) {
        // Abrir la página de login
        driver.get("https://www.saucedemo.com/v1/index.html");

        // Encontrar los campos de usuario y contraseña y realizar el login
        driver.findElement(By.id("user-name")).sendKeys(username);  // Campo de nombre de usuario
        driver.findElement(By.id("password")).sendKeys(password);  // Campo de contraseña
        driver.findElement(By.id("login-button")).click();          // Botón de login

        // Verificar que el login fue exitoso dependiendo del usuario
        if (username.equals("standard_user") || username.equals("performance_glitch_user")) {
            // Si el usuario es estándar o con un fallo de rendimiento, debería ser redirigido a la página de inventario
            assertTrue(driver.getCurrentUrl().contains("inventory.html"));
        } else {
            // Si el usuario está bloqueado o tiene problemas, debería seguir en la página de login
            assertTrue(driver.getCurrentUrl().contains("index.html"));
        }
    }
}

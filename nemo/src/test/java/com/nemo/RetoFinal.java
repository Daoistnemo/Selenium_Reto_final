package com.nemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class RetoFinal {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Especificar la ruta donde descargaste el ChromeDriver manualmente
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\User\\Documents\\Selenium\\Proyecto_selenium_final\\nemo\\chromedriver.exe");

        // Si necesitas correr el navegador en modo headless (sin UI), usa las
        // siguientes opciones:
        ChromeOptions options = new ChromeOptions();

        // Crear el driver usando las opciones configuradas
        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterEach
    public void tearDown() {
        // Cerrar el navegador después de cada prueba
        if (driver != null) {
            driver.quit();
        }
    }

    // Prueba 1: Login sin ingresar usuario y contraseña
    @Test
    public void testLoginWithoutCredentials() {
        // Abrir la página de login
        driver.get("https://www.saucedemo.com/v1/index.html");

        // Intentar hacer login sin ingresar usuario y contraseña
        driver.findElement(By.id("login-button")).click();

        // Verificar que el mensaje de error aparece
        String errorMessage = driver.findElement(By.cssSelector("h3.error-message-container")).getText();

        // Asegurarse de que el mensaje de error sea el esperado
        assertEquals("Epic sadface: Username and password are required", errorMessage,
                "El mensaje de error no es el esperado");
    }

    // Prueba 2: Login con credenciales (Parámetros)
    @ParameterizedTest
    @CsvSource({
            "standard_user,secret_sauce", // Usuario estándar
            "locked_out_user,1245", // Usuario bloqueado
            "problem_user,problem123", // Usuario con problemas
            "performance_glitch_user,secret_sauce" // Usuario con fallo de rendimiento
    })
    public void testLogin(String username, String password) {
        // Abrir la página de login
        driver.get("https://www.saucedemo.com/v1/index.html");

        // Encontrar los campos de usuario y contraseña y realizar el login
        driver.findElement(By.id("user-name")).sendKeys(username); // Campo de nombre de usuario
        driver.findElement(By.id("password")).sendKeys(password); // Campo de contraseña
        driver.findElement(By.id("login-button")).click(); // Botón de login

        // Verificar que el login fue exitoso dependiendo del usuario
        if (username.equals("standard_user") || username.equals("performance_glitch_user")) {
            // Si el usuario es estándar o con un fallo de rendimiento, debería ser
            // redirigido a la página de inventario
            assertTrue(driver.getCurrentUrl().contains("inventory.html"));
        } else {
            // Si el usuario está bloqueado o tiene problemas, debería seguir en la página
            // de login
            assertTrue(driver.getCurrentUrl().contains("index.html"));
        }
    }

    // Prueba 3: Login con credenciales (Parámetros)
    @ParameterizedTest
    @CsvSource({
            "standard_user,secret_sauce",
            // "locked_out_user,secret_sauce",
            "problem_user,secret_sauce",
            "performance_glitch_user,secret_sauce"
    })
    public void testLoginLogoutForAllUsers(String username, String password) {
        // Iniciar sesión con las credenciales del usuario
        driver.get("https://www.saucedemo.com/v1/index.html");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        assertTrue(driver.getCurrentUrl().contains("inventory.html"));

        // Hacer clic en el botón "Open Menu"
        WebElement openMenuButton = driver.findElement(By.xpath("//button[text()='Open Menu']"));
        openMenuButton.click(); // Este es el clic en el botón para abrir el menú
        driver.findElement(By.id("logout_sidebar_link")).click();

        // Verificar que el usuario fue redirigido a la página de login
        assertTrue(driver.getCurrentUrl().contains("index.html"));
    }

    @Test
    // Test 4: Compra de producto
    public void testBuyProduct() {
        // Iniciar sesión con las credenciales del usuario
        driver.get("https://www.saucedemo.com/v1/index.html");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Verifica si inicio Sesion.
        assertTrue(driver.getCurrentUrl().contains("inventory.html"));

        // Hacer clic en el botón "Add to cart"
        WebElement FirstProductBuyButton = driver.findElement(By.xpath("//*[@id='inventory_container']/div/div[1]/div[3]/button"));
        FirstProductBuyButton.click(); // Este es el clic en el botón para abrir el menú

        // verificar que el número del carrito cambie
        WebElement cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));
        assertTrue(cartBadge.getText().equals("1"),
                "El número de productos en el carrito no es 1, es: " + cartBadge.getText());
        // Cambiar el orden de los productos. Localizar el elemento <select> usando el
        // XPath

        WebElement dropdown = driver.findElement(By.xpath("//*[@id='inventory_filter_container']/select"));
        // Crear un objeto Select para interactuar con el menú desplegable
        Select select = new Select(dropdown);

        // Seleccionar una opción por su valor
        select.selectByValue("lohi"); // Esto selecciona la opción con el valor "lohi"

        //Añadir los dos primeros productos al carrito

        WebElement NewProductBuyButton1 = driver.findElement(By.xpath("//*[@id='inventory_container']/div/div[1]/div[3]/button"));
        NewProductBuyButton1.click(); 

        WebElement NewProductBuyButton2 = driver.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[2]/div[3]/button"));
        NewProductBuyButton2.click(); 

        // Ir a la pagina del carrito
        WebElement CartButton = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        CartButton.click();


        //Verificar que se ingreso al Carrito.

        assertTrue(driver.getCurrentUrl().contains("cart.html"));

        //Eliminar un producto

        WebElement RemoveButton = driver.findElement(By.xpath("//*[@id='cart_contents_container']//div[contains(@class, 'cart_item')][1]//button[@class='btn_secondary cart_button']"));
        RemoveButton.click();

        // Actualizar el cartBadge después de eliminar un producto
        cartBadge = driver.findElement(By.cssSelector("span.shopping_cart_badge"));

        // Verificar la cantidad de productos
        assertTrue(cartBadge.getText().equals("2"),"El número de productos en el carrito no es 2, es: " + cartBadge.getText());

        //Continuar con la compra.
        WebElement CheckoutButton = driver.findElement(By.xpath("//a[@class='btn_action checkout_button' and @href='./checkout-step-one.html']"));
        CheckoutButton.click();

        //Rellnar datos y continuar (Verificando el cambio de pagina)

        assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"));


        driver.findElement(By.id("first-name")).sendKeys("Kelvin");
        driver.findElement(By.id("last-name")).sendKeys("Garcia");
        driver.findElement(By.id("postal-code")).sendKeys("00051");

        WebElement SubmitButton = driver.findElement(By.xpath("//input[@class='btn_primary cart_button' and @type='submit' and @value='CONTINUE']"));
        SubmitButton.click();

        //Continua con la compra verificando el cambio de pagina hacia el paso dos del Checkout

        assertTrue(driver.getCurrentUrl().contains("checkout-step-two.html"));

        WebElement FinishButton = driver.findElement(By.xpath("//a[@class='btn_action cart_button' and @href='./checkout-complete.html']"));
        FinishButton.click();

        //Verificar que aparezca el mensaje de compra realizada

      // Localiza el mensaje usando XPath
      WebElement messageElement = driver.findElement(By.xpath("//*[@id='checkout_complete_container']/h2"));

      // Verifica que el mensaje es el esperado
      String expectedMessage = "THANK YOU FOR YOUR ORDER";
      assertEquals(expectedMessage, messageElement.getText(), "El mensaje no es el esperado.");

      // Cerrar sesion y verificar el correcto cierre de sesión

      WebElement openMenuButton = driver.findElement(By.xpath("//button[text()='Open Menu']"));
      openMenuButton.click(); // Este es el clic en el botón para abrir el menú
      driver.findElement(By.id("logout_sidebar_link")).click();

      // Verificar que el usuario fue redirigido a la página de login
      assertTrue(driver.getCurrentUrl().contains("index.html"));

    }

}

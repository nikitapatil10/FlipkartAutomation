package demo;

import static org.junit.jupiter.api.Assumptions.abort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    ChromeDriver driver;
    public TestCases()
    {
        System.out.println("Constructor: TestCases");
        WebDriverManager.chromedriver().timeout(30).setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
   

    @Test
    public  void testCase01() throws InterruptedException{
        System.out.println("Start Test case: testCase01");
        //open url http://www.flipkart.com
        driver.get("http://www.flipkart.com");
        //insert text as "Washing Machine" and search
        TestCases.sendTheTextToLocator(driver, "Washing Machine");
        Thread.sleep(1000);
        //locate the locator for popularity and click
        driver.findElement(By.xpath("//div[text()='Popularity']")).click();
        //locate the locator for ratings
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='_3LWZlK']"));
        //print the count of items with rating less than or equal to 4 stars
        countRatings(list);
        System.out.println("end Test case: testCase01");
    
    }
    @Test
    public void testCase02() throws InterruptedException{
        System.out.println("Start Test case: testCase02");
        //insert text as "iPhone" and search
        TestCases.sendTheTextToLocator(driver, "iPhone");
        Thread.sleep(1000);
        //locate the locator for discount percent 
        List<WebElement> discountText = driver.findElements(By.xpath("//div[@class='_3Ay6Sb']"));
        checkDiscount(discountText,driver);
        System.out.println("end Test case: testCase02");
    }

    @Test
    public void testCase03() throws InterruptedException{
        System.out.println("Start Test case: testCase03");
        //insert text as "Coffee Mug" and search
        TestCases.sendTheTextToLocator(driver, "Coffee Mug");
        Thread.sleep(2000);
        //locate the locator for ratings
        WebElement selectRating = driver.findElement(By.xpath("//div[text()='4★ & above']/preceding-sibling::div"));
        selectRating.click();
        Thread.sleep(2000);
        List<WebElement> reviewList = driver.findElements(By.xpath("//div[@class='gUuXy- _2D5lwg']/span[2]"));
        highestReview(reviewList, driver);
        System.out.println("end Test case: testCase03");
    }

    public static void sendTheTextToLocator(WebDriver driver,String text)
    {
         //locate the element for search box
        WebElement searchTextBox = driver.findElement(By.xpath("//input[@name='q']"));
        searchTextBox.clear();
        searchTextBox.sendKeys(text);
        searchTextBox.submit();
    }

    public static void countRatings(List<WebElement> ratings)
    {
        int count = 0;
        for(WebElement element : ratings)
        {
            // System.out.println(element.getText());
            String ratingsStars = element.getText();
            double num = Double.parseDouble(ratingsStars);
            // System.out.println(num);
            if (num <= 4) 
                count++;
        }
        System.out.println("Items with rating less than or equal to 4 stars are : "+count);
    }

    public static void checkDiscount(List<WebElement> list,WebDriver driver)
    {
        for(int i =0;i<list.size();i++)
        {
            //check the discount is more than 17 %
            // System.out.println(element.getText());
            String text = list.get(i).getText();
            String array[] = text.split("%");
            // System.out.println(array[0]);
            if(Integer.parseInt(array[0]) > 17)
            {
                String titleText = driver.findElement(By.xpath(String.format("(//div[@class='_4rR01T'])[%d]", i + 1))).getText();
                 //print the titles and discount percent
                System.out.println("Title of the item is : "+titleText);
                System.err.println("Discount on the item is : "+text);
            }

        }
    }

    public static void highestReview(List<WebElement> list, WebDriver driver)
    {
          //print the title and image url of the 5 items with highest number of review
          String num = "";
          Integer arr[] = new Integer[list.size()];
          for(int i =0;i<list.size();i++)
          {
            String text = list.get(i).getText();
            // Find the indices of opening and closing parentheses
            int openingIndex = text.indexOf('(');
            int closingIndex = text.indexOf(')');
            // Extract the number between parentheses
            String numberWithinParentheses = text.substring(openingIndex + 1, closingIndex);
            num = numberWithinParentheses.replace(",", "");
            arr[i] = Integer.parseInt(num);
            // System.out.println(numberWithinParentheses); // Output: 333
            //image url locator //div[@class='CXW8mj']/img
            //title locator //a[@class='s1Q9rs']
          }
          
          Arrays.sort(arr, Collections.reverseOrder());
         
          for(int i = 0; i <= 4;i++)
          {
            for(int j = 0;j<list.size();j++)
            {
              String text = list.get(j).getText();
              // Find the indices of opening and closing parentheses
              int openingIndex = text.indexOf('(');
              int closingIndex = text.indexOf(')');
              // Extract the number between parentheses
              String numberWithinParentheses = text.substring(openingIndex + 1, closingIndex);
              num = numberWithinParentheses.replace(",", "");
            //   System.out.println(arr[i]);
            //   System.out.println(num);
              if(arr[i] == Integer.parseInt(num))
              {
                String titleText = driver.findElement(By.xpath(String.format("(//a[@class='s1Q9rs'])[%d]", j + 1))).getText();
                String url = driver.findElement(By.xpath(String.format("(//a[@class='s1Q9rs']/preceding-sibling::a/div/div/div/img)[%d]", j + 1))).getAttribute("src");
                System.out.println("The title of the item is : "+titleText);
                System.out.println("The url of the image is : "+url);
              }  
              else
                 continue;
            }
        }
    }
    @AfterTest
    public void endTest()
    {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }


}

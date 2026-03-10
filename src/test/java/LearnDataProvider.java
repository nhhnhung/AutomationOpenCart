import Data.DataProviderFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class LearnDataProvider {
    @Test(dataProvider = "data_provider_02", dataProviderClass = DataProviderFactory.class)
    public void testDataProviderMultiParam(String username, String password, String role) {
        System.out.println("Username is: " + username);
        System.out.println("Password is: " + password);
        System.out.println("Role is: " + role);
    }

    @Test(dataProvider = "data_provider_03", dataProviderClass = DataProviderFactory.class)
    public void testDataLogin(String username, String password, String role) {
        System.out.println("Username is: " + username);
        System.out.println("Password is: " + password);
        System.out.println("Role is: " + role);
    }

    @Test(priority = 1, dataProvider = "data_provider_login_excel", dataProviderClass = DataProviderFactory.class)
    public void testLoginFromDataProviderExcel(String stt, String hoten) {
        System.out.println("STT: " + stt);
        System.out.println("Họ và tên: " + hoten);
    }

    @DataProvider (name = "data-provider")
    public Object[][] dpMethod (Method m){
        switch (m.getName()) {
            case "Sum":
                return new Object[][] {{2, 3 , 5}, {5, 7, 9}};
            case "Diff":
                return new Object[][] {{2, 3, -1}, {5, 7, -2}};
        }
        return null;
    }


    @Test(dataProvider = "data-provider")
    public void Sum(int a, int b, int result) {
        int sum = a + b;
        Assert.assertEquals(result, sum);
    }

    @Test(dataProvider = "data-provider")
    public void Diff(int a, int b, int result) {
        int diff = a - b;
        Assert.assertEquals(result, diff);
    }
}

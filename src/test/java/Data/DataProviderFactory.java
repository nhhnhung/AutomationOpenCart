package Data;

import com.opencart.helpers.ExcelHelper;
import com.opencart.helpers.SystemHelper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;

public class DataProviderFactory {
    @DataProvider(name = "data_provider_02", parallel = true)
    public Object[][] dataHRM() {
        return new Object[][]{{"anhtester", "123456", "Admin"}, {"joe.larson", "joe.larson", "Employee"}};
    }

    @DataProvider(name = "data_provider_03")
    public Object[][] dataLogin() {
        return new Object[][]{{"anhtester", "123456", "Admin"}};
    }

    @DataProvider(name = "data_provider_login")
    public Object[][] dataLoginHRM() {
        return new Object[][]{{"frances.burns", "frances.burns"}};
    }

    //Lấy từ file excel
    @DataProvider(name = "data_provider_login_excel")
    public Object[][] dataLoginHRMFromExcel() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] data = excelHelper.getExcelData(SystemHelper.getCurrentDir() + "datatest.xlsx", "Sheet1");
        System.out.println("Login Data from Excel: " + data);
        return data;
    }

    @DataProvider(name = "registerData")
    public Object[][] registerData(Method m) {
        ExcelHelper excelHelper = new ExcelHelper();
        String filePath = SystemHelper.getCurrentDir()
                + File.separator
                + "RegisterData.xlsx";

        switch (m.getName()) {

            case "registerSuccess":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "HAPPY"
                );

            case "registerNegativeBoundary":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "NEGATIVE",
                        "BOUNDARY_FAIL"
                );

            default:
                throw new RuntimeException(
                        "No data found for test method: " + m.getName()
                );
        }
    }
@DataProvider(name = "loginData")
    public Object[][] loginData(Method m) {
        ExcelHelper excelHelper = new ExcelHelper();
        String filePath = SystemHelper.getCurrentDir()
                + File.separator
                + "LoginData.xlsx";

        switch (m.getName()) {

            case "testLoginSuccess":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "HAPPY"
                );

            case "testLoginNegativeBoundary":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "NEGATIVE"
                );
            case "testLoginWithPassSQLInjection":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "SECURITY"
                );

            default:
                throw new RuntimeException(
                        "No data found for test method: " + m.getName()
                );
        }
    }



    @DataProvider(name = "searchData")
    public Object[][] searchData(Method m) {
        ExcelHelper excelHelper = new ExcelHelper();
        String filePath = SystemHelper.getCurrentDir()
                + File.separator
                + "SearchData.xlsx";

        switch (m.getName()) {

            case "searchValidKeyword":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "HAPPY"
                );

            case "searchInvalidKeyword":
                return excelHelper.getExcelData(
                        filePath,
                        "Sheet1",
                        "NEGATIVE",
                        "SECURITY"
                );

            default:
                throw new RuntimeException(
                        "No data found for test method: " + m.getName()
                );
        }
    }
}

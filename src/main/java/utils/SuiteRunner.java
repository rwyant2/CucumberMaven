package utils;

import com.aventstack.extentreports.ExtentReports;
import cucumber.api.cli.Main;
import org.apache.maven.shared.invoker.*;
import java.io.File;
import java.util.Collections;

public class SuiteRunner {

    public static void main(String[] args) {

        //todo: make the directories compatible with Windows
        String [] argv = new String[]{
//                "--help",""
                "-g","","./src/main/resources/features"
//                ,"--plugin","html:target/this_is_my_report_yay_me.html" // i get an html report
                ,"--plugin","json:target/cucumber-report/cucumber.json" // json for cluecumber
        };

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            byte exitstatus = Main.run(argv, contextClassLoader);
        } catch (Exception e) {
            System.out.println("Cucumber CLI did an oopsie!");
            e.printStackTrace();
        }
        

//        InvocationRequest request = new DefaultInvocationRequest();
//        String absPath = new File("").getAbsolutePath();
//        request.setPomFile(new File(absPath + "/pom.xml"));
//        request.setGoals(Collections.singletonList("cluecumber-report:reporting -Dparm=\"banana\""));
//        Invoker invoker = new DefaultInvoker();
//        invoker.setMavenHome(new File("/usr"));

//        try {
//            invoker.execute(request);
//        } catch (MavenInvocationException e) {
//            e.printStackTrace();
//        }
    }
}


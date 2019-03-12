package utils;

import cucumber.api.cli.Main;

public class SuiteRunner {

    public static void main(String[] args) {

        String [] argv = new String[]{
//                "--help",""
                "-g","","./src/main/resources/features"
                ,"--plugin","html:target/this_is_my_report_yay_me.html" // i get an html report
        };

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            byte exitstatus = Main.run(argv, contextClassLoader);
        } catch (Exception e) {
            System.out.println("Cucumber CLI did an oopsie!");
            e.printStackTrace();
        }
    }
}


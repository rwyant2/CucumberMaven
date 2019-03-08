package utils;


import cucumber.api.cli.Main;
import cucumber.api.junit.Cucumber;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.IOException;

public class example {

    public static void main(String[] args) {
//        JUnitCore core = new JUnitCore();
//        core.addListener(new TextListener(System.out));
//        Result result = core.run(CucumberRunnerTest.class);
////        Result result = core.run(Cucumber.class);
//        resultReport(result);
//    }
//
//        public static void resultReport(Result result) {
//            System.out.println("Finished. Result: Failures: " +
//                    result.getFailureCount() + ". Ignored: " +
//                    result.getIgnoreCount() + ". Tests run: " +
//                    result.getRunCount() + ". Time: " +
//                    result.getRunTime() + "ms.");
//        }

        String [] argv = new String[]{
//                "-g glue code here"
//                , "snippets = SnippetType.CAMELCASE"
//                , "--p --add "
//                , "pretty"
                "./src/main/resources/features"

        };

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            byte exitstatus = Main.run(argv, contextClassLoader);
        } catch (Exception e) {
            System.out.println("oopsie");
            e.printStackTrace();
        }


        //byte exitstatus = Main.run(argv, contextClassLoader);
    }

}
}

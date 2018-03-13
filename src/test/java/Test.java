import com.udgrp.common.PoolsImpl;
import com.udgrp.service.ESMainService;

/**
 * Created by kejw on 2017/8/16.
 */
public class Test extends PoolsImpl {

    @org.junit.Test
    public void searchESSHW() {
        String page1 = new ESMainService().searchESSHW("粤AH7733", "2017-06-18", "2017-06-18", 1, 24, "1");
        System.out.println(page1);
    }

    @org.junit.Test
    public void searchES() {
        String page2 = new ESMainService().searchES("", "2017-06-18", "2017-06-18", 1, 10000, "1");
        System.out.println(page2);
    }

    @org.junit.Test
    public void searchESGPS() {
        String page3 = new ESMainService().searchESGPS("粤AH9506", "", "", 1, 12);
        System.out.println(page3);
    }

    @org.junit.Test
    public void searchESGPSMISS() {
        String page4 = new ESMainService().searchESGPSMISS("粤AG2057", "2017-06-18", "2017-06-18", 1, 20);
        System.out.println(page4);
    }

    @org.junit.Test
    public void searchES_City() {
        String page4 = new ESMainService().searchES_City("粤AH9506", "2017-06-12", "2017-06-18", 20);
        System.out.println(page4);
    }

    @org.junit.Test
    public void searchES_Scope() {
        String page4 = new ESMainService().searchES_Scope("", "2017-06-12", "2017-06-18");
        System.out.println(page4);
    }

    @org.junit.Test
    public void searchESGPSMISS_2() {
        String page4 = new ESMainService().searchESGPSMISS_2("粤AG2057", "2017-06-18", "2017-06-18",1, 20);
        System.out.println(page4);
    }
}

package WHS_planner.Schedule;

import WHS_planner.Main;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Here be dragons
 *
 *
 * Please avoid touching if you value your sanity
 *
 *
 * This just kind of works
 *
 * By John Broderick
* */

public class GrabDay
{
    private final String USER_AGENT = "Mozilla/5.0";

    private HttpsURLConnection connection;

    private String calURL = "https://ipass.wayland.k12.ma.us/school/ipass/hello.html";

    private String user;
    private String pass;

    private List<String> cookies;


    private String error;

    public GrabDay(String user, String pass)
    {
        this.user = user;
        this.pass = pass;
    }


    public boolean testConn() throws Exception
    {

        String url = "https://ipass.wayland.k12.ma.us/school/ipass/syslogin.html";


        CookieHandler.setDefault(new CookieManager());
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);


        Grabber grab = new Grabber();

        String page = grab.getPageContent(url);
        String params = grab.getForm(page, user, pass);

        grab.send(url, params);

        connection.disconnect();

        return !error.contains("Invalid");
    }



    public void grabData()
    {
        String url = "https://ipass.wayland.k12.ma.us/school/ipass/syslogin.html";

        CookieHandler.setDefault(new CookieManager());
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        Grabber grabber = new Grabber();

        try
        {
            String page = grabber.getPageContent(url);
            String params = grabber.getForm(page, user, pass);

            grabber.send(url, params);

            //String res = grabber.getPageContent("https://ipass.wayland.k12.ma.us/school/ipass/samschedule.html");

            File output = getCalendar("tmp", grabber);

            connection.disconnect();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void grabSchedule(String scheduleFileName)
    {
        Grabber grabber = new Grabber();

        String url = "https://ipass.wayland.k12.ma.us/school/ipass/syslogin.html";

        CookieHandler.setDefault(new CookieManager());
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        try
        {
            String page = grabber.getPageContent(url);
            String params = grabber.getForm(page, user, pass);
            grabber.send(url, params);

            String cookie = cookies.get(2);

            setQuarter(cookie);
            connection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private File getCalendar(String foldername, Grabber g) throws Exception
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);


        File root = new File(foldername);
        root.mkdir();

        int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        for (int i = 0; i < months.length; i++)
        {
            String mon;
            switch(i)
            {
                case 0: mon = "January";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                    break;
                case 1: mon = "February";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                    break;
                case 2: mon = "March";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                    break;
                case 3: mon = "April";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                    break;
                case 4: mon = "May";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                        break;
                case 5: mon = "June";
                    year = Calendar.getInstance().get(Calendar.YEAR)+1;
                    break;
                case 6: mon = "July";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                case 7: mon = "August";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                case 8: mon = "September";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                case 9: mon = "October";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                case 10: mon = "November";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                case 11: mon = "December";
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                default: mon = "Error";
                    break;
            }

            if(Calendar.getInstance().get(Calendar.MONTH) < 8)
            {
                year = Calendar.getInstance().get(Calendar.YEAR);
            }


            String childs = foldername + "/"+mon;
            File child = new File(childs);
            child.mkdir();

            for (int j = 0; j < months[i]; j++)
            {

                String filename = childs+"/"+(j+1);
                String supp = "?month="+(i+1)+"&day="+(j+1)+"&year="+year;

                String res = g.getPageContent(calURL + supp);
                parseHtml(res, filename);
            }
        }

        return root;
    }



    private File parseHtml(String input, String name) throws IOException
    {
        File f = new File(name);

        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);

        char[] data = input.toCharArray();

        int i = 0;
        while(i < data.length)
        {
            if(data[i] == '\n')
            {
                bw.newLine();
            }
            else
            {
                bw.write(data[i]);
            }
            i++;

        }

        bw.flush();
        bw.close();
        fw.close();

        return f;
    }

    public void setQuarter(String cookie) throws Exception
    {
        WebClient client = new WebClient();

        client.getOptions().setJavaScriptEnabled(true);

        client.getCookieManager().setCookiesEnabled(true);

        client.addCookie(cookie, new URL("https://ipass.wayland.k12.ma.us/school/ipass/syslogin.html"), client);

        HtmlPage page = client.getPage("https://ipass.wayland.k12.ma.us/school/ipass/samschedule.html");

        List<HtmlAnchor> anchors = page.getAnchors();

        HtmlAnchor anchor2 = null;

        for (int i = 0; i < anchors.size(); i++)
        {
            anchor2 = anchors.get(1);
        }

        HtmlPage sched = anchor2.click();


        File f = new File(Main.SAVE_FOLDER + File.separator +"output.html");
        sched.save(f);

        client.close();
    }

    private class Grabber
    {
        private Grabber()
        {

        }

        private String getPageContent(String url) throws Exception
        {
            URL ipass = new URL(url);
            connection = (HttpsURLConnection) ipass.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("User-Agent", USER_AGENT);

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");

            if (cookies != null)
            {
                for (String cookie : cookies)
                {
                    connection.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
            }

            int resp = connection.getResponseCode();

//            System.out.println("\nSending GET request to " + url);
//            System.out.println("Response code is: "+resp);

            InputStreamReader ir = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(ir);

            String inLine;
            StringBuilder response = new StringBuilder();

            while((inLine = br.readLine()) != null)
            {
                response.append(inLine);
                response.append("\n");
            }
            br.close();
            ir.close();

            return response.toString();
        }



        private String getForm(String html, String user, String pass) throws Exception
        {
            System.out.println("Getting form data...");

            Document doc = Jsoup.parse(html);

            Element loginform = doc.getElementById("login");
            Elements inputelements = loginform.getElementsByTag("input");

            ArrayList<String> params = new ArrayList<>();

            for(Element el : inputelements)
            {
                String key = el.attr("name");
                String val = el.attr("value");

                if(key.equals("userid"))
                {
                    val = user;
                }
                else if(key.equals("password"))
                {
                    val = pass;
                }

                params.add(key + "=" + URLEncoder.encode(val, "UTF-8"));
            }

            StringBuffer buffer = new StringBuffer();

            for(String param : params)
            {
                if(buffer.length() == 0)
                {
                    buffer.append(param);
                }
                else
                {
                    buffer.append("&" + param);
                }
            }

            return buffer.toString();
        }


        private void send(String url, String params) throws Exception
        {
            System.out.println("Attempting to send data");

            URL obj = new URL(url);

            connection = (HttpsURLConnection) obj.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");



            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            //System.out.println(params);
            dos.writeBytes(params);
            dos.flush();
            dos.close();


            int response = connection.getResponseCode();

            System.out.println("\nSending POST request to "+url);
//            System.out.println("Parameters : " + params); //Unsafe
            System.out.println("Response code:" + response);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer stringbuffer = new StringBuffer();

            while((input = br.readLine()) != null)
            {
                stringbuffer.append(input);
            }


            setCookies(connection.getHeaderFields().get("Set-Cookie"));
            error = stringbuffer.toString();
            br.close();

        }

        private List<String> getCookies()
        {
            return cookies;
        }

        private void setCookies(List<String> cs)
        {
            cookies = cs;
        }
    }
}
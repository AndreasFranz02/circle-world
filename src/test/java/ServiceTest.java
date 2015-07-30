import com.opitzconsulting.soa.testing.AbstractSoaTest;
import com.opitzconsulting.soa.testing.MockService;
import com.opitzconsulting.soa.testing.util.SoapUtil;
import org.junit.*;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by DSP on 23.07.2015.
 */
public class ServiceTest extends AbstractSoaTest {

    private static final String COMPOSITE_NAME = "CreateAccountViaNavWrapper";
    private static final String COMPOSITE_REV = "1.0";
    private static final String SERVICE_NAME = "createaccountnavwsync_client_ep";
    private static final String EXTERNAL_SERVICE_NAME = "CallNavWrapper";
    private String request;
    private static Connection connection = null;
    private Statement statement;
    private ResultSet resultSet;


    @Test
    public void testCreateValidAccount() throws Exception {

        // Step 1: Prepare the request
        String message = SoapUtil.getInstance().soapEnvelope(SoapUtil.SoapVersion.SOAP11, request);

        // Step 2: Execute the request
        String response = invokeCompositeService(COMPOSITE_NAME, COMPOSITE_REV, SERVICE_NAME, message);
        String responseBody = SoapUtil.getInstance().getSoapBody(response);

        // Step 3: Verify the response
        assertXpathEvaluatesTo("count(//cre:payload)", "1", responseBody);
        assertXpathEvaluatesTo("//cre:statusCode/text()", "201", responseBody);

        //Step 3a: extract customerNumber from response
        //Step 3b: send query for customerNumber
        String sql =
                "SELECT * " +
                "FROM [NAVTESTDEV1].Copy_Dynamics_NAV.dbo.[FP Commerce$Customer] as SH " +
                "WHERE SH.No_ = '000006037'";
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        //Step 3c: assert that querry found sth
        assertNotNull(resultSet);
    }

    @Test
    public void testNavUnavailable() throws Exception {

        // implement mockService
      mockCompositeReference(COMPOSITE_NAME, COMPOSITE_REV, EXTERNAL_SERVICE_NAME, new MockService() {
            public String serviceCallReceived(String serviceName, String requestStr) {
//                return SoapUtil.getInstance().soapEnvelope(SoapUtil.SoapVersion.SOAP11, "responseSyncNAV.xml");
                return "ok";
            }
        });

        // Prepare the request
        String message = SoapUtil.getInstance().soapEnvelope(SoapUtil.SoapVersion.SOAP11, request);


        // Invoke the service after the mock has been defined
        String response = invokeCompositeService(COMPOSITE_NAME, COMPOSITE_REV, SERVICE_NAME, message);

        System.out.println(message);

        //Connect to queue
        //check if wanted error is in queue

        //extract customerNumber from response
        //connect to DB navtestdev1

        //send querry for customerNumber
        //assert that querry found sth
    }

    @Test
    public void testDbConnection() throws Exception {
        assertNotNull(connection);
    }

    @Test
    public void testGARBAGE_QueueConnection() throws Exception {
        Context ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
        Queue myQueue = (Queue) ctx.lookup("MyQueue");

        QueueConnection connection = factory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueReceiver receiver = session.createReceiver(myQueue);
        connection.start();
        Message message = receiver.receive();
    }

    //==============================================Test=Setup==============================================\\

    //@BeforeClass
    public static void setUpDB() throws Exception {
        // Create a variable for the connection string. 7092
        String connectionUrl = "jdbc:sqlserver://navtestdev1:1433;databaseName=OraclePOC;user=HOME24\\dawid.pauksztelo;password=HG)-oGz24;integratedSecurity=true";

        // Declare the JDBC objects.
        connection = null;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionUrl);
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownDB() throws Exception {
        if (connection != null) try { connection.close(); } catch(Exception e) {}
    }

    @Before
    public void declareNamespace() throws Exception {
        declareXpathNS("cre", "http://xmlns.oracle.com/BusinessEntityServices/CreateAccountViaNavWrapper/CreateAccountNavWSync");
        request = readClasspathFile("request");
        statement = null;
        resultSet = null;
    }

    @After
    public void tearDown() throws Exception {
        if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
        if (statement != null) try { statement.close(); } catch(Exception e) {}
    }
}

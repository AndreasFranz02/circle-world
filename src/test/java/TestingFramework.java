import com.opitzconsulting.soa.testing.AbstractSoaTest;
import com.opitzconsulting.soa.testing.util.SoapUtil;
import org.junit.Test;


/**
 * Created by AFR on 30.07.2015.
 */
public class TestingFramework extends AbstractSoaTest {
    @Test
    public void configSetShouldReturnConfigSet() throws Exception {

    //Prepare request
    String message = SoapUtil.getInstance().soapEnvelope(SoapUtil.SoapVersion.SOAP11, readClasspathFile("request"));

    // Execute request
       String response = invokeSyncHTTPCompositeService("CreateAccountViaNavWrapper", "1.0", "createaccountnavwsync_client_ep", message);
       String responseBody = SoapUtil.getInstance().getSoapBody(response);

     //Verify response
        assertXpathEvaluatesTo("count(//cre:payload)", "1", responseBody);
        assertXpathEvaluatesTo("//cre:statusCode/text()", "200", responseBody);
    }
}

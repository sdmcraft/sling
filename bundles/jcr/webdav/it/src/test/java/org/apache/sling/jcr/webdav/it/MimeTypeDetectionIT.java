package org.apache.sling.jcr.webdav.it;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.sling.testing.tools.sling.SlingClient;
import org.apache.sling.testing.tools.sling.SlingTestBase;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by satyadeep on 5/14/15.
 */
public class MimeTypeDetectionIT extends SlingTestBase {

    private SlingClient slingClient = new SlingClient(this.getServerBaseUrl(), this.getServerUsername(), this.getServerPassword());

    @Test
    public void testGetResource() throws Exception {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost("http://localhost:8080");
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);
        HttpClient client = new HttpClient();
        Credentials creds = new UsernamePasswordCredentials("admin", "admin");
        client.getState().setCredentials(AuthScope.ANY, creds);
        client.setHostConfiguration(hostConfig);
        client.getParams().setAuthenticationPreemptive(true);

        String host = "http://localhost:8080";
        String resourcePath = "/content";
        DavMethod pFind = new PropFindMethod(host + resourcePath, DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_1);
        client.executeMethod(pFind);

        MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();
        MultiStatusResponse[] responses = multiStatus.getResponses();
        MultiStatusResponse currResponse;
        ArrayList files = new ArrayList();
        System.out.println("Folders and files in " + resourcePath + ":");
        for (int i=0; i<responses.length; i++) {
            currResponse = responses[i];
            if (!(currResponse.getHref().equals(resourcePath) || currResponse.getHref().equals(resourcePath + "/"))) {
                System.out.println(currResponse.getHref());
            }
        }
    }
}

package com.sample.handlers;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;
import java.util.Map;

/**
 * Custom handler implementation to extract and preserve the api key 
 * Header in the Message Context and saving the API key to a property.
 */
public class CustomApikeyHeaderHandler extends AbstractHandler {
    public boolean handleRequest(MessageContext messageContext) {
        try {
            authenticate(messageContext);
        } catch (APISecurityException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }

    public boolean authenticate(MessageContext msgCtx) throws APISecurityException {
        Map headers = getTransportHeaders(msgCtx);
        String authHeader = getAuthorizationHeader(headers);
        msgCtx.setProperty("preserveApikey", authHeader);
        return true;
    }

    private String getAuthorizationHeader(Map headers) {
        return (String) headers.get("apikey");
    }

    private Map getTransportHeaders(MessageContext messageContext) {
        return (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext().
                getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
    }
}

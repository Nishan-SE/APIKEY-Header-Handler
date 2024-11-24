# APIKEY-Header-Handler
This is a custom handler implementation to preserve the API KEY and pass the API KEY to the backend server per API level in WSO2 API Manager.

The main branch contains the source code of the handler implemented for APIM 4.1.0. Please make a clone of this repository and update the dependencies then build the handler to support in other versions of the WSO2 API Manager.

# Build the project 

Execute the following command from the root directory of the project to build
```sh
mvn clean install
```

# Configuration of JAR file

Copy the built JAR artifact and add it inside the `<APIM_HOME>/repository/components/lib` directory and start the server to load the required classes.

# Configuration of Velocity Template 

Add the required handler class to the velocity_template.xml to include the handler in synapse definition of API when redeploying.

Navigate to `<APIM_HOME>/repository/resources/api_templates/velocity_template.xml` and add the below changes.
```xml
<handlers xmlns="http://ws.apache.org/ns/synapse">
<handler class="com.sample.handlers.CustomApikeyHeaderHandler"/>
                #foreach( $handler in $handlers )
<handler xmlns="http://ws.apache.org/ns/synapse" class="$handler.className">
                    #if($handler.hasProperties())
                        #set ($map = $handler.getProperties() )
                        #foreach($property in $map.entrySet())
    <property name="$!property.key" value="$!property.value"/>
                        #end
                    #end
</handler>
                #end
                ## check and set enable schema validation
                #if($enableSchemaValidation)
<handler class="org.wso2.carbon.apimgt.gateway.handlers.security.SchemaValidator"/>
                #end
```
Save the velocity_template.xml and Restart the server to reflect the changes.

# Configuration of mediation sequence  

Add the mediation sequence for header mapping to get the "preserveApikey" property from the handler class to the new header "backend-apikey" which will be passed to the backend.

Navigate to Policies tab from the publisher portal under the API Configurations and add new policy using the `sample-insequence-to-preserve-apikey.j2 ` mediation file.

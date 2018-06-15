//package prosodicproc.connections;
//
//import com.sun.deploy.util.SessionState;
//import org.apache.catalina.WebResource;
//import org.glassfish.jersey.client.ClientResponse;
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//
//
//public class restCalls {
//
//
//        public static void main(String[] args) {
//            try {
//
//                SessionState.Client client = Client.create();
//
//                com.sun.jersey.api.client.WebResource webResource = client
//                        .resource("http://clarin.phonetik.uni-muenchen.de/BASWebServices/services/runMAUSBasic");
//
//                com.sun.jersey.api.client.ClientResponse response = webResource.accept("application/json")
//                        .get(ClientResponse.class);
//
//                if (response.getStatus() != 200) {
//                    throw new RuntimeException("Failed : HTTP error code : "
//                            + response.getStatus());
//                }
//
//                String output = response.getEntity(String.class);
//
//                System.out.println("Output from Server .... \n");
//                System.out.println(output);
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//            }
//
//        }
//    }
package redisSATools.basicLoadModeler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] argv ) throws InterruptedException{
    	if(!(argv.length==3 || argv.length==4)) {
    		System.out.println("Wrong number of arguments, please use three or four: (1)Host (2)Port (3)Length of test window in seconds (4)Password (optional)");
    		return;
    	}
    	java.security.Security.setProperty("networkaddress.cache.ttl" , "5");
    	String host = argv[0];
		int port = Integer.valueOf(argv[1]);
		long lengthOfWindowInSeconds = Integer.valueOf(argv[2]);
		String password = null;
		if(argv.length == 4)
			password = argv[3];
		System.out.println("Using Host: " + host + " and Port: " + port);
        Loader loader = new Loader(host, port, password);
        System.out.println("Starting load...");
        loader.implementLoad(lengthOfWindowInSeconds);
        System.out.println("Load complete");
        
    }
}

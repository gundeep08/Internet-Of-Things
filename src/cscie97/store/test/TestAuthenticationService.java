package cscie97.store.test;

import java.nio.file.AccessDeniedException;
import java.util.logging.Logger;

import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AuthenticationException;
import com.cscie97.store.authentication.InvalidAccessTokenException;

import cscie97.store.model.CommandProcessor;
import cscie97.store.model.CommandProcessorException;
import cscie97.store.model.StoreModelServiceException;

public class TestAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(TestAuthenticationService.class.getClass().getName());
	
	public static void main(String [] args) throws CommandProcessorException, StoreModelServiceException, LedgerException, AuthenticationException, InvalidAccessTokenException, AccessDeniedException {
		    String filePath = args[0];
		    //String filePath = "/Users/ggumbe350/workspace/HSEFall2022/gundeep_gumber_assignment4/src/cscie97/store/authentication/resources/StoreAuthenticationScript.txt";
			CommandProcessor commandprocessor = new CommandProcessor();
			TestAuthenticationService testStoreController = new TestAuthenticationService();
			LOGGER.info("Process the commands from the file at the filePath location.");
			commandprocessor.processCommandFile(filePath);
			String emergencyTestOutput = testStoreController.testEvents(commandprocessor, "event Emergency type flood location aisle_A1");
			System.out.println(emergencyTestOutput);
			String basketTestOutput = testStoreController.testEvents(commandprocessor, "event Customer Basket customer jane action add product prod10 location store_123:aisle_A2:shelf_q1");
			System.out.println(basketTestOutput);
			String enterStoreTestOutput = testStoreController.testEvents(commandprocessor, "event EnterStore customer cust_23 turnstile turn_a2");
			System.out.println(enterStoreTestOutput);
		
	}
	
	private String testEvents(CommandProcessor commandprocessor, String eventMessage) throws CommandProcessorException, StoreModelServiceException, LedgerException, AuthenticationException, AccessDeniedException, InvalidAccessTokenException {
		 Object output = commandprocessor.processCommand(eventMessage);
		 return output.toString();
	}
	
}
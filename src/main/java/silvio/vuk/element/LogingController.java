package silvio.vuk.element;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import silvio.vuk.element.dao.UserIPBlockedDAO;
import silvio.vuk.element.dao.UsersIPDAO;

@Controller
public class LogingController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogingController.class);
	
	@Autowired
	private ReCaptcha reCaptchaService = null;

	@Autowired
	@Qualifier("UsersIPDAO")
	private UsersIPDAO userIPDAO;
	
	@Autowired
	@Qualifier("userBlockedIPDAO")
	private UserIPBlockedDAO userBlockedIPDAO;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		return "home";
	}
	
	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="/login/login")
	public String login(Locale locale, Model model)
	{
	    String ip = getIP();
		
	    try
	    {
	    	boolean isUserBlocked = userBlockedIPDAO.checkBlockedIP(ip);
	    	if(isUserBlocked)
	    		return "banned";
	    }
	    catch(IllegalStateException ex)
	    {
	    	model.addAttribute("error", ex.getMessage());
	    }
	    
		return "login";
	}
	
	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="/login/failure")
	public String loginFailure(Locale locale, Model model)
	{
	    String ip = getIP();
	    
	    try
	    {
	    	boolean isUserBlocked = userBlockedIPDAO.checkBlockedIP(ip);
	    	
	    	if(isUserBlocked)
	    	{
	    		return "banned";
	    	}
	    	
	    	int occ = userIPDAO.checkIP(ip);
	    	
	    	switch(occ)
	    	{
	    	case -1:
	    		throw new IllegalStateException("CheckIP function - error with database!");
	    	case 0:
	    		userIPDAO.insertIP(ip);
	    		break;
	    	case 5:
	    		userBlockedIPDAO.insertBlockedIP(ip);
	    		userIPDAO.removeIP(ip);
	    		return "banned";
	    	default:
	    		userIPDAO.increaseIPsOccurrence(ip, occ+1);
	    	}
	 
	    }
	    catch(IllegalStateException ex)
	    {
	    	model.addAttribute("error", ex.getMessage());
	    }
	    
		logger.info("Login failed for user with : {}", ip);
		model.addAttribute("error", "Greška kod validacije pokušajte ponovo!");
		return "login";
	}
	
	
	@RequestMapping(value="/login/logout")
	public String logout(Locale locale, Model model)
	{   
	    String ip = getIP();
		
	    try
	    {
	    	boolean isUserBlocked = userBlockedIPDAO.checkBlockedIP(ip);
	    	if(isUserBlocked)
	    		return "banned";
	    }
	    catch(IllegalStateException ex)
	    {
	    	model.addAttribute("error", ex.getMessage());
	    }
	    
		return "login";
	}
	
	@RequestMapping(value="/login/access_denied")
	public String accessDenied(Locale locale, Model model)
	{
		
		return "accessdenied";
	}
	
	/**
	 * Dohvaæa i parsira IP adresu korisnika.
	 * 
	 * @return IP adresu korisnika.
	 * @throws IllegalStateException
	 */
	private String getIP() throws IllegalStateException
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String details = auth.getDetails().toString();
		
		int indexOfRemote = details.indexOf(':');
		indexOfRemote = details.indexOf(':', indexOfRemote+1);
		
		if(indexOfRemote == -1)
			throw new IllegalStateException("getIP indexOfRemote value = -1! check the received String from current user!");
		
		int indexOfBreak = details.indexOf(';');
		
		if(indexOfBreak == -1)
			throw new IllegalStateException("getIP indexOfBreak value = -1! check the received String from current user!");
		
		String ip = details.substring(indexOfRemote + 1, indexOfBreak);
		
		return ip.trim();
	}
	
	@PreAuthorize("isAnonymous()")
	@RequestMapping(value="/login/recaptcha", method=RequestMethod.POST)
	public String verify(ServletRequest request, Model model) 
	{ 
		String challenge = request.getParameter("recaptcha_challenge_field");
		String response = request.getParameter("recaptcha_response_field");
		String remoteAddr = request.getRemoteAddr();
		
		ReCaptchaResponse reCaptchaResponse = reCaptchaService.checkAnswer(remoteAddr, challenge, response);

		remoteAddr = remoteAddr.trim();
		
		if(reCaptchaResponse.isValid()) 
		{
			userBlockedIPDAO.removeBlockedIP(remoteAddr);
			logger.info("User with address: {}, unblocked his account", remoteAddr);
			return "login";
		} 
		else 
		{
			model.addAttribute("message", "Pokušajte ponovo!");
			return "banned";
		}
	}
	
}

package fil.univ.login.service;

import fil.univ.login.config.TwilioConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;

@Service
public class PhoneverificationService {

    private final TwilioConfiguration twilioproperties;

    @Autowired
    public PhoneverificationService(TwilioConfiguration twilioproperties) {
        this.twilioproperties=twilioproperties;
    }


    //method to send sms code
    public VerificationResult startVerification(String phone) {
        try {
            Verification verification = Verification.creator("VAfc5a3f813ed4d99a7fca0eb7e1b022eb", phone, "sms").create();
            if("approved".equals(verification.getStatus())|| "pending".equals(verification.getStatus())) {
                return new VerificationResult(verification.getSid());
            }
        } catch (ApiException exception) {
            return new VerificationResult(new String[] {exception.getMessage()});
        }
        return null;
    }


}
package campus_care.service.otp;
import campus_care.dto.VerifyOtpRequest;
public interface OtpService {

    void sendOtp(String studentEmail);

    void verifyOtp(VerifyOtpRequest dto);
void deleteExpiredOtp();
    void deleteOtp(String studentEmail);
}
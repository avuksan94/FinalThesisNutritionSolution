package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.EncryptionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EncryptionServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String doBCryptPassEncoding(String plainTextPassword) {
        return bCryptPasswordEncoder.encode(plainTextPassword);
    }
}
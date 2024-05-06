package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.EncryptionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/*
The BcryptPasswordEncoder employs the bcrypt hashing algorithm,
which has been in use since 1999. This algorithm is frequently updated
to match modern computational advancements.
Hashing with BcryptPasswordEncoder demands considerable CPU computations,
making it resistant to rapid brute-force attacks. The time taken for hashing
increases with the number of configured rounds, enhancing security.
 */

/*
Password Encoder explained page 87
*/

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
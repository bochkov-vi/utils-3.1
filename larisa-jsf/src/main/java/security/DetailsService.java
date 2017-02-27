package security;

import com.google.common.collect.Collections2;
import larisa.entity.Account;
import larisa.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by bochkov
 */
@Service
public class DetailsService implements UserDetailsService {
    @Autowired
    AccountRepository repository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = repository.findOne(s);
        UserDetails userDetails = null;
        if (account != null) {
            Collection<? extends GrantedAuthority> authorities;
            authorities = Collections2.transform(account.getRoles(), r -> new SimpleGrantedAuthority(r.toString()));
            userDetails = new User(account.getId(), account.getPassword(), authorities);
        }
        return userDetails;
    }
}

package kvoting.intern.flowerwebapp.account;

import kvoting.intern.flowerwebapp.registration.Registration;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "CC_USER_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account implements UserDetails {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_EMAIL")
    private String email;
    @Column(name = "USER_PASS", length = 500)
    private String password;
    @Column(name = "USER_NM")
    private String name;
    @Column(name = "USER_DEPT")
    private String department;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "USER_ROLE")
    private Set<AccountRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "registrant", fetch = FetchType.LAZY)
    private Set<Registration> Regs = new HashSet<>();

    @OneToMany(mappedBy = "processor", fetch = FetchType.LAZY)
    private Set<Registration> processRegs = new HashSet<>();

    public void addRole(AccountRole role) {
        roles.add(role);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

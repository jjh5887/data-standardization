package kvoting.intern.flowerwebapp.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kvoting.intern.flowerwebapp.account.serialize.CustomAuthorityDeserializer;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
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
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_EMAIL")
    private String email;
    @Column(name = "USER_PASS", length = 500)
    @JsonIgnore
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

    @OneToMany(mappedBy = "registrant", fetch = FetchType.LAZY)
    private Set<Registration> processRegs = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<Word> words = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<Domain> domains = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<Dict> dicts = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<CustomDomain> customDomains = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<Constraint> constraints = new HashSet<>();

    @OneToMany(mappedBy = "modifier", fetch = FetchType.LAZY)
    private Set<CommonCode> commonCodes = new HashSet<>();

    public void addRole(AccountRole role) {
        roles.add(role);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
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

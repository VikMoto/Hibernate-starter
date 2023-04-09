package com.vmdev.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = "users")
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true) //because in user default connection ManyToOne in column Company
//    @JoinColumn(name = "company_id") //unidirectional
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "company_locale",
            joinColumns = @JoinColumn(name = "company_id"))
//    @AttributeOverride(name = "lang" , column = @Column(name = "language"))
//    private List<LocaleInfo> locales = new ArrayList<>();
    @Column(name = "description")//use Only to READ table "company_locale"
    private List<String> locales = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
